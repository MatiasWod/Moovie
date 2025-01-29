import React, {useEffect, useState} from 'react'
import {Pagination, PaginationItem} from "@mui/material";
import MediaCard from "../../components/media/MediaCard/MediaCard";
import MediaService from "../../../services/MediaService";
import pagingSizes from "../../../api/values/PagingSizes";
import {Row, Spinner} from "react-bootstrap";
import '../../components/media/MediaCard/MediaCard.css'
import FiltersGroup from "../../components/filters/FiltersGroup/FiltersGroup";
import mediaTypes from "../../../api/values/MediaTypes";
import useMediaList from "../../../hooks/useMediasList";
import mediaOrderBy from "../../../api/values/MediaOrderBy";
import sortOrder from "../../../api/values/SortOrder";

const CreateListView = () => {
    const [selectedItems, setSelectedItems] = useState([])
    const [mediaList, setMediaList] = useState(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState(false)
    const [errorMessage, setErrorMessage] = useState('')


    const onClickCallback = (mediaId) => {
        setSelectedItems((state) => state.includes(mediaId)
            ? state.filter((i) => i !== mediaId) : [...state, mediaId]
        )
    }

    const { medias, mediasLoading, mediasError } = useMediaList({
        type: mediaTypes.TYPE_MOVIE,
        page: 1,
        sortOrder: sortOrder.ASC,
        orderBy: mediaOrderBy.NAME,
        selectedProviders: [],
        selectedGenres: [],
    });


    return <div className={'container d-flex flex-column'}>
        <div className={'container d-flex flex-row'}>
            <FiltersGroup genresList={[]} providersList={[]} searchBar={true}/>
            <div className={'container d-flex flex-column'}>
                <div style={{overflowY: "auto", maxHeight: "90vh"}} className={'flex-wrap d-flex'}>
                    {medias ? medias.data.map((media, _) => (
                        <MediaCard key={media.id} isSelected={selectedItems.includes(media.id)} media={media} onClick={() => onClickCallback(media.id)} pageName={'createList'}>
                        </MediaCard>
                     ))
                    : <Spinner/>}
                </div>
                <div className={'m-'}>
                    <Pagination>
                        <PaginationItem>
                            1
                        </PaginationItem>
                    </Pagination>
                </div>
            </div>
            <div id={'preview'}>

            </div>
        </div>
    </div>
}

export default CreateListView