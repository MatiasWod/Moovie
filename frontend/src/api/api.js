import axios from "axios";
import Qs from "qs";

const api = axios.create({
    baseURL: 'http://localhost:8080/',
    timeout: 50000,
    paramsSerializer: params => Qs.stringify(params, {arrayFormat: 'repeat'})
});

// Interceptor para agregar el token a todas las solicitudes
api.interceptors.request.use(
    config => {
        const token = sessionStorage.getItem('jwtToken');
        console.log(token);
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default api;
