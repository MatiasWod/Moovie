import reviewApi from "../api/ReviewApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";

const ReviewService = (() => {
    const getReviewById = async (id) => {
        const response = await reviewApi.getReviewById(id);
        return response;
    };

    const deleteReviewById = async (id) => {
        const response = await reviewApi.deleteReviewById(id);
        return  response;
    };

    const likeReview = async (id) => {
        const response = await reviewApi.likeReview(id);
        return  response;

    };

    const getComments = async (id, page = 1) => {
        const response = await reviewApi.getComments(id, page);
        return parsePaginatedResponse(response);
    }

    return {
        getReviewById,
        deleteReviewById,
        likeReview,
        getComments
    };
})();

export default ReviewService;
