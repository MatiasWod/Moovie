import UserService from '../../services/UserService';
import userApi from '../../api/UserApi';
import { parsePaginatedResponse } from '../../utils/ResponseUtils';

jest.mock('../../api/UserApi'); // Mock the entire userApi module

describe('UserService', () => {
  afterEach(() => {
    jest.clearAllMocks(); // Clear mocks after each test
  });

  beforeEach(() => {
    userApi.getMilkyLeaderboard = jest.fn();
    userApi.getSearchedUsers = jest.fn();
    userApi.setPfp = jest.fn();
    userApi.getSpecialListFromUser = jest.fn();
    userApi.getLikedOrFollowedListFromUser = jest.fn();
    userApi.currentUserHasLikedList = jest.fn();
    userApi.likeList = jest.fn();
    userApi.unlikeList = jest.fn();
    userApi.currentUserHasFollowedList = jest.fn();
    userApi.followList = jest.fn();
    userApi.unfollowList = jest.fn();
    userApi.currentUserWW = jest.fn();
    userApi.insertMediaIntoWW = jest.fn();
    userApi.removeMediaFromWW = jest.fn();
  });

  test('should fetch milky leaderboard', async () => {
    const mockLeaderboard = { data: [{ username: 'user1' }, { username: 'user2' }] };
    userApi.getMilkyLeaderboard.mockResolvedValue(mockLeaderboard);

    const result = await UserService.getMilkyLeaderboard({ page: 1, pageSize: 10 });

    expect(userApi.getMilkyLeaderboard).toHaveBeenCalledWith({ page: 1, pageSize: 10 });
    expect(result.data).toEqual(mockLeaderboard.data);
  });

  test('should fetch searched users', async () => {
    const mockUsers = { data: [{ username: 'user1' }, { username: 'user2' }], links: null };
    userApi.getSearchedUsers.mockResolvedValue(mockUsers);

    const result = await UserService.getSearchedUsers({
      username: 'user',
      orderBy: 'username',
      sortOrder: 'asc',
      page: 1,
    });

    expect(userApi.getSearchedUsers).toHaveBeenCalledWith({
      username: 'user',
      orderBy: 'username',
      sortOrder: 'asc',
      page: 1,
    });
    expect(result).toEqual(mockUsers);
  });

  test('should set profile picture', async () => {
    const mockResponse = { status: 200 };
    userApi.setPfp.mockResolvedValue(mockResponse);

    const result = await UserService.setPfp({ username: 'user1', pfp: 'image-data' });

    expect(userApi.setPfp).toHaveBeenCalledWith('user1', 'image-data');
    expect(result).toEqual(mockResponse);
  });

  test('should fetch special list from user', async () => {
    const mockList = { data: [{ id: 1, name: 'Favorite Movies' }], links: null };
    userApi.getSpecialListFromUser.mockResolvedValue(mockList);

    const result = await UserService.getSpecialListFromUser({
      username: 'user1',
      type: 'watched',
      orderBy: 'name',
      order: 'asc',
      pageNumber: 1,
    });

    expect(userApi.getSpecialListFromUser).toHaveBeenCalledWith(
      'user1',
      'watched',
      'name',
      'asc',
      1
    );
    expect(result).toEqual(mockList);
  });

  test('should get liked or followed list from user', async () => {
    const mockList = { data: [{ id: 1, name: 'Liked List' }], links: null };
    userApi.getLikedOrFollowedListFromUser.mockResolvedValue(mockList);

    const result = await UserService.getLikedOrFollowedListFromUser(
      'user1',
      'liked',
      'name',
      'asc',
      1
    );

    expect(userApi.getLikedOrFollowedListFromUser).toHaveBeenCalledWith(
      'user1',
      'liked',
      'name',
      'asc',
      1
    );
    expect(result).toEqual(mockList);
  });

  test('should check if user has liked a list', async () => {
    const mockResponse = { status: 200, data: { liked: true } };
    userApi.currentUserHasLikedList.mockResolvedValue(mockResponse);

    const result = await UserService.currentUserHasLiked(1, 'user1');

    expect(userApi.currentUserHasLikedList).toHaveBeenCalledWith(1, 'user1');
    expect(result).toEqual(true);
  });

  test('should check if user has followed a list', async () => {
    const mockResponse = { status: 200, data: { followed: true } };
    userApi.currentUserHasFollowedList.mockResolvedValue(mockResponse);

    const result = await UserService.currentUserHasFollowed(1, 'user1');

    expect(userApi.currentUserHasFollowedList).toHaveBeenCalledWith(1, 'user1');
    expect(result).toEqual(true);
  });

  test('should check user WW status', async () => {
    const mockStatus = { watched: true };
    userApi.currentUserWW.mockResolvedValue(mockStatus);

    const result = await UserService.userWWStatus('watched', 1, 'user1');

    expect(userApi.currentUserWW).toHaveBeenCalledWith('watched', 'user1', 1);
    expect(result).toEqual(mockStatus.watched);
  });

  test('should insert media into WW', async () => {
    const mockResponse = { status: 200 };
    userApi.insertMediaIntoWW.mockResolvedValue(mockResponse);

    const result = await UserService.insertMediaIntoWW('watched', 1, 'user1');

    expect(userApi.insertMediaIntoWW).toHaveBeenCalledWith('watched', 1, 'user1');
    expect(result).toEqual(mockResponse);
  });

  test('should remove media from WW', async () => {
    const mockResponse = { status: 200 };
    userApi.removeMediaFromWW.mockResolvedValue(mockResponse);

    const result = await UserService.removeMediaFromWW('watched', 1, 'user1');

    expect(userApi.removeMediaFromWW).toHaveBeenCalledWith('watched', 'user1', 1);
    expect(result).toEqual(mockResponse);
  });
});
