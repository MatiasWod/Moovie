import api from './api.js'
import search from "../pages/views/search";

const mediaApi = (()=> {

    const getMedia = ({type, page, pageSize, orderBy, sortOrder, search, providers, genres}) => {
        return api.get('/media',
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

    const getMediaById = (id) => {
        return api.get(`/media/${id}`);
    }

    const getReviewsByMediaId = (mediaId,page= 1) => {
        return api.get(`/media/${mediaId}/reviews`);
    }

    const getActorsInMedia = (mediaId) =>{
        return api.get(
            `media/${mediaId}/actors`
        )
    }

    const createReview = ({mediaId, rating, reviewContent}) => {
        return api.post(`/media/${Number(mediaId)}/review`,
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
        getMediaById,
        getReviewsByMediaId,
        getActorsInMedia,
        createReview
    }

})();

export default mediaApi;