import React, {useEffect, useState} from "react";
import "./discover.css"
import MediaCard from "../components/mediaCard/MediaCard";
import MediaService from "../../services/MediaService";
import {createSearchParams, useNavigate, useSearchParams} from "react-router-dom";
import pagingSizes from "../../api/values/PagingSizes";
import ProviderService from "../../services/ProviderService";
import GenreService from "../../services/GenreService";
import {useTranslation} from "react-i18next";
import {Spinner} from "react-bootstrap";
import FiltersGroup from "../components/filters/filtersGroup/filtersGroup";
import {Pagination} from "@mui/material";
import mediaTypes from "../../api/values/MediaTypes";
import mediaOrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";

const Discover = () => {
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    // Filter States
    const [type, setType] = useState(searchParams.get("type") || mediaTypes.TYPE_ALL);
    const [orderBy, setOrderBy] = useState(searchParams.get("orderBy") || mediaOrderBy.TOTAL_RATING);
    const [sortOrder, setSortOrder] = useState(searchParams.get("sortOrder") || SortOrder.DESC);
    const [selectedProviders, setSelectedProviders] = useState(
        searchParams.get("providers") ? JSON.parse(searchParams.get("providers")) : []
    );
    const [selectedGenres, setSelectedGenres] = useState(
        searchParams.get("genres") ? JSON.parse(searchParams.get("genres")) : []
    );
    const [searchQuery, setSearchQuery] = useState(searchParams.get("search") || "");
    const [page, setPage] = useState(parseInt(searchParams.get("page")) || 1);

    const [medias, setMedias] = useState(undefined);
    const [mediasLoading, setMediasLoading] = useState(true);
    const [mediasError, setMediasError] = useState(null);

    const handlePaginationChange = (event, value) => {
        setPage(value);
        updateUrlParams(value);
    };

    const updateUrlParams = (currentPage = page) => {
        const queryParams = {
            type,
            orderBy,
            sortOrder,
            page: currentPage.toString(),
        };

        if (searchQuery) {
            queryParams.search = searchQuery;
        }
        if (selectedProviders.length > 0) {
            queryParams.providers = JSON.stringify(selectedProviders);
        }
        if (selectedGenres.length > 0) {
            queryParams.genres = JSON.stringify(selectedGenres);
        }

        navigate({
            pathname: "/discover",
            search: createSearchParams(queryParams).toString(),
        });
    };

    const handleFilterChange = ({
        type: newType,
        sortOrder: newSortOrder,
        orderBy: newOrderBy,
        search: newSearch,
        selectedProviders: newProviders,
        selectedGenres: newGenres
    }) => {
        setType(newType);
        setSortOrder(newSortOrder);
        setOrderBy(newOrderBy);
        setSearchQuery(newSearch);
        setSelectedProviders(newProviders);
        setSelectedGenres(newGenres);
        setPage(1);
    };

    useEffect(() => {
        async function fetchMediaData() {
            try {
                setMediasLoading(true);
                const mediasResponse = await MediaService.getMedia({
                    type,
                    page,
                    pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                    sortOrder,
                    orderBy,
                    search: searchQuery,
                    providers: selectedProviders.map(p => p.id),
                    genres: selectedGenres.map(g => g.id),
                });

                const { data: medias, links } = mediasResponse;

                const mediasWithDetails = await Promise.all(
                    medias.map(async (media) => {
                        const [providers, genres] = await Promise.all([
                            ProviderService.getProvidersForMedia(media.id).catch(() => []),
                            GenreService.getGenresForMedia(media.id).catch(() => [])
                        ]);
                        return { ...media, providers, genres };
                    })
                );

                setMedias({
                    links,
                    data: mediasWithDetails,
                });
            } catch (error) {
                console.error("Error fetching media data:", error);
                setMediasError(error);
            } finally {
                setMediasLoading(false);
            }
        }

        fetchMediaData();
    }, [type, page, sortOrder, orderBy, searchQuery, selectedProviders, selectedGenres]);

    if (mediasLoading) {
        return (
            <div className="d-flex justify-content-center align-items-center" style={{ height: "calc(100vh - 76px)" }}>
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        );
    }

    return (
        <div className="d-flex flex-column" style={{ height: "calc(100vh - 76px)" }}>
            <div className="d-flex flex-row h-100">
                {/* Filters Section */}
                <FiltersGroup
                    type={type}
                    sortOrder={sortOrder}
                    orderBy={orderBy}
                    query={searchQuery}
                    searchBar={true}
                    initialSelectedGenres={selectedGenres}
                    initialSelectedProviders={selectedProviders}
                    submitCallback={handleFilterChange}
                />

                {/* Media Cards Section */}
                <div className="d-flex flex-column h-100 px-3">
                    <div style={{
                        overflowY: "auto",
                        height: "calc(100% - 50px)", // Account for pagination height
                        width: "calc(160px * 5)", // Exactly 5 cards width
                        display: "grid",
                        gridTemplateColumns: "repeat(5, 1fr)",
                        gap: "0", // Remove all gap
                        padding: "0.25rem",
                        alignContent: "start",
                        margin: "0 auto" // Center the grid
                    }}>
                        {mediasError ? (
                            <div className="alert alert-danger" role="alert">
                                {t('common.errorOccurred')}
                            </div>
                        ) : (
                            medias?.data?.map((media, index) => (
                                <div 
                                    key={media.id} 
                                    style={{
                                        width: "100%",
                                        aspectRatio: "2/3",
                                        transform: `translateX(${(index % 5) * -25}px)`,
                                        transition: "all 0.3s ease",
                                        position: "relative",
                                        zIndex: 1,
                                        marginLeft: index % 5 === 0 ? "0" : "-25px",
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.transform = `translateX(${(index % 5) * -25}px) scale(1.1)`;
                                        e.currentTarget.style.zIndex = "9999";
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.transform = `translateX(${(index % 5) * -25}px)`;
                                        e.currentTarget.style.zIndex = "1";
                                    }}
                                >
                                    <MediaCard
                                        media={media}
                                        size="small"
                                    />
                                </div>
                            ))
                        )}
                        
                        {medias?.data?.length === 0 && (
                            <div className="text-center py-8" style={{ gridColumn: "span 5" }}>
                                <p className="text-lg text-gray-600">
                                    {t('discover.noMediaAvailable')}
                                </p>
                            </div>
                        )}
                    </div>

                    <div className="d-flex justify-content-center py-2">
                        {!mediasLoading && medias?.links?.last?.page > 1 && (
                            <Pagination
                                page={page}
                                count={medias.links.last.page}
                                onChange={handlePaginationChange}
                            />
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Discover;