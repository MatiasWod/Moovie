import api from './api.js'
import search from "../pages/views/search";

const mediaApi = (()=> {

    const getMedia = ({type, page, pageSize, orderBy, sortOrder, search}) => {
        return api.get('/media',
            {
                params: {
                    'type': type,
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
                    'pageNumber': page,
                    'pageSize': pageSize,
                    'search': search
                }
            });
    }

    const getMediaById = (id) => {
        return api.get(`/media/${id}`);
    }

    const getReviewsByMediaId = (mediaId,page= 1) => {
        return api.get(`/media/${mediaId}/reviews`);
    }

    // const createReview = (mediaId,page=1) => {
    //     return api.post(`/media/${mediaId}/review`, page);
    // }



    return {
        getMedia,
        getMediaById,
        getReviewsByMediaId,
    }

})();

export default mediaApi;