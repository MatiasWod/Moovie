import reviewApi from '../api/ReviewApi';
import { parsePaginatedResponse } from '../utils/ResponseUtils';
import mediaApi from '../api/MediaApi';
import userApi from '../api/UserApi';

const ReviewService = (() => {
  const getReviewById = async (id) => {
    const response = await reviewApi.getReviewById(id);
    return response;
  };

  const getReviewsByMediaId = async (mediaId, page = 1) => {
    const res = await reviewApi.getReviewsByMediaId(mediaId);
    return parsePaginatedResponse(res);
  };

  const getReviewsByMediaIdandUsername = async (mediaId, username) => {
    const res = await reviewApi.getReviewsByMediaIdandUsername(mediaId, username);
    return res;
  };

  const getMovieReviewsFromUser = async (username, page = 1) => {
    const res = await reviewApi.getMovieReviewsFromUser(username, page);
    return parsePaginatedResponse(res);
  };

  const editReview = async (mediaId, rating, reviewContent, reviewId) => {
    const res = await reviewApi.editReview({ mediaId, rating, reviewContent, reviewId });
    return res;
  };

  const createReview = async (mediaId, rating, reviewContent) => {
    const res = await reviewApi.createReview({ mediaId, rating, reviewContent });
    return res;
  };

  const deleteReviewById = async (id) => {
    const response = await reviewApi.deleteReviewById(id);
    return response;
  };

  const likeReview = async (username, id) => {
    const response = await reviewApi.likeReview(username, id);
    return response;
  };

  return {
    getReviewById,
    getReviewsByMediaId,
    getReviewsByMediaIdandUsername,
    getMovieReviewsFromUser,
    editReview,
    createReview,
    deleteReviewById,
    likeReview,
  };
})();

export default ReviewService;
