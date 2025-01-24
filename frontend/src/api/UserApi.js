import api from './api.js';

const userApi = (() => {

    //AUTHENTICATION STUFF

    const attemptReconnect = async () => {

    }

    const login = async ({username, password}) => {
        const credentials = btoa(`${username}:${password}`);
        try {
            const response = await api.get('/users/username/' + username, {
                headers: {
                    'Authorization': `Basic ${credentials}`,
                }
            });
            const token = response.headers.get('Authorization');
            if (token) {
                sessionStorage.setItem('jwtToken', token);
                sessionStorage.setItem('username', username);
            } else {
                throw new Error('No token found in response');
            }
            return response;
        } catch (error) {
            throw error;
        }
    };

    const register = async ({ email, username, password }) => {
        try {
            return await api.post('/users', {
                email,
                username,
                password,
            });
        } catch (error) {
            throw error
        }
    };

    const confirmToken = async (token) => {
        const response = await api.put(`users/verify/${token}`);
        const jwtToken = response.headers['authorization'];
        if (jwtToken) {
            sessionStorage.setItem('jwtToken', jwtToken);
            localStorage.setItem('jwtToken', jwtToken);
        }
        return response;
    }


    const listUsers = ({}) => {
        // Implementar la lista de usuarios
    };

    const authTest = async () => {
        try {
            const response = await api.get('/users/authtest');
            return response.status === 200;
        } catch (error) {
            console.error('Auth test error:', error);
            return false;
        }
    };

    //PROFILE STUFF

    const getProfileByUsername = (username) =>{
        return api.get(`users/profile/${username}`);
    }

    const getUsersCount = () => {
        return api.get('users/usersCount');
    }

    const getMilkyLeaderboard = ({page, pageSize}) => {
        return api.get('/users/milkyLeaderboard',
            {
                params: {
                    'page': page,
                    'pageSize': pageSize
                }
            });
    }

    const getSpecialListFromUser = (username, type, orderBy, order, pageNumber = 1) => {
        return api.get(`/users/${username}/${type}?orderBy=${orderBy}&order=${order}&pageNumber=${pageNumber}`);
    };


    const getSearchedUsers = ({username,orderBy,sortOrder,page}) => {
        return api.get(`/users/search`,
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

    const setPfp = (username, pfp) => {
        return api.put(`/users/${username}/image`, pfp);
    }

    /*
    LIKES AND FOLLOWED
     */
    const getLikedOrFollowedListFromUser = (username, type, orderBy, sortOrder, pageNumber = 1) => {
        if (type !== "followed" && type !== "liked") {
            throw new Error(`Invalid type: ${type}. Expected "followed" or "liked".`);
        }
        const endpoint = type === "followed" ? "listLikes" : "listFollows";
        return api.get(`/users/${username}/${endpoint}`, {
            params: {
                username,
                orderBy,
                sortOrder,
                pageNumber
            }
        });
    };

    const currentUserHasLikedList = (moovieListId, username) => {
        return api.get(`/users/${username}/listLikes/${moovieListId}`);
    }
    const likeList = (moovieListId, username) =>{
        return api.post(`/users/${username}/listLikes`,
            {"id":moovieListId});
    }

    const unlikeList = (moovieListId, username) =>{
        return api.delete(`/users/${username}/listLikes/${moovieListId}`);
    }

    const currentUserHasFollowedList = (moovieListId, username) => {
        return api.get(`/users/${username}/listFollows/${moovieListId}`);
    }

    const followList = (moovieListId, username) =>{
        return api.post(`/users/${username}/listFollows`,
            {"id":moovieListId});
    }

    const unfollowList = (moovieListId, username) =>{
        return api.delete(`/users/${username}/listFollows/${moovieListId}`);
    }


    // MODERATION STUFF

    const banUser = (username) => {
        const banUserDTO = {
            banMessage: "User banned by moderator"
        };
        return api.put(`/users/${username}/ban`, banUserDTO);
    }

    const unbanUser = (username) => {
        return api.put(`/users/${username}/unban`);
    }


    //WATCHED AND WATCHLIST (WW)
    const currentUserWW = (ww, username, mediaId) => {
        return api.get(`/users/${username}/${ww}/${mediaId}`);
    }

    const insertMediaIntoWW = (ww, username, mediaId) => {
        return api.post(`/users/${username}/${ww}`,
            {"id":mediaId});
    }

    const removeMediaFromWW = (ww, username, mediaId) => {
        return api.delete(`/users/${username}/${ww}/${mediaId}`);
    }


    return {
        login,
        register,
        listUsers,
        authTest,
        getProfileByUsername,
        getUsersCount,
        getMilkyLeaderboard,
        getSpecialListFromUser,
        getSearchedUsers,
        banUser,
        unbanUser,
        currentUserHasLikedList,
        likeList,
        unlikeList,
        getLikedOrFollowedListFromUser,
        currentUserHasFollowedList,
        followList,
        unfollowList,
        confirmToken,
        currentUserWW,
        insertMediaIntoWW,
        removeMediaFromWW,
        setPfp
    };

})();

export default userApi;
