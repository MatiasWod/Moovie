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

    const getDirectorsForQuery = ({search}) => {
        return api.get('/directors',
            {
                params: {
                    'search': search
                }
            }
        );
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
        getDirectorsForQuery,
        getTvCreatorById,
        getTvCreatorsSearch,
        getTvCreatorsByMediaId
    }
})();

export default castApi;