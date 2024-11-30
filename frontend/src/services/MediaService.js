import mediaApi from "../api/MediaApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import api from "../api/api";

const MediaService = (() => {
    const getMedia = async ({type, page, pageSize, orderBy, sortOrder, search}) => {
        const res = await mediaApi.getMedia({type, page, pageSize, orderBy, sortOrder, search});
        return parsePaginatedResponse(res)
    }

    const getMediaById = async (id) => {
        const res = await api.get(`/media/${id}`);
        return res;
    }

    const getReviewsByMediaId = async (mediaId,page= 1) => {
        const res = await api.get(`/media/${mediaId}/reviews`);
        return parsePaginatedResponse(res);
    }

    return {
        getMedia,
        getMediaById,
        getReviewsByMediaId
    }
})();

export default MediaService;