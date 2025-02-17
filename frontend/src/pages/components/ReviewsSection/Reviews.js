import React, { useEffect, useState } from "react";
import { createSearchParams, useNavigate, useSearchParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import PaginationButton from "../paginationButton/PaginationButton";
import ProfileImage from "../profileImage/ProfileImage";
import { Divider } from "@mui/material";
import reviewService from "../../../services/ReviewService";
import moovieListReviewService from "../../../services/MoovieListReviewService";
import commentApi from "../../../api/CommentApi";
import moovieListReviewApi from "../../../api/MoovieListReviewApi";
import CommentList from "../commentList/CommentList";
import CommentField from "../commentField/CommentField";
import mediaService from "../../../services/MediaService";
import ReviewForm from "../forms/reviewForm/ReviewForm";
import ConfirmationForm from "../forms/confirmationForm/confirmationForm";
import ConfirmationModal from "../forms/confirmationForm/confirmationModal";


const ReviewItem = ({ review, source, isLoggedIn, currentUser, handleReport, reloadReviews }) => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [reportedReviewId, setReportedReviewId] = useState(null);
    const [commentLoading, setCommentLoading] = useState(false);
    const [showDeleteReview, setShowDeleteReview] = useState(false);
    const [showEditReview, setShowEditReview] = useState(false);

    const handleToggleDelete = () => {
        setShowDeleteReview(!showDeleteReview)
    }

    const handleDelete = async () =>{
        try{
            if(source === 'media'){
                await reviewService.deleteReviewById(review.id);
                setShowDeleteReview(!showDeleteReview);
            }
            reloadReviews();
        } catch (e){

        }
    }

    const handleToggleEdit = () => {
        setShowEditReview(!showEditReview)
    }

    const handleEdit = async () =>{
        try{
            if(source === 'media'){
                await reviewService.editReview( );
                handleToggleEdit();
            }
            reloadReviews();
        } catch (e){

        }
    }


    const [reloadComments, setReloadComments] = useState(false);

    const handleCommentSubmit = async (reviewId, comment) => {
        try {
            setCommentLoading(true);
            if (source === 'media') {
                await commentApi.createReviewComment(reviewId, comment);
            } else if (source === 'list') {
                await moovieListReviewApi.createListReviewComment(reviewId, comment);
            }
            setReloadComments(!reloadComments);
        } catch (error) {
            console.error("Error creating comment:", error);
        } finally {
            setCommentLoading(false);
        }
    };

    const [media, setMedia] = useState(null);

    useEffect(() => {
        if(source === 'user' || source === 'media'){
            const fetchMedia = async () => {
                try {
                    let response = await  mediaService.getMediaById(review.mediaId)
                    setMedia(response.data);
                } catch (error) {
                    console.error("Error fetching reviews:", error);
                }
            };

            fetchMedia();
        }
    }, [source]);


    return (
        <div key={review.id} className="review container-fluid bg-white my-3">
            <div className="review-header d-flex align-items-center justify-between">
                <div>
                    <ProfileImage
                        username={review.username}
                        size="100px"
                        onClick={() => navigate(`/profile/${review.username}`)}
                    />
                    <div>
                        <strong>{review.username} </strong>
                        {media && (
                            <>
                                {t('reviews.onMedia') + " "}
                                <strong>
                                    <a href={`/details/${media.id}`} className="media-link">
                                        {media.name}
                                    </a>
                                </strong>
                            </>
                        )}
                    </div>
                </div>
                <div>
                    {source !== 'list' && (
                        <>{review.rating}/5<i className="bi bi-star-fill"/></>
                    )}
                    {isLoggedIn && (
                        <button
                            className="btn btn-warning btn-sm mx-1"
                            onClick={() => setReportedReviewId(review.id)}
                        >
                            <i className="bi bi-flag"></i>
                        </button>
                    )}
                    {(isLoggedIn && review.username === currentUser.username) && (
                        <button
                            className="btn btn-primary btn-sm me-1"
                            onClick={handleToggleEdit}
                        >
                            <i className="bi bi-pencil"></i> Edit
                        </button>
                    )}
                    {(isLoggedIn && review.username === currentUser.username) && (
                            <button
                            className="btn btn-danger btn-sm"
                            onClick={handleToggleDelete}>
                            <i className="bi bi-trash"></i>
                        </button>
                    )}
                </div>
            </div>
            <div className="review-content">{review.reviewContent}</div>
            {source === 'media' && (
                <>
                    <CommentList reviewId={review.id} reload={{reloadComments}}/>
                    {isLoggedIn && (
                        <CommentField onSubmit={(comment) => handleCommentSubmit(review.id, comment)} isLoading={commentLoading} />
                    )}
                </>
            )}
            <Divider />
            {showDeleteReview && (

                <div className="overlay">
                    <ConfirmationModal onConfirm={handleDelete} onCancel={handleToggleDelete}
                                       message={t('reviews.aboutToDelete')} title={t('review.confirmDelete')} />
                </div>
            )}
            {showEditReview && (
                <ReviewForm mediaName={media.name}
                mediaId={review.mediaId}
                userReview={review}
                closeReview={handleToggleEdit}
                onReviewSubmit={handleEdit}/>)
            }
        </div>
    );
};

function Reviews({ id, username, source, handleParentReload }) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [reviews, setReviews] = useState([]);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);
    const { user, isLoggedIn } = useSelector(state => state.auth);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [reload, setReload] = useState(false);

    const toggleReload = () => {
        setReload(!reload);
    }


    useEffect(() => {
        const fetchReviews = async () => {
            setLoading(true);
            setError(null);
            try {
                let response;
                if (source === 'media') {
                    response = await reviewService.getReviewsByMediaId(id, page);
                } else if (source === 'list') {
                    response = await moovieListReviewService.getMoovieListReviewsByListId(id, page);
                } else if (source === 'user') {
                    response = await reviewService.getMovieReviewsFromUser(username, page);
                }
                setReviews(response.data);
            } catch (error) {
                console.error("Error fetching reviews:", error);
                setError(error);
            } finally {
                setLoading(false);
            }
        };
        fetchReviews();
    }, [id, page, reload]);

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/${source}/${id}`,
            search: createSearchParams({ page: newPage.toString() }).toString(),
        });
    };

    return (
        <div className="reviews-container">
            {loading && <p>{t('reviews.loading')}</p>}
            {error && <p className="error">{t('reviews.error')} {error.message}</p>}
            {!loading && !error && reviews.length > 0 ? (
                reviews.map(review => (
                    <ReviewItem
                        key={review.id}
                        review={review}
                        source={source}
                        isLoggedIn={isLoggedIn}
                        currentUser={user}
                        reloadComments={() => handleParentReload()}
                        reloadReviews = {()=> toggleReload()}
                    />
                ))
            ) : (
                !loading && !error && <p>{t('reviews.noneFound')}</p>
            )}
            <div className="flex justify-center pt-4">
                <PaginationButton page={page} lastPage={5} setPage={handlePageChange} />
            </div>
        </div>
    );
}


export default Reviews;
