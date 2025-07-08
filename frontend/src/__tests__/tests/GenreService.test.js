import GenreService from '../../services/GenreService';
import genreApi from '../../api/GenreApi';
import {genreDto, genreDto2} from "../mocks";

jest.mock('../../api/GenreApi');

describe('GenreService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getAllGenres', () => {
    it('should return an array of genres', async () => {
      const mockGenres = [genreDto,genreDto2];
      genreApi.getAllGenres.mockResolvedValue(mockGenres);

      const result = await GenreService.getAllGenres();

      expect(result).toEqual(mockGenres);
      expect(genreApi.getAllGenres).toHaveBeenCalledTimes(1);
      expect(result.length).toBe(2);
      expect(result[0]).toEqual(genreDto);
      expect(result[1]).toEqual(genreDto2);
    });

    it('should return an empty array if no genres are found', async () => {
      genreApi.getAllGenres.mockResolvedValue([]);

      const result = await GenreService.getAllGenres();

      expect(result).toEqual([]);
      expect(genreApi.getAllGenres).toHaveBeenCalledTimes(1);
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
