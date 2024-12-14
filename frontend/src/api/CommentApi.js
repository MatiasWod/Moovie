import api from './api.js';

const commentApi = (() => {

    const deleteComment = async (commentId) => {
        const response = await api.delete('/comment/' + commentId);
        return response;    
    }

    return {
        deleteComment
    }

})()

export default commentApi;