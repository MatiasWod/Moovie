import React, {useEffect, useState} from "react";
import listApi from "../../api/ListApi";
import DropdownMenu from "../components/dropdownMenus/DropdownMenu/DropdownMenu";
import CardsListOrderBy from "../../api/values/CardsListOrderBy";
import SortOrder from "../../api/values/SortOrder";

function BrowseLists(){

    const [search, setSearch] = useState(null);
    const [ownerUsername, setOwnerUsername] = useState(null);
    const [type, setType] = useState(null);
    const [orderBy, setOrderBy] = useState(CardsListOrderBy.LIKE_COUNT);
    const [order, setOrder] = useState(SortOrder.DESC);
    const [pageNumber, setPageNumber] = useState(1);


    const [mlcList, setMlcList] = useState([]);
    const [mlcListLoading, setMlcListLoading] = useState(true);
    const [mlcListError, setMlcListError] = useState(null);

    const fetchMlcList = async () => {
        try {
            const response = await listApi.getLists(
                {
                    search: search,
                    ownerUsername: ownerUsername,
                    type: type,
                    orderBy: orderBy,
                    order: order,
                    pageNumber: pageNumber
                }
            );
            setMlcList(response.data);
        } catch (err) {
            setMlcListError(err);
        } finally {
            setMlcListLoading(false);
        }
    };

    useEffect(() => {
        fetchMlcList();
    }, [order, orderBy, pageNumber]);

    return (
        <div>
            <div>Lists browsing</div>
            <div>Oredr by: {orderBy}</div>
            <div>Sort Order: {order}</div>

            <DropdownMenu setOrderBy={setOrderBy} setSortOrder={setOrder} currentOrderDefault={order} values={Object.values(CardsListOrderBy)}/>

            <div>
                {mlcList.map(list => (
                    <div>{list.name}</div>
                ))}
            </div>
        </div>
)
    ;
}

export default BrowseLists;