import castApi from "../api/CastApi";

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

    return{
        getActorsForQuery,
        getMediasForActor,
        getMediasForDirector
    }
})();

export default CastService;