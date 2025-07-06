import commentApi from '../api/CommentApi';
import { parsePaginatedResponse } from '../utils/ResponseUtils';
import listApi from '../api/ListApi';

const CommentService = (() => {
  const getCommentsFromUrl = async ({ url, pageNumber, pageSize }) => {
    const res = await commentApi.getCommentsFromUrl(url, pageNumber, pageSize);
    return parsePaginatedResponse(res);
  };

  return {
    getCommentsFromUrl,
  };
})();

export default CommentService;
