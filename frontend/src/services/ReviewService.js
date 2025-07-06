import reviewApi from '../api/ReviewApi';
import { parsePaginatedResponse } from '../utils/ResponseUtils';
import mediaApi from '../api/MediaApi';
import userApi from '../api/UserApi';

const ReviewService = (() => {
  const getReviewById = async (id) => {
    const response = await reviewApi.getReviewById(id);
    return response;
  };

  const getReviewsByMediaIdandUrl = async (url, mediaId) => {
    const res = await reviewApi.getReviewsByMediaIdandUrl(url, mediaId);
    return res;
  };

  const editReview = async (url, mediaId, rating, reviewContent) => {
    const res = await reviewApi.editReview({ url, mediaId, rating, reviewContent });
    return res;
  };

  const createReview = async (mediaId, rating, reviewContent) => {
    const res = await reviewApi.createReview({ mediaId, rating, reviewContent });
    return res;
  };

  const deleteReviewByUrl = async (url) => {
    const response = await reviewApi.deleteReviewByUrl(url);
    return response;
  };

  const likeReview = async (url) => {
    const response = await reviewApi.likeReview(url);
    return response;
  };


  const deleteLikeFromReview = async (url) => {
    const response = await reviewApi.deleteLikeFromReview(url);
    return response;
  }

  return {
    getReviewById,
    getReviewsByMediaIdandUrl,
    editReview,
    createReview,
    deleteReviewByUrl,
    likeReview,
    deleteLikeFromReview
  };
})();

export default ReviewService;
