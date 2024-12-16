import listApi from "../api/ListApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import userApi from "../api/UserApi";
import store from "../store/store";

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

    const currentUserHasLiked = async (moovieListId, username) => {
        try {
            const res = await userApi.currentUserHasLikedList(moovieListId, username);
            const parsedResponse = parsePaginatedResponse(res);
            if (!parsedResponse || res.status === 204) {
                return false;
            }
            return true;
        } catch (error) {
            return false;
        }
    };

    const currentUserHasFollowed = async (moovieListId, username) => {
        try {
            const res = await userApi.currentUserHasFollowedList(moovieListId, username);
            const parsedResponse = parsePaginatedResponse(res);
            if (!parsedResponse || res.status === 204) {
                return false;
            }
            return true;
        } catch (error) {
            return false;
        }
    };

    const currentUserLikeFollowStatus = async (moovieListId, username) => {
        try {

            const [likedStatus, followedStatus] = await Promise.all([
                currentUserHasLiked(moovieListId, username),
                currentUserHasFollowed(moovieListId, username)
            ]);

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

    const likeList = async (moovieListId, username) => {
        try {
            return await userApi.likeList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const unlikeList = async (moovieListId, username) => {
        try {
            return await userApi.unlikeList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const followList = async (moovieListId, username) => {
        try {
            return await userApi.followList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const unfollowList = async (moovieListId, username) => {
        try {
            return await userApi.unfollowList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const getRecommendedLists = async (id) => {
        return await listApi.getRecommendedLists(id);
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
        followList,
       getRecommendedLists
   }
})();

export default ListService;