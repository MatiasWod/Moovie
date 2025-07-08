import reportApi from '../../api/reportApi';
import api from '../../api/api.js';
import commentReportApi from '../../api/CommentReportApi';
import moovieListReportApi from '../../api/MoovieListReportApi';
import moovieListReviewReportApi from '../../api/MoovieListReviewReportApi.js';
import reviewReportApi from '../../api/ReviewReportApi.js';
import { parsePaginatedResponse } from '../../utils/ResponseUtils.js';
import {
  commentDto,
  commentReportDto,
  moovieListDto, moovieListReportDto,
  moovieListReviewDto, moovieListReviewReportDto,
  reportDto,
  reviewDto,
  reviewReportDto
} from "../mocks";
import ReportTypes from "../../api/values/ReportTypes";

jest.mock('../../api/api.js');
jest.mock('../../api/CommentReportApi');
jest.mock('../../api/MoovieListReportApi');
jest.mock('../../api/MoovieListReviewReportApi.js');
jest.mock('../../api/ReviewReportApi.js');
jest.mock('../../utils/ResponseUtils.js');

describe('reportApi', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('reporting methods', () => {
    it('reportReview calls reviewReportApi.reportReview with correct params', async () => {
      reviewReportApi.reportReview.mockResolvedValue({status: 201});
      const res = await reportApi.reportReview({ reviewId: reviewDto.id, type: ReportTypes.Spam });
      expect(reviewReportApi.reportReview).toHaveBeenCalledWith({ reviewId: reviewDto.id, type: ReportTypes.Spam });
      expect(res.status).toEqual(201);
    });

    it('reportComment calls commentReportApi.reportComment with correct params', async () => {
      commentReportApi.reportComment.mockResolvedValue({status: 201});
      const res = await reportApi.reportComment({ commentId: commentDto.id, type: ReportTypes.Hate });
      expect(commentReportApi.reportComment).toHaveBeenCalledWith({ commentId: commentDto.id, type: ReportTypes.Hate });
      expect(res.status).toEqual(201);
    });

    it('reportMoovieList calls moovieListReportApi.reportMoovieList with correct params', async () => {
      moovieListReportApi.reportMoovieList.mockResolvedValue({status: 201});
      const res = await reportApi.reportMoovieList({ moovieListId: moovieListDto.id, type: ReportTypes.Spam });
      expect(moovieListReportApi.reportMoovieList).toHaveBeenCalledWith({ moovieListId: moovieListDto.id, type: ReportTypes.Spam });
        expect(res.status).toEqual(201);
    });

    it('reportMoovieListReview calls moovieListReviewReportApi.reportMoovieListReview with correct params', async () => {
      moovieListReviewReportApi.reportMoovieListReview.mockResolvedValue({status: 201});
      const res = await reportApi.reportMoovieListReview({ moovieListReviewId: moovieListReviewDto.id, type: ReportTypes.Spam });
      expect(moovieListReviewReportApi.reportMoovieListReview).toHaveBeenCalledWith({ moovieListReviewId: moovieListReviewDto.id, type: ReportTypes.Spam  });
      expect(res.status).toEqual(201);
    });
  });


  describe('getReports', () => {
    it('calls commentReportApi.getReports for contentType "comment"', async () => {
      commentReportApi.getReports.mockResolvedValue(commentReportDto);
      const result = await reportApi.getReports({ contentType: 'comment', pageNumber: 1 });
      expect(commentReportApi.getReports).toHaveBeenCalledWith({ pageNumber: 1 });
      expect(result).toBe(commentReportDto);
    });

    it('calls reviewReportApi.getReports for contentType "review"', async () => {
      reviewReportApi.getReports.mockResolvedValue(reviewReportDto);
      const result = await reportApi.getReports({ contentType: 'review', pageNumber: 1 });
      expect(reviewReportApi.getReports).toHaveBeenCalledWith({ pageNumber: 1 });
      expect(result).toBe(reviewReportDto);
    });

    it('calls moovieListReportApi.getReports for contentType "moovieList"', async () => {
      moovieListReportApi.getReports.mockResolvedValue(moovieListReportDto);
      const result = await reportApi.getReports({ contentType: 'moovieList', pageNumber: 1 });
      expect(moovieListReportApi.getReports).toHaveBeenCalledWith({ pageNumber: 1 });
      expect(result).toBe(moovieListReportDto);
    });

    it('calls moovieListReviewReportApi.getReports for contentType "moovieListReview"', async () => {
      moovieListReviewReportApi.getReports.mockResolvedValue(moovieListReviewReportDto);
      const result = await reportApi.getReports({ contentType: 'moovieListReview', pageNumber: 1 });
      expect(moovieListReviewReportApi.getReports).toHaveBeenCalledWith({ pageNumber: 1 });
      expect(result).toBe(moovieListReviewReportDto);
    });

    it('throws error for unknown contentType', async () => {
      await expect(reportApi.getReports({ contentType: 'unknown', pageNumber: 1 }))
          .rejects.toThrow('Unknown content type: unknown');
    });
  });

  describe('getCountFromUrl', () => {
    it('returns total count from response headers (various header keys)', async () => {
      const headersVariants = [
        { 'total-count': '10' },
        { 'Total-Count': '20' },
        { 'Total-Elements': '30' },
        { 'total-elements': '40' },
      ];

      for (const headers of headersVariants) {
        api.get.mockResolvedValue({ headers });
        const count = await reportApi.getCountFromUrl(process.env.REACT_APP_API_URL + '/listReports');
        expect(count).toBe(headers[Object.keys(headers)[0]]);
      }
    });

    it('returns 0 and logs error on failure', async () => {
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
      api.get.mockRejectedValue(new Error('fail'));

      const count = await reportApi.getCountFromUrl('/bad-url');
      expect(count).toBe(0);
      expect(consoleSpy).toHaveBeenCalled();

      consoleSpy.mockRestore();
    });
  });


  // Backwards compatibility resolve report functions

  describe('resolveReviewReport', () => {
    it('resolves first found review report', async () => {
      reviewReportApi.getReports.mockResolvedValue({ data: [reviewDto], status: 200 });
      reviewReportApi.resolveReport.mockResolvedValue({status: 204});

      const res = await reportApi.resolveReviewReport(reviewDto.id);

      expect(reviewReportApi.getReports).toHaveBeenCalledWith({reviewId: reviewDto.id});
      expect(reviewReportApi.resolveReport).toHaveBeenCalledWith(reviewDto.id);
      expect(res.status).toBe(204);
    });

    it('throws if no report found', async () => {
      reviewReportApi.getReports.mockResolvedValue({ data: [] });
      await expect(reportApi.resolveReviewReport(reviewDto.id)).rejects.toThrow('No report found for the given review ID');
    });
  });

  describe('resolveCommentReport', () => {
    it('resolves first found comment report', async () => {
      commentReportApi.getReports.mockResolvedValue({ data: [commentDto] });
      commentReportApi.resolveReport.mockResolvedValue({status: 204});

      const res = await reportApi.resolveCommentReport(commentDto.id);

      expect(commentReportApi.getReports).toHaveBeenCalledWith({commentId: commentDto.id});
      expect(commentReportApi.resolveReport).toHaveBeenCalledWith(commentDto.id);
      expect(res.status).toBe(204);
    });

    it('throws if no report found', async () => {
      commentReportApi.getReports.mockResolvedValue({ data: [] });
      await expect(reportApi.resolveCommentReport(commentDto.id)).rejects.toThrow('No report found for the given comment ID');
    });
  });

  describe('resolveMoovieListReport', () => {
    it('resolves first found moovie list report', async () => {
      moovieListReportApi.getReports.mockResolvedValue({ data: [moovieListDto] });
      moovieListReportApi.resolveReport.mockResolvedValue({status: 204});

      const res = await reportApi.resolveMoovieListReport(moovieListDto.id);

      expect(moovieListReportApi.getReports).toHaveBeenCalledWith({ moovieListId: moovieListDto.id });
      expect(moovieListReportApi.resolveReport).toHaveBeenCalledWith(moovieListDto.id);
      expect(res.status).toBe(204);
    });

    it('throws if no report found', async () => {
      moovieListReportApi.getReports.mockResolvedValue({ data: [] });
      await expect(reportApi.resolveMoovieListReport(moovieListDto.id)).rejects.toThrow('No report found for the given moovie list ID');
    });
  });

  describe('resolveMoovieListReviewReport', () => {
    it('resolves first found moovie list review report', async () => {
      moovieListReviewReportApi.getReports.mockResolvedValue({ data: [moovieListReviewDto] });
      moovieListReviewReportApi.resolveReport.mockResolvedValue({status: 204});

      const res = await reportApi.resolveMoovieListReviewReport(moovieListReviewDto.id);

      expect(moovieListReviewReportApi.getReports).toHaveBeenCalledWith({ moovieListReviewId: moovieListReviewDto.id });
      expect(moovieListReviewReportApi.resolveReport).toHaveBeenCalledWith(moovieListReviewDto.id);
      expect(res.status).toBe(204);
    });

    it('throws if no report found', async () => {
      moovieListReviewReportApi.getReports.mockResolvedValue({ data: [] });
      await expect(reportApi.resolveMoovieListReviewReport(moovieListReviewDto.id)).rejects.toThrow('No report found for the given moovie list review ID');
    });
  });
});