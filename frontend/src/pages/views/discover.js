import React, {useEffect, useState} from "react";
import "./discover.css"
import OrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";
import DropdownMenu from "../components/dropdownMenu/DropdownMenu";
import MediaCard from "../components/mediaCard/MediaCard";
import PaginationButton from "../components/paginationButton/PaginationButton";
import MediaService from "../../services/MediaService";
import {createSearchParams, useNavigate, useSearchParams} from "react-router-dom";
import pagingSizes from "../../api/values/PagingSizes";

const Discover = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const [type, setType] = useState(searchParams.get("type"));
    const [orderBy, setOrderBy] = useState(searchParams.get("orderBy") || [OrderBy.TMDB_RATING])
    const [sortOrder, setSortOrder] = useState(searchParams.get("sortOrder") || [SortOrder.DESC])
    const [page, setPage] = useState(searchParams.get("page") || 1);

    const [medias, setMedias] = useState(undefined);
    const [mediasLoading, setMediasLoading] = useState(true);
    const [mediasError, setMediasError] = useState(null);

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: "/discover",
            search: createSearchParams({
                type,
                orderBy,
                sortOrder,
                page: newPage.toString(),
            }).toString(),
        });
    };

    useEffect(() => {
        async function getData() {
            try {
                const data = await MediaService.getMedia({
                    type: type,
                    page: page,
                    pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                    sortOrder: sortOrder,
                    orderBy: orderBy,
                });
                setMedias(data);
                setMediasLoading(false);
            } catch (error) {
                setMediasError(error);
                setMediasLoading(false);
            }
        }

        getData();
    }, [type, page, sortOrder, orderBy]);



    return (
        <div className="moovie-default default-container">
            <div className="discover-container">
                <div className="discover-menus">
                    <DropdownMenu
                        values={Object.values(OrderBy)}
                        setOrderBy={setOrderBy}
                        setSortOrder={setSortOrder}
                        currentOrderDefault={sortOrder}
                    />
                    <div>Genres</div>
                </div>

                <div className="discover-media-card-container">
                    {(medias && medias.data) ? (
                        <>
                            {medias.data.map((media) => (
                                <div className="discover-media-card" key={media.id}>
                                    <MediaCard media={media} />
                                </div>
                            ))}
                            <div className="flex justify-center pt-4">
                                {(medias.data.length > 0 && medias.links.last.page > 1) && (
                                    <PaginationButton
                                        page={page}
                                        lastPage={medias.links.last.page}
                                        setPage={handlePageChange}
                                    />
                                )}
                            </div>
                        </>
                    ) : (
                        <p>No media available.</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Discover;