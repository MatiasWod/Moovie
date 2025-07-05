import api from './api.js';
import VndType from '../enums/VndType';

const reviewApi = (() => {
  const getReviewById = (id) => {
    return api.get(`/reviews/${id}`);
  };

  const getReviewsByMediaId = (mediaId, page = 1) => {
    return api.get(`/reviews`, {
      params: {
        mediaId: mediaId,
        pageNumber: page,
      },
    });
  };

  const getReviewsByMediaIdandUsername = (mediaId, username) => {
    return api.get(`/reviews`, {
      params: {
        mediaId: mediaId,
        username: username,
      },
    });
  };

  const getMovieReviewsFromUser = (username, page = 1) => {
    return api.get(`/reviews`, {
      params: {
        username: username,
        pageNumber: page,
      },
    });
  };

  const editReview = ({ mediaId, rating, reviewContent, reviewId }) => {
    return api.put(
      `/reviews/${reviewId}`,
      { mediaId: mediaId, rating: Number(rating), reviewContent: reviewContent },
      {
        headers: {
          'Content-Type': VndType.APPLICATION_REVIEW_FORM,
        },
      }
    );
  };

  const createReview = ({ mediaId, rating, reviewContent }) => {
    return api.post(
      `/reviews`,
      { mediaId: mediaId, rating: Number(rating), reviewContent: reviewContent },
      {
        headers: {
          'Content-Type': VndType.APPLICATION_REVIEW_FORM,
        },
      }
    );
  };

  const likeReview = (username, id) => {
    return api.put(
      `/reviews/${id}`,
      { username: username, feedbackType: 'LIKE' },
      {
        headers: {
          'Content-Type': VndType.APPLICATION_MOOVIELIST_FEEDBACK_FORM,
        },
      }
    );
  };

  const deleteReviewById = (id) => {
    return api.delete(`/reviews/${id}`);
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

export default reviewApi;
