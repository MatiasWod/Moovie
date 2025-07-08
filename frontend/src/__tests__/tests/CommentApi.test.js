import commentApi from '../../api/CommentApi.js';
import api from '../../api/api.js';
import VndType from '../../enums/VndType';
import {commentDto, reviewDto} from "../mocks";

jest.mock('../../api/api.js');

describe('commentApi', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('createReviewComment', () => {
    it('should call api.post with correct url, body and headers', async () => {
      const mockResponse = { status: 201 };
      api.post.mockResolvedValue(mockResponse);

      const reviewId = reviewDto.id;
      const comment = 'Great review!';
      const response = await commentApi.createReviewComment(reviewId, comment);

      expect(api.post).toHaveBeenCalledWith(
          '/comments/',
          { commentContent: comment, reviewId },
          { headers: { 'Content-Type': VndType.APPLICATION_COMMENT_FORM } }
      );
      expect(response).toEqual(mockResponse);
    });
  });

  describe('getReviewComments', () => {
    it('should call api.get with correct url and params', async () => {
      const mockResponse = { data: [commentDto], status: 200 };
      api.get.mockResolvedValue(mockResponse);

      const reviewId = reviewDto.id;
      const pageNumber = 1;
      const response = await commentApi.getReviewComments(reviewId, pageNumber);

      expect(api.get).toHaveBeenCalledWith('/comments/', {
        params: { reviewId, pageNumber },
      });
      expect(response).toEqual(mockResponse);
      expect(response.data).toEqual([commentDto]);
      expect(response.status).toBe(200);
    });

    it('should default pageNumber to 1 if not provided', async () => {
      api.get.mockResolvedValue({});

      await commentApi.getReviewComments(reviewDto.id);

      expect(api.get).toHaveBeenCalledWith('/comments/', {
        params: { reviewId: reviewDto.id, pageNumber: 1 },
      });
    });
  });

  describe('commentFeedback', () => {
    it('should call api.put with correct url, body and headers', async () => {
      const mockResponse = { status: 200 };
      api.put.mockResolvedValue(mockResponse);

      const commentId = commentDto.id;
      const feedback = 'like';
      const response = await commentApi.commentFeedback(commentId, feedback);

      expect(api.put).toHaveBeenCalledWith(
          '/comments/' + commentId,
          { feedbackType: feedback },
          { headers: { 'Content-Type': VndType.APPLICATION_COMMENT_FEEDBACK_FORM } }
      );
      expect(response).toEqual(mockResponse);
    });
  });

  describe('deleteComment', () => {
    it('should call api.delete with correct url', async () => {
      const mockResponse = { status: 204 };
      api.delete.mockResolvedValue(mockResponse);

      const commentId = commentDto.id;
      const response = await commentApi.deleteComment(commentId);

      expect(api.delete).toHaveBeenCalledWith('/comments/' + commentId);
      expect(response).toEqual(mockResponse);
    });
  });

  describe('getCommentsFromUrl', () => {
    it('should call api.get with url and params', async () => {
      const mockResponse = { data: [commentDto], status: 200 };
      api.get.mockResolvedValue(mockResponse);

      const url = reviewDto.commentsUrl;
      const pageNumber = 1;
      const pageSize = 10;

      const response = await commentApi.getCommentsFromUrl(url, pageNumber, pageSize);

      expect(api.get).toHaveBeenCalledWith(url, {
        params: { pageNumber, pageSize },
      });
      expect(response).toEqual(mockResponse);
      expect(response.data).toEqual([commentDto]);
      expect(response.status).toBe(200);
    });

    it('should omit params if undefined', async () => {
      api.get.mockResolvedValue({});

      const url = reviewDto.commentsUrl;

      await commentApi.getCommentsFromUrl(url);

      expect(api.get).toHaveBeenCalledWith(url, { params: {} });
    });
  });

  describe('getReportedComments', () => {
    it('should call api.get with isReported=true and pageNumber', async () => {
      const mockResponse = { data: [commentDto] };
      api.get.mockResolvedValue(mockResponse);

      const pageNumber = 1;
      const response = await commentApi.getReportedComments(pageNumber);

      expect(api.get).toHaveBeenCalledWith('/comments', {
        params: { isReported: true, pageNumber },
      });
      expect(response).toEqual(mockResponse);
        expect(response.data).toEqual([commentDto]);
    });

    it('should default pageNumber to 1', async () => {
      api.get.mockResolvedValue({});

      await commentApi.getReportedComments();

      expect(api.get).toHaveBeenCalledWith('/comments', {
        params: { isReported: true, pageNumber: 1 },
      });
    });
  });
});
