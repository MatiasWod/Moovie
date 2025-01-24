import React, {useEffect, useState} from "react";
import userApi from "../../../api/UserApi";
import {createSearchParams, useNavigate, useSearchParams} from "react-router-dom";
import PaginationButton from "../paginationButton/PaginationButton";
import listService from "../../../services/ListService";
import ProfileImage from "../profileImage/ProfileImage";
import {useSelector} from "react-redux";
import ConfirmationForm from "../forms/confirmationForm/confirmationForm";
import reviewService from "../../../services/ReviewService";
import ReportForm from "../forms/reportForm/reportForm";
import reportApi from "../../../api/ReportApi";
import CommentField from "../commentField/CommentField";
import { Divider } from "@mui/material";
import commentApi from "../../../api/CommentApi";
import moovieListReviewApi from "../../../api/MoovieListReviewApi";
import CommentList from "../commentList/CommentList";
import {useTranslation} from "react-i18next";
import moovieListReviewService from "../../../services/MoovieListReviewService";


function Reviews({ id, username, source , handleParentReload }) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [reviews, setReviews] = useState(undefined);
    const [reviewsLoading, setReviewsLoading] = useState(true);
    const [reviewsError, setReviewsError] = useState(null);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);
    const [selectedReviewId, setSelectedReviewId] = useState(null);
    const {isLoggedIn, user} = useSelector(state => state.auth);
    const [reload, setReload] = useState(false);
    const [reportedReviewId, setReportedReviewId] = useState(null);
    const [commentLoading, setCommentLoading] = useState({});


    const fetchReviews = async (currentPage) => {
        try {
            let response;
            switch (source) {
                case 'media':
                    response = await reviewService.getReviewsByMediaId(id, currentPage);
                    break;
                case 'list':
                    response = await moovieListReviewService.getMoovieListReviewsByListId(id, currentPage);
                    break;
                case 'user':
                    response = await reviewService.getMovieReviewsFromUser(username, currentPage);
                    source = 'profile';
                    break;
                default:
                    throw new Error(`Unsupported source: ${source}`);
            }
            setReviews(response);
            setReviewsLoading(false);
            return response;
        } catch (error) {
            setReviewsError(error);
            setReviewsLoading(false);
            return null;
        }
    };


    useEffect(() => {
        fetchReviews(page);
    }, [id,reload,page]);

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/${source}/${id}`,
            search: createSearchParams({
                page: newPage.toString(),
            }).toString(),
        });
    };

    const handleOpenConfirmationDelete = (reviewId) => {
        if (!isLoggedIn) {
            navigate('/login');
            return;
        }
        setSelectedReviewId(reviewId);
    };

    const handleReportReview = (reviewId) => {
        console.log("Report review", reviewId);
        setReportedReviewId(reviewId);
    };

    const handleReportReviewSubmit = async (reportReason, additionalInfo) => {
        await reportApi.reportReview({
            reviewId: reportedReviewId,
            reportedBy: user.username,
            content: additionalInfo,
            type: reportReason
        });
        setReportedReviewId(null);
    };

    const handleCloseConfirmationDelete = () => {
        setSelectedReviewId(null);
    };

    const handleConfirmDelete = () => {
        // Reload reviews after successful deletion
        setReload(!reload);
        handleParentReload();
        handleCloseConfirmationDelete();
    };

    const handleCommentSubmit = async (reviewId, comment) => {
        console.log("Comment submitted for review", reviewId, comment);
        try {
            switch (source) {
                case 'media':
                    await commentApi.createReviewComment(reviewId, comment);
                    break;
                case 'list':
                    await moovieListReviewApi.createListReviewComment(reviewId, comment);
                    break;
            }
            // Force a reload of the comments
            setReload(!reload);
        } catch (error) {
            console.error("Error creating comment:", error);
        }
    };

    if (reviewsLoading) return <div>Cargando reseñas...</div>;
    if (reviewsError) return <div>Error al cargar reseñas: {reviewsError.message}</div>;
    if (!reviews || reviews.length === 0) return <div>No hay reseñas disponibles.</div>;

    return (
        <div className="reviews-container">
            {reviews?.data?.length > 0 ? (
                reviews.data.map((review) => (
                    <div key={review.id} className="review container-fluid bg-white my-3">
                        <div className="review-header d-flex align-items-center justify-between">
                            <div>
                                <ProfileImage
                                    image={review.imageUrl}
                                    size="100px"
                                    onClick={() => navigate(`/profile/${review.username}`)}
                                />
                                <strong>{review.username}</strong>
                            </div>
                            <div>{review.rating}/5<i className="bi bi-star-fill"/>
                                {isLoggedIn && (
                                    <button
                                        className="btn btn-warning btn-sm mx-1"
                                        onClick={() => handleReportReview(review.id)}
                                    >
                                        <i className="bi bi-flag"></i>
                                    </button>
                                )}
                                {isLoggedIn && (
                                    <button
                                        className="btn btn-danger btn-sm"
                                        onClick={() => handleOpenConfirmationDelete(review.id)}
                                    >
                                        <i className="bi bi-trash"></i>
                                    </button>
                                )}
                                {selectedReviewId === review.id && (
                                    <ConfirmationForm
                                        service={reviewService.deleteReviewById}
                                        serviceParams={[review.id]}
                                        actionName={t('reviews.deleteYourReview')}
                                        onConfirm={handleConfirmDelete}
                                        onCancel={handleCloseConfirmationDelete}
                                    />
                                )}
                                {reportedReviewId === review.id && (
                                    <ReportForm
                                        onReportSubmit={handleReportReviewSubmit}
                                        onCancel={() => setReportedReviewId(null)}
                                    />
                                )}
                            </div>
                        </div>
                        <div className="review-content">{review.reviewContent}</div>
                        {source === 'media' && (
                            <>
                                <CommentList reviewId={review.id} key={`${review.id}-${reload}`} />
                                {isLoggedIn && (
                                    <CommentField
                                        onSubmit={(comment) => handleCommentSubmit(review.id, comment)}
                                        isLoading={commentLoading[review.id]}
                                    />
                                )}
                            </>
                        )}
                        <Divider/>
                    </div>
                    
                ))
            ) : (
                <p>No se encontraron reseñas.</p>
            )}
            <div className="flex justify-center pt-4">
                {reviews?.data?.length > 0 && reviews.links?.last?.page > 1 && (
                    <PaginationButton
                        page={page}
                        lastPage={reviews.links.last.page}
                        setPage={handlePageChange}
                    />
                )}
            </div>
        </div>
    );
}

export default Reviews;