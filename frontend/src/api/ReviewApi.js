import api from './api.js'

const reviewApi = (()=> {



    const getReviewById = (id) => {
        return api.get(`/review/${id}`);
    }

    const deleteReviewById = (id) => {
        return api.delete(`/review/${id}`);
    }

    const likeReview = (id) => {
        return api.post(`/review/${id}/like`);
    }


    return {
        getReviewById,
        deleteReviewById,
        likeReview,
    }

})();

export default reviewApi;