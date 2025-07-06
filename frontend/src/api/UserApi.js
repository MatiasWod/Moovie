import api from './api';
import VndType from '../enums/VndType';
import { parsePaginatedResponse } from '../utils/ResponseUtils';
import MediaService from "../services/MediaService";
import ListService from "../services/ListService";
import sortOrder from "./values/SortOrder";

const userApi = (() => {

  const getUserByUsernameForProfile = (username) => {
    return api.get(`/users/${username}`);
  }

  const getUserByUsername = (url) => {
    return api.get(url);
  };

  const getMilkyLeaderboard = ({ page, pageSize }) => {
    return api.get('/users/milkyLeaderboard', {
      params: {
        page: page,
        pageSize: pageSize,
      },
    });
  };

  const getSearchedUsers = ({ username, orderBy, sortOrder, page }) => {
    return api.get(`/users`, {
      params: {
        search: username,
        orderBy: orderBy,
        sortOrder: sortOrder,
        pageNumber: page,
      },
    });
  };

  const getSpecialListFromUser = async (url, orderBy, order, pageNumber = 1, search) => {
    const response = await api.get(url,
        {
        params: {
          ...(search && { 'search': search })
        },
        }
    );

    const moovieList = response.data[0];
    return ListService.getListContent({
      url: moovieList.contentUrl,
      orderBy,
      sortOrder: order,
      pageNumber
    });
  };

  const getProfileListsFromUser = async (url, orderBy, order, pageNumber = 1, search) => {
    const response = await api.get(url,
        {
          params: {
            orderBy: orderBy,
            order: order,
            pageNumber: pageNumber,
            ...(search && { 'search': search })
          },
        }
    );
    return response;
  }

  const setPfp = (username, pfp) => {
    return api.post(`/images`, pfp);
  };


  /*
    LIKES AND FOLLOWED
    */

  const currentUserHasLikedList = (url, username) => {
    return api.get(url + `/${username}`);
  };

  const currentUserHasFollowedList = (url, username) => {
    return api.get(url + `/${username}`);
  };

  //WATCHED AND WATCHLIST (WW)
  const getMediaStatusFromWW = async (url, mediaId, search) => {
    const response = await api.get(url,
        {
          params: {
            ...(search && { 'search': search })
          },
        }
    );

    const moovieList = response.data[0];
    return ListService.getListContentByMediaId({
      url: moovieList.contentUrl, mediaId: mediaId
    });
  };

  //TODO CAMBIAR A USAR URL.
  const insertMediaIntoWW = async (url, mediaId, search) => {
    const response = await api.get(url,
        {
          params: {
            ...(search && { 'search': search })
          },
        }
    );

    const moovieList = response.data[0];

    return ListService.insertMediaIntoMoovieList({url: moovieList.contentUrl, mediaIds: [mediaId]});
  };

  //TODO CAMBIAR A USAR URL.
  const removeMediaFromWW = async (url, mediaId, search) => {
    const response = await api.get(url,
        {
          params: {
            ...(search && { 'search': search })
          },
        }
    );

    const moovieList = response.data[0];

    return ListService.deleteMediaFromMoovieList({url: moovieList.contentUrl, mediaId: mediaId});
  };

  //TODO CAMBIAR A USAR URL.
  const currentUserHasLikedReview = (reviewId, username) => {
    return api.get(`/users/${username}/reviewLikes/${reviewId}`);
  };

  //TODO CAMBIAR A USAR URL.
  const currentUserHasLikedMoovieListReview = (reviewId, username) => {
    return api.get(`/users/${username}/moovieListsReviewsLikes/${reviewId}`);
  };
  //TODO CAMBIAR A USAR URL.
  const currentUserCommentFeedback = (commentId, username) => {
    return api.get(`/users/${username}/commentsFeedback/${commentId}`);
  };
  //TODO CAMBIAR A USAR URL.
  const getWatchedCountFromMovieListId = (movieListId, username) => {
    return api.get(`/users/${username}/watched/count?listId=${movieListId}`);
  };


  const login = async ({ username, password }) => {
    const credentials = btoa(`${username}:${password}`);
    try {
      const response = await api.get(`/users/${username}`, {
        headers: {
          Authorization: `Basic ${credentials}`,
        },
        skipRetry: true,
      });
      console.log(response.headers);
      const authToken = response.headers['moovie-authtoken'];
      const refreshToken = response.headers['moovie-refreshtoken'];
      if (authToken && refreshToken) {
        console.log('saving in storage', authToken, refreshToken, username);
        sessionStorage.setItem('username', username);
        sessionStorage.setItem('jwt', authToken);
        sessionStorage.setItem('refreshToken', refreshToken);
      }
      console.log(
          'sessionStorage',
          sessionStorage.getItem('username'),
          sessionStorage.getItem('jwt'),
          sessionStorage.getItem('refreshToken')
      );
      return response;
    } catch (error) {
      console.log(error);

      // Map specific backend error messages to translation keys
      let translationKey = 'login.loginFailed'; // default

      if (error.response && error.response.data && error.response.data.message) {
        const errorMessage = error.response.data.message;
        const status = error.response.status;

        // Map specific error messages to translation keys
        if (status === 401) {
          // 401 Unauthorized errors
          if (errorMessage.includes('Username is incorrect')) {
            translationKey = 'login.usernameIncorrect';
          } else if (errorMessage.includes('Invalid token')) {
            translationKey = 'login.invalidToken';
          } else if (errorMessage.includes('Bad credentials') || errorMessage.includes('authentication token')) {
            translationKey = 'login.badCredentials';
          } else {
            translationKey = 'login.badCredentials'; // default for 401
          }
        } else if (status === 403) {
          // 403 Forbidden errors
          if (errorMessage.includes('User not verified')) {
            translationKey = 'login.userNotVerified';
          } else if (errorMessage.includes('User is banned')) {
            translationKey = 'login.userBanned';
          } else {
            translationKey = 'login.loginFailed'; // default for 403
          }
        }

        const apiError = new Error(translationKey);
        apiError.status = status;
        apiError.translationKey = translationKey;
        throw apiError;
      }
      throw error;
    }
  };

  const register = async ({ email, username, password }) => {
    try {
      return await api.post(
          '/users',
          {
            email,
            username,
            password,
          },
          {
            headers: {
              'Content-Type': VndType.APPLICATION_USER_FORM,
            },
          }
      );
    } catch (error) {
      throw error;
    }
  };

  const confirmToken = async (username,token) => {
    const response = await api.put(
        `users/${username}`,
        {
          token: token,
        },
        {
          headers: {
            'Content-Type': VndType.APPLICATION_USER_TOKEN_FORM,
          },
        }
    );
    if (response.headers['moovie-authtoken'] && response.headers['moovie-refreshtoken']) {
      sessionStorage.setItem('jwt', response.headers['moovie-authtoken']);
      sessionStorage.setItem('refreshToken', response.headers['moovie-refreshtoken']);
    }
    return response;
  };

  const resendVerificationEmail = async (token) => {
    try {
      return await api.post(
          'users/',
          { token },
          {
            headers: {
              'Content-Type': VndType.APPLICATION_RESEND_TOKEN_FORM,
            },
          }
      );
    } catch (error) {
      throw error;
    }
  };

  const forgotPassword = async (email) => {
    try {
      return await api.post(
          '/users/',
          { email: email },
          {
            headers: {
              'Content-Type': VndType.APPLICATION_PASSWORD_TOKEN_FORM,
            },
          }
      );
    } catch (error) {
      throw error;
      throw error;
    }
  };

  const resetPassword = async (username,token, password) => {
    try {
      return await api.put(
          `users/${username}`,
          {
            password: password,
            token: token,
          },
          {
            headers: {
              'Content-Type': VndType.APPLICATION_USER_PASSWORD,
            },
          }
      );
    } catch (error) {
      throw error;
    }
  };

  const listUsers = ({ role ,pageNumber,pageSize}) => {
    return api.get('/users', { params: { role: role,pageNumber: pageNumber,pageSize:pageSize } });
  };

  const getBanMessage = (username) => {
    try {
      return api.get(`/users/${username}/banMessage`);
    } catch (error) {
      return ' ';
    }
  };

  const authTest = async () => {
    try {
      const response = await api.get('/users/authtest');
      return response.status === 200;
    } catch (error) {
      console.error('Auth test error:', error);
      return false;
    }
  };

  // MODERATION STUFF

  const banUser = (username) => {
    const banUserDTO = {
      modAction: 'BAN',
      banMessage: 'User banned by moderator',
    };
    return api.put(`/users/${username}`, banUserDTO, {
      headers: {
        'Content-Type': VndType.APPLICATION_USER_BAN_FORM,
      },
    });
  };

  const unbanUser = (username) => {
    const banUserDTO = {
      modAction: 'UNBAN',
    };
    return api.put(`/users/${username}`, banUserDTO, {
      headers: {
        'Content-Type': VndType.APPLICATION_USER_BAN_FORM,
      },
    });
  };

  const makeUserModerator = (username) => {
    return api.put(`/users/${username}`, {});
  };


  return {
    getUserByUsernameForProfile,
    getUserByUsername,
    getMilkyLeaderboard,
    getSpecialListFromUser,
    getProfileListsFromUser,
    getSearchedUsers,
    setPfp,
    currentUserHasLikedList,
    currentUserHasFollowedList,
    getMediaStatusFromWW,
    insertMediaIntoWW,
    removeMediaFromWW,
    currentUserHasLikedReview,
    currentUserHasLikedMoovieListReview,
    currentUserCommentFeedback,
    getWatchedCountFromMovieListId,
    login,
    register,
    listUsers,
    authTest,
    banUser,
    unbanUser,
    makeUserModerator,
    confirmToken,
    resendVerificationEmail,
    getBanMessage,
    forgotPassword,
    resetPassword,
  };
})();

export default userApi;
