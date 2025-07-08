import api from './api';

const providerApi = (() => {
  const getAllProviders = (page) => {
    return api.get('/providers',
        {
        params: {
          pageNumber: page || 1,
        },
      }
    );
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
