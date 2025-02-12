import api from './api.js';

const userApi = (() => {

    //AUTHENTICATION STUFF

    const login = async ({username, password}) => {
        const credentials = btoa(`${username}:${password}`);
        try {
            const response = await api.get(`/users/${username}`, {
                headers: {
                    'Authorization': `Basic ${credentials}`,
                }
            });
            const token = response.headers.get('Authorization');
            if (token) {
                sessionStorage.setItem('jwtToken', token);
                sessionStorage.setItem('username', username);
            } else {
                throw new Error(response.data.message);
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
            },
                {
                headers: {
                    'Content-Type': 'application/vnd.user-form.v1+json'
                }
            });
        } catch (error) {
            throw error
        }
    };

    const confirmToken = async (token) => {
        const response = await api.put(`users/`,
            {
                token: token
            },
            {
                headers: {
                    'Content-Type': 'application/vnd.user_token_form.v1+json'
                }
            }
        );
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

    const getUsersCount = () => {
        return api.get('users/count');
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




    return {
        login,
        register,
        listUsers,
        authTest,
        getUsersCount,
        banUser,
        unbanUser,
        confirmToken,
    };

})();

export default userApi;
