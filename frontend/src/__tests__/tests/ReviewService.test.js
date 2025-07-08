import ReviewService from '../../services/ReviewService';
import reviewApi from '../../api/ReviewApi';
import {reviewDto} from "../mocks";

jest.mock('../../api/ReviewApi');

describe('ReviewService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getReviewsByMediaIdandUrl', () => {
    it('should return reviews for given mediaId and URL', async () => {
      const mockReviews = [reviewDto];
      reviewApi.getReviewsByMediaIdandUrl.mockResolvedValue(mockReviews);

      const result = await ReviewService.getReviewsByMediaIdandUrl(process.env.REACT_APP_API_URL + '/reviews', 14);

      expect(reviewApi.getReviewsByMediaIdandUrl).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + '/reviews', 14);
      expect(result).toEqual(mockReviews);
      expect(result).toHaveLength(1);
      expect(result[0]).toEqual(reviewDto);
    });
  });

  describe('editReview', () => {
    it('should edit review and return response', async () => {
      const mockResponse = { status: 200 };
      reviewApi.editReview.mockResolvedValue(mockResponse);

      const args = {
        url: reviewDto.url,
        mediaId: reviewDto.mediaId,
        rating: 2,
        reviewContent: 'Updated review',
      };

      const result = await ReviewService.editReview(args.url, args.mediaId, args.rating, args.reviewContent);

      expect(reviewApi.editReview).toHaveBeenCalledWith(args);
      expect(result).toEqual(mockResponse);
      expect(result.status).toBe(200);
    });
  });

  describe('createReview', () => {
    it('should create a review and return response', async () => {
      const mockResponse = { status: 201 };
      reviewApi.createReview.mockResolvedValue(mockResponse);

      const result = await ReviewService.createReview(5, 5, 'Awesome!');

      expect(reviewApi.createReview).toHaveBeenCalledWith({
        mediaId: 5,
        rating: 5,
        reviewContent: 'Awesome!',
      });
      expect(result).toEqual(mockResponse);
      expect(result.status).toBe(201);
    });
  });

  describe('deleteReviewByUrl', () => {
    it('should delete review and return response', async () => {
      const mockResponse = { status: 204 };
      reviewApi.deleteReviewByUrl.mockResolvedValue(mockResponse);

      const result = await ReviewService.deleteReviewByUrl(reviewDto.url);

      expect(reviewApi.deleteReviewByUrl).toHaveBeenCalledWith(reviewDto.url);
      expect(result).toEqual(mockResponse);
      expect(result.status).toBe(204);
    });
  });

  describe('likeReview', () => {
    it('should like a review and return response', async () => {
      const mockResponse = { status: 200 };
      reviewApi.likeReview.mockResolvedValue(mockResponse);

      const result = await ReviewService.likeReview(reviewDto.likesUrl);

      expect(reviewApi.likeReview).toHaveBeenCalledWith(reviewDto.likesUrl);
      expect(result).toEqual(mockResponse);
      expect(result.status).toBe(200);
    });
  });

  describe('deleteLikeFromReview', () => {
    it('should delete like from review and return response', async () => {
      const mockResponse = { status: 204 };
      reviewApi.deleteLikeFromReview.mockResolvedValue(mockResponse);

      const result = await ReviewService.deleteLikeFromReview(reviewDto.likesUrl + '/' + reviewDto.username);

      expect(reviewApi.deleteLikeFromReview).toHaveBeenCalledWith(reviewDto.likesUrl + '/' + reviewDto.username);
      expect(result).toEqual(mockResponse);
    });
  });
});