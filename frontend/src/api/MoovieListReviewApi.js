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

  const deleteMoovieListReviewByUrl = (url) => {
    return api.delete(url);
  };

  const likeMoovieListReview = (url) => {
    return api.post(url);
  };

  const deleteLikeFromMoovieListReview = (url, username) => {
    return api.delete(url + `/${username}`);
  }

  return {
    editReview,
    createMoovieListReview,
    deleteMoovieListReviewByUrl,
    likeMoovieListReview,
    deleteLikeFromMoovieListReview
  };
})();

export default moovieListReviewApi;
