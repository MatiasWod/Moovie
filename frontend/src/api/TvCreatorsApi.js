import api from "./api";

const tvCreatorsApi = (() => {

    const getTvCreatorById = (id) => {
        return api.get(`/tvcreators/${id}`);
    }

    const getMediasForTVCreator = ({id}) => {
        return api.get(`/tvcreators/${id}/medias`);
    }

    return{
        getTvCreatorById,
        getMediasForTVCreator
    }
})();

export default tvCreatorsApi;