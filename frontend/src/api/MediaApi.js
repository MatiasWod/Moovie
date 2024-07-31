import api from './api.js'

const mediaApi = (()=> {

    const getMedia = ({page, pageSize}) => {
        return api.get('/media',
            {
                params: {
                    'page': page,
                    'pageSize': pageSize
                }
            });
    }

    const getMediaById = (id) => {
        return api.get(`/media/${id}`);
    }

    return {
        getMedia,
        getMediaById
    }

})();

export default mediaApi;