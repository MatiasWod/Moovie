import api from './api.js';
import VndType from '../enums/VndType';

const moovieListReviewApi = (() => {
  const getMoovieListReviewById = (id) => {
    return api.get(`/moovieListReviews/${id}`);
  };

  const getMoovieListReviewsFromUserId = (userId, page = 1) => {
    return api.get(`/moovieListReviews`, {
      params: {
        userId: userId,
        pageNumber: page,
      },
    });
  };

  const editReview = (id, listId, reviewContent) => {
    return api.put(
      `/moovieListReviews/${id}`,
      {
        reviewContent: reviewContent,
        listId: listId,
      },
      {
        headers: {
          'Content-Type': VndType.APPLICATION_MOOVIELIST_REVIEW_FORM,
        },
      }
    );
  };

  const createMoovieListReview = (id, reviewContent) => {
    return api.post(
      `/moovieListReviews`,
      {
        reviewContent: reviewContent,
        listId: id,
      },
      {
        headers: {
          'Content-Type': VndType.APPLICATION_MOOVIELIST_REVIEW_FORM,
        },
      }
    );
  };

  const deleteMoovieListReviewById = (id) => {
    return api.delete(`/moovieListReviews/${id}`);
  };

  const likeMoovieListReview = (url, username) => {
    return api.post(url + `/${username}`);
  };

  const deleteLikeFromMoovieListReview = (url, username) => {
    return api.delete(url + `/${username}`);
  }

  return {
    getMoovieListReviewById,
    getMoovieListReviewsFromUserId,
    editReview,
    createMoovieListReview,
    deleteMoovieListReviewById,
    likeMoovieListReview,
    deleteLikeFromMoovieListReview
  };
})();

export default moovieListReviewApi;
