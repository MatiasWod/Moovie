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

    const getLikedOrFollowedListFromUser = (username, type, orderBy, sortOrder, pageNumber = 1) => {
        if (type !== "followed" && type !== "liked") {
            throw new Error(`Invalid type: ${type}. Expected "followed" or "liked".`);
        }
        const endpoint = type === "followed" ? "liked" : "followed";
        return api.get(`/list/${endpoint}`, {
            params: {
                username,
                orderBy,
                sortOrder,
                pageNumber
            }
        });
    };

    const currentUserHasLiked = (moovieListId) => {
        return api.get(`list/${moovieListId}/liked`);
    }

    const currentUserHasLFollowed = (moovieListId) => {
        return api.get(`list/${moovieListId}/followed`);
    }

    const likeList = (moovieListId) =>{
        return api.post(`list/${moovieListId}/liked`);
    }

    const unlikeList = (moovieListId) =>{
        return api.delete(`list/${moovieListId}/liked`);
    }

    const followList = (moovieListId) =>{
        return api.post(`list/${moovieListId}/followed`);
    }

    const unfollowList = (moovieListId) =>{
        return api.delete(`list/${moovieListId}/followed`);
    }


    return{
        getLists,
        getListById,
        getListContentById,
        getMoovieListReviewsFromListId,
        createMoovieListReview,
        insertMediaIntoMoovieList,
        editReview,
        getLikedOrFollowedListFromUser,
        currentUserHasLiked,
        currentUserHasLFollowed,
        unlikeList,
        likeList,
        followList,
        unfollowList
    }
})();

export default listApi;