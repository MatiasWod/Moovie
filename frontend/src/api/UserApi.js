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
            console.log('response', response);
            const token = response.headers.get('Authorization');
            console.log('token to set', token);
            if (token) {
                sessionStorage.setItem('jwtToken', token);
                sessionStorage.setItem('username', username);
            } else {
                throw new Error('No token found in response');
            }
            return response;
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    };

    const register = ({email, username, password}) => {
        // Implementar el registro
    };

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

    const getMovieReviewsFromUser = (userId,page= 1) => {
        return api.get(`/users/${userId}/reviews`);
    }


    return {
        login,
        register,
        listUsers,
        authTest,
        getProfileByUsername,
        getUsersCount,
        getMilkyLeaderboard,
        getMovieReviewsFromUser
    };

})();

export default userApi;
