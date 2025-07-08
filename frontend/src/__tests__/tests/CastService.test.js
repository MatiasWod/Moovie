import CastService from '../../services/CastService';
import castApi from '../../api/CastApi';
import {actorDto, actorDto2, directorDto, directorDto2, tvCreatorsDto, tvCreatorsDto2} from "../mocks";
import { parsePaginatedResponse } from '../../utils/ResponseUtils';
import data from "bootstrap/js/src/dom/data";

jest.mock('../../api/CastApi');
jest.mock('../../utils/ResponseUtils');

describe('CastService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getActorsForQuery', () => {
    it('should call castApi.getActorsForQuery and parsePaginatedResponse', async () => {
      castApi.getActorsForQuery.mockResolvedValue({data: actorDto, status: 200});
      parsePaginatedResponse.mockReturnValue({data: actorDto, status: 200});

      const result = await CastService.getActorsForQuery({search: "Chris Pratt", pageNumber: 1});

      expect(castApi.getActorsForQuery).toHaveBeenCalledWith({search: "Chris Pratt", pageNumber: 1});
      expect(parsePaginatedResponse).toHaveBeenCalledWith({data: actorDto, status: 200});
      expect(result.data).toEqual(actorDto);
      expect(result.status).toBe(200);
    });
  });

  describe('getActorsByMediaId', () => {
    it('should call castApi.getActorsByMediaId and parsePaginatedResponse', async () => {
      const response = { data: [actorDto,actorDto2], status: 200 };
      castApi.getActorsByMediaId.mockResolvedValue(response);
      parsePaginatedResponse.mockReturnValue(response);

      const result = await CastService.getActorsByMediaId(42);

      expect(castApi.getActorsByMediaId).toHaveBeenCalledWith(42);
      expect(parsePaginatedResponse).toHaveBeenCalledWith(response);
      expect(result.data.length).toBe(2);
      expect(result.status).toBe(200);
      expect(result.data[0]).toEqual(actorDto);
      expect(result.data[1]).toEqual(actorDto2);
    });
  });

  describe('getActorById', () => {
    it('should call castApi.getActorById', async () => {
      castApi.getActorById.mockResolvedValue(actorDto2);

      const result = await CastService.getActorById(1);

      expect(castApi.getActorById).toHaveBeenCalledWith(1);
      expect(result).toBe(actorDto2);
      expect(result.actorId).toBe(1);
    });
  });

  describe('getActorsFromUrl', () => {
    it('should call castApi.getActorsFromUrl', async () => {
      const actors = [actorDto, actorDto2];
      castApi.getActorsFromUrl.mockResolvedValue(actors);

      const result = await CastService.getActorsFromUrl(process.env.REACT_APP_API_URL + "/actors?mediaId=190");

      expect(castApi.getActorsFromUrl).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + "/actors?mediaId=190");
      expect(result).toBe(actors);
      expect(result.length).toBe(2);
      expect(result[0]).toEqual(actorDto);
      expect(result[1]).toEqual(actorDto2);
    });
  });

  describe('getDirectorsForQuery', () => {
    it('should call castApi.getDirectorsForQuery and parsePaginatedResponse', async () => {
      const response = { data: [directorDto,directorDto2], status: 200 };
      castApi.getDirectorsForQuery.mockResolvedValue(response);
      parsePaginatedResponse.mockReturnValue(response);

      const result = await CastService.getDirectorsForQuery({ search: 'C', pageNumber: 1 });

      expect(castApi.getDirectorsForQuery).toHaveBeenCalledWith({ search: 'C', pageNumber: 1 });
      expect(parsePaginatedResponse).toHaveBeenCalledWith(response);
      expect(result).toBe(response);
      expect(result.data.length).toBe(2);
      expect(result.data[0]).toEqual(directorDto);
      expect(result.data[1]).toEqual(directorDto2);
    });
  });

  describe('getDirectorFromUrl', () => {
    it('should call castApi.getDirectorFromUrl', async () => {
      const directors = [directorDto];
      castApi.getDirectorFromUrl.mockResolvedValue(directors);

      const result = await CastService.getDirectorFromUrl(process.env.REACT_APP_API_URL + "/directors/525");

      expect(castApi.getDirectorFromUrl).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + "/directors/525");
      expect(result).toBe(directors);
      expect(result.length).toBe(1);
      expect(result[0]).toEqual(directorDto);
    });
  });

  describe('getTvCreatorById', () => {
    it('should call castApi.getTvCreatorById', async () => {
      castApi.getTvCreatorById.mockResolvedValue(tvCreatorsDto);

      const result = await CastService.getTvCreatorById(99898);

      expect(castApi.getTvCreatorById).toHaveBeenCalledWith(99898);
      expect(result).toBe(tvCreatorsDto);
      expect(result.id).toBe(99898);
    });
  });

  describe('getTvCreatorsFromUrl', () => {
    it('should call castApi.getTvCreatorsFromUrl', async () => {
      const resultData = [tvCreatorsDto, tvCreatorsDto2];
      castApi.getTvCreatorsFromUrl.mockResolvedValue(resultData);

      const result = await CastService.getTvCreatorsFromUrl(process.env.REACT_APP_API_URL + "/tvCreators?mediaId=190");

      expect(castApi.getTvCreatorsFromUrl).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + "/tvCreators?mediaId=190");
      expect(result).toBe(resultData);
    });
  });

  describe('getDirectorById', () => {
    it('should call castApi.getDirectorById', async () => {
      castApi.getDirectorById.mockResolvedValue(directorDto);

      const result = await CastService.getDirectorById(525);

      expect(castApi.getDirectorById).toHaveBeenCalledWith(525);
      expect(result).toBe(directorDto);
      expect(result.directorId).toBe(525);
    });
  });
});
