import api from './api'

const listApi = (() => {

    const getLists = ({search, ownerUsername, type, orderBy, order, pageNumber, pageSize}) =>{
        return api.get('list',
            {
                params:{
                    'search' : search,
                    'ownerUsername': ownerUsername,
                    'type': type,
                    'orderBy': orderBy,
                    'order': order,
                    'pageNumber': pageNumber,
                    'pageSize': pageSize
                }
        });
    }

    const getListById = (id) => {
        return api.get( `/list/${id}`);
    }

    const getListByIdList = (idListString) => {
        return api.get(`/list?ids=${idListString}`);
    };


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




    const insertMediaIntoMoovieList = ({ id, mediaIds }) => {
        return api.post(
            `/list/${id}/content`,
            { mediaIdList: mediaIds },  // Rename `mediaIds` to `mediaIdList`
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );
    };

    const deleteMediaFromMoovieList = ({ id, mediaId }) => {
        return api.delete(
            `/list/${id}/content/${mediaId}`
        );
    };

    //PUT

    const editMoovieList = async (mlId, name, description) => {
        const form = {
            listName: name,
            listDescription: description,
        };
        const response = await api.put('/list/' + mlId,
            form);
        return response;
    }



    const getRecommendedLists =  (id) => {
        return api.get(`/list/${id}/recommendedLists`,
            {
                params:{
                    'id': id
                }
            })
    }

    const editListContent =  (listId, mediaId, customOrder) => {
        const input = {
            mediaId: mediaId,
            moovieListId: listId,
            customOrder: customOrder
        };

        const response =  api.put(`list/${listId}/content/${mediaId}`,
            input
        );
        return response;
    }

    // POST

    const createMoovieList = (name, description) => {
        const body = {
            name: name,
            description: description
        }
        return api.post('list', body)
    }


    return{
        getLists,
        getListById,
        getListByIdList,
        getListContentById,
        insertMediaIntoMoovieList,
        deleteMediaFromMoovieList,
        editMoovieList,
        getRecommendedLists,
        editListContent,
        createMoovieList
    }
})();

export default listApi;