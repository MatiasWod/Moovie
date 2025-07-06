import { parsePaginatedResponse } from '../utils/ResponseUtils';
import userApi from '../api/UserApi';
import WatchlistWatched from '../api/values/WatchlistWatched';
import CommentStatusEnum from '../api/values/CommentStatusEnum';

const UserService = (() => {
  const getMilkyLeaderboard = async ({ page, pageSize }) => {
    const res = await userApi.getMilkyLeaderboard({ page, pageSize });
    return parsePaginatedResponse(res);
  };

  const getSearchedUsers = async ({ username, orderBy, sortOrder, page = 1 }) => {
    const res = await userApi.getSearchedUsers({ username, orderBy, sortOrder, page });
    return parsePaginatedResponse(res);
  };

  const setPfp = async ({ username, pfp }) => {
    return await userApi.setPfp(username, pfp);
  };

  const getSpecialListFromUser = async ({ username, type, orderBy, order, pageNumber }) => {
    const res = await userApi.getSpecialListFromUser(username, type, orderBy, order, pageNumber);
    return parsePaginatedResponse(res);
  };

  const currentUserHasLiked = async (url, username) => {
    try {
      const res = await userApi.currentUserHasLikedList(url, username);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const currentUserHasFollowed = async (url, username) => {
    try {
      const res = await userApi.currentUserHasFollowedList(url, username);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const userWWStatus = async (url, mediaId, search) => {
    try {
      const res = await userApi.getMediaStatusFromWW(url, mediaId, search);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const userWatchedStatus = async (url, mediaId, search) => {
    try {
      const res = await userApi.getMediaStatusFromWatched(url, mediaId, search);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const insertMediaIntoWW = async (ww, mediaId, username) => {
    try {
      return await userApi.insertMediaIntoWW(ww, mediaId, username);
    } catch (error) {
      return null;
    }
  };

  const removeMediaFromWW = async (url, mediaId, search) => {
    try {
      return await userApi.removeMediaFromWW(url, mediaId, search);
    } catch (error) {
      return null;
    }
  };

  const currentUserLikeFollowStatus = async (likesUrl, followersUrl, username) => {
    try {
      const [likedStatus, followedStatus] = await Promise.all([
        currentUserHasLiked(likesUrl, username),
        currentUserHasFollowed(followersUrl, username),
      ]);

      return {
        liked: likedStatus,
        followed: followedStatus,
      };
    } catch (error) {
      return {
        liked: false,
        followed: false,
      };
    }
  };

  const currentUserWWStatus = async (mediaId, url) => {
    try {
      const [watchedStatus, watchlistStatus] = await Promise.all([
        userWWStatus(url, mediaId, WatchlistWatched.Watched),
        userWWStatus(url, mediaId, WatchlistWatched.Watchlist),
      ]);

      return {
        watched: watchedStatus,
        watchlist: watchlistStatus,
      };
    } catch (error) {
      return {
        watched: false,
        watchlist: false,
      };
    }
  };

  const currentUserWatchedStatus = async (mediaId, url) => {
    try {
      const [watchedStatus, watchlistStatus] = await Promise.all([
        userWatchedStatus(url, mediaId, WatchlistWatched.Watched),
      ]);

      return {
        watched: watchedStatus,
      };
    } catch (error) {
      return {
        watched: false,
      };
    }
  };

  const currentUserHasLikedReview = async (url, username) => {
    try {
      const res = await userApi.currentUserHasLikedReview(url, username);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const currentUserHasLikedMoovieListReview = async (url, username) => {
    try {
      const res = await userApi.currentUserHasLikedMoovieListReview(url, username);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const currentUserCommentFeedback = async (commentId, username) => {
    try {
      let res = await userApi.currentUserCommentFeedback(commentId, username);
      if (res.status === 204) {
        return CommentStatusEnum.NONE;
      }
      res = res.data;
      if (res.liked === true) {
        return CommentStatusEnum.LIKE;
      }
      if (res.disliked === true) {
        return CommentStatusEnum.DISLIKE;
      }
      return CommentStatusEnum.NONE;
    } catch (e) {
      return CommentStatusEnum.NONE;
    }
  };

  const banUser = async (url) => {
    return await userApi.banUser(url);
  }

  const unbanUser = async (url) => {
      return await userApi.unbanUser(url);
  }

  const makeUserModerator = async (url) => {
      return await userApi.makeUserModerator(url);
  }

  return {
    getMilkyLeaderboard,
    getSearchedUsers,
    setPfp,
    getSpecialListFromUser,
    currentUserHasLiked,
    currentUserHasFollowed,
    userWWStatus,
    userWatchedStatus,
    insertMediaIntoWW,
    removeMediaFromWW,
    currentUserLikeFollowStatus,
    currentUserWWStatus,
    currentUserWatchedStatus,
    currentUserHasLikedReview,
    currentUserHasLikedMoovieListReview,
    currentUserCommentFeedback,
    banUser,
    unbanUser,
    makeUserModerator,
  };
})();

export default UserService;
