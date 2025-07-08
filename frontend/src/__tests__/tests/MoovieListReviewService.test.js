import MoovieListReviewService from '../../services/MoovieListReviewService';
import moovieListReviewApi from '../../api/MoovieListReviewApi';

jest.mock('../../api/MoovieListReviewApi');

describe('MoovieListReviewService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('editReview', () => {
    it('should edit a review and return the response', async () => {
      const response = { status: 200 };
      moovieListReviewApi.editReview.mockResolvedValue(response);

      const result = await MoovieListReviewService.editReview(process.env.REACT_APP_API_URL + '/moovieListReviews/1', 42, 'Great list!');

      expect(moovieListReviewApi.editReview).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + '/moovieListReviews/1', 42, 'Great list!');
      expect(result).toEqual(response);
    });
  });

  describe('createMoovieListReview', () => {
    it('should create a review and return the response', async () => {
      const response = { id: 1, content: 'Nice!' };
      moovieListReviewApi.createMoovieListReview.mockResolvedValue(response);

      const result = await MoovieListReviewService.createMoovieListReview(99, 'Nice!');

      expect(moovieListReviewApi.createMoovieListReview).toHaveBeenCalledWith(99, 'Nice!');
      expect(result).toEqual(response);
    });
  });

  describe('deleteMoovieListReviewByUrl', () => {
    it('should delete the review and return the response', async () => {
      const response = { status: 204 };
      moovieListReviewApi.deleteMoovieListReviewByUrl.mockResolvedValue(response);

      const result = await MoovieListReviewService.deleteMoovieListReviewByUrl(process.env.REACT_APP_API_URL + '/moovieListReviews/1');

      expect(moovieListReviewApi.deleteMoovieListReviewByUrl).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + '/moovieListReviews/1');
      expect(result).toEqual(response);
    });
  });

  describe('likeMoovieListReview', () => {
    it('should like a review and return the response', async () => {
      const response = { status: 200 };
      moovieListReviewApi.likeMoovieListReview.mockResolvedValue(response);

      const result = await MoovieListReviewService.likeMoovieListReview(process.env.REACT_APP_API_URL + '/moovieListReviews/1/likes');

      expect(moovieListReviewApi.likeMoovieListReview).toHaveBeenCalledWith(process.env.REACT_APP_API_URL + '/moovieListReviews/1/likes');
      expect(result).toEqual(response);
    });
  });

  describe('deleteLikeFromMoovieListReview', () => {
    it('should delete a like and return the response', async () => {
      const response = { status: 204 };
      moovieListReviewApi.deleteLikeFromMoovieListReview.mockResolvedValue(response);

      const result = await MoovieListReviewService.deleteLikeFromMoovieListReview('/moovieListReviews/1/likes', 'john');

      expect(moovieListReviewApi.deleteLikeFromMoovieListReview).toHaveBeenCalledWith('/moovieListReviews/1/likes', 'john');
      expect(result).toEqual(response);
    });
  });
});