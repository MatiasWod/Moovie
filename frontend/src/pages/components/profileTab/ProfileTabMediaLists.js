import {useEffect, useState} from "react";
import SortOrder from "../../../api/values/SortOrder";
import OrderBy from "../../../api/values/MediaOrderBy";
import UserApi from "../../../api/UserApi";
import ListService from "../../../services/ListService";
import pagingSizes from "../../../api/values/PagingSizes";
import ListContentPaginated from "../listContentPaginated/ListContentPaginated";

function ProfileTabMediaLists({ type, username }) {
    const [currentOrderBy, setOrderBy] = useState(OrderBy.CUSTOM_ORDER);
    const [currentSortOrder, setSortOrder] = useState(SortOrder.DESC);
    const [page, setPage] = useState(1);

    //GET WATCHED/WATCHLIST ID
    const [specialList, setSpecialList] = useState(undefined);
    const [specialListLoading, setSpecialListLoading] = useState(true);
    const [specialListError, setSpecialListError] = useState(null);

    useEffect(() => {
        async function getData() {
            try {
                const data = await UserApi.getSpecialListFromUser(username, type);
                setSpecialList(data.data);
                setSpecialListLoading(false);
            } catch (error) {
                setSpecialListError(error);
                setSpecialListLoading(false);
            }
        }
        getData();
    }, [type, username]);


    // CONTENT

    const [listContent, setListContent] = useState(undefined);
    const [listContentLoading, setListContentLoading] = useState(true);

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getListContentById({
                    id: specialList.id,
                    orderBy: currentOrderBy,
                    sortOrder: currentSortOrder,
                    pageNumber: page,
                    pageSize: pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT,
                });
                setListContent(data);
                setListContentLoading(false);
            } catch {
                setListContentLoading(false);
            }
        }
        getData();
    }, [specialList, currentOrderBy, currentSortOrder, page]);

    return (
        <ListContentPaginated
            listContent={listContent}
            page={page}
            lastPage={listContent?.links?.last?.page}
            handlePageChange={setPage}
            currentOrderBy={currentOrderBy}
            setOrderBy={setOrderBy}
            currentSortOrder={currentSortOrder}
            setSortOrder={setSortOrder}
        />
    );
}

export default ProfileTabMediaLists;