import api from './api.js';

const reportApi = (() => {


    // --------------- REPORTING ---------------

    const reportReview = async ({reviewId, reportedBy, content, type}) => {
        const reportDTO = {
            reportedBy: reportedBy,
            content: content,
            type: type
        }
        const response = await api.post('/reviews/' + reviewId + '/report', reportDTO);
        return response;
    }

    const reportComment = async ({commentId, reportedBy, content, type}) => {
        const response = await api.post('/reports',
            {
                commentId: commentId,
                reportedBy: reportedBy,
                content: content,
                type: type
            },
            {params:
                {
                    commentId: commentId
                }
            }
        );
        return response;
    }

    const reportMoovieList = async ({moovieListId, reportedBy, content, type}) => {
        const reportDTO = {
            moovieListId: moovieListId,
            reportedBy: reportedBy,
            content: content,
            type: type
        }
        const response = await api.post('/list/' + moovieListId + '/report', reportDTO);
        return response;
    }

    const reportMoovieListReview = async ({moovieListReviewId, reportedBy, content, type}) => {
        const reportDTO = {
            moovieListReviewId: moovieListReviewId,
            reportedBy: reportedBy,
            content: content,
            type: type
        }
        const response = await api.post('/moovieListReviews/' + moovieListReviewId + '/report', reportDTO);
        return response;
    }

    // --------------- GET REPORTS ---------------

    const getReports = async ({contentType}) => {
        const response = await api.get('/reports', {params: {contentType}});
        return response;
    }


    // --------------- ACTIONS ---------------

    const resolveReviewReport = async ({reviewId}) => {
        const response = await api.delete('/reviews/' + reviewId + '/report');
        return response;
    }

    const resolveCommentReport = async ({commentId}) => {
        const response = await api.delete('/reports',
            {params:
                {
                    commentId: commentId
                }
            }
        );
        return response;
    }

    const resolveMoovieListReport = async ({moovieListId}) => {
        const response = await api.delete('/list/' + moovieListId + '/report');
        return response;
    }

    const resolveMoovieListReviewReport = async ({moovieListReviewId}) => {
        const response = await api.delete('/moovieListReviews/' + moovieListReviewId + '/report');
        return response;
    }

    return {
        reportReview,
        reportComment,
        reportMoovieList,
        reportMoovieListReview,
        getReports,
        resolveReviewReport,
        resolveCommentReport,
        resolveMoovieListReport,
        resolveMoovieListReviewReport
    }
})();

export default reportApi;