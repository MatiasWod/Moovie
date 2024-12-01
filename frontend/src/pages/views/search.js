import React, {useEffect, useState} from 'react';
import {createSearchParams, useNavigate, useParams, useSearchParams} from "react-router-dom";
import mediaApi from "../../api/MediaApi";
import PagingSizes from "../../api/values/PagingSizes";
import searchBar from "../components/searchBar/SearchBar";
import SortOrder from "../../api/values/SortOrder";
import mediaOrderBy from "../../api/values/MediaOrderBy";
import MediaTypes from "../../api/values/MediaTypes";
import MediaCard from "../components/mediaCard/MediaCard";
import MediaService from "../../services/MediaService";
import pagingSizes from "../../api/values/PagingSizes";
import PaginationButton from "../components/paginationButton/PaginationButton";

function Healthcheck() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const {search} = useParams();

    const [medias, setMedias] = useState(undefined);
    const [mediaLoading, setMediaLoading] = useState(true);
    const [mediaError, setMediaError] = useState(null);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/search/${search}`,
            search: createSearchParams({
                search: search,
                page: newPage.toString(),
            }).toString(),
        });
    };


    useEffect(() => {
        async function getData() {
            try {
                const data = await MediaService.getMedia({
                    type: MediaTypes.TYPE_ALL,
                    page: page,
                    pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                    sortOrder: SortOrder.DESC,
                    orderBy: mediaOrderBy.RELEASE_DATE,
                    search:search
                });
                setMedias(data);
                setMediaLoading(false);
            } catch (error) {
                setMediaError(error);
                setMediaLoading(false);
            }
        }

        getData();
    }, [search,page]);
    return (
        <div class="discover-media-card-container">
            {medias?.data?.map((media) => (
                <div className="discover-media-card"><MediaCard media={media}/></div>
            ))}
            <div className="flex justify-center pt-4">
                {(medias?.data?.length > 0 && medias?.links?.last.page > 1) && (
                    <PaginationButton
                        page={page}
                        lastPage={medias.links.last.page}
                        setPage={handlePageChange}
                    />
                )}
            </div>
        </div>
    )
    ;
}

export default Healthcheck;