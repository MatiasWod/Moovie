import api from './api.js'

const mediaApi = (()=> {

    const getMedia = ({type, page, pageSize, orderBy, sortOrder}) => {
        return api.get('/media',
            {
                params: {
                    'type': type,
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
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