import userApi from "../api/UserApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import MediaService from "./MediaService";

const UserService = (() => {

    const getSpecialListFromUser = async ({username, type, orderBy, order, pageNumber}) => {
        const res = await userApi.getSpecialListFromUser(username,type,orderBy,order,pageNumber);
        return parsePaginatedResponse(res);
    }

    return {
        getSpecialListFromUser,
    }
})();

export default UserService;