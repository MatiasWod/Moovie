import React, {useEffect, useState} from "react";
import "./discover.css"
import OrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";
import listApi from "../../api/ListApi";
import PagingSizes from "../../api/values/PagingSizes";
import mediaApi from "../../api/MediaApi";
import MediaTypes from "../../api/values/MediaTypes";
import DropdownMenu from "../components/dropdownMenu/DropdownMenu";
import MediaCard from "../components/mediaCard/MediaCard";

const Discover = () => {
    const [type, setType] = useState([MediaTypes.TYPE_ALL]);
    const [orderBy, setOrderBy] = useState([OrderBy.TMDB_RATING])
    const [sortOrder, setSortOrder] = useState([SortOrder.DESC])
    const [page, setPage] = useState([1])

    const [medias, setMedias] = useState([]);
    const [mediasLoading, setMediasLoading] = useState(true);
    const [mediasError, setMediasError] = useState(null);

    const fetchMedia = async () => {
        try {
            const response = await mediaApi
                .getMedia(
                    {
                        type: type,
                        orderBy: orderBy,
                        sortOrder: sortOrder,
                        page: page,
                        pageSize: PagingSizes.MEDIA_DEFAULT_PAGE_SIZE});
            setMedias(response.data);
        } catch (err) {
            setMediasError(err);
        } finally {
            setMediasLoading(false);
        }
    };

    useEffect(() => {
        fetchMedia();
    }, [type, orderBy, sortOrder, page]);



    return (
        <div className="moovie-default default-container">
            <div className="discover-container">
                <div class="discover-menus">
                    <DropdownMenu values={Object.values(OrderBy)} setOrderBy={setOrderBy} setSortOrder={setSortOrder} currentOrderDefault={sortOrder} />
                    <div>Genres</div>
                </div>

                <div class="discover-media-card-container">
                    {medias.map((media) => (
                        <div className="discover-media-card"><MediaCard media={media} /></div>

                    ))}
                </div>
            </div>
        </div>
    )
}

export default Discover;