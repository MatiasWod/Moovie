import UserService from '../../services/UserService';
import userApi from '../../api/UserApi';
import { parsePaginatedResponse } from '../../utils/ResponseUtils';
import {moovieListDto, userDto, userDto2, watchedMoovieListDto} from "../mocks";
import MoovieListTypes from "../../api/values/MoovieListTypes";
import CommentStatusEnum from "../../api/values/CommentStatusEnum";

jest.mock('../../api/UserApi');
jest.mock('../../utils/ResponseUtils');

describe('UserService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getMilkyLeaderboard', () => {
    it('should return parsed paginated leaderboard data', async () => {
      const apiResponse = { data: [userDto, userDto2], status: 200 };
      userApi.getMilkyLeaderboard.mockResolvedValue(apiResponse);
      parsePaginatedResponse.mockReturnValue(apiResponse);

      const result = await UserService.getMilkyLeaderboard({ page: 1, pageSize: 5 });

      expect(userApi.getMilkyLeaderboard).toHaveBeenCalledWith({ page: 1, pageSize: 5 });
      expect(parsePaginatedResponse).toHaveBeenCalledWith(apiResponse);
      expect(result).toBe(apiResponse);
      expect(result.data).toHaveLength(2);
      expect(result.data[0]).toEqual(userDto);
      expect(result.data[1]).toEqual(userDto2);
      expect(result.status).toBe(200);
    });
  });

  describe('getSearchedUsers', () => {
    it('should return parsed paginated searched users', async () => {
      const apiResponse = { data: [userDto, userDto2], status: 200 };
      userApi.getSearchedUsers.mockResolvedValue(apiResponse);
      parsePaginatedResponse.mockReturnValue(apiResponse);

      const params = { username: 'testUser', orderBy: 'name', sortOrder: 'asc', page: 1 };
      const result = await UserService.getSearchedUsers(params);

      expect(userApi.getSearchedUsers).toHaveBeenCalledWith(params);
      expect(parsePaginatedResponse).toHaveBeenCalledWith(apiResponse);
      expect(result).toBe(apiResponse);
      expect(result.data).toHaveLength(2);
      expect(result.data[0]).toEqual(userDto);
      expect(result.data[1]).toEqual(userDto2);
      expect(result.status).toBe(200);
    });
  });

  describe('setPfp', () => {
    it('should set profile picture for username', async () => {
      const response = { status: 201 };
      userApi.setPfp.mockResolvedValue(response);

      const result = await UserService.setPfp({ username: userDto.username, pfp: 'imageData' });

      expect(userApi.setPfp).toHaveBeenCalledWith(userDto.username, 'imageData');
      expect(result).toEqual(response);
      expect(result.status).toBe(201);
    });
  });

  describe('getSpecialListFromUser', () => {
    it('should return parsed paginated special list', async () => {
      const apiResponse = { data: [watchedMoovieListDto], status: 200 };
      userApi.getSpecialListFromUser.mockResolvedValue(apiResponse);
      parsePaginatedResponse.mockReturnValue(apiResponse);

      const args = { username: userDto.username, type: MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type, orderBy: null, order: null, pageNumber: 1 };
      const result = await UserService.getSpecialListFromUser(args);

      expect(userApi.getSpecialListFromUser).toHaveBeenCalledWith(userDto.username, MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type, null, null, 1);
      expect(parsePaginatedResponse).toHaveBeenCalledWith(apiResponse);
      expect(result).toBe(apiResponse);
      expect(result.data).toHaveLength(1);
      expect(result.data[0]).toEqual(watchedMoovieListDto);
      expect(result.status).toBe(200);
    });
  });

  // Utility function to test the boolean status methods that handle 204 and errors
  const testBooleanStatusMethod = (methodName, apiMethodName, ...args) => {
    describe(methodName, () => {
      it('should return true when API returns data', async () => {
        userApi[apiMethodName].mockResolvedValue({ status: 200, data: true });
        parsePaginatedResponse.mockReturnValue(true);

        const result = await UserService[methodName](...args);

        expect(userApi[apiMethodName]).toHaveBeenCalled();
        expect(result).toBe(true);
      });

      it('should return false when API returns 204', async () => {
        userApi[apiMethodName].mockResolvedValue({ status: 204 });
        parsePaginatedResponse.mockReturnValue(null);

        const result = await UserService[methodName](...args);

        expect(result).toBe(false);
      });

      it('should return false on error', async () => {
        userApi[apiMethodName].mockRejectedValue(new Error('fail'));

        const result = await UserService[methodName](...args);

        expect(result).toBe(false);
      });
    });
  };

  testBooleanStatusMethod('currentUserHasLiked', 'currentUserHasLikedList', userDto.likedMoovieListsUrl, userDto.username);
  testBooleanStatusMethod('currentUserHasFollowed', 'currentUserHasFollowedList', userDto.followedMoovieListsUrl, userDto.username);
  testBooleanStatusMethod('currentUserHasLikedReview', 'currentUserHasLikedReview', userDto.likedReviewsUrl, userDto.username);
  testBooleanStatusMethod('currentUserHasLikedMoovieListReview', 'currentUserHasLikedMoovieListReview', userDto.likedMoovieListsReviewsUrl, userDto.username);

  // userWWStatus and userWatchedStatus are similar, but with 3 params
  const testUserStatusMethod = (methodName, apiMethodName) => {
    describe(methodName, () => {
      it('should return true when API returns data', async () => {
        userApi[apiMethodName].mockResolvedValue({ status: 200, data: true });
        parsePaginatedResponse.mockReturnValue(true);

        const result = await UserService[methodName](userDto.url, 1, 'search');

        expect(userApi[apiMethodName]).toHaveBeenCalledWith(userDto.moovieListsUrl, 1, 'search');
        expect(result).toBe(true);
      });

      it('should return false when API returns 204', async () => {
        userApi[apiMethodName].mockResolvedValue({ status: 204 });
        parsePaginatedResponse.mockReturnValue(null);

        const result = await UserService[methodName](userDto.moovieListsUrl, 1, 'search');

        expect(result).toBe(false);
      });

      it('should return false on error', async () => {
        userApi[apiMethodName].mockRejectedValue(new Error('fail'));

        const result = await UserService[methodName]('url', 1, 'search');

        expect(result).toBe(false);
      });
    });
  };

  testUserStatusMethod('userWWStatus', 'getMediaStatusFromWW');
  testUserStatusMethod('userWatchedStatus', 'getMediaStatusFromWatched');

  describe('insertMediaIntoWW', () => {
    it('should insert media and return response', async () => {
      const response = { status: 200 };
      userApi.insertMediaIntoWW.mockResolvedValue(response);

      const result = await UserService.insertMediaIntoWW('watched', 1, userDto.username);

      expect(userApi.insertMediaIntoWW).toHaveBeenCalledWith('watched', 1, userDto.username);
      expect(result).toEqual(response);
    });

    it('should return null on error', async () => {
      userApi.insertMediaIntoWW.mockRejectedValue(new Error('fail'));

      const result = await UserService.insertMediaIntoWW('watched', 1, userDto.username);

      expect(result).toBeNull();
    });
  });

  describe('removeMediaFromWW', () => {
    it('should remove media and return response', async () => {
      const response = { status: 204 };
      userApi.removeMediaFromWW.mockResolvedValue(response);

      const result = await UserService.removeMediaFromWW('watched', 1, userDto.username);

      expect(userApi.removeMediaFromWW).toHaveBeenCalledWith('watched', 1, userDto.username);
      expect(result).toEqual(response);
    });

    it('should return null on error', async () => {
      userApi.removeMediaFromWW.mockRejectedValue(new Error('fail'));

      const result = await UserService.removeMediaFromWW('url', 7, 'search');

      expect(result).toBeNull();
    });
  });

  describe('currentUserLikeFollowStatus', () => {
    it('should return liked and followed status', async () => {
      jest.spyOn(UserService, 'currentUserHasLiked').mockResolvedValue(false);
      jest.spyOn(UserService, 'currentUserHasFollowed').mockResolvedValue(false);

      const result = await UserService.currentUserLikeFollowStatus(moovieListDto.likesUrl, moovieListDto.followersUrl, userDto.username);

      expect(result).toEqual({ liked: false, followed: false });
    });

    it('should return false for both on error', async () => {
      jest.spyOn(UserService, 'currentUserHasLiked').mockRejectedValue(new Error('fail'));
      jest.spyOn(UserService, 'currentUserHasFollowed').mockRejectedValue(new Error('fail'));

      const result = await UserService.currentUserLikeFollowStatus('likesUrl', 'followersUrl', 'user');

      expect(result).toEqual({ liked: false, followed: false });
    });
  });

  describe('currentUserWWStatus', () => {
    it('should return watched and watchlist status', async () => {
      jest.spyOn(UserService, 'userWWStatus').mockResolvedValue(false);

      const result = await UserService.currentUserWWStatus(1, watchedMoovieListDto.url);

      expect(result).toEqual({ watched: false, watchlist: false });
    });

    it('should return false for both on error', async () => {
      jest.spyOn(UserService, 'userWWStatus').mockRejectedValue(new Error('fail'));

      const result = await UserService.currentUserWWStatus(1, 'url');

      expect(result).toEqual({ watched: false, watchlist: false });
    });
  });

  describe('currentUserWatchedStatus', () => {
    it('should return watched status', async () => {
      jest.spyOn(UserService, 'userWatchedStatus').mockResolvedValue(false);

      const result = await UserService.currentUserWatchedStatus(1, watchedMoovieListDto.url);

      expect(result).toEqual({ watched: false });
    });

    it('should return false watched status on error', async () => {
      jest.spyOn(UserService, 'userWatchedStatus').mockRejectedValue(new Error('fail'));

      const result = await UserService.currentUserWatchedStatus(1, 'url');

      expect(result).toEqual({ watched: false });
    });
  });

  describe('currentUserCommentFeedback', () => {
    it('should return LIKE if liked is true', async () => {
      userApi.currentUserCommentFeedback.mockResolvedValue({
        status: 200,
        data: { liked: true, disliked: false },
      });

      const result = await UserService.currentUserCommentFeedback(10, 'john');

      expect(result).toBe(CommentStatusEnum.LIKE);
    });

    it('should return DISLIKE if disliked is true', async () => {
      userApi.currentUserCommentFeedback.mockResolvedValue({
        status: 200,
        data: { liked: false, disliked: true },
      });

      const result = await UserService.currentUserCommentFeedback(10, 'john');

      expect(result).toBe(CommentStatusEnum.DISLIKE);
    });

    it('should return NONE if neither liked nor disliked', async () => {
      userApi.currentUserCommentFeedback.mockResolvedValue({
        status: 200,
        data: { liked: false, disliked: false },
      });

      const result = await UserService.currentUserCommentFeedback(10, 'john');

      expect(result).toBe(CommentStatusEnum.NONE);
    });

    it('should return NONE if status is 204', async () => {
      userApi.currentUserCommentFeedback.mockResolvedValue({
        status: 204,
      });

      const result = await UserService.currentUserCommentFeedback(10, 'john');

      expect(result).toBe(CommentStatusEnum.NONE);
    });

    it('should return NONE on error', async () => {
      userApi.currentUserCommentFeedback.mockRejectedValue(new Error('fail'));

      const result = await UserService.currentUserCommentFeedback(10, 'john');

      expect(result).toBe(CommentStatusEnum.NONE);
    });
  });

  describe('banUser', () => {
    it('should call banUser API and return response', async () => {
      const response = { status: 204 };
      userApi.banUser.mockResolvedValue(response);

      const result = await UserService.banUser('/ban/1');

      expect(userApi.banUser).toHaveBeenCalledWith('/ban/1');
      expect(result).toEqual(response);
    });
  });

  describe('unbanUser', () => {
    it('should call unbanUser API and return response', async () => {
      const response = { status: 204 };
      userApi.unbanUser.mockResolvedValue(response);

      const result = await UserService.unbanUser('/unban/1');

      expect(userApi.unbanUser).toHaveBeenCalledWith('/unban/1');
      expect(result).toEqual(response);
    });
  });

  describe('makeUserModerator', () => {
    it('should call makeUserModerator API and return response', async () => {
      const response = { status: 204 };
      userApi.makeUserModerator.mockResolvedValue(response);

      const result = await UserService.makeUserModerator('/mod/1');

      expect(userApi.makeUserModerator).toHaveBeenCalledWith('/mod/1');
      expect(result).toEqual(response);
    });
  });
});