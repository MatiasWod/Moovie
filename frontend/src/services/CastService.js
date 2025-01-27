import castApi from "../api/CastApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";

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

    return{
        getActorsForQuery,
        getMediasForActor,
        getMediasForDirector,
        getActorsByMediaId
    }
})();

export default CastService;