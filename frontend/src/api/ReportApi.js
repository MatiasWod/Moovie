import api from './api.js';

const reportApi = (() => {


    const reportReview = async ({reviewId, reportedBy, content, type}) => {
        const reportDTO = {
            reportedBy: reportedBy,
            content: content,
            type: type
        }
        const response = await api.post('/review/' + reviewId + '/report', reportDTO);
        return response;
    }

    const reportComment = async ({commentId, reportedBy, content, type}) => {
        const reportDTO = {
            commentId: commentId,
            reportedBy: reportedBy,
            content: content,
            type: type
        }
        const response = await api.post('/comment/' + commentId + '/report', reportDTO);
        return response;
    }

    const reportMoovieList = async ({moovieListId, reportedBy, content, type}) => {
        const reportDTO = {
            moovieListId: moovieListId,
            reportedBy: reportedBy,
            content: content,
            type: type
        }
        const response = await api.post('/moovieList/' + moovieListId + '/report', reportDTO);
        return response;
    }

    const reportMoovieListReview = async ({moovieListReviewId, reportedBy, content, type}) => {
        const reportDTO = {
            moovieListReviewId: moovieListReviewId,
            reportedBy: reportedBy,
            content: content,
            type: type
        }
        const response = await api.post('/moovieListReview/' + moovieListReviewId + '/report', reportDTO);
        return response;
    }

    const getReports = async ({contentType}) => {
        const response = await api.get('/reports', {params: {contentType}});
        return response;
    }

    return {
        reportReview,
        reportComment,
        reportMoovieList,
        reportMoovieListReview,
        getReports
    }
})();

export default reportApi;