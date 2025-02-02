import {parsePaginatedResponse} from "../utils/ResponseUtils";
import profileApi from "../api/ProfileApi";

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


    return {
        getMilkyLeaderboard,
        getSearchedUsers,
        setPfp,
        getSpecialListFromUser
    }
});

export default ProfileService;

