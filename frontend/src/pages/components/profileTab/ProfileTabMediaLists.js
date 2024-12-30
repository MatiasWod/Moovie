import {useEffect, useState} from "react";
import SortOrder from "../../../api/values/SortOrder";
import OrderBy from "../../../api/values/MediaOrderBy";
import UserApi from "../../../api/UserApi";
import ListService from "../../../services/ListService";
import pagingSizes from "../../../api/values/PagingSizes";
import ListContentPaginated from "../listContentPaginated/ListContentPaginated";
import UserService from "../../../services/UserService";
import MediaService from "../../../services/MediaService";

function ProfileTabMediaLists({ type, username }) {
    const [currentOrderBy, setOrderBy] = useState(OrderBy.CUSTOM_ORDER);
    const [currentSortOrder, setSortOrder] = useState(SortOrder.DESC);
    const [page, setPage] = useState(1);

    const [listContent, setListContent] = useState(undefined);
    const [listPagination, setListPagination] = useState(undefined);
    const [listContentLoading, setListContentLoading] = useState(true);
    const [listContentError, setListContentError] = useState(false);


    useEffect(() => {
        async function getData() {
            try {
                const data = await UserService.getSpecialListFromUser(
                    {
                        username: username,
                        type: type,
                        orderBy: currentOrderBy,
                        sortOrder: currentSortOrder,
                        pageNumber: page
                    });
                setListPagination(data.data);
                const idList = MediaService.getIdMediaFromObjectList(data.data);
                console.log(idList);
                setListContent(await MediaService.getMediaByIdList(idList));
                console.log(listContent);
                setListContentLoading(false);
            } catch (error) {
                setListContentError(error);
                setListContentLoading(false);
            }
        }
        getData();
    }, [type, username, currentOrderBy, currentSortOrder, page]);


    return (
        <ListContentPaginated
            listContent={listContent}
            page={page}
            lastPage={listPagination?.links?.last?.page}
            handlePageChange={setPage}
            currentOrderBy={currentOrderBy}
            setOrderBy={setOrderBy}
            currentSortOrder={currentSortOrder}
            setSortOrder={setSortOrder}
        />
    );
}

export default ProfileTabMediaLists;