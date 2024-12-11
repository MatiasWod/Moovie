import api from "./api";

const providerApi = (() => {

    const getAllProviders = () => {
        return api.get('/providers');
    }

    return{
        getAllProviders
    }
})();

export default providerApi;