import api from "./api";

const castApi = (() => {

    const getActorsForQuery = ({search}) => {
        return api.get('/actors',
            {
                params: {
                    'search': search
                }
            }
        );
    }


    const getActorsByMediaId = (mediaId) =>{
        return api.get(
            `/actors`,
            {
                params: {
                    mediaId: mediaId
                }
            }
        )
    }


    const getTvCreatorById = (id) => {
        return api.get(`/tvCreators/${id}`);
    }

    const getTvCreatorsSearch = (search) => {
        return api.get(`/tvCreators`,
            {
                params: {
                    search: search
                }
            }
        )
    }

    const getTvCreatorsByMediaId = (mediaId) =>{
        return api.get(
            `/tvCreators`,
            {
                params: {
                    mediaId: mediaId
                }
            }
        )
    }

    return{
        getActorsForQuery,
        getActorsByMediaId,
        getTvCreatorById,
        getTvCreatorsSearch,
        getTvCreatorsByMediaId
    }
})();

export default castApi;