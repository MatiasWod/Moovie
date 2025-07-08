import ListService from '../../services/ListService';
import listApi from '../../api/ListApi';
import api from '../../api/api';
import { parsePaginatedResponse } from '../../utils/ResponseUtils';
import {mediaIdListIdDto, moovieListDto} from "../mocks";

jest.mock('../../api/api');
jest.mock('../../api/ListApi');
jest.mock('../../utils/ResponseUtils');

describe('ListService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getLists', () => {
    it('should return parsed paginated lists', async () => {
      const response = { data: [moovieListDto], status: 200 };
      listApi.getLists.mockResolvedValue(response);
      parsePaginatedResponse.mockReturnValue(response);

      const result = await ListService.getLists({
        search: 'My Favorite Movies',
        ownerUsername: 'john_doe',
        type: 1,
        orderBy: 'likeCount',
        order: 'desc',
        pageNumber: 1,
        pageSize: 10,
      });

      expect(listApi.getLists).toHaveBeenCalledWith({
        search: 'My Favorite Movies',
        ownerUsername: 'john_doe',
        type: 1,
        orderBy: 'likeCount',
        order: 'desc',
        pageNumber: 1,
        pageSize: 10,
      });
      expect(parsePaginatedResponse).toHaveBeenCalledWith(response);
      expect(result).toBe(response);
      expect(result.data[0]).toEqual(moovieListDto);
      expect(result.status).toBe(200);
    });
  });

  describe('getListById', () => {
    it('should return parsed list by id', async () => {
      const response = { data: moovieListDto };
      listApi.getListById.mockResolvedValue(response);
      parsePaginatedResponse.mockReturnValue(response);

      const result = await ListService.getListById(1);

      expect(listApi.getListById).toHaveBeenCalledWith(1);
      expect(parsePaginatedResponse).toHaveBeenCalledWith(response);
      expect(result).toBe(response);
      expect(result.data).toEqual(moovieListDto);
    });
  });

  describe('getListsFromUrl', () => {
    it('should return lists from URL', async () => {
      const url = process.env.REACT_APP_API_URL + '/lists?page=1';
      const data = [moovieListDto];
      listApi.getListsFromUrl.mockResolvedValue(data);

      const result = await ListService.getListsFromUrl({ url, pageNumber: 1, pageSize: 5 });

      expect(listApi.getListsFromUrl).toHaveBeenCalledWith(url, 1, 5);
      expect(result).toEqual(data);
      expect(result.length).toBe(1);
      expect(result[0]).toEqual(moovieListDto);
    });
  });

  describe('editMoovieList', () => {
    it('should trim name and description and call API', async () => {
      const response = { status: 200 };
      listApi.editMoovieList.mockResolvedValue(response);

      const result = await ListService.editMoovieList({
        url: process.env.REACT_APP_API_URL + "/lists/1",
        name: 'My New List',
        description: 'New description',
      });

      expect(listApi.editMoovieList).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + "/lists/1", 'My New List', 'New description');
      expect(result).toBe(response);
      expect(result.status).toBe(200);
    });
  });

  describe('getListContent', () => {
    it('should return media data enriched with customOrder', async () => {
      const mockResponse = {
        data: [
          { mediaUrl: process.env.REACT_APP_API_URL + "/medias/190", customOrder: 1 },
          { mediaUrl: process.env.REACT_APP_API_URL + "/medias/1", customOrder: 2 },
        ],
      };
      const mockLinks = { self: process.env.REACT_APP_API_URL + '/lists/1/content' };
      parsePaginatedResponse.mockReturnValueOnce(mockResponse).mockReturnValueOnce({ links: mockLinks });

      api.get.mockImplementation((url) =>
          Promise.resolve({
            data: { id: Number(url.split('/').pop()) },
          })
      );

      const result = await ListService.getListContent({
        url: process.env.REACT_APP_API_URL + '/lists/1/content',
        orderBy: 'name',
        sortOrder: 'asc',
        pageNumber: 1,
        pageSize: 2,
      });

      expect(listApi.getListContent).toHaveBeenCalledWith({
        url: process.env.REACT_APP_API_URL + '/lists/1/content',
        orderBy: 'name',
        sortOrder: 'asc',
        pageNumber: 1,
        pageSize: 2,
      });

      expect(result.data[0]).toEqual(expect.objectContaining({ id: 190, customOrder: 1 }));
      expect(result.data[1]).toEqual(expect.objectContaining({ id: 1, customOrder: 2 }));
      expect(result.links).toEqual(mockLinks);
    });

    it('should return empty array and links if list is empty', async () => {
      parsePaginatedResponse.mockReturnValueOnce({ data: [] }).mockReturnValueOnce({ links: { next: '/page/2' } });

      const result = await ListService.getListContent({
        url: process.env.REACT_APP_API_URL + '/lists/420/content',
        orderBy: 'name',
        sortOrder: 'asc',
        pageNumber: 1,
        pageSize: 5,
      });

      expect(result).toEqual({ data: [], links: { next: '/page/2' } });
    });
  });

  describe('getListContentByMediaId', () => {
    it('should call listApi with url and mediaId', async () => {
      const response = mediaIdListIdDto;
      listApi.getListContentByMediaId.mockResolvedValue(response);

      const result = await ListService.getListContentByMediaId({ url: process.env.REACT_APP_API_URL + "/lists/1/content", mediaId: 190 });

      expect(listApi.getListContentByMediaId).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + "/lists/1/content", 190);
      expect(result).toEqual(response);
      expect(result.mediaId).toBe(190);
      expect(result).toEqual(mediaIdListIdDto);
    });
  });

  describe('createMoovieList', () => {
    it('should create a new list', async () => {
      const response = { status: 201 };
      listApi.createMoovieList.mockResolvedValue(response);

      const result = await ListService.createMoovieList({
        name: 'New List',
        type: 2,
        description: 'My favorite movies',
      });

      expect(listApi.createMoovieList).toHaveBeenCalledWith("New List", 2, 'My favorite movies');
      expect(result).toEqual(response);
      expect(result.status).toBe(201);
    });
  });

  describe('editListContent', () => {
    it('should call editListContent with correct values', async () => {
      const response = { status: 200 };
      listApi.editListContent.mockResolvedValue(response);

      const result = await ListService.editListContent({
        url: process.env.REACT_APP_API_URL + '/lists/1/content',
        mediaId: 101,
        customOrder: 5,
      });

      expect(listApi.editListContent).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + '/lists/1/content', 101, 5);
      expect(result).toEqual(response);
      expect(result.status).toBe(200);
    });
  });

  describe('insertMediaIntoMoovieList', () => {
    it('should insert media IDs into list', async () => {
      const response = { status: 201 };
      listApi.insertMediaIntoMoovieList.mockResolvedValue(response);

      const result = await ListService.insertMediaIntoMoovieList({
        url: process.env.REACT_APP_API_URL + '/lists/1/content',
        mediaIds: [1, 2, 3],
      });

      expect(listApi.insertMediaIntoMoovieList).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + '/lists/1/content', [1, 2, 3]);
      expect(result).toEqual(response);
    });
  });

  describe('deleteMediaFromMoovieList', () => {
    it('should delete media from list', async () => {
      const response = { status: 204 };
      listApi.deleteMediaFromMoovieList.mockResolvedValue(response);

      const result = await ListService.deleteMediaFromMoovieList({
        url: '/lists/1/content',
        mediaId: 1,
      });

      expect(listApi.deleteMediaFromMoovieList).toHaveBeenCalledWith({ url: '/lists/1/content', mediaId: 1 });
      expect(result).toEqual(response);
    });
  });

  describe('likeList', () => {
    it('should return response if successful', async () => {
      const response = { status: 200 };
      listApi.likeList.mockResolvedValue(response);

      const result = await ListService.likeList('/lists/1/likes');

      expect(listApi.likeList).toHaveBeenCalledWith('/lists/1/likes');
      expect(result).toEqual(response);
    });

    it('should return null on error', async () => {
      listApi.likeList.mockRejectedValue(new Error('Error'));

      const result = await ListService.likeList('/lists/1/likes');

      expect(result).toBeNull();
    });
  });
});
