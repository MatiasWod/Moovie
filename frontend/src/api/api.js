import axios from "axios";
import Qs from "qs";

const api = axios.create({
    baseURL: process.env.REACT_APP_API_URL,
    timeout: 50000,
    paramsSerializer: params => Qs.stringify(params, {arrayFormat: 'repeat'})
});

export default api;