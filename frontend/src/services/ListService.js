import listApi from "../api/ListApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import api from "../api/api";

const ListService = (() => {

    const getLists = async ({search, ownerUsername, type, orderBy, order, pageNumber, pageSize}) =>{
        const res = await listApi.getLists({search, ownerUsername, type, orderBy, order, pageNumber, pageSize});
        return parsePaginatedResponse(res);
    }

    const getListById = async (id) => {
        const res = await listApi.getListById(id);
        return res;
    }

    const getListContentById= async ({id, orderBy, sortOrder, pageNumber, pageSize}) => {
        const res = await listApi.getListContentById({id, orderBy, sortOrder, pageNumber, pageSize});
        return parsePaginatedResponse(res);
    }

    const getMoovieListReviewsFromListId = async ({id,pageNumber}) => {
        const res = await listApi.getMoovieListReviewsFromListId({id,pageNumber});
        return parsePaginatedResponse(res);
    }

   return{
        getLists,
        getListById,
        getListContentById,
       getMoovieListReviewsFromListId
   }
})();

export default ListService;