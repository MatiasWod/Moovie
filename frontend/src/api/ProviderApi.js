import api from './api';

const providerApi = (() => {
  const getAllProviders = () => {
    return api.get('/providers');
  };

  const getProvidersFromUrl = (url) => {
    return api.get(url);
  };

  return {
    getAllProviders,
    getProvidersFromUrl,
  };
})();

export default providerApi;
