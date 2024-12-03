import userApi from "../api/UserApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";

const UserService = (() => {
    const getMilkyLeaderboard = async ({page, pageSize}) => {
        const res = await userApi.getMilkyLeaderboard({page, pageSize});
        return parsePaginatedResponse(res);
    }

    const getMovieReviewsFromUser = async (userId,page= 1) => {
        const res = await userApi.get(`/users/${userId}/reviews`);
        return parsePaginatedResponse(res);
    }

    const getSearchedUsers = async (username,orderBy,sortOrder,page=1) => {
        const res = await userApi.getSearchedUsers({username,orderBy,sortOrder,page});
        return parsePaginatedResponse(res);
    }

    const getLikedOrFollowedListFromUser = async (username, type, orderBy, sortOrder, pageNumber) =>{
        const res = await userApi.getLikedOrFollowedListFromUser(username, type, orderBy, sortOrder, pageNumber);
        return parsePaginatedResponse(res);
    }

    return {
        getMilkyLeaderboard,
        getMovieReviewsFromUser,
        getSearchedUsers,
        getLikedOrFollowedListFromUser
    }
})();

export default UserService;