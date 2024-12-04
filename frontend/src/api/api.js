import axios from "axios";
import Qs from "qs";

const api = axios.create({
    baseURL: 'http://localhost:8080/',
    timeout: 50000,
    paramsSerializer: params => Qs.stringify(params, {arrayFormat: 'repeat'})
});

// Interceptor to add the token to all requests
api.interceptors.request.use(
    config => {
        const token = sessionStorage.getItem('jwtToken');
        //console.log('Retrieved token:', token);
        if (token === undefined || token === null || token === 'undefined') {
            console.log('No token');
            return config;
        }
        if (token) {
            config.headers['Authorization'] = `${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        return error.response;
    }
);

export default api;
