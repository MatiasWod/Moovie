import api from './api.js';
import VndType from '../enums/VndType';

const moovieListReviewReportApi = (() => {
  // --------------- REPORTING ---------------

  const reportMoovieListReview = async ({ moovieListReviewId, type }) => {
    const response = await api.post(
      '/moovieListReviewReports',
      {
        resourceId: moovieListReviewId,
        type: type,
      },
      {
        headers: {
          'Content-Type': VndType.APPLICATION_REPORT_FORM,
        },
      }
    );
    return response;
  };

  // --------------- GET REPORTS ---------------

  const getReports = async ({ reportType, moovieListReviewId, pageNumber, pageSize } = {}) => {
    const params = {};
    if (reportType !== null && reportType !== undefined) params.reportType = reportType;
    if (moovieListReviewId !== null && moovieListReviewId !== undefined)
      params.moovieListReviewId = moovieListReviewId;
    if (pageNumber !== null && pageNumber !== undefined) params.pageNumber = pageNumber;
    if (pageSize !== null && pageSize !== undefined) params.pageSize = pageSize;

    const response = await api.get('/moovieListReviewReports', { params });
    return response;
  };

  const getReportCounts = async ({ reportType, moovieListReviewId } = {}) => {
    const params = {
      pageSize: 1, // Minimal page size since we only need the count from headers
      pageNumber: 1,
    };
    if (reportType !== null && reportType !== undefined) params.reportType = reportType;
    if (moovieListReviewId !== null && moovieListReviewId !== undefined)
      params.moovieListReviewId = moovieListReviewId;

    try {
      const response = await api.get('/moovieListReviewReports', { params });

      // Extract count from headers - check both possible header names
      const totalCount =
        response.headers['total-count'] ||
        response.headers['Total-Count'] ||
        response.headers['total-elements'] ||
        response.headers['Total-Elements'] ||
        '0';

      // Return response object with count data in the same format as the old endpoint
      return {
        ...response,
        data: { count: parseInt(totalCount, 10) },
      };
    } catch (error) {
      console.log('----------ERROR REQUEST----------');
      console.log('GET MOOVIELIST REVIEW REPORT COUNTS reportType', reportType);
      console.log('GET MOOVIELIST REVIEW REPORT COUNTS moovieListReviewId', moovieListReviewId);
      console.log('ERROR request', error.config.params);
      console.log('ERROR status', error.response.status);
      console.log('ERROR data', error.response.data);
      console.log('ERROR headers', error.response.headers);
      console.log('--------------------------------');
      return {
        data: { count: 0 },
      };
    }
  };

  // --------------- ACTIONS ---------------

  const resolveReport = async (reportId) => {
    const response = await api.delete(`/moovieListReviewReports/${reportId}`);
    return response;
  };

  const getReport = async (reportId) => {
    const response = await api.get(`/moovieListReviewReports/${reportId}`);
    return response;
  };

  return {
    reportMoovieListReview,
    getReports,
    getReportCounts,
    resolveReport,
    getReport,
  };
})();

export default moovieListReviewReportApi;
