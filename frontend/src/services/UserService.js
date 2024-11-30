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

    return {
        getMilkyLeaderboard,
        getMovieReviewsFromUser
    }
})();

export default UserService;