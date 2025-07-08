import GenreService from '../../services/GenreService';
import genreApi from '../../api/GenreApi';
import {genreDto, genreDto2} from "../mocks";
import {parsePaginatedResponse} from "../../utils/ResponseUtils";

jest.mock('../../api/GenreApi');
jest.mock('../../utils/ResponseUtils');
describe('GenreService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getAllGenres', () => {
    it('should return an array of genres', async () => {
      const mockGenres = {data:[genreDto,genreDto2], links: {next: null, previous: null}};
      genreApi.getAllGenres.mockResolvedValue(mockGenres);
      parsePaginatedResponse.mockReturnValueOnce(mockGenres);
      const result = await GenreService.getAllGenres();

      expect(result).toEqual(mockGenres);
      expect(genreApi.getAllGenres).toHaveBeenCalledTimes(1);
      expect(result.data.length).toBe(2);
      expect(result.data[0]).toEqual(genreDto);
      expect(result.data[1]).toEqual(genreDto2);
    });
  });

  describe('getGenresFromUrl', () => {
    it('should return genres from a given URL', async () => {
      const url = process.env.REACT_APP_API_URL + "/genres/1";
      const mockGenres = genreDto;
      genreApi.getGenresFromUrl.mockResolvedValue(mockGenres);

      const result = await GenreService.getGenresFromUrl(url);

      expect(result).toEqual(mockGenres);
      expect(genreApi.getGenresFromUrl).toHaveBeenCalledWith(url);
    });

    it('should return an empty array if no genres are found for the URL', async () => {
      const url = process.env.REACT_APP_API_URL + "/genres/99";
      genreApi.getGenresFromUrl.mockResolvedValue([]);

      const result = await GenreService.getGenresFromUrl(url);

      expect(result).toEqual([]);
      expect(genreApi.getGenresFromUrl).toHaveBeenCalledWith(url);
    });
  });
});
