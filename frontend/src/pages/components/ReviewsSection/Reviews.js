import React, {useEffect, useState} from "react";
import userApi from "../../../api/UserApi";
import mediaService from "../../../services/MediaService";
import {createSearchParams, useNavigate, useSearchParams} from "react-router-dom";
import PaginationButton from "../paginationButton/PaginationButton";
import listService from "../../../services/ListService";
import ProfileImage from "../profileImage/ProfileImage";
import {useSelector} from "react-redux";
import ConfirmationForm from "../forms/confirmationForm/confirmationForm";
import reviewService from "../../../services/ReviewService";
import ReportForm from "../forms/reportForm/reportForm";
import reportApi from "../../../api/ReportApi";

function Reviews({ id, source }) {
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

    const fetchReviews = async (currentPage) => {
        try {
            let response;
            switch (source) {
                case 'media':
                    response = await mediaService.getReviewsByMediaId(id, currentPage);
                    break;
                case 'list':
                    response = await listService.getMoovieListReviewsFromListId({
                        id: id,
                        pageNumber: currentPage
                    });
                    break;
                case 'user':
                    response = await userApi.getMovieReviewsFromUser(id, currentPage);
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
        handleCloseConfirmationDelete();
    };

    if (reviewsLoading) return <div>Cargando reseñas...</div>;
    if (reviewsError) return <div>Error al cargar reseñas: {reviewsError.message}</div>;
    if (!reviews || reviews.length === 0) return <div>No hay reseñas disponibles.</div>;

    return (
        <div className="reviews-container">
            <h2>Reseñas</h2>
            {reviews?.data?.length > 0 ? (
                reviews.data.map((review) => (
                    <div key={review.id} className="review container-fluid bg-white">
                        <div className="review-header d-flex align-items-center justify-between">
                            <div>
                                <ProfileImage
                                    imgSrc={`http://localhost:8080/users/${review.username}/image`}
                                    size="100px"
                                    defaultProfilePicture="https://example.com/default-profile.jpg"/>
                                <strong>{review.username}</strong> - <small>{review.rating}</small>
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
                                        actionName={"eliminar tu Review"}
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