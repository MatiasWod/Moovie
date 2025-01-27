import api from "./api";

const castApi = (() => {

    const getActorsForQuery = ({search}) => {
        return api.get('/cast/actors',
            {
                params: {
                    'search': search
                }
            }
        );
    }

    const getMediasForActor = ({id}) => {
        return api.get(`/cast/actors/${id}/medias`)
    }

    const getMediasForDirector = ({id}) => {
        return api.get(`/cast/director/${id}/medias`)
    }


    const getActorsByMediaId = (mediaId) =>{
        return api.get(
            `cast/actors`,
            {
                params: {
                    mediaId: mediaId
                }
            }
        )
    }


    const getTvCreatorById = (id) => {
        return api.get(`cast/tvCreators/${id}`);
    }

    const getTvCreatorsSearch = (search) => {
        return api.get(`cast/tvCreators`,
            {
                params: {
                    search: search
                }
            }
        )
    }

    const getTvCreatorsByMediaId = (mediaId) =>{
        return api.get(
            `cast/tvCreators`,
            {
                params: {
                    mediaId: mediaId
                }
            }
        )
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

export default castApi;