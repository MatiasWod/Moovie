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

  const deleteMoovieListReviewByUrl = async (url) => {
    const res = await moovieListReviewApi.deleteMoovieListReviewByUrl(url);
    return res;
  }


  const likeMoovieListReview = async (url) => {
    const res = await moovieListReviewApi.likeMoovieListReview(url);
    return res;
  };

  const deleteLikeFromMoovieListReview = async (url, username) => {
    const res = await moovieListReviewApi.deleteLikeFromMoovieListReview(url, username);
    return res;
  };

  return {
    editReview,
    createMoovieListReview,
    likeMoovieListReview,
    deleteMoovieListReviewByUrl,
    deleteLikeFromMoovieListReview
  };
})();

export default MoovieListReviewService;
