import api from './api.js';

const commentApi = (() => {

    const createReviewComment = async (reviewId, comment) => {
        const createReviewDTO = {
            commentContent: comment
        }
        const response = await api.post('/reviews/' + reviewId + '/comment', createReviewDTO);
        return response;
    }

    const getReviewComments = async (reviewId) => {
        const response = await api.get('/reviews/' + reviewId + '/comments');
        return response;
    }

    const deleteComment = async (commentId) => {
        const response = await api.delete('/comment/' + commentId);
        return response;    
    }

    return {
        deleteComment,
        createReviewComment,
        getReviewComments
    }

})()

export default commentApi;
