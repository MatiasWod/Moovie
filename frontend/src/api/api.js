import axios from "axios";
import Qs from "qs";

export default axios.create({
    baseURL: 'http://localhost:8080/',
    timeout: 50000,
    paramsSerializer: params => Qs.stringify(params, {arrayFormat: 'repeat'})
});

