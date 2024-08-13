import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import listApi from '../../api/ListApi'
import ListHeader from "../components/listHeader/ListHeader";
import ListContent from "../components/listContent/ListContent";
import PaginationButton from "../components/paginationButton/PaginationButton";
import PagingSizes from "../../api/values/PagingSizes";
import OrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";
import DropdownMenu from "../components/dropdownMenus/DropdownMenu/DropdownMenu";
import "../components/mainStyle.css"


function List() {
    const {id} = useParams();

    const [currentPage, setCurrentPage] = useState(1);
    const [currentOrderBy, setOrderBy] = useState(OrderBy.CUSTOM_ORDER);
    const [currentSortOrder, setSortOrder] = useState(SortOrder.DESC);


    //GET VALUES FOR LIST
    const [list, setList] = useState([]);
    const [listLoading, setListLoading] = useState(true);
    const [listError, setListError] = useState(null);

    const fetchList = async () => {
        try {
            const response = await listApi.getListById(id);
            setList(response.data);
        } catch (err) {
            setListError(err);
        } finally {
            setListLoading(false);
        }
    };

    useEffect(() => {
        fetchList();
    }, []);


    //GET VALUES FOR LIST CONTENT
    const [listContent, setListContent] = useState([]);
    const [listContentLoading, setListContentLoading] = useState(true);
    const [listContentError, setListContentError] = useState(null);

    const fetchListContent = async () => {
        try {
            const response = await listApi.getListContentById({
                id: id,
                orderBy: currentOrderBy,
                sortOrder: currentSortOrder,
                pageNumber: currentPage,
                pageSize: PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT
            });
            setListContent(response.data);
        } catch (err) {
            setListContentError(err);
        } finally {
            setListContentLoading(false);
        }
    };

    useEffect(() => {
        fetchListContent();
    }, [currentPage, currentSortOrder, currentOrderBy]);


    return (
        <div className="default-container moovie-default">
            <h1>Details Page</h1>
            <ListHeader list={list}/>
            <h1>------</h1>

            <DropdownMenu setOrderBy={setOrderBy} setSortOrder={setSortOrder} currentOrderDefault={currentSortOrder} values={Object.values(OrderBy)}/>

            <ListContent listContent={listContent}/>

            <PaginationButton currentPage={currentPage} setCurrentPage={setCurrentPage}
                              totalPages={Math.ceil(list.mediaCount / PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT)}/>

        </div>
    );

}

export default List;