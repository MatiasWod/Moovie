import api from './api';

const genreApi = (() => {
  const getAllGenres = (page) => {
    return api.get('/genres', {
      params: {
        pageNumber: page || 1,
      },
    });
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
