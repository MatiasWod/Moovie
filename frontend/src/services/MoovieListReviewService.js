import moovieListReviewApi from '../api/MoovieListReviewApi';
import reviewApi from '../api/ReviewApi';

const MoovieListReviewService = (() => {
  const getMoovieListReview = async (id) => {
    const res = await moovieListReviewApi.getMoovieListReviewById(id);
    return res;
  };

  const getMoovieListReviewsFromUserId = async (userId, pageNumber) => {
    const res = await moovieListReviewApi.getMoovieListReviewsFromUserId(userId, pageNumber);
    return res;
  };

  const editReview = async (id, listId, reviewContent) => {
    const res = await moovieListReviewApi.editReview(id, listId, reviewContent);
    return res;
  };

  const createMoovieListReview = async (id, reviewContent) => {
    const res = await moovieListReviewApi.createMoovieListReview(id, reviewContent);
    return res;
  };

  const deleteMoovieListReview = async (id) => {
    const res = await moovieListReviewApi.deleteMoovieListReviewById(id);
    return res;
  };

  const likeMoovieListReview = async (url, username) => {
    const res = await moovieListReviewApi.likeMoovieListReview(url, username);
    return res;
  };

  const deleteLikeFromMoovieListReview = async (url, username) => {
    const res = await moovieListReviewApi.likeMoovieListReview(url, username);
    return res;
  };

  return {
    getMoovieListReview,
    getMoovieListReviewsFromUserId,
    editReview,
    createMoovieListReview,
    deleteMoovieListReview,
    likeMoovieListReview,
    deleteLikeFromMoovieListReview
  };
})();

export default MoovieListReviewService;
