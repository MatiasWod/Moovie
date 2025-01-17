import React, {useEffect, useState} from "react";
import listApi from "../../api/ListApi";
import DropdownMenu from "../components/dropdownMenu/DropdownMenu";
import CardsListOrderBy from "../../api/values/CardsListOrderBy";
import SortOrder from "../../api/values/SortOrder";
import ListCard from "../components/listCard/ListCard";
import "./browseLists.css"
import SearchBar from "../components/searchBar/SearchBar";
import "./../components/mainStyle.css"
import PaginationButton from "../components/paginationButton/PaginationButton";
import {createSearchParams, useNavigate, useSearchParams} from "react-router-dom";
import ListService from "../../services/ListService";
import pagingSizes from "../../api/values/PagingSizes";
import {useTranslation} from "react-i18next";

function BrowseLists(){

    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const { t } = useTranslation();

    const [search, setSearch] = useState(null);
    const [ownerUsername, setOwnerUsername] = useState(null);
    const [type, setType] = useState(null);
    const [orderBy, setOrderBy] = useState(CardsListOrderBy.LIKE_COUNT);
    const [sortOrder, setSortOrder] = useState(SortOrder.DESC);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);

    const [mlcList, setMlcList] = useState(undefined);
    const [mlcListLoading, setMlcListLoading] = useState(true);
    const [mlcListError, setMlcListError] = useState(null);

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/browselists`,
            search: createSearchParams({
                orderBy:orderBy,
                sortOrder: sortOrder,
                page: newPage.toString(),
            }).toString(),
        });
    };

    useEffect(() => {
        navigate({
            pathname: `/browselists`,
            search: createSearchParams({
                orderBy: orderBy,
                sortOrder: sortOrder,
                page: page.toString(),
            }).toString(),
        });
    }, [orderBy, sortOrder, page, navigate]);

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getLists({
                    orderBy: orderBy,
                    ownerUsername: ownerUsername,
                    pageNumber: page,
                    pageSize: pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS,
                    search: search,
                    type: type,
                    order: sortOrder
                });
                setMlcList(data);
                setMlcListLoading(false);
            } catch (error) {
                setMlcListError(error);
                setMlcListLoading(false);
            }
        }
        getData();
    }, [search,orderBy,sortOrder,page]);

    return (
        <div className="moovie-default default-container">

            <div className="browse-lists-header">
                <div className="title">{t('browseLists.communityLists')}</div>

                <div className="browse-list-header-searchable">
                    <SearchBar />
                    <div style={{display:"flex", float:"right"}}>
                        <div style={{marginInline:"10px"}}>{t('browseLists.orderBy')}</div>
                        <DropdownMenu setOrderBy={setOrderBy} setSortOrder={setSortOrder} currentOrderDefault={sortOrder} values={Object.values(CardsListOrderBy)}/>
                    </div>
                </div>
            </div>

            <div className="list-card-container">
                {mlcList?.data?.map(list => (
                    <ListCard listCard={list}/>
                ))}
            </div>

            <div className="flex justify-center pt-4">
                {mlcList?.data?.length > 0 && mlcList.links?.last?.page > 1 && (
                    <PaginationButton
                        page={page}
                        lastPage={mlcList.links.last.page}
                        setPage={handlePageChange}
                    />
                )}
            </div>
        </div>

)
    ;
}

export default BrowseLists;