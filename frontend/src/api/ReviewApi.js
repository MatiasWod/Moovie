import api from './api.js'

const reviewApi = (()=> {



    const getReviewById = (id) => {
        return api.get(`/reviews/${id}`);
    }

    const getReviewsByMediaId = (mediaId,page= 1) => {
        return api.get(`/reviews`,
        {
            params: {
                'mediaId': mediaId,
                'pageNumber': page
            }
        });
    }

    const getReviewsByMediaIdandUserId = (mediaId,userId) => {
        return api.get(`/reviews`,
            {
            params: {
                'mediaId': mediaId,
                'userId': userId
            }
        });
    }

    const getMovieReviewsFromUser = (username,page= 1) => {
        return api.get(`/reviews`,
            {
                params: {
                    'username': username,
                    'pageNumber': page
                }
            });
    }

    const editReview = ({mediaId,rating,reviewContent}) => {
        return api.put(`/reviews`,
            { rating: Number(rating), reviewContent: reviewContent },
            {
                params: {
                    'mediaId': mediaId
                }
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );
    }

    const createReview = ({mediaId, rating, reviewContent}) => {
        return api.post(`/reviews`,
            { rating: Number(rating), reviewContent: reviewContent },
            {
                params: {
                'mediaId': mediaId
                }
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );
    }


    const deleteReviewById = (id) => {
        return api.delete(`/reviews/${id}`);
    }


    const likeReview = (id) => {
        return api.post(`/reviews/${id}/like`);
    }




    return {
        getReviewById,
        getReviewsByMediaId,
        getReviewsByMediaIdandUserId,
        getMovieReviewsFromUser,
        editReview,
        createReview,
        deleteReviewById,
        likeReview,
    }

})();

export default reviewApi;