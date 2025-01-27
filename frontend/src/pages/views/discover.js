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
import ProviderService from "../../services/ProviderService";
import ProviderFilter from "../components/filters/providerFilter/ProviderFilter";
import GenreService from "../../services/GenreService";
import GenreFilter from "../components/filters/genreFilter/GenreFilter";
import {useTranslation} from "react-i18next";
import {Tooltip as ReactTooltip} from "react-tooltip";


const Discover = () => {
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const [type, setType] = useState(searchParams.get("type"));
    const [orderBy, setOrderBy] = useState(searchParams.get("orderBy") || [OrderBy.TMDB_RATING])
    const [sortOrder, setSortOrder] = useState(searchParams.get("sortOrder") || [SortOrder.DESC])
    const [selectedProviders, setSelectedProviders] = useState(new Set(searchParams.get("providers") ? JSON.parse(searchParams.get("providers")) : []));
    const [selectedGenres, setSelectedGenres] = useState(new Set(searchParams.get("genres") ? JSON.parse(searchParams.get("genres")) : []));
    const [page, setPage] = useState(searchParams.get("page") || 1);

    const [medias, setMedias] = useState(undefined);
    const [mediasLoading, setMediasLoading] = useState(true);
    const [mediasError, setMediasError] = useState(null);

    const [genresForMedias, setGenresForMedias] = useState(undefined);
    const [genresForMediasLoading, setGenresForMediasLoading] = useState(true);
    const [genresForMediasError, setGenresForMediasError] = useState(null);

    const [providersForMedias, setProvidersForMedias] = useState(undefined);
    const [providersForMediasLoading, setProvidersForMediasLoading] = useState(true);
    const [providersForMediasError, setProvidersForMediasError] = useState(null);

    const [providers, setProviders] = useState(undefined);
    const [providersLoading, setProvidersLoading] = useState(true);
    const [providersError, setProvidersError] = useState(null);

    const [genres, setGenres] = useState(undefined);
    const [genresLoading, setGenresLoading] = useState(true);
    const [genresError, setGenresError] = useState(null);

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: "/discover",
            search: createSearchParams({
                type,
                orderBy,
                sortOrder,
                providers: JSON.stringify(Array.from(selectedProviders)),
                genres: JSON.stringify(Array.from(selectedGenres)),
                page: newPage.toString(),
            }).toString(),
        });
    };

    useEffect(() => {
        const providersFromUrl = searchParams.get("providers");
        if (providersFromUrl) {
            try {
                const providerIds = JSON.parse(providersFromUrl);
                setSelectedProviders(new Set(providerIds));
            } catch (error) {
                console.error("Error parsing providers from URL:", error);
            }
        }
    }, [searchParams]);

    useEffect(() => {
        const genresFromUrl = searchParams.get("genres");
        if (genresFromUrl) {
            try {
                const genreIds = JSON.parse(genresFromUrl);
                setSelectedGenres(new Set(genreIds));
            } catch (error) {
                console.error("Error parsing genres from URL:", error);
            }
        }
    }, [searchParams]);

    useEffect(() => {
        const providerIds = Array.from(selectedProviders);
        const genreIds = Array.from(selectedGenres);

        const queryParams = {
            orderBy,
            sortOrder,
            page: page.toString(),
        };

        if (providerIds.length > 0) {
            queryParams.providers = JSON.stringify(providerIds);
        }

        if (genreIds.length > 0) {
            queryParams.genres = JSON.stringify(genreIds);
        }

        navigate({
            pathname: `/discover`,
            search: createSearchParams(queryParams).toString(),
        });
    }, [orderBy, sortOrder, selectedProviders, selectedGenres, page, navigate]);


    useEffect(() => {
        async function getData() {
            try {
                const data = await ProviderService.getAllProviders();
                setProviders(data);
                setProvidersLoading(false);
            } catch (error) {
                setProvidersError(error);
                setProvidersLoading(false);
            }
        }

        getData();
    }, []);



    useEffect(() => {
        async function getData() {
            try {
                const data = await GenreService.getAllGenres();
                setGenres(data);
                setGenresLoading(false);
            } catch (error) {
                setGenresError(error);
                setGenresLoading(false);
            }
        }

        getData();
    }, []);



    useEffect(() => {
        async function getData() {
            try {
                const mediasResponse = await MediaService.getMedia({
                    type: type,
                    page: page,
                    pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                    sortOrder: sortOrder,
                    orderBy: orderBy,
                    providers: Array.from(selectedProviders),
                    genres: Array.from(selectedGenres),
                });


                const { data: medias, links } = mediasResponse;

                const mediasWithProviders = await Promise.all(
                    medias.map(async (media) => {
                        try {
                            const providers = await ProviderService.getProvidersForMedia(media.id);
                            return { ...media, providers };
                        } catch (error) {
                            return { ...media, providers: [] };
                        }
                    })
                );

                const mediasWithGenres = await Promise.all(
                    mediasWithProviders.map(async (media) => {
                        try {
                            const genres = await GenreService.getGenresForMedia(media.id);
                            return { ...media, genres };
                        } catch (error) {
                            return { ...media, genres: [] };
                        }
                    })
                );
                setMedias({
                    links,
                    data: mediasWithGenres,
                });
                setMediasLoading(false);
            } catch (error) {
                console.error("Error in fetching media data:", error);
                setMediasError(error);
                setMediasLoading(false);
            }
        }

        getData();
    }, [type, page, sortOrder, orderBy, selectedProviders, selectedGenres]);






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
                    <div>{t('discover.providers')}
                    <ProviderFilter providers={providers?.data}
                                    selectedProviders={selectedProviders}
                                    setSelectedProviders={setSelectedProviders}
                                    ></ProviderFilter></div>
                    <div>{t('discover.genres')}
                        <GenreFilter genres={genres?.data}
                                        selectedGenres={selectedGenres}
                                        setSelectedGenres={setSelectedGenres}
                        ></GenreFilter></div>

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
                        <p>{t('discover.noMediaAvailable')}</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Discover;