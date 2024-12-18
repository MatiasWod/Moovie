import mediaApi from "../api/MediaApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import api from "../api/api";
import mediaService from "./MediaService";
import userApi from "../api/UserApi";
import WatchlistWatched from "../api/values/WatchlistWatched";

const MediaService = (() => {
    const getMedia = async ({type, page, pageSize, orderBy, sortOrder, search, providers, genres}) => {
        const res = await mediaApi.getMedia({type, page, pageSize, orderBy, sortOrder, search, providers, genres});
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

    const currentUserWWStatus = async (mediaId, username) => {
        try {
            const [watchedStatus, watchlistStatus] = await Promise.all([
                userWWStatus(WatchlistWatched.Watched, mediaId, username),
                userWWStatus(WatchlistWatched.Watchlist, mediaId, username)
            ]);


            return {
                watched: watchedStatus,
                watchlist: watchlistStatus
            };
        } catch (error) {
            return {
                watched: false,
                watchlist: false
            };
        }
    }

    const userWWStatus = async (ww, mediaId, username) => {
        try{
            const res = await userApi.currentUserWW(ww, username, mediaId);
            const parsedResponse = parsePaginatedResponse(res);
            if (!parsedResponse || res.status === 204) {
                return false;
            }
            return true;
        } catch (error){
            return false;
        }
    }

    const insertMediaIntoWW = async (ww, mediaId, username) =>{
        try {
            return await userApi.insertMediaIntoWW(ww, username, mediaId)
        } catch (error){
            return null;
        }
    }

    const removeMediaFromWW = async (ww, mediaId, username) => {
        try {
            return await userApi.removeMediaFromWW(ww, username, mediaId)
        } catch (error){
            return null;
        }
    }




    return {
        getMedia,
        getMediaById,
        getReviewsByMediaId,
        getActorsByMediaId,
        createReview,
        currentUserWWStatus,
        userWWStatus,
        insertMediaIntoWW,
        removeMediaFromWW
    }
})();

export default MediaService;