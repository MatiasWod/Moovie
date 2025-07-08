import providerApi from '../api/ProviderApi';
import {parsePaginatedResponse} from "../utils/ResponseUtils";

const ProviderService = (() => {
  const getAllProviders = async (page) => {
    const res = await providerApi.getAllProviders(page);
    return parsePaginatedResponse(res);
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
