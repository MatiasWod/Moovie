import tvCreatorsApi from "../api/TvCreatorsApi";

const TVCreatorsService = (() => {
    const getTvCreatorById = async (id) => {
        return await tvCreatorsApi.getTvCreatorById(id);
    }

    const getMediasForTVCreator = async (id) => {
        return await tvCreatorsApi.getMediasForTVCreator(id);
    }

    return{
        getTvCreatorById,
        getMediasForTVCreator
    }
})();

export default TVCreatorsService;