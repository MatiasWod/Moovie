import genreApi from '../api/GenreApi';

const GenreService = (() => {
  const getAllGenres = async () => {
    return await genreApi.getAllGenres();
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
