import React, {useMemo, useState} from 'react'
import MediaCard from "../../components/media/MediaCard/MediaCard";
import {Spinner} from "react-bootstrap";
import '../../components/media/MediaCard/MediaCard.css'
import FiltersGroup from "../../components/filters/FiltersGroup/FiltersGroup";
import mediaTypes from "../../../api/values/MediaTypes";
import useMediaList from "../../../hooks/useMediasList";
import mediaOrderBy from "../../../api/values/MediaOrderBy";
import SortOrder from "../../../api/values/SortOrder";
import {Pagination} from "@mui/material";

const CreateListView = () => {
    const [selectedItems, setSelectedItems] = useState([])

    const [selectedProviders, setSelectedProviders] = useState([]);
    const [selectedGenres, setSelectedGenres] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [type, setType] = useState(mediaTypes.TYPE_ALL)
    const [orderBy, setOrderBy] = useState(mediaOrderBy.TOTAL_RATING)
    const [sortOrder, setSortOrder] = useState(SortOrder.ASC)
    const [page, setPage] = useState(1)

    const memoizedProviders = useMemo(() => Array.from(selectedProviders || []), [selectedProviders]);
    const memoizedGenres = useMemo(() => Array.from(selectedGenres || []), [selectedGenres]);


    const handleFilterChange = ({ type, sortOrder, orderBy, search, selectedProviders, selectedGenres }) => {
        setSelectedProviders(selectedProviders)
        setSelectedGenres(selectedGenres)
        setOrderBy(orderBy)
        setSearchQuery(search)
        setType(type)
        setSortOrder(sortOrder)
        setPage(1)
    };


    const onClickCallback = (mediaId) => {
        setSelectedItems((state) => state.includes(mediaId)
            ? state.filter((i) => i !== mediaId) : [...state, mediaId]
        )
    }

    const handlePaginationChange = (event, value) => {
        setPage(value);
    };


    const { medias, mediasLoading, mediasError } = useMediaList({
        type: type,
        page: page,
        sortOrder: sortOrder,
        orderBy: orderBy,
        search: searchQuery,
        selectedProviders: memoizedProviders,
        selectedGenres: memoizedGenres,
    });



    return <div className={'container d-flex flex-column'}>
        <div className={'m-1'} style={{width: "100vw",height: "1vh"}}></div>
        <div className={'container d-flex flex-row'}>
            <FiltersGroup submitCallback={handleFilterChange} searchBar={true} type={type} orderBy={orderBy} sortOrder={sortOrder} query={searchQuery}/>
            <div className={'container d-flex flex-column'}>
                <div style={{overflowY: "auto", maxHeight: "83vh"}} className={'flex-wrap d-flex'}>
                    {mediasLoading ? <Spinner /> : medias.data.map(media => (
                        <MediaCard key={media.id} isSelected={selectedItems.includes(media.id)} media={media} onClick={() => onClickCallback(media.id)} pageName={'createList'}>
                        </MediaCard>
                    ))}
                    {mediasError && <div>There’s been an error: {mediasError.message}</div>}
                </div>
                <div className={'m-1 d-flex justify-center'}>
                    { !mediasLoading &&
                        <Pagination onChange={handlePaginationChange} page={page} count={medias.links.last.page}/>
                    }
                </div>
            </div>
            <div id={'preview'}>

            </div>
        </div>
    </div>
}

export default CreateListView