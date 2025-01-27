import castApi from "../api/CastApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import mediaApi from "../api/MediaApi";

const CastService = (() => {

    const getActorsForQuery = async (search) => {
        return await castApi.getActorsForQuery(search);
    }

    const getMediasForActor = async (id) => {
        return await castApi.getMediasForActor(id);
    }

    const getMediasForDirector = async (id) => {
        return await castApi.getMediasForDirector(id);
    }

    const getActorsByMediaId = async (mediaId) =>{
        const res = await castApi.getActorsByMediaId(mediaId);
        return parsePaginatedResponse(res);
    }

    const getTvCreatorById = async (id) => {
        return await castApi.getTvCreatorById(id);
    }

    const getTvCreatorsSearch = async (search) => {
        return await castApi.getTvCreatorsSearch(search);
    }

    const getTvCreatorsByMediaId = async (mediaId) =>{
        const res = await castApi.getTvCreatorsByMediaId(mediaId);
        return parsePaginatedResponse(res);
    }

    return{
        getActorsForQuery,
        getMediasForActor,
        getMediasForDirector,
        getActorsByMediaId,
        getTvCreatorById,
        getTvCreatorsSearch,
        getTvCreatorsByMediaId
    }
})();

export default CastService;