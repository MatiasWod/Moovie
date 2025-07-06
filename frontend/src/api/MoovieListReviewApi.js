import api from './api.js';
import VndType from '../enums/VndType';

const moovieListReviewApi = (() => {

  const editReview = (url, listId, reviewContent) => {
    return api.put(
      url,
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
    editReview,
    createMoovieListReview,
    deleteMoovieListReviewById,
    likeMoovieListReview,
    deleteLikeFromMoovieListReview
  };
})();

export default moovieListReviewApi;
