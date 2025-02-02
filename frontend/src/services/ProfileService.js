import {parsePaginatedResponse} from "../utils/ResponseUtils";
import profileApi from "../api/ProfileApi";
import WatchlistWatched from "../api/values/WatchlistWatched";

const ProfileService = (() => {
    const getMilkyLeaderboard = async ({page, pageSize}) => {
        const res = await profileApi.getMilkyLeaderboard({page, pageSize});
        return parsePaginatedResponse(res);
    }

    const getSearchedUsers = async ({username,orderBy,sortOrder,page=1}) => {
        const res = await profileApi.getSearchedUsers({username,orderBy,sortOrder,page});
        return parsePaginatedResponse(res);
    }

    const setPfp = async ({username, pfp}) =>{
        return await profileApi.setPfp(username, pfp);
    }

    const getSpecialListFromUser = async ({username, type, orderBy, order, pageNumber}) => {
        const res = await profileApi.getSpecialListFromUser(username,type,orderBy,order,pageNumber);
        return parsePaginatedResponse(res);
    }


    const getLikedOrFollowedListFromUser = async (username, type, orderBy, sortOrder, pageNumber) =>{
        const res = await profileApi.getLikedOrFollowedListFromUser(username, type, orderBy, sortOrder, pageNumber);
        return parsePaginatedResponse(res);
    }

    const currentUserHasLiked = async (moovieListId, username) => {
        try {
            const res = await profileApi.currentUserHasLikedList(moovieListId, username);
            const parsedResponse = parsePaginatedResponse(res);
            if (!parsedResponse || res.status === 204) {
                return false;
            }
            return true;
        } catch (error) {
            return false;
        }
    };

    const likeList = async (moovieListId, username) => {
        try {
            return await profileApi.likeList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const unlikeList = async (moovieListId, username) => {
        try {
            return await profileApi.unlikeList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const currentUserHasFollowed = async (moovieListId, username) => {
        try {
            const res = await profileApi.currentUserHasFollowedList(moovieListId, username);
            const parsedResponse = parsePaginatedResponse(res);
            if (!parsedResponse || res.status === 204) {
                return false;
            }
            return true;
        } catch (error) {
            return false;
        }
    };

    const followList = async (moovieListId, username) => {
        try {
            return await profileApi.followList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const unfollowList = async (moovieListId, username) => {
        try {
            return await profileApi.unfollowList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const userWWStatus = async (ww, mediaId, username) => {
        try{
            const res = await profileApi.currentUserWW(ww, username, mediaId);
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
            return await profileApi.insertMediaIntoWW(ww, username, mediaId)
        } catch (error){
            return null;
        }
    }

    const removeMediaFromWW = async (ww, mediaId, username) => {
        try {
            return await profileApi.removeMediaFromWW(ww, username, mediaId)
        } catch (error){
            return null;
        }
    }

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

    return {
        getMilkyLeaderboard,
        getSearchedUsers,
        setPfp,
        getSpecialListFromUser,
        getLikedOrFollowedListFromUser,
        currentUserHasLiked,
        likeList,
        unlikeList,
        currentUserHasFollowed,
        followList,
        unfollowList,
        userWWStatus,
        insertMediaIntoWW,
        removeMediaFromWW,
        currentUserLikeFollowStatus,
        currentUserWWStatus

    }
});

export default ProfileService;

