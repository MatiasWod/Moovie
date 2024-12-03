import api from "./api";

const castApi = (() => {

    const getActorsForQuery = ({search}) => {
        return api.get('/cast/actor/',
            {
                params: {
                    'search': search
                }
            }
        );
    }

    return{
        getActorsForQuery
    }
})();

export default castApi;