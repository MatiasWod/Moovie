import React, {useEffect, useState} from 'react'
import {Pagination, PaginationItem} from "@mui/material";
import MediaCard from "../../components/media/MediaCard/MediaCard";
import MediaService from "../../../services/MediaService";
import pagingSizes from "../../../api/values/PagingSizes";
import {Row, Spinner} from "react-bootstrap";
import '../../components/media/MediaCard/MediaCard.css'
import FiltersGroup from "../../components/filters/FiltersGroup/FiltersGroup";

const CreateListView = () => {
    const [selectedItems, setSelectedItems] = useState([])
    const [mediaList, setMediaList] = useState(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState(false)
    const [errorMessage, setErrorMessage] = useState('')

    useEffect(() => {
        const fetchMedia = async () => {
            setLoading(true)
            try {
                const response = await MediaService.getMedia({
                    page: 1,
                    pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                })
                setMediaList(response.data)
            } catch (e) {
                setError(true)
                setErrorMessage(e.message)
                setLoading(false)
            } finally {
                setLoading(false)
            }
        }

        fetchMedia()
    }, []);

    const onClickCallback = (mediaId) => {
        setSelectedItems((state) => state.includes(mediaId)
            ? state.filter((i) => i !== mediaId) : [...state, mediaId]
        )
    }

    if (loading) return <Row className={'d-flex justify-center'}><Spinner/></Row>
    if (error) return <div>There's Been an error: {errorMessage}</div>

    return <div className={'container d-flex flex-column'}>
        <div className={'container d-flex flex-row'}>
            <FiltersGroup genresList={[]} providersList={['a',
                'u','y','t','r','e','w','p','1','2','q','w','b']} searchBar={true}/>
            <div className={'container d-flex flex-column'}>
                <div style={{overflowY: "auto", maxHeight: "90vh"}} className={'flex-wrap d-flex'}>
                    {mediaList ? mediaList.map((media, _) => (
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