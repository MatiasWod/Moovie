import api from './api.js';
import VndType from '../enums/VndType';

const commentReportApi = (() => {
    // --------------- REPORTING ---------------

    const reportComment = async ({ commentId, type }) => {
        const response = await api.post(
            '/commentsReports',
            {
                resourceId: commentId,
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

    const getReports = async ({ reportType, commentId, pageNumber, pageSize } = {}) => {
        const params = {};
        if (reportType !== null && reportType !== undefined) params.reportType = reportType;
        if (commentId !== null && commentId !== undefined) params.commentId = commentId;
        if (pageNumber !== null && pageNumber !== undefined) params.pageNumber = pageNumber;
        if (pageSize !== null && pageSize !== undefined) params.pageSize = pageSize;

        const response = await api.get('/commentsReports', { params });
        return response;
    };

    const getReportCounts = async ({ reportType, commentId } = {}) => {
        const params = {
            pageSize: 1, // Minimal page size since we only need the count from headers
            pageNumber: 1
        };
        if (reportType !== null && reportType !== undefined) params.reportType = reportType;
        if (commentId !== null && commentId !== undefined) params.commentId = commentId;

        try {
            const response = await api.get('/commentsReports', { params });

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
            console.log('GET COMMENT REPORT COUNTS reportType', reportType);
            console.log('GET COMMENT REPORT COUNTS commentId', commentId);
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

    const resolveReport = async (reportId) => {
        const response = await api.delete(`/commentsReports/${reportId}`);
        return response;
    };

    const getReport = async (reportId) => {
        const response = await api.get(`/commentsReports/${reportId}`);
        return response;
    };

    return {
        reportComment,
        getReports,
        getReportCounts,
        resolveReport,
        getReport,
    };
})();

export default commentReportApi; 