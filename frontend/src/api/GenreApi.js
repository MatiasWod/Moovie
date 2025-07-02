import api from './api';

const genreApi = (() => {
  const getAllGenres = () => {
    return api.get('/genres');
  };

  const getGenresFromUrl = (url) => {
    return api.get(url);
  };

  return {
    getAllGenres,
    getGenresFromUrl,
  };
})();

export default genreApi;
