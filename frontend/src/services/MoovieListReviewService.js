import moovieListReviewApi from "../api/MoovieListReviewApi";

const MoovieListReviewService = ( () => {
    const getMoovieListReview = async (id) => {
        const res = await moovieListReviewApi.getMoovieListReviewById(id);
        return res;
    }

    const getMoovieListReviewsByListId = async (id,pageNumber) => {
        const res = await moovieListReviewApi.getMoovieListReviewsByListId(id,pageNumber);
        return res;
    }

    const getMoovieListReviewsFromUserId = async (userId,pageNumber) => {
        const res = await moovieListReviewApi.getMoovieListReviewsFromUserId(userId,pageNumber);
        return res;
    }


    const editReview = async (id,reviewContent) => {
        const res = await moovieListReviewApi.editReview(id,reviewContent);
        return res;
    }

    const createMoovieListReview = async (id,reviewContent) => {
        const res = await moovieListReviewApi.createMoovieListReview(id,reviewContent);
        return res;
    }

    const deleteMoovieListReview = async (id) => {
        const res = await moovieListReviewApi.deleteMoovieListReviewById(id);
        return res;
    }

    const likeMoovieListReview = async (id) => {
        const res = await moovieListReviewApi.likeMoovieListReview(id);
        return res;
    }

    return {
        getMoovieListReview,
        getMoovieListReviewsByListId,
        getMoovieListReviewsFromUserId,
        editReview,
        createMoovieListReview,
        deleteMoovieListReview,
        likeMoovieListReview
    }
})();

export default MoovieListReviewService;