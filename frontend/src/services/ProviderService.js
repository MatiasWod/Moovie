import providerApi from "../api/ProviderApi";

const ProviderService = (() => {

    const getAllProviders = async () => {
        return await providerApi.getAllProviders();
    }

    return{
        getAllProviders
    }
})();

export default ProviderService;