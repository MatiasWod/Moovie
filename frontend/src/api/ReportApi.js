import { parsePaginatedResponse } from '../utils/ResponseUtils.js';
import api from './api.js';
import commentReportApi from './CommentReportApi.js';
import moovieListReportApi from './MoovieListReportApi.js';
import moovieListReviewReportApi from './MoovieListReviewReportApi.js';
import reviewReportApi from './ReviewReportApi.js';

const reportApi = (() => {
  // --------------- REPORTING ---------------

  const reportReview = async ({ reviewId, reportedBy, content, type }) => {
    return reviewReportApi.reportReview({ reviewId, type });
  };

  const reportComment = async ({ commentId, reportedBy, content, type }) => {
    return commentReportApi.reportComment({ commentId, type });
  };

  const reportMoovieList = async ({ moovieListId, reportedBy, content, type }) => {
    return moovieListReportApi.reportMoovieList({ moovieListId, type });
  };

  const reportMoovieListReview = async ({ moovieListReviewId, reportedBy, content, type }) => {
    return moovieListReviewReportApi.reportMoovieListReview({ moovieListReviewId, type });
  };

  // --------------- GET REPORTS ---------------

  const getReports = async ({ contentType, pageNumber }) => {
    // Map the old contentType parameter to the appropriate API
    switch (contentType) {
      case 'comment':
        return commentReportApi.getReports({ pageNumber });
      case 'review':
        return reviewReportApi.getReports({ pageNumber });
      case 'moovieList':
        return moovieListReportApi.getReports({ pageNumber });
      case 'moovieListReview':
        return moovieListReviewReportApi.getReports({ pageNumber });
      default:
        throw new Error(`Unknown content type: ${contentType}`);
    }
  };

  const getReportCounts = async ({ contentType, reportType, resourceId, resourceUrl } = {}) => {
    // Map the old contentType parameter to the appropriate API
    switch (contentType) {
      case 'comment':
        return commentReportApi.getReportCounts({
          reportType,
          commentId: resourceId,
          commentUrl: resourceUrl,
        });
      case 'review':
        return reviewReportApi.getReportCounts({
          reportType,
          reviewId: resourceId,
          reviewUrl: resourceUrl,
        });
      case 'moovieList':
        return moovieListReportApi.getReportCounts({
          reportType,
          moovieListId: resourceId,
          moovieListUrl: resourceUrl,
        });
      case 'moovieListReview':
        return moovieListReviewReportApi.getReportCounts({
          reportType,
          moovieListReviewId: resourceId,
          moovieListReviewUrl: resourceUrl,
        });
      default:
        // Return 0 count for unknown content types
        return {
          data: { count: 0 },
        };
    }
  };

  const getCountFromUrl = async (url) => {
    try {
      const response = await api.get(url, { params: { pageSize: 1, pageNumber: 1 } });
      return (
        response.headers['total-count'] ||
        response.headers['Total-Count'] ||
        response.headers['Total-Elements'] ||
        response.headers['total-elements'] ||
        0
      );
    } catch (error) {
      console.error('Error getting count from URL:', error);
      return 0;
    }
  };

  // --------------- ACTIONS ---------------

  const resolveReports = async (url) => {
    try {
      // api.delete(report.url) for ALL reports from the paginated response.
      console.log('Resolving reports from:', url);
      const request = await api.get(url);
      const response = parsePaginatedResponse(request);
      console.log('Paginated Response:', response);

      const allReportUrls = new Set(response.data.map((report) => report.url));
      while (response.hasNextPage) {
        console.log('Next page:', response.nextPage);
        const nextPage = await api.get(response.nextPage);
        const nextResponse = parsePaginatedResponse(nextPage);
        nextResponse.data.forEach((report) => allReportUrls.add(report.url));
      }
      console.log('Found', allReportUrls.size, 'reports to resolve');
      const reportUrlsArray = Array.from(allReportUrls);
      console.log('Report URLs:', reportUrlsArray);
      await Promise.all(reportUrlsArray.map((reportUrl) => api.delete(reportUrl)));
    } catch (error) {
      console.error('Error resolving reports:', error);
    }
  };

  const resolveReviewReport = async (reviewId) => {
    // For backward compatibility, we need to get the report ID first
    // This is a limitation of the new API structure
    const reports = await reviewReportApi.getReports({ reviewId });
    if (reports.data && reports.data.length > 0) {
      return reviewReportApi.resolveReport(reports.data[0].id);
    }
    throw new Error('No report found for the given review ID');
  };

  const resolveCommentReport = async (commentId) => {
    // For backward compatibility, we need to get the report ID first
    const reports = await commentReportApi.getReports({ commentId });
    if (reports.data && reports.data.length > 0) {
      return commentReportApi.resolveReport(reports.data[0].id);
    }
    throw new Error('No report found for the given comment ID');
  };

  const resolveMoovieListReport = async (moovieListId) => {
    // For backward compatibility, we need to get the report ID first
    const reports = await moovieListReportApi.getReports({ moovieListId });
    if (reports.data && reports.data.length > 0) {
      return moovieListReportApi.resolveReport(reports.data[0].id);
    }
    throw new Error('No report found for the given moovie list ID');
  };

  const resolveMoovieListReviewReport = async (moovieListReviewId) => {
    // For backward compatibility, we need to get the report ID first
    const reports = await moovieListReviewReportApi.getReports({ moovieListReviewId });
    if (reports.data && reports.data.length > 0) {
      return moovieListReviewReportApi.resolveReport(reports.data[0].id);
    }
    throw new Error('No report found for the given moovie list review ID');
  };

  return {
    // Legacy methods for backward compatibility
    reportReview,
    reportComment,
    reportMoovieList,
    reportMoovieListReview,
    getReports,
    getReportCounts,
    getCountFromUrl,
    resolveReviewReport,
    resolveCommentReport,
    resolveMoovieListReport,
    resolveMoovieListReviewReport,
    resolveReports,
    // New specialized APIs for direct access
    commentReports: commentReportApi,
    reviewReports: reviewReportApi,
    moovieListReports: moovieListReportApi,
    moovieListReviewReports: moovieListReviewReportApi,
  };
})();

export default reportApi;
