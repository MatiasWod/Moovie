import api from './api.js'

const moovieListReviewApi = (()=> {



    const getMoovieListReviewById = (id) => {
        return api.get(`/moovieListReviews/${id}`);
    }

    const getMoovieListReviewsByListId = (id,page=1) => {
        return api.get(`/moovieListReviews`,
            {params:
                    {
                        listId: id,
                        pageNumber: page
                    }
            });
    }

    const getMoovieListReviewsFromUserId = (userId,page=1) => {
        return api.get(`/moovieListReviews`,
            {
                params:{
                    'userId': userId,
                    'pageNumber': page,
                }
            });
    }

    const editReview = (id,reviewContent) => {
        return api.put(`/moovieListReviews}`,
            {
                reviewContent: reviewContent
            },
        {
                params:{
                    'listId': id
                }
        },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
            );
    }

    const createMoovieListReview = (id,reviewContent) => {
        return api.post(`/moovieListReviews`,
            {
                reviewContent : reviewContent
            },
            {params:
                    {
                        listId:id,
                    }
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
    }

    const deleteMoovieListReviewById = (id) => {
        return api.delete(`/moovieListReviews/${id}`);
    }

    const likeMoovieListReview = (id) => {
        return api.post(`/moovieListReviews/${id}/like`);
    }


    return {
        getMoovieListReviewById,
        getMoovieListReviewsByListId,
        getMoovieListReviewsFromUserId,
        editReview,
        createMoovieListReview,
        deleteMoovieListReviewById,
        likeMoovieListReview
    }

})();

export default moovieListReviewApi;