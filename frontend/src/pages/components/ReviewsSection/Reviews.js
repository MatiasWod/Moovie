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


const ReviewItem = ({ review, source, isLoggedIn, currentUser, handleReport, handleDelete, reloadComments }) => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [reportedReviewId, setReportedReviewId] = useState(null);
    const [commentLoading, setCommentLoading] = useState(false);


    const handleCommentSubmit = async (reviewId, comment) => {
        try {
            setCommentLoading(true);
            if (source === 'media') {
                await commentApi.createReviewComment(reviewId, comment);
            } else if (source === 'list') {
                await moovieListReviewApi.createListReviewComment(reviewId, comment);
            }
            reloadComments();
        } catch (error) {
            console.error("Error creating comment:", error);
        } finally {
            setCommentLoading(false);
        }
    };

    console.log(review);




    return (
        <div key={review.id} className="review container-fluid bg-white my-3">
            <div className="review-header d-flex align-items-center justify-between">
                <div>
                    <ProfileImage
                        username={ review.username }
                        size="100px"
                        onClick={() => navigate(`/profile/${review.username}`)}
                    />
                    <strong>{review.username}</strong>
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
                    {(isLoggedIn && review.username === currentUser.username) &&(
                        <button
                            className="btn btn-danger btn-sm"
                            onClick={() => handleDelete(review.id)}
                        >
                            <i className="bi bi-trash"></i>
                        </button>
                    )}
                </div>
            </div>
            <div className="review-content">{review.reviewContent}</div>
            {source === 'media' && (
                <>
                    <CommentList reviewId={review.id} />
                    {isLoggedIn && (
                        <CommentField onSubmit={(comment) => handleCommentSubmit(review.id, comment)} isLoading={commentLoading} />
                    )}
                </>
            )}
            <Divider />
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

    useEffect(() => {
        const fetchReviews = async () => {
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
            }
        };
        fetchReviews();
    }, [id, page]);

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/${source}/${id}`,
            search: createSearchParams({ page: newPage.toString() }).toString(),
        });
    };

    return (
        <div className="reviews-container">
            { reviews && reviews.length > 0 ? (
                reviews.map(review => (
                    <ReviewItem
                        key={review.id}
                        review={review}
                        source={source}
                        isLoggedIn={isLoggedIn}
                        currentUser={user}
                        handleDelete={() => handleParentReload()}
                        reloadComments={() => handleParentReload()}
                    />
                ))
            ) : (
                <p>{t('reviews.noneFound')}</p>
            )}
            <div className="flex justify-center pt-4">
                <PaginationButton page={page} lastPage={5} setPage={handlePageChange} />
            </div>
        </div>
    );
}

export default Reviews;
