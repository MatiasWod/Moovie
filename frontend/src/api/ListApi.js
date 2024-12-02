import api from './api'

const listApi = (() => {

    const getLists = ({search, ownerUsername, type, orderBy, order, pageNumber, pageSize}) =>{

        console.log(ownerUsername);
        console.log(type);

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

    const createMoovieListReview = (id,page=1) => {
        return api.post(`/list/${id}/moovieListReview`,
            {
                params:{
                    'pageNumber': page,
                }
            });
    }

    //PUT

    const editReview = (id,page=1) => {
        return api.put(`/list/${id}/moovieListReview`,
            {
                params:{
                    'pageNumber': page,
                }
            });
    }

    return{
        getLists,
        getListById,
        getListContentById,
        getMoovieListReviewsFromListId,
        createMoovieListReview,
        editReview
    }
})();

export default listApi;