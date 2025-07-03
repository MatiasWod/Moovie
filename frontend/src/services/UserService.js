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

  const getLikedOrFollowedListFromUser = async (username, type, orderBy, sortOrder, pageNumber) => {
    const res = await userApi.getLikedOrFollowedListFromUser(
      username,
      type,
      orderBy,
      sortOrder,
      pageNumber
    );
    return parsePaginatedResponse(res);
  };

  const currentUserHasLiked = async (moovieListId, username) => {
    try {
      const res = await userApi.currentUserHasLikedList(moovieListId, username);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const currentUserHasFollowed = async (moovieListId, username) => {
    try {
      const res = await userApi.currentUserHasFollowedList(moovieListId, username);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const userWWStatus = async (ww, mediaId, username) => {
    try {
      const res = await userApi.currentUserWW(ww, username, mediaId);
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

  const removeMediaFromWW = async (ww, mediaId, username) => {
    try {
      return await userApi.removeMediaFromWW(ww, username, mediaId);
    } catch (error) {
      return null;
    }
  };

  const currentUserLikeFollowStatus = async (moovieListId, username) => {
    try {
      const [likedStatus, followedStatus] = await Promise.all([
        currentUserHasLiked(moovieListId, username),
        currentUserHasFollowed(moovieListId, username),
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

  const currentUserWWStatus = async (mediaId, username) => {
    try {
      const [watchedStatus, watchlistStatus] = await Promise.all([
        userWWStatus(WatchlistWatched.Watched, mediaId, username),
        userWWStatus(WatchlistWatched.Watchlist, mediaId, username),
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

  const currentUserHasLikedReview = async (reviewId, username) => {
    try {
      const res = await userApi.currentUserHasLikedReview(reviewId, username);
      const parsedResponse = parsePaginatedResponse(res);
      if (!parsedResponse || res.status === 204) {
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  const currentUserHasLikedMoovieListReview = async (reviewId, username) => {
    try {
      const res = await userApi.currentUserHasLikedMoovieListReview(reviewId, username);
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

  const getWatchedCountFromMovieListId = async (moovieListId, username) => {
    try {
      const res = await userApi.getWatchedCountFromMovieListId(moovieListId, username);
      return parsePaginatedResponse(res);
    } catch (e) {
      return null;
    }
  };

  return {
    getMilkyLeaderboard,
    getSearchedUsers,
    setPfp,
    getSpecialListFromUser,
    getLikedOrFollowedListFromUser,
    currentUserHasLiked,
    currentUserHasFollowed,
    userWWStatus,
    insertMediaIntoWW,
    removeMediaFromWW,
    currentUserLikeFollowStatus,
    currentUserWWStatus,
    currentUserHasLikedReview,
    currentUserHasLikedMoovieListReview,
    currentUserCommentFeedback,
    getWatchedCountFromMovieListId,
  };
})();

export default UserService;
