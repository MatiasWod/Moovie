import api from './api.js';
import VndType from '../enums/VndType';

const reportApi = (() => {
  // --------------- REPORTING ---------------

  const reportReview = async ({ reviewId, reportedBy, content, type }) => {
    const response = await api.post(
      '/reports',
      {
        type: type,
      },
      {
        params: {
          reviewId: reviewId,
        },
        headers: {
          'Content-Type': VndType.APPLICATION_REPORT_FORM,
        },
      }
    );
    return response;
  };

  const reportComment = async ({ commentId, reportedBy, content, type }) => {
    const response = await api.post(
      '/reports',
      {
        type: type,
      },
      {
        params: {
          commentId: commentId,
        },
        headers: {
          'Content-Type': VndType.APPLICATION_REPORT_FORM,
        },
      }
    );
    return response;
  };

  const reportMoovieList = async ({ moovieListId, reportedBy, content, type }) => {
    const response = await api.post(
      '/reports',
      {
        type: type,
      },
      {
        params: {
          moovieListId: moovieListId,
        },
        headers: {
          'Content-Type': VndType.APPLICATION_REPORT_FORM,
        },
      }
    );
    return response;
  };

  const reportMoovieListReview = async ({ moovieListReviewId, reportedBy, content, type }) => {
    console.log(type);
    const response = await api.post(
      '/reports',
      {
        type: type,
      },
      {
        params: {
          moovieListReviewId: moovieListReviewId,
        },
        headers: {
          'Content-Type': VndType.APPLICATION_REPORT_FORM,
        },
      }
    );
    return response;
  };

  // --------------- GET REPORTS ---------------

  const getReports = async ({ contentType }) => {
    const response = await api.get('/reports', { params: { contentType } });
    return response;
  };

  const getReportCounts = async ({ contentType, reportType, resourceId } = {}) => {
    const params = {
      pageSize: 1, // Minimal page size since we only need the count from headers
      pageNumber: 1
    };
    if (contentType !== null && contentType !== undefined) params.contentType = contentType;
    if (reportType !== null && reportType !== undefined) params.reportType = reportType;
    if (resourceId !== null && resourceId !== undefined) params.resourceId = resourceId;

    try{
    const response = await api.get('/reports', { params });
    
    // Extract count from headers - check both possible header names
    const totalCount = response.headers['total-count'] || 
                      response.headers['Total-Count'] ||
                      response.headers['total-elements'] ||
                      response.headers['Total-Elements'] ||
                      '0';

    // Return response object with count data in the same format as the old endpoint
    return {
      ...response,
        data: { count: parseInt(totalCount, 10) }
      };
    } catch (error) {
      console.log('----------ERROR REQUEST----------');
      console.log('GET REPORT COUNTS contentType',contentType);
      console.log('GET REPORT COUNTS reportType',reportType);
      console.log('GET REPORT COUNTS resourceId',resourceId);
      console.log('ERROR request', error.config.params);
      console.log('ERROR status', error.response.status);
      console.log('ERROR data', error.response.data);
      console.log('ERROR headers', error.response.headers);
      console.log('--------------------------------');
      return {
        data: { count: 0 }
      };
    }
  };

  // --------------- ACTIONS ---------------

  const resolveReviewReport = async (reviewId) => {
    const response = await api.delete('/reports', {
      params: {
        reviewId: reviewId,
      },
    });
    return response;
  };

  const resolveCommentReport = async (commentId) => {
    const response = await api.delete('/reports', {
      params: {
        commentId: commentId,
      },
    });
    return response;
  };

  const resolveMoovieListReport = async (moovieListId) => {
    const response = await api.delete('/reports', {
      params: {
        moovieListId: moovieListId,
      },
    });
    return response;
  };

  const resolveMoovieListReviewReport = async (moovieListReviewId) => {
    const response = await api.delete('/reports', {
      params: {
        moovieListReviewId: moovieListReviewId,
      },
    });
    return response;
  };

  return {
    reportReview,
    reportComment,
    reportMoovieList,
    reportMoovieListReview,
    getReports,
    getReportCounts,
    resolveReviewReport,
    resolveCommentReport,
    resolveMoovieListReport,
    resolveMoovieListReviewReport,
  };
})();

export default reportApi;
