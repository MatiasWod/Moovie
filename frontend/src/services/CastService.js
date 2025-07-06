import castApi from '../api/CastApi';
import { parsePaginatedResponse } from '../utils/ResponseUtils';

const CastService = (() => {
  const getActorsForQuery = async ({ search, pageNumber }) => {
    const res = await castApi.getActorsForQuery({ search, pageNumber });
    return parsePaginatedResponse(res);
  };

  const getActorsByMediaId = async (mediaId) => {
    const res = await castApi.getActorsByMediaId(mediaId);
    return parsePaginatedResponse(res);
  };

  const getActorById = async (id) => {
    return await castApi.getActorById(id);
  };

  const getActorsFromUrl = async (url) => {
    return await castApi.getActorsFromUrl(url);
  };

  const getDirectorsForQuery = async ({ search, pageNumber }) => {
    const res = await castApi.getDirectorsForQuery({ search, pageNumber });
    return parsePaginatedResponse(res);
  };

  const getDirectorFromUrl = async (url) => {
    return await castApi.getDirectorFromUrl(url);
  }

  const getTvCreatorById = async (id) => {
    return await castApi.getTvCreatorById(id);
  };

  const getTvCreatorsSearch = async (search) => {
    return await castApi.getTvCreatorsSearch(search);
  };

  const getTvCreatorsByMediaId = async (mediaId) => {
    const res = await castApi.getTvCreatorsByMediaId(mediaId);
    return parsePaginatedResponse(res);
  };

  const getTvCreatorsFromUrl = async (url) => {
    return await castApi.getTvCreatorsFromUrl(url);
  };

  const getDirectorById = async (id) => {
    return await castApi.getDirectorById(id);
  };

  return {
    getActorsForQuery,
    getActorById,
    getActorsByMediaId,
    getActorsFromUrl,
    getDirectorsForQuery,
    getDirectorFromUrl,
    getTvCreatorById,
    getTvCreatorsSearch,
    getTvCreatorsByMediaId,
    getTvCreatorsFromUrl,
    getDirectorById,
  };
})();

export default CastService;
