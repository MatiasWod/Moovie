import api from './api.js';

const userApi = (() => {

    const login = async ({username, password}) => {
        const credentials = btoa(`${username}:${password}`);
        try {
            const response = await api.get('/users', {
                headers: {
                    'Authorization': `Basic ${credentials}`,
                }
            });
            const token = response.headers.get('Authorization');
            console.log(token);
            sessionStorage.setItem('jwtToken', token);
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

    return {
        login,
        register,
        listUsers
    };

})();

export default userApi;
