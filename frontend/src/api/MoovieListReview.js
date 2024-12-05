import api from './api.js'

const moovieListReviewApi = (()=> {



    const getMoovieListReviewById = (id) => {
        return api.get(`/moovieListReview/${id}`);
    }

    const deleteMoovieListReviewById = (id) => {
        return api.delete(`/moovieListReview/${id}`);
    }

    const likeMoovieListReview = (id) => {
        return api.post(`/moovieListReview/${id}/like`);
    }


    return {
        getMoovieListReviewById,
        deleteMoovieListReviewById,
        likeMoovieListReview
    }

})();

export default moovieListReviewApi;