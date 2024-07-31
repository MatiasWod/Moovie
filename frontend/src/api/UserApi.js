import api from './api.js'

const userApi = (()=> {

    const login = ({username, password})=> {
        const credentials = btoa(JSON.stringify({username, password}))
        return api.get('/users',
            {
                headers: {
                    'Authorization': `Basic ${credentials}`,
                }
            });
    }

    const register = ({email, username, password})=> {

    }

    const listUsers = ({}) => {

    }

    return {
        login,
        register,
        listUsers
    }

})();

export default userApi;