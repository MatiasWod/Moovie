import api from "./api";
import VndType from "../enums/VndType";

const profileApi = (() => {

    const getProfileByUsername = (username) =>{
        return api.get(`/profiles/${username}`);
    }



    const getMilkyLeaderboard = ({page, pageSize}) => {
        return api.get('/profiles/milkyLeaderboard',
            {
                params: {
                    'page': page,
                    'pageSize': pageSize
                }
            });
    }


    const getSearchedUsers = ({username,orderBy,sortOrder,page}) => {
        return api.get(`/profiles`,
            {
                params: {
                    'username': username,
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
                    'pageNumber': page
                }
            }
        );
    }


    const getSpecialListFromUser = (username, type, orderBy, order, pageNumber = 1) => {
            return api.get(`/profiles/${username}/${type}?orderBy=${orderBy}&order=${order}&pageNumber=${pageNumber}`);
    };



    const setPfp = (username, pfp) => {
        return api.put(`/profiles/${username}/image`, pfp);
    }

    /*
    LIKES AND FOLLOWED
    */
    const getLikedOrFollowedListFromUser = (username, type, orderBy, sortOrder, pageNumber = 1) => {
            if (type !== "followed" && type !== "liked") {
                throw new Error(`Invalid type: ${type}. Expected "followed" or "liked".`);
            }
            const endpoint = type === "followed" ? "listLikes" : "listFollows";
            return api.get(`/profiles/${username}/${endpoint}`, {
                params: {
                    username,
                    orderBy,
                    sortOrder,
                    pageNumber
                }
            });
        };

    const currentUserHasLikedList = (moovieListId, username) => {
            return api.get(`/profiles/${username}/listLikes/${moovieListId}`);
        }
    const likeList = (moovieListId, username) =>{
            return api.post(`/profiles/${username}/listLikes`,
                {"id":moovieListId});
        }

    const unlikeList = (moovieListId, username) =>{
            return api.delete(`/profiles/${username}/listLikes/${moovieListId}`);
        }



    const currentUserHasFollowedList = (moovieListId, username) => {
            return api.get(`/profiles/${username}/listFollows/${moovieListId}`);
        }

    const followList = (moovieListId, username) =>{
            return api.post(`/profiles/${username}/listFollows`,
                {"id":moovieListId},
                {headers: {
                        'Content-Type': VndType.APPLICATION_FOLLOW_FORM
                    }
                });
        }

    const unfollowList = (moovieListId, username) =>{
            return api.delete(`/profiles/${username}/listFollows/${moovieListId}`);
        }


        //WATCHED AND WATCHLIST (WW)
    const currentUserWW = (ww, username, mediaId) => {
            return api.get(`/profiles/${username}/${ww}/${mediaId}`);
        }

    const insertMediaIntoWW = (ww, username, mediaId) => {
        let contentType = "application/json";

        if (ww === "watched") {
            contentType = VndType.APPLICATION_WATCHED_MEDIA_FORM;
        }else {
            contentType = VndType.APPLICATION_WATCHLIST_MEDIA_FORM;
        }


        return api.post(`/profiles/${username}/${ww}`,

                {"id":mediaId},
            {headers: {
                    'Content-Type': contentType
                }
            });
        }

    const removeMediaFromWW = (ww, username, mediaId) => {
            return api.delete(`/profiles/${username}/${ww}/${mediaId}`);
        }


        return {
        getProfileByUsername,
        getMilkyLeaderboard,
        getSpecialListFromUser,
        getSearchedUsers,
        setPfp,
        currentUserHasLikedList,
        likeList,
        unlikeList,
        getLikedOrFollowedListFromUser,
        currentUserHasFollowedList,
        followList,
        unfollowList,
        currentUserWW,
        insertMediaIntoWW,
        removeMediaFromWW,

    }

}
);

export default profileApi;
