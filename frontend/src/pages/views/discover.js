import { Pagination } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { createSearchParams, useLocation, useNavigate, useSearchParams } from 'react-router-dom';
import api from '../../api/api';
import { MediaOrderBy as mediaOrderBy } from '../../api/values/MediaOrderBy';
import mediaTypes from '../../api/values/MediaTypes';
import pagingSizes from '../../api/values/PagingSizes';
import SortOrder from '../../api/values/SortOrder';
import MediaService from '../../services/MediaService';
import FiltersGroup from '../components/filters/filtersGroup/filtersGroup';
import MediaCard from '../components/mediaCard/MediaCard';
import './discover.css';

const Discover = () => {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const location = useLocation();

  // Filter States
  const [type, setType] = useState(searchParams.get('type') || mediaTypes.TYPE_ALL);
  const [orderBy, setOrderBy] = useState(searchParams.get('orderBy') || mediaOrderBy.TOTAL_RATING);
  const [sortOrder, setSortOrder] = useState(searchParams.get('sortOrder') || SortOrder.DESC);
  const [selectedProviders, setSelectedProviders] = useState(
    location.state?.selectedProviders ? location.state.selectedProviders : []
  );
  const [selectedGenres, setSelectedGenres] = useState(
    location.state?.selectedGenres ? location.state.selectedGenres : []
  );
  const [searchQuery, setSearchQuery] = useState(searchParams.get('search') || '');
  const [page, setPage] = useState(parseInt(searchParams.get('page')) || 1);

  const [medias, setMedias] = useState(undefined);
  const [mediasLoading, setMediasLoading] = useState(true);
  const [mediasError, setMediasError] = useState(null);

  const { isLoggedIn, user } = useSelector((state) => state.auth);
  const [watchedUrl, setWatchedUrl] = useState('loading');
  const [watchlistUrl, setWatchlistUrl] = useState('loading');

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
      pathname: '/discover',
      search: createSearchParams(queryParams).toString(),
    });
  };

  const handleFilterChange = ({
    type: newType,
    sortOrder: newSortOrder,
    orderBy: newOrderBy,
    search: newSearch,
    selectedProviders: newProviders,
    selectedGenres: newGenres,
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
    if (!user?.defaultPrivateMoovieListsUrl) return;
    const fetchUrls = async () => {
      const [watchedRes, watchlistRes] = await Promise.all([
        api.get(user?.defaultPrivateMoovieListsUrl, {
          params: {
            search: 'Watched',
          },
        }).then((res) => res.data?.[0]?.url),
        api.get(user?.defaultPrivateMoovieListsUrl, {
          params: {
            search: 'Watchlist',
          },
        }).then((res) => res.data?.[0]?.url),
      ]);
      setWatchedUrl(watchedRes);
      setWatchlistUrl(watchlistRes);
    };
    fetchUrls();
  }, [user?.defaultPrivateMoovieListsUrl]);

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
          providers: selectedProviders.map((p) => p.id),
          genres: selectedGenres.map((g) => g.id),
        });

        const { data: medias, links } = mediasResponse;

        setMedias({
          links,
          data: medias,
        });
      } catch (error) {
        console.error('Error fetching media data:', error);
        setMediasError(error);
      } finally {
        setMediasLoading(false);
      }
    }

    fetchMediaData();
  }, [type, page, sortOrder, orderBy, searchQuery, selectedProviders, selectedGenres]);

  if (mediasLoading) {
    return (
      <div
        className="d-flex justify-content-center align-items-center"
        style={{ height: 'calc(100vh - 76px)' }}
      >
        <Spinner animation="border" role="status">
          <span className="visually-hidden">{t('loader.loading')}</span>
        </Spinner>
      </div>
    );
  }

  return (
    <div className="d-flex flex-column" style={{ height: 'calc(100vh - 76px)' }}>
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
        <div className="container d-flex flex-column">
          <div
            style={{
              overflowY: 'auto',
              maxHeight: '80vh',
              width: '900px',
              display: 'grid',
              gridTemplateColumns: 'repeat(5, 1fr)',
              gap: '16px',
              padding: '8px',
            }}
            className="mx-auto"
          >
            {mediasError ? (
              <div className="alert alert-danger" role="alert" style={{ gridColumn: 'span 5' }}>
                {t('common.errorOccurred')}
              </div>
            ) : (
              medias?.data?.map((media) => <MediaCard key={media.id} media={media} watchedUrl={watchedUrl} watchlistUrl={watchlistUrl} size="small" />)
            )}

            {medias?.data?.length === 0 && (
              <div className="text-center py-8" style={{ gridColumn: 'span 5' }}>
                <p className="text-lg text-gray-600">{t('discover.noMediaAvailable')}</p>
              </div>
            )}
          </div>

          <div className="m-1 d-flex justify-content-center">
            {!mediasLoading && medias?.links?.last?.pageNumber > 1 && (
              <Pagination
                page={page}
                count={medias.links.last.pageNumber}
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
