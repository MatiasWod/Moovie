import api from "./api";

const castApi = (() => {

    const getActorsForQuery = ({search}) => {
        return api.get('/cast/actor',
            {
                params: {
                    'search': search
                }
            }
        );
    }

    const getMediasForActor = ({id}) => {
        return api.get(`/cast/actor/${id}/medias`)
    }

    const getMediasForDirector = ({id}) => {
        return api.get(`/cast/director/${id}/medias`)
    }

    return{
        getActorsForQuery,
        getMediasForActor,
        getMediasForDirector
    }
})();

export default castApi;