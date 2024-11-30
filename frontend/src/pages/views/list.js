import React, {useEffect, useState} from 'react';
import {createSearchParams, useNavigate, useParams, useSearchParams} from "react-router-dom";
import ListHeader from "../components/listHeader/ListHeader";
import OrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";
import DropdownMenu from "../components/dropdownMenu/DropdownMenu";
import "../components/mainStyle.css"
import ListService from "../../services/ListService";
import pagingSizes from "../../api/values/PagingSizes";
import ListContentPaginated from "../components/listContentPaginated/ListContentPaginated";


function List() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    const {id} = useParams();
    const [currentOrderBy, setOrderBy] = useState(OrderBy.CUSTOM_ORDER);
    const [currentSortOrder, setSortOrder] = useState(SortOrder.DESC);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);


    //GET VALUES FOR LIST
    const [list, setList] = useState(undefined);
    const [listLoading, setListLoading] = useState(true);
    const [listError, setListError] = useState(null);


    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/list/${id}`,
            search: createSearchParams({
                orderBy:currentOrderBy,
                sortOrder: currentSortOrder,
                page: newPage.toString(),
            }).toString(),
        });
    };

    useEffect(() => {
        navigate({
            pathname: `/list/${id}`,
            search: createSearchParams({
                orderBy: currentOrderBy,
                sortOrder: currentSortOrder,
                page: page.toString(),
            }).toString(),
        });
    }, [id, currentOrderBy, currentSortOrder, page, navigate]);

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getListById(id);
                setList(data);
                setListLoading(false);
            } catch (error) {
                setListError(error);
                setListLoading(false);
            }
        }
        getData();
    }, [id]);


    //GET VALUES FOR LIST CONTENT
    const [listContent, setListContent] = useState(undefined);
    const [listContentLoading, setListContentLoading] = useState(true);
    const [listContentError, setListContentError] = useState(null);

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getListContentById({
                    id: id,
                    orderBy: currentOrderBy,
                    sortOrder: currentSortOrder,
                    pageNumber: page,
                    pageSize: pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT
                });
                setListContent(data);
                setListContentLoading(false);
            } catch (error) {
                setListContentError(error);
                setListContentLoading(false);
            }
        }
        getData();
    }, [currentOrderBy,currentSortOrder,page]);


    return (
        <div className="default-container moovie-default">
            <ListHeader list={list?.data || []}/>

            <ListContentPaginated
                listContent={listContent}
                page={page}
                lastPage={listContent?.links?.last?.page}
                handlePageChange={handlePageChange}
                currentOrderBy={currentOrderBy}
                setOrderBy={setOrderBy}
                currentSortOrder={currentSortOrder}
                setSortOrder={setSortOrder}
            />

        </div>
    );

}

export default List;