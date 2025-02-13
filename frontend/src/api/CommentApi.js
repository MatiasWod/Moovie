import api from './api.js';

const commentApi = (() => {

    const createReviewComment = async (reviewId, comment) => {
        const response = await api.post('/comments/',
            {
                commentContent: comment
            },
            {
                params: {
                    'reviewId': reviewId
                },
                headers: {
                    'Content-Type': 'application/vnd.comment-form.v1+json'
                }
            }
            );
        return response;
    }

    const getReviewComments = async (reviewId,pageNumber=1,) => {
        const response =  api.get('/comments/',
            {
                params: {
                    'reviewId': reviewId,
                    'pageNumber': pageNumber
                }
            }
            );
        return response;
    }

    const commentFeedback = async (commentId, feedback) => {
        const response = await api.put('/comments/' + commentId,
            {
                feedback: feedback
            },
            {
                headers: {
                    'Content-Type': 'application/vnd.comment-feedback-form.v1+json'
                }
            }
            );
        return response
    }

    const removeCommentFeedback = async (commentId, feedback) => {
        const response = await api.delete('/comments/' + commentId,
            {
                feedback: feedback
            },
            {
                headers: {
                    'Content-Type': 'application/vnd.comment-feedback-form.v1+json'
                }
            }
        );
        return response
    }




    const deleteComment = async (commentId) => {
        const response = await api.delete('/comments/' + commentId);
        return response;    
    }

    return {
        deleteComment,
        createReviewComment,
        commentFeedback,
        removeCommentFeedback,
        getReviewComments
    }

})();

export default commentApi;
