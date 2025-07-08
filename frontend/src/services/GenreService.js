import genreApi from '../api/GenreApi';
import { parsePaginatedResponse } from "../utils/ResponseUtils";

const GenreService = (() => {
  const getAllGenres = async (page) => {
    const res = await genreApi.getAllGenres(page);
    return parsePaginatedResponse(res);
  };

  const getGenresFromUrl = async (url) => {
    return await genreApi.getGenresFromUrl(url);
  };

  return {
    getAllGenres,
    getGenresFromUrl,
  };
})();

export default GenreService;
