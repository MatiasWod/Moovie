import {useEffect, useState} from "react";
import SortOrder from "../../../api/values/SortOrder";
import OrderBy from "../../../api/values/MediaOrderBy";
import UserApi from "../../../api/UserApi";
import ListService from "../../../services/ListService";
import pagingSizes from "../../../api/values/PagingSizes";
import ListContentPaginated from "../listContentPaginated/ListContentPaginated";
import CardsListOrderBy from "../../../api/values/CardsListOrderBy";
import MoovieListTypes from "../../../api/values/MoovieListTypes";
import ListCardsPaginated from "../ListCardsPaginated/ListCardsPaginated";

function ProfileTabMediaLists({ type, username }) {

    const [search, setSearch] = useState(null);
    const [orderBy, setOrderBy] = useState(CardsListOrderBy.LIKE_COUNT);
    const [sortOrder, setSortOrder] = useState(SortOrder.DESC);
    const [page, setPage] = useState(1);

    const [lists, setLists] = useState(undefined);
    const [listsLoading, setListsLoading] = useState(true);
    const [listError, setListError] = useState(false);


    let typeQuery = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.type;

    switch (type){
        case("public-lists"):
            typeQuery = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.type;
            break;
        case("private-lists"):
            typeQuery = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type;
            break;
        default:
            typeQuery = -1;
    }

    useEffect(() => {
        async function getData() {
            try {
                    if(typeQuery===-1){
                        return <div>Error</div>
                    } else{
                        const data = await ListService.getLists({
                            orderBy: orderBy,
                            ownerUsername: username,
                            pageNumber: page,
                            pageSize: pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS,
                            search: search,
                            type: typeQuery,
                            order: sortOrder
                        });
                        setLists(data);
                        setListsLoading(false);
                    }
            } catch {
                setListError(false);
            }
        }
        getData();
    }, [orderBy, sortOrder, page, type, username]);

    return (
        <ListCardsPaginated
            mlcList={lists}
            page={page}
            lastPage={lists?.links?.last?.page}
            handlePageChange={setPage}
            currentOrderBy={orderBy}
            setOrderBy={setOrderBy}
            currentSortOrder={sortOrder}
            setSortOrder={setSortOrder}
        />
    );
}

export default ProfileTabMediaLists;