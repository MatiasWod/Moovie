import api from './api.js';
import VndType from '../enums/VndType';
import * as url from "url";

const reviewApi = (() => {
  const getReviewById = (id) => {
    return api.get(`/reviews/${id}`);
  };

  const getReviewsByMediaIdandUrl = (url, mediaId) => {
    return api.get(url, {
      params: {
        mediaId: mediaId,
      },
    });
  };

  const editReview = ({ url, mediaId, rating, reviewContent}) => {
    return api.put(
      url,
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

  const deleteReviewByUrl = (url) => {
    return api.delete(url);
  };

  return {
    getReviewById,
    getReviewsByMediaIdandUrl,
    editReview,
    createReview,
    deleteReviewByUrl,
    deleteLikeFromReview,
    likeReview,
  };
})();

export default reviewApi;
