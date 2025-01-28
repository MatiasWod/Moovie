import api from './api.js';

const commentApi = (() => {

    const createReviewComment = async (reviewId, comment) => {
        const response = await api.post('/comments',
            {
                commentContent: comment
            },
            {
                params: {
                    'reviewId': reviewId
                }
            }
            );
        return response;
    }

    const getReviewComments = async (reviewId,pageNumber=1,) => {
        const response = await api.get('/comments',
            {
                params: {
                    'reviewId': reviewId,
                    'pageNumber': pageNumber
                }
            }
            );
        return response;
    }

    const deleteComment = async (commentId) => {
        const response = await api.delete('/comments/' + commentId);
        return response;    
    }

    return {
        deleteComment,
        createReviewComment,
        getReviewComments
    }

})()

export default commentApi;
