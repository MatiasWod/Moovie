import api from "./api";

const tvCreatorsApi = (() => {

    const getTvCreatorById = (id) => {
        return api.get(`/tvcreators/${id}`);
    }

    return{
        getTvCreatorById,
    }
})();

export default tvCreatorsApi;