import listApi from "../api/ListApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";

const ListService = (() => {

    const getLists = async ({search, ownerUsername, type, orderBy, order, pageNumber, pageSize}) =>{
        const res = await listApi.getLists({search, ownerUsername, type, orderBy, order, pageNumber, pageSize});
        return parsePaginatedResponse(res);
    }

    const getListById = async (id) => {
        const res = await listApi.getListById(id);
        return res;
    }

    const getListContentById= async ({id, orderBy, sortOrder, pageNumber, pageSize}) => {
        const res = await listApi.getListContentById({id, orderBy, sortOrder, pageNumber, pageSize});
        return parsePaginatedResponse(res);
    }

    const getMoovieListReviewsFromListId = async ({id,pageNumber}) => {
        const res = await listApi.getMoovieListReviewsFromListId({id,pageNumber});
        return parsePaginatedResponse(res);
    }

    const insertMediaIntoMoovieList = async ({id, mediaIds}) => {
        const res = await listApi.insertMediaIntoMoovieList({id,mediaIds});
        return res;
    }

    const getLikedOrFollowedListFromUser = async (username, type, orderBy, sortOrder, pageNumber) =>{
        const res = await listApi.getLikedOrFollowedListFromUser(username, type, orderBy, sortOrder, pageNumber);
        return parsePaginatedResponse(res);
    }

    const currentUserHasLiked = async (moovieListId) => {
        try {
            const res = await listApi.currentUserHasLiked(moovieListId);
            const parsedResponse = parsePaginatedResponse(res);
            if (!parsedResponse || res.status === 204) {
                return false;
            }
            return true;
        } catch (error) {
            return false;
        }
    };

    const currentUserHasFollowed = async (moovieListId) => {
        try {
            const res = await listApi.currentUserHasLFollowed(moovieListId);
            const parsedResponse = parsePaginatedResponse(res);
            if (!parsedResponse || res.status === 204) {
                return false;
            }
            return true;
        } catch (error) {
            return false;
        }
    };

    const currentUserLikeFollowStatus = async (moovieListId) => {
        try {
            const [likedStatus, followedStatus] = await Promise.all([
                currentUserHasLiked(moovieListId),
                currentUserHasFollowed(moovieListId)
            ]);

            console.log(likedStatus + " " + followedStatus)
            return {
                liked: likedStatus,
                followed: followedStatus
            };
        } catch (error) {
            return {
                liked: false,
                followed: false
            };
        }
    };

    const likeList = async (moovieListId) => {
        try {
            return await listApi.likeList(moovieListId)
        } catch (error){
            return null;
        }
    }

    const unlikeList = async (moovieListId) => {
        try {
            return await listApi.unlikeList(moovieListId)
        } catch (error){
            return null;
        }
    }

    const followList = async (moovieListId) => {
        try {
            return await listApi.followList(moovieListId)
        } catch (error){
            return null;
        }
    }

    const unfollowList = async (moovieListId) => {
        try {
            return await listApi.unfollowList(moovieListId)
        } catch (error){
            return null;
        }
    }



   return{
        getLists,
        getListById,
        getListContentById,
        getMoovieListReviewsFromListId,
        insertMediaIntoMoovieList,
        getLikedOrFollowedListFromUser,
        currentUserHasLiked,
        currentUserHasFollowed,
        currentUserLikeFollowStatus,
        likeList,
        unlikeList,
        unfollowList,
        followList
   }
})();

export default ListService;