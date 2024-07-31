import api from './api'

const listApi = (() => {

    const getListById = (id) => {
        return api.get( `/list/${id}`);
    }


    const getListContentById= ({id, orderBy, sortOrder, pageNumber, pageSize}) => {
        return api.get(`/list/${id}/content`,
            {
                params: {
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
                    'pageNumber': pageNumber,
                    'pageSize': pageSize
                }
            });
    }

    return{
        getListById,
        getListContentById
    }
})();

export default listApi;