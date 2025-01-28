import api from './api.js';

const reportApi = (() => {


    // --------------- REPORTING ---------------

    const reportReview = async ({reviewId, reportedBy, content, type}) => {
        const response = await api.post('/reports',
            {
                reportedBy: reportedBy,
                content: content,
                type: type
            },
            {params:
                {
                    reviewId: reviewId
                }
            });
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
        const response = await api.post('/reports',
            {
                moovieListId: moovieListId,
                reportedBy: reportedBy,
                content: content,
                type: type
            },
            {params:
                {
                    moovieListId: moovieListId
                }
            }
        );
        return response;
    }

    const reportMoovieListReview = async ({moovieListReviewId, reportedBy, content, type}) => {
        const response = await api.post('/reports',
            {
                moovieListReviewId: moovieListReviewId,
                reportedBy: reportedBy,
                content: content,
                type: type
            },
            {params:
                {
                    moovieListReviewId: moovieListReviewId
                }
            }
        );
        return response;
    }

    // --------------- GET REPORTS ---------------

    const getReports = async ({contentType}) => {
        const response = await api.get('/reports', {params: {contentType}});
        return response;
    }


    // --------------- ACTIONS ---------------

    const resolveReviewReport = async ({reviewId}) => {
        const response = await api.delete('/reports',
            {params:
                {
                    reviewId: reviewId
                }
            });
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
        const response = await api.delete('/reports',
            {params:
                {
                    moovieListId: moovieListId
                }
            });
        return response;
    }

    const resolveMoovieListReviewReport = async ({moovieListReviewId}) => {
        const response = await api.delete('/reports',
            {params:
                {
                    moovieListReviewId: moovieListReviewId
                }
            });
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