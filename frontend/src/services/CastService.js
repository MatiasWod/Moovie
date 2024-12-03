import castApi from "../api/CastApi";

const CastService = (() => {

    const getActorsForQuery = async ({search}) => {
        return await castApi.getActorsForQuery(search);
    }

    return{
        getActorsForQuery
    }
})();

export default CastService;