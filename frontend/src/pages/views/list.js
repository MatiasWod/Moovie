import React, {useEffect, useState} from 'react';
import {createSearchParams, useNavigate, useParams, useSearchParams} from "react-router-dom";
import ListHeader from "../components/listHeader/ListHeader";
import OrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";
import "../components/mainStyle.css"
import ListService from "../../services/ListService";
import pagingSizes from "../../api/values/PagingSizes";
import ListContentPaginated from "../components/listContentPaginated/ListContentPaginated";
import Reviews from "../components/ReviewsSection/Reviews";
import ListCard from "../components/listCard/ListCard";
import {ProgressBar} from "react-bootstrap"
import ListApi from "../../api/ListApi";
import Error403 from "./errorViews/error403";
import {parsePaginatedResponse} from "../../utils/ResponseUtils";

function List() {
    const [error403, setError403] = useState(false);
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

    const [updateListHeader, setUpdateListHeader] = useState(false);

    const handleUpdateList = () =>{
        //To update if edited
        setUpdateListHeader(true);
    }

    useEffect(() => {
        async function getData() {
            try {
                let data = await ListApi.getListById(id);
                if(data.status === 403 || data.status === 404){
                    setError403(true);
                    console.log("error found");
                    return;
                }
                data = parsePaginatedResponse(data);
                setList(data);
                setListLoading(false);
                setUpdateListHeader(false);

            } catch (error) {
                setListError(error);
                setListLoading(false);
            }
        }
        getData();
    }, [id, updateListHeader]);

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
                console.log("List content:"+ listContent);
                setListContentLoading(false);
            } catch (error) {
                setListContentError(error);
                setListContentLoading(false);
            }
        }
        getData();
    }, [currentOrderBy,currentSortOrder,page]);

    const [listRecommendations, setListRecommendations] = useState(undefined);
    const [listRecommendationsLoading, setlistRecommendationsLoading] = useState(true);
    const [listRecommendationsError, setlistRecommendationsError] = useState(null);

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getRecommendedLists(id)
                setListRecommendations(data);
                console.log("Recomended list:"+ listRecommendations);

                setlistRecommendationsLoading(false);
            } catch (error) {
                setlistRecommendationsError(error);
                setlistRecommendationsLoading(false);
            }
        }
        getData();
    }, [id]);

    if(error403){
        return(
            <Error403></Error403>
        )
    }

    return (
        <div className="default-container moovie-default">
            <ListHeader list={list?.data || []} updateHeader={handleUpdateList} />

            <ProgressBar
                now={list?.data?.mediaCount === 0 ? 100 : (list?.data?.currentUserWatchAmount / list?.data?.mediaCount) * 100}
                label={`${Math.round(list?.data?.mediaCount === 0 ? 100 : (list?.data?.currentUserWatchAmount / list?.data?.mediaCount) * 100)}%`}
            />


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

            <h3>Si te gustó esta, también te podría gustar...</h3>
            <div className="moovie-default default-container">
                <div className="list-card-container">
                    {listRecommendations?.data?.map(list => (
                        <ListCard listCard={list}/>
                    ))}
                </div>
            </div>

            <Reviews id={id} source={'list'}></Reviews>

        </div>
    );

}

export default List;