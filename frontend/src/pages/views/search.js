import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import mediaApi from "../../api/MediaApi";
import PagingSizes from "../../api/values/PagingSizes";
import searchBar from "../components/searchBar/SearchBar";
import SortOrder from "../../api/values/SortOrder";
import mediaOrderBy from "../../api/values/MediaOrderBy";
import MediaTypes from "../../api/values/MediaTypes";
import MediaCard from "../components/mediaCard/MediaCard";

function Healthcheck() {

    const {search} = useParams();

    const [medias, setMedias] = useState([]);
    const [mediaLoading, setMediaLoading] = useState(true);
    const [mediaError, setMediaError] = useState(null);

    const fetchMedia = async () => {
        try {
            const response = await mediaApi.getMedia({
                type: MediaTypes.TYPE_ALL,
                orderBy: mediaOrderBy.RELEASE_DATE,
                sortOrder: SortOrder.DESC,
                page: 1,
                pageSize: PagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                search: search
            });
            setMedias(response.data);
        } catch (err) {
            setMediaError(err);
        } finally {
            setMediaLoading(false);
        }
    };

    useEffect(() => {
        fetchMedia();
    }, []);
    return (
        <div class="discover-media-card-container">
            {medias.map((media) => (
                <div className="discover-media-card"><MediaCard media={media}/></div>

            ))}
        </div>
    )
    ;
}

export default Healthcheck;