import MediaService from '../../services/MediaService';
import mediaApi from '../../api/MediaApi';
import { parsePaginatedResponse } from '../../utils/ResponseUtils';
import {actorDto2, mediaDto, mediaDto2, mediaDto3, mediaIdListIdDto, providerDto, providerDto2} from "../mocks";

jest.mock('../../api/MediaApi');
jest.mock('../../utils/ResponseUtils');

describe('MediaService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getMedia', () => {
    it('should return parsed paginated media', async () => {
      const response = { data: mediaDto, status: 200 };
      mediaApi.getMedia.mockResolvedValue(response);
      parsePaginatedResponse.mockReturnValue(response);

      const result = await MediaService.getMedia({
        type: 'Serie',
        page: 1,
        pageSize: 10,
        orderBy: 'rating',
        sortOrder: 'desc',
        search: 'ONE PIECE',
        providers: [213],
        genres: [2,20],
      });

      expect(mediaApi.getMedia).toHaveBeenCalledWith({
        type: 'Serie',
        page: 1,
        pageSize: 10,
        orderBy: 'rating',
        sortOrder: 'desc',
        search: 'ONE PIECE',
        providers: [213],
        genres: [2,20],
      });
      expect(parsePaginatedResponse).toHaveBeenCalledWith(response);
      expect(result).toBe(response);
      expect(result.data).toEqual(mediaDto);
      expect(result.status).toBe(200);

    });
  });

  describe('getProvidersForMedia', () => {
    it('should return providers from url', async () => {
      const providers = [providerDto, providerDto2];
      mediaApi.getProvidersForMedia.mockResolvedValue(providers);

      const result = await MediaService.getProvidersForMedia({ url: process.env.REACT_APP_API_URL + "/medias?providers=1" });

      expect(mediaApi.getProvidersForMedia).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + "/medias?providers=1");
      expect(result).toEqual(providers);
      expect(result.length).toBe(2);
      expect(result[0]).toEqual(providerDto);
      expect(result[1]).toEqual(providerDto2);
    });
  });

  describe('getMediaById', () => {
    it('should return parsed media by ID', async () => {
      const response = { data: mediaDto, status: 200 };
      mediaApi.getMediaById.mockResolvedValue(response);
      parsePaginatedResponse.mockReturnValue(response);

      const result = await MediaService.getMediaById(190);

      expect(mediaApi.getMediaById).toHaveBeenCalledWith(190);
      expect(parsePaginatedResponse).toHaveBeenCalledWith(response);
      expect(result).toBe(response);
      expect(result.data).toEqual(mediaDto);
      expect(result.status).toBe(200);
    });
  });

  describe('getMediaByIdList', () => {
    it('should return media array from ID list', async () => {
      const data = [mediaDto, mediaDto2];
      mediaApi.getMediaByIdList.mockResolvedValue(data);

      const result = await MediaService.getMediaByIdList('190,1');

      expect(mediaApi.getMediaByIdList).toHaveBeenCalledWith('190,1');
      expect(result).toEqual(data);
      expect(result.length).toBe(2);
      expect(result[0]).toEqual(mediaDto);
      expect(result[1]).toEqual(mediaDto2);
    });
  });

  describe('getIdMediaFromObjectList', () => {
    it('should return comma-separated mediaId string', () => {
      const list = [mediaIdListIdDto];
      const result = MediaService.getIdMediaFromObjectList(list);

      expect(result).toEqual('190');
    });

    it('should return empty string when list is empty', () => {
      const result = MediaService.getIdMediaFromObjectList([]);

      expect(result).toBe('');
    });
  });

  describe('getMediasForTVCreator', () => {
    it('should return medias for a TV creator', async () => {
      const medias = [mediaDto];
      mediaApi.getMediasForTVCreator.mockResolvedValue(medias);

      const result = await MediaService.getMediasForTVCreator(1225826);

      expect(mediaApi.getMediasForTVCreator).toHaveBeenCalledWith(1225826);
      expect(result).toEqual(medias);
      expect(result.length).toBe(1);
      expect(result[0]).toEqual(mediaDto);
      expect(result[0].id).toBe(190);
      expect(result[0].name).toBe('ONE PIECE');
    });
  });

  describe('getMediasForDirector', () => {
    it('should return medias for a director', async () => {
      const medias = [mediaDto3];
      mediaApi.getMediasForDirector.mockResolvedValue(medias);

      const result = await MediaService.getMediasForDirector(525);

      expect(mediaApi.getMediasForDirector).toHaveBeenCalledWith(525);
      expect(result).toEqual(medias);
      expect(result.length).toBe(1);
      expect(result[0]).toEqual(mediaDto3);
    });
  });

  describe('getMediasForActor', () => {
    it('should return medias for an actor', async () => {
      const medias = [mediaDto,mediaDto2];
      mediaApi.getMediasForActor.mockResolvedValue(medias);

      const result = await MediaService.getMediasForActor(1);

      expect(mediaApi.getMediasForActor).toHaveBeenCalledWith(1);
      expect(result).toEqual(medias);
      expect(result.length).toBe(2);
      expect(result[0]).toEqual(mediaDto);
      expect(result[1]).toEqual(mediaDto2);
    });
  });

  describe('getMediasFromUrl', () => {
    it('should return media list from url', async () => {
      const medias = [mediaDto,mediaDto2,mediaDto3];
      mediaApi.getMediasFromUrl.mockResolvedValue(medias);

      const result = await MediaService.getMediasFromUrl(process.env.REACT_APP_API_URL + '/medias?type=0&orderBy=voteCount&sortOrder=DESC&pageNumber=1&pageSize=5');

      expect(mediaApi.getMediasFromUrl).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + '/medias?type=0&orderBy=voteCount&sortOrder=DESC&pageNumber=1&pageSize=5');
      expect(result).toEqual(medias);
      expect(result.length).toBe(3);
      expect(result[0]).toEqual(mediaDto);
      expect(result[1]).toEqual(mediaDto2);
      expect(result[2]).toEqual(mediaDto3);
    });
  });
});
