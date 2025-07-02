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

  const getDirectorsForQuery = ({ search, pageNumber }) => {
    return api.get('/directors', {
      params: {
        search: search,
        pageNumber: pageNumber,
      },
    });
  };

  const getTvCreatorById = (id) => {
    return api.get(`/tvCreators/${id}`);
  };

  const getTvCreatorsSearch = (search) => {
    return api.get(`/tvCreators`, {
      params: {
        search: search,
      },
    });
  };

  const getTvCreatorsByMediaId = (mediaId) => {
    return api.get(`/tvCreators`, {
      params: {
        mediaId: mediaId,
      },
    });
  };

  const getDirectorById = (id) => {
    return api.get(`/directors/${id}`);
  };

  return {
    getActorsForQuery,
    getActorsByMediaId,
    getActorById,
    getDirectorsForQuery,
    getTvCreatorById,
    getTvCreatorsSearch,
    getTvCreatorsByMediaId,
    getDirectorById
  };
})();

export default castApi;
