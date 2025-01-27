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

    return{
        getActorsForQuery,
        getMediasForActor,
        getMediasForDirector,
        getActorsByMediaId
    }
})();

export default castApi;