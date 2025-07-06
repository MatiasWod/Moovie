import moovieListReviewApi from '../api/MoovieListReviewApi';
import reviewApi from '../api/ReviewApi';

const MoovieListReviewService = (() => {

  const editReview = async (url, listId, reviewContent) => {
    const res = await moovieListReviewApi.editReview(url, listId, reviewContent);
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
    editReview,
    createMoovieListReview,
    deleteMoovieListReview,
    likeMoovieListReview,
    deleteLikeFromMoovieListReview
  };
})();

export default MoovieListReviewService;
