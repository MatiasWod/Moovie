import listApi from '../api/ListApi';
import { parsePaginatedResponse } from '../utils/ResponseUtils';
import mediaService from './MediaService';
import userApi from '../api/UserApi';
import api from '../api/api';

const ListService = (() => {
  const getLists = async ({
    search,
    ownerUsername,
    type,
    orderBy,
    order,
    pageNumber,
    pageSize,
  }) => {
    const res = await listApi.getLists({
      search,
      ownerUsername,
      type,
      orderBy,
      order,
      pageNumber,
      pageSize,
    });
    return parsePaginatedResponse(res);
  };

  const getListById = async (id) => {
    const res = await listApi.getListById(id);
    return parsePaginatedResponse(res);
  };

  const getListsFromUrl = async ({ url, pageNumber, pageSize }) => {
    return await listApi.getListsFromUrl(url, pageNumber, pageSize);
  };

  const getListByIdList = async (idList) => {
    const res = await listApi.getListByIdList(idList);
    return res;
  };

  const editMoovieList = async ({ url, name, description }) => {
    const res = await listApi.editMoovieList(url, name.trim(), description.trim());
    return res;
  };

  const getIdListFromObjectList = (list) => {
    let toRet = '';
    for (const m of list) {
      toRet += m.mlId + ',';
    }
    return toRet.slice(0, -1); // Removes the last comma
  };

  const getListContent = async ({ url, orderBy, sortOrder, pageNumber, pageSize }) => {
    const res = await listApi.getListContent({ url, orderBy, sortOrder, pageNumber, pageSize });
    const contentList = parsePaginatedResponse(res).data;
    const contentListLinks = parsePaginatedResponse(res).links;

    if (contentList.length === 0) {
      return {
        data: [],
        links: contentListLinks,
      };
    }

    // Note: Promise.all maintains the order of the original array, so this will preserve the contentList order
    const medias = await Promise.all(
      contentList.map(async (content) => {
        const res = await api.get(content.mediaUrl);
        const media = res.data;
        media.customOrder = content.customOrder;
        return media;
      })
    );

    return {
      data: medias,
      links: contentListLinks,
    };
  };

  const getListContentByMediaId = async ({ url, mediaId }) => {
    return await listApi.getListContentByMediaId(url, mediaId);
  };

  const createMoovieList = async ({ name, type, description }) => {
    return await listApi.createMoovieList(name, type, description);
  };

  const editListContent = async ({ listId, mediaId, customOrder }) => {
    return listApi.editListContent(listId, mediaId, customOrder);
  };

  const insertMediaIntoMoovieList = async ({ url, mediaIds }) => {
    const res = await listApi.insertMediaIntoMoovieList(url, mediaIds);
    return res;
  };

  const deleteMediaFromMoovieList = async ({ url, mediaId }) => {
    const res = await listApi.deleteMediaFromMoovieList({ url, mediaId });
    return res;
  };

  const getRecommendedLists = async (id) => {
    return await listApi.getRecommendedLists(id);
  };

  const likeList = async (moovieListId, username) => {
    try {
      return await listApi.likeList(moovieListId, username);
    } catch (error) {
      return null;
    }
  };

  const unlikeList = async (moovieListId, username) => {
    try {
      return await listApi.unlikeList(moovieListId, username);
    } catch (error) {
      return null;
    }
  };

  const followList = async (moovieListId, username) => {
    try {
      return await listApi.followList(moovieListId, username);
    } catch (error) {
      return null;
    }
  };

  const unfollowList = async (moovieListId, username) => {
    try {
      return await listApi.unfollowList(moovieListId, username);
    } catch (error) {
      return null;
    }
  };

  return {
    getLists,
    getListById,
    getListsFromUrl,
    getListContent,
    getListContentByMediaId,
    getListByIdList,
    getIdListFromObjectList,
    insertMediaIntoMoovieList,
    deleteMediaFromMoovieList,
    editMoovieList,
    getRecommendedLists,
    editListContent,
    createMoovieList,
    likeList,
    unlikeList,
    followList,
    unfollowList,
  };
})();

export default ListService;
