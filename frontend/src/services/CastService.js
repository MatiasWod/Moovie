import castApi from "../api/CastApi";
import api from "../api/api";

const CastService = (() => {

    const getActorsForQuery = async (search) => {
        return await castApi.getActorsForQuery(search);
    }

    const getMediasForActor = async (id) => {
        return await castApi.getMediasForActor(id);
    }

    return{
        getActorsForQuery,
        getMediasForActor
    }
})();

export default CastService;