import providerApi from '../api/ProviderApi';

const ProviderService = (() => {
  const getAllProviders = async () => {
    return await providerApi.getAllProviders();
  };

  const getProvidersFromUrl = async (url) => {
    return await providerApi.getProvidersFromUrl(url);
  };

  return {
    getAllProviders,
    getProvidersFromUrl,
  };
})();

export default ProviderService;
