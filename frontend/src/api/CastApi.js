import api from './api';

const castApi = (() => {
  const getActorsForQuery = ({ search, pageNumber }) => {
    return api.get('/actors', {
      params: {
        search: search,
        pageNumber: pageNumber,
      },
    });
  };

  const getActorsByMediaId = (mediaId) => {
    return api.get(`/actors`, {
      params: {
        mediaId: mediaId,
      },
    });
  };

  const getActorById = (id) => {
    return api.get(`/actors/${id}`);
  };

  const getActorsFromUrl = (url) => {
    return api.get(url);
  };

  const getDirectorsForQuery = ({ search, pageNumber }) => {
    return api.get('/directors', {
      params: {
        search: search,
        pageNumber: pageNumber,
      },
    });
  };

  const getDirectorFromUrl = (url) => {
    return api.get(url);
  }

  const getTvCreatorById = (id) => {
    return api.get(`/tvCreators/${id}`);
  };


  const getTvCreatorsFromUrl = (url) => {
    return api.get(url);
  };

  const getDirectorById = (id) => {
    return api.get(`/directors/${id}`);
  };

  return {
    getActorsForQuery,
    getActorsByMediaId,
    getActorById,
    getActorsFromUrl,
    getDirectorsForQuery,
    getDirectorFromUrl,
    getTvCreatorById,
    getTvCreatorsFromUrl,
    getDirectorById,
  };
})();

export default castApi;
