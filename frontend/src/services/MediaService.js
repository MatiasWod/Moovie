import mediaApi from "../api/MediaApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";

const MediaService = (() => {
    const getMedia = async ({type, page, pageSize, orderBy, sortOrder, search}) => {
        const res = await mediaApi.getMedia({type, page, pageSize, orderBy, sortOrder, search});
        return parsePaginatedResponse(res)
    }
    return {
        getMedia
    }
})();

export default MediaService;