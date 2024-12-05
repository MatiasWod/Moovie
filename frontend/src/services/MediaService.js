import mediaApi from "../api/MediaApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import api from "../api/api";
import mediaService from "./MediaService";
import userApi from "../api/UserApi";

const MediaService = (() => {
    const getMedia = async ({type, page, pageSize, orderBy, sortOrder, search}) => {
        const res = await mediaApi.getMedia({type, page, pageSize, orderBy, sortOrder, search});
        return parsePaginatedResponse(res)
    }

    const getMediaById = async (id) => {
        const res = await mediaApi.getMediaById(id);
        return parsePaginatedResponse(res);
    }

    const getReviewsByMediaId = async (mediaId,page= 1) => {
        const res = await mediaApi.getReviewsByMediaId(mediaId);
        return parsePaginatedResponse(res);
    }

    const getActorsByMediaId = async (mediaId) =>{
        const res = await mediaApi.getActorsInMedia(mediaId);
        return parsePaginatedResponse(res);
    }

    const createReview = async (mediaId,rating, reviewContent) => {
        const res = await mediaApi.createReview({mediaId,rating,reviewContent});
        return res;
    }





    return {
        getMedia,
        getMediaById,
        getReviewsByMediaId,
        getActorsByMediaId,
        createReview
    }
})();

export default MediaService;