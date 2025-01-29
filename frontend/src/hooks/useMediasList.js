import { useState, useEffect } from "react";
import MediaService from "../services/MediaService";
import pagingSizes from "../api/values/PagingSizes";
import ProviderService from "../services/ProviderService";
import GenreService from "../services/GenreService";
import {Spinner} from "react-bootstrap";

const useMediaList = ({ type, page, sortOrder, orderBy, selectedProviders, selectedGenres }) => {
    const [medias, setMedias] = useState({ data: [], links: {} });
    const [mediasLoading, setMediasLoading] = useState(true);
    const [mediasError, setMediasError] = useState(null);

    useEffect(() => {
        const getData = async () => {
            try {
                if (!mediasLoading) return
                setMediasError(null);

                const mediasResponse = await MediaService.getMedia({
                    type,
                    page,
                    pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                    sortOrder,
                    orderBy,
                    providers: Array.from(selectedProviders),
                    genres: Array.from(selectedGenres),
                });

                const { data: medias, links } = mediasResponse;

                // Fetch providers for each media
                const mediasWithProviders = await Promise.all(
                    medias.map(async (media) => {
                        try {
                            const providers = await ProviderService.getProvidersForMedia(media.id);
                            return { ...media, providers };
                        } catch {
                            return { ...media, providers: [] };
                        }
                    })
                );

                // Fetch genres for each media
                const mediasWithGenres = await Promise.all(
                    mediasWithProviders.map(async (media) => {
                        try {
                            const genres = await GenreService.getGenresForMedia(media.id);
                            return { ...media, genres };
                        } catch {
                            return { ...media, genres: [] };
                        }
                    })
                );

                setMedias({ links, data: mediasWithGenres });
            } catch (error) {
                console.error("Error fetching media data:", error);
                setMediasError(error);
            } finally {
                console.log("api call finished")
                setMediasLoading(false);
            }
        };

        getData();
    }, [type, page, sortOrder, orderBy, selectedProviders, selectedGenres]);

    if (mediasLoading) return <Spinner/>
    if (mediasError) return <div>Theres been an error: {mediasError}</div>
    return { medias, mediasLoading, mediasError };
};

export default useMediaList;
