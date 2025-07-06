import api from './api.js';
import VndType from '../enums/VndType';
import * as url from "url";

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

  const likeReview = (url) => {
    return api.post(url);
  };

  const deleteLikeFromReview = (url) => {
    return api.delete(url);
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
    deleteLikeFromReview,
    likeReview,
  };
})();

export default reviewApi;
