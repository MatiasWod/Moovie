import { useState, useEffect } from 'react';
import MediaService from '../services/MediaService';
import pagingSizes from '../api/values/PagingSizes';
import api from '../api/api';

const useMediaList = ({
  type,
  page,
  sortOrder,
  orderBy,
  search,
  selectedProviders,
  selectedGenres,
}) => {
  const [medias, setMedias] = useState({ data: [], links: {} });
  const [mediasLoading, setMediasLoading] = useState(true);
  const [mediasError, setMediasError] = useState(null);

  useEffect(() => {
    const getMedias = async () => {
      try {
        setMediasError(null);
        setMediasLoading(true);

        const mediasResponse = await MediaService.getMedia({
          type: type,
          page: page,
          pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
          orderBy: orderBy,
          sortOrder: sortOrder,
          search: search,
          providers: Array.from(selectedProviders.map((e) => e.id)),
          genres: Array.from(selectedGenres.map((e) => e.id)),
        });

        const { data: medias, links } = mediasResponse;
        console.log('medias', medias);
        const mediasWithDetails = await Promise.all(
          medias.map(async (media) => {
            try {
              // Fetch providers and genres data from their respective URLs
              // The .catch(() => []) handles cases where the API call fails and returns empty array
              // The .then((res) => res.data) extracts the data property from the response
              const [providers, genres] = await Promise.all([
                api
                  .get(media.providersUrl)
                  .catch(() => ({ data: [] })) // Return object with data property instead of just array
                  .then((res) => res.data),
                api
                  .get(media.genresUrl)
                  .catch(() => ({ data: [] })) // Return object with data property instead of just array
                  .then((res) => res.data),
              ]);
              console.log('providers', providers);
              console.log('genres', genres);
              return { ...media, providers: providers, genres: genres };
            } catch {
              return { ...media, providers: [], genres: [] };
            }
          })
        );
        console.log('mediasWithDetails', mediasWithDetails);

        setMedias({ links, data: mediasWithDetails });
      } catch (error) {
        console.error('Error fetching media data:', error);
        setMedias({ data: [], links: {} });
        setMediasError(error);
      } finally {
        setMediasLoading(false);
      }
    };

    getMedias();
  }, [type, page, sortOrder, orderBy, search, selectedProviders, selectedGenres]);

  return { medias, mediasLoading, mediasError };
};

export default useMediaList;
