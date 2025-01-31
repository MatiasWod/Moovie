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
import {useSelector} from "react-redux";
import CreateListForm from "../../components/forms/createListForm/CreateListForm";

const CreateListView = () => {

    const [selectedMedia, setSelectedMedia] = useState([])

    const [selectedProviders, setSelectedProviders] = useState([]);
    const [selectedGenres, setSelectedGenres] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [type, setType] = useState(mediaTypes.TYPE_ALL)
    const [orderBy, setOrderBy] = useState(mediaOrderBy.TOTAL_RATING)
    const [sortOrder, setSortOrder] = useState(SortOrder.DESC)
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


    const onClickCallback = (media) => {
        setSelectedMedia((state) => state.includes(media)
            ? state.filter((i) => i !== media) : [...state, media]
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

    return <div className={'d-flex flex-column'}>
        <div className={'m-1'} style={{width: "100vw",height: "1vh"}}></div>
        <div className={'d-flex flex-row m-2'}>
            <FiltersGroup submitCallback={handleFilterChange} searchBar={true} type={type} orderBy={orderBy} sortOrder={sortOrder} query={searchQuery}/>
            <div className={'container d-flex flex-column'}>
                <div style={{overflowY: "auto", maxHeight: "80vh", width: "60vw"}} className={'flex-wrap d-flex justify-content-evenly'}>
                    {mediasLoading ? <Spinner /> : medias.data.map(media => (
                        <MediaCard key={media.id} isSelected={selectedMedia.includes(media)} media={media} onClick={() => onClickCallback(media)} pageName={'createList'}>
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
            <div style={{maxWidth: "20vw"}} className={'container d-flex flex-column'}>
                <CreateListForm selectedMedia={selectedMedia} onDeleteCallback={onClickCallback}/>
            </div>
        </div>
    </div>
}

export default CreateListView