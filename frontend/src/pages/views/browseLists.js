import React, {useEffect, useState} from "react";
import listApi from "../../api/ListApi";
import DropdownMenu from "../components/dropdownMenu/DropdownMenu";
import CardsListOrderBy from "../../api/values/CardsListOrderBy";
import SortOrder from "../../api/values/SortOrder";
import ListCard from "../components/listCard/ListCard";
import "./browseLists.css"
import SearchBar from "../components/searchBar/SearchBar";
import "./../components/mainStyle.css"

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
        <div className="moovie-default default-container">

            <div className="browse-lists-header">
                <div className="title">Community Lists</div>
                <div className="browse-list-header-searchable">
                    <SearchBar />
                    <div style={{display:"flex", float:"right"}}>
                        <div style={{marginInline:"10px"}}>Order By </div>
                        <DropdownMenu setOrderBy={setOrderBy} setSortOrder={setOrder} currentOrderDefault={order} values={Object.values(CardsListOrderBy)}/>
                    </div>
                </div>
            </div>

            <div className="list-card-container">
                {mlcList.map(list => (
                    <ListCard listCard={list}/>
                ))}
            </div>
        </div>
)
    ;
}

export default BrowseLists;