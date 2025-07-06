import VndType from '../enums/VndType';
import api from './api';

const listApi = (() => {
  const getLists = ({ search, ownerUsername, type, orderBy, order, pageNumber, pageSize }) => {
    return api.get('lists', {
      params: {
        search: search,
        ownerUsername: ownerUsername,
        type: type,
        orderBy: orderBy,
        order: order,
        pageNumber: pageNumber,
        pageSize: pageSize,
      },
    });
  };

  const getReportedLists = (pageNumber = 1) => {
    return api.get('/lists', {
      params: {
        isReported: true,
        pageNumber: pageNumber,
      },
    });
  };

  const getListById = (id) => {
    return api.get(`/lists/${id}`);
  };

  const getListsFromUrl = (url, pageNumber, pageSize) => {
    return api.get(url, {
      params: {
        ...(pageNumber && { pageNumber: pageNumber }),
        ...(pageSize && { pageSize: pageSize }),
      },
    });
  };


  const getListContent = ({ url, orderBy, sortOrder, pageNumber, pageSize }) => {
    return api.get(url, {
      params: {
        orderBy: orderBy,
        sortOrder: sortOrder,
        pageNumber: pageNumber,
        pageSize: pageSize,
      },
    });
  };

  const getListContentByMediaId = (url, mediaId) => {
    return api.get(url + `/${mediaId}`);
  };

  const deleteList = (url) => {
    return api.delete(url);
  };

  const insertMediaIntoMoovieList = (url, mediaIds) => {
    return api.post(
      url,
      { mediaIdList: mediaIds }, // Rename `mediaIds` to `mediaIdList`
      {
        headers: {
          'Content-Type': VndType.APPLICATION_MOOVIELIST_MEDIA_FORM,
        },
      }
    );
  };

  const deleteMediaFromMoovieList = ({ url, mediaId }) => {
    return api.delete(url + `/${mediaId}`);
  };

  //PUT

  const editMoovieList = async (url, name, description) => {
    const form = {
      listName: name,
      listDescription: description,
    };
    const response = await api.put(url, form, {
      headers: {
        'Content-Type': VndType.APPLICATION_MOOVIELIST_FORM,
      },
    });
    return response;
  };

  const getMediaFromList = (listId, mediaId) => {
    return api.get(`/lists/${listId}/content/${mediaId}`);
  };

  const editListContent = (url, mediaId, customOrder) => {
    const input = {
      mediaId: mediaId,
      customOrder: customOrder,
    };

    const response = api.put(url + `/${mediaId}`, input, {
      headers: {
        'Content-Type': VndType.APPLICATION_MOOVIELIST_MEDIA_FORM,
      },
    });
    return response;
  };

  // POST

  const createMoovieList = (name, type, description) => {
    const body = {
      name: name,
      type: type,
      description: description,
    };
    return api.post('lists', body, {
      headers: {
        'Content-Type': VndType.APPLICATION_MOOVIELIST_FORM,
      },
    });
  };

  const likeList = (url) => {
    return api.post(
        url,
      {
      }
    );
  };

  return {
    getLists,
    getListById,
    getListsFromUrl,
    getReportedLists,
    deleteList,
    getListContent,
    getListContentByMediaId,
    insertMediaIntoMoovieList,
    deleteMediaFromMoovieList,
    editMoovieList,
    editListContent,
    createMoovieList,
    likeList,
    getMediaFromList,
  };
})();

export default listApi;
