import api from './api.js'
import search from "../pages/views/search";

const mediaApi = (()=> {

    const getMedia = ({type, page, pageSize, orderBy, sortOrder, search, providers, genres}) => {
        return api.get('/medias/search',
            {
                params: {
                    'type': type,
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
                    'pageNumber': page,
                    'pageSize': pageSize,
                    'search': search,
                    'providers': providers,
                    'genres': genres
                }
            });
    }

    const getMediaByIdList = (idListString) => {
        return api.get(`/medias?ids=${idListString}`);
    };

    const getProvidersForMedia = (url) => {
        return api.get(url);
    }

    const getMediaById = (id) => {
        return api.get(`/medias/${id}`);
    }

    const getReviewsByMediaId = (mediaId,page= 1) => {
        return api.get(`/medias/${mediaId}/reviews`);
    }

    const getReviewsByMediaIdandUserId = (mediaId,userId) => {
        return api.get(`/medias/${mediaId}/review/user/${userId}`);
    }

    const getActorsInMedia = (mediaId) =>{
        return api.get(
            `medias/${mediaId}/actors`
        )
    }

    const getTvCreatorsByMediaId = (mediaId) =>{
        return api.get(
            `medias/${mediaId}/tvcreators`
        )
    }

    const createReview = ({mediaId, rating, reviewContent}) => {
        return api.post(`/medias/${Number(mediaId)}/review`,
            { rating: Number(rating), reviewContent: reviewContent },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );
    }

    const editReview = ({mediaId,rating,reviewContent}) => {
        return api.put(`/medias/${Number(mediaId)}/review`,
            { rating: Number(rating), reviewContent: reviewContent },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );
    }



    return {
        getMedia,
        getProvidersForMedia,
        getMediaById,
        getMediaByIdList,
        getReviewsByMediaId,
        getReviewsByMediaIdandUserId,
        getActorsInMedia,
        getTvCreatorsByMediaId,
        createReview,
        editReview
    }

})();

export default mediaApi;