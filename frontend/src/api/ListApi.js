import api from './api'

const listApi = (() => {

    const getLists = ({search, ownerUsername, type, orderBy, order, pageNumber, pageSize}) =>{
        return api.get('list/search',
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

    const getMoovieListReviewsFromListId = ({id,pageNumber}) => {
        return api.get(`/list/${id}/moovieListReviews`,
            {
                params: {
                    'pageNumber': pageNumber
                }
            });
    }


    //POST

    const createMoovieListReview = ({id,page=1}) => {
        return api.post(`/list/${id}/moovieListReview`,
            {
                params:{
                    'pageNumber': page,
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

    //PUT

    const editReview = (id,page=1) => {
        return api.put(`/list/${id}/moovieListReview`,
            {
                params:{
                    'pageNumber': page,
                }
            });
    }



    const getRecommendedLists = (id) => {
        return api.get(`/list/${id}/recommendedLists`,
            {
                params:{
                    'id': id
                }
            })
    }


    return{
        getLists,
        getListById,
        getListByIdList,
        getListContentById,
        getMoovieListReviewsFromListId,
        createMoovieListReview,
        insertMediaIntoMoovieList,
        editReview,

        getRecommendedLists
    }
})();

export default listApi;