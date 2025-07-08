import ProviderService from '../../services/ProviderService';
import providerApi from '../../api/ProviderApi';
import {actorDto, providerDto, providerDto2} from "../mocks";
import {parsePaginatedResponse} from "../../utils/ResponseUtils";

jest.mock('../../api/ProviderApi');
jest.mock('../../utils/ResponseUtils');

describe('ProviderService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getAllProviders', () => {
    it('should return all providers', async () => {
      const providers = [providerDto, providerDto2];
      providerApi.getAllProviders.mockResolvedValue({data: providers, status: 200});
      parsePaginatedResponse.mockReturnValue({data: providers, status: 200});

      const result = await ProviderService.getAllProviders();

      expect(providerApi.getAllProviders).toHaveBeenCalledTimes(1);
      expect(result.data).toEqual(providers);
      expect(result.data).toHaveLength(2);
      expect(result.data[0]).toEqual(providerDto);
      expect(result.data[1]).toEqual(providerDto2);
      expect(result.status).toBe(200);
    });

    it('should return an empty array if no providers are found', async () => {
      providerApi.getAllProviders.mockResolvedValue({data: [], status: 200});
      parsePaginatedResponse.mockReturnValue({data: [], status: 200});

      const result = await ProviderService.getAllProviders();

      expect(result.data).toEqual([]);
      expect(result.status).toBe(200);
    });
  });

  describe('getProvidersFromUrl', () => {
    it('should return providers from a given URL', async () => {
      const url = process.env.REACT_APP_API_URL + "/providers?mediaId=190";
      const providers = [providerDto,providerDto2];
      providerApi.getProvidersFromUrl.mockResolvedValue(providers);

      const result = await ProviderService.getProvidersFromUrl(url);

      expect(providerApi.getProvidersFromUrl).toHaveBeenCalledWith(url);
      expect(result).toEqual(providers);
      expect(result.length).toBe(2);
      expect(result[0]).toEqual(providerDto);
      expect(result[1]).toEqual(providerDto2);
    });

    it('should return an empty array if no providers are found for the URL', async () => {
      providerApi.getProvidersFromUrl.mockResolvedValue([]);

      const result = await ProviderService.getProvidersFromUrl(process.env.REACT_APP_API_URL + '/providers');

      expect(result).toEqual([]);
    });
  });
});