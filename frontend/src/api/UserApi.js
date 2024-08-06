import api from './api.js';

const userApi = (() => {

    const login = async ({ username, password }) => {
        const credentials = btoa(`${username}:${password}`);
        try {
            const response = await api.get('/users', {
                headers: {
                    'Authorization': `Basic ${credentials}`,
                }
            });
            console.log('response', response);
            const token = response.headers.get('Authorization');
            console.log('token to set',token);
            if (token) {
                sessionStorage.setItem('jwtToken', token);
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

    const getUsersCount = () => {
        return api.get('users/usersCount');
    }

    const getMilkyLeaderboard = ({page, pageSize}) => {
        return api.get( '/users/milkyLeaderboard',
            {
                params:{
                    'page': page,
                    'pageSize': pageSize
                }
            });
    }

    return {
        login,
        register,
        listUsers,
        authTest,
        getUsersCount,
        getMilkyLeaderboard
    };

})();

export default userApi;
