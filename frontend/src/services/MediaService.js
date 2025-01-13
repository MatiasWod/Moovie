import mediaApi from "../api/MediaApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import api from "../api/api";
import mediaService from "./MediaService";
import userApi from "../api/UserApi";
import WatchlistWatched from "../api/values/WatchlistWatched";
import listApi from "../api/ListApi";
import MediaApi from "../api/MediaApi";

const MediaService = (() => {
    const getMedia = async ({type, page, pageSize, orderBy, sortOrder, search, providers, genres}) => {
        const res = await mediaApi.getMedia({type, page, pageSize, orderBy, sortOrder, search, providers, genres});
        return parsePaginatedResponse(res);
    }

    const getProvidersForMedia = async ({url}) => {
        const res = await mediaApi.getProvidersForMedia(url);
        return res;
    }

    const getMediaById = async (id) => {
        const res = await mediaApi.getMediaById(id);
        return parsePaginatedResponse(res);
    }

    const getMediaByIdList = async (idList) => {
        const res = await mediaApi.getMediaByIdList(idList);
        return res;
    }

    const getReviewsByMediaId = async (mediaId,page= 1) => {
        const res = await mediaApi.getReviewsByMediaId(mediaId);
        return parsePaginatedResponse(res);
    }

    const getReviewsByMediaIdandUserId = async (mediaId,userId) => {
        const res = await mediaApi.getReviewsByMediaIdandUserId(mediaId,userId);
        return res;
    }


    const getActorsByMediaId = async (mediaId) =>{
        const res = await mediaApi.getActorsInMedia(mediaId);
        return parsePaginatedResponse(res);
    }

    const getTvCreatorsByMediaId = async (mediaId) =>{
        const res = await mediaApi.getTvCreatorsByMediaId(mediaId);
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

    const getIdMediaFromObjectList = (list) => {
        let toRet = "";
        for (const m of list) {
            toRet += m.mediaId + ",";
        }
        return toRet.slice(0, -1); // Removes the last comma
    };




    return {
        getMedia,
        getProvidersForMedia,
        getMediaById,
        getMediaByIdList,
        getReviewsByMediaId,
        getReviewsByMediaIdandUserId,
        getActorsByMediaId,
        getTvCreatorsByMediaId,
        createReview,
        currentUserWWStatus,
        userWWStatus,
        insertMediaIntoWW,
        removeMediaFromWW,
        getIdMediaFromObjectList
    }
})();

export default MediaService;