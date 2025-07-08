import { CircularProgress } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { MediaOrderBy as mediaOrderBy } from '../../../../api/values/MediaOrderBy';
import mediaTypes from '../../../../api/values/MediaTypes';
import SortOrder from '../../../../api/values/SortOrder';
import GenreService from '../../../../services/GenreService';
import ProviderService from '../../../../services/ProviderService';
import ChipsDisplay from './chipsDisplay';
import FilterList from './filterList';
import FilterSection from './filterSection';
import FormButtons from './formButtons';
import { parsePaginatedResponse } from "../../../../utils/ResponseUtils";
import PaginationButton from "../../paginationButton/PaginationButton";
import InfiniteScrollList from './InfiniteScrollList';

const FiltersGroup = ({
  type,
  sortOrder,
  orderBy,
  query,
  searchBar,
  initialSelectedGenres = [],
  initialSelectedProviders = [],
  submitCallback,
}) => {
  const { t } = useTranslation(); // Initialize translations

  const [openGenres, setOpenGenres] = useState(false);
  const [openProviders, setOpenProviders] = useState(false);
  const [searchGenre, setSearchGenre] = useState('');
  const [searchProvider, setSearchProvider] = useState('');

  const [selectedGenres, setSelectedGenres] = useState(initialSelectedGenres);
  const [selectedProviders, setSelectedProviders] = useState(initialSelectedProviders);
  const [queryInput, setQueryInput] = useState(query);
  const [sortOrderInput, setSortOrderInput] = useState(sortOrder || SortOrder.DESC);
  const [mediaTypeInput, setMediaTypeInput] = useState(type || mediaTypes.TYPE_ALL);
  const [mediaOrderByInput, setMediaOrderByInput] = useState(orderBy || mediaOrderBy.TOTAL_RATING);

  const [genresList, setGenresList] = useState([]);
  const [providersRes, setProvidersRes] = useState([]);
  const [providersList, setProvidersList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(1);
  const [hasMoreProviders, setHasMoreProviders] = useState(true);
  const [initialLoading, setInitialLoading] = useState(true);
  const [genresLoaded, setGenresLoaded] = useState(false);
  const [providersLoaded, setProvidersLoaded] = useState(false);
  const [genresPage, setGenresPage] = useState(1);
  const [hasMoreGenres, setHasMoreGenres] = useState(true);

  useEffect(() => {
    async function getProviders() {
      try {
        setLoading(true);
        const response = await ProviderService.getAllProviders(page);
        setProvidersRes(response);
        const providerList = response.data.map((provider) => ({
          name: provider.providerName,
          id: provider.providerId,
        }));
        setProvidersList((prev) =>
          page === 1 ? providerList : [...prev, ...providerList]
        );
        // Check if there are more pages
        if (response?.links?.last?.pageNumber && page >= response.links.last.pageNumber) {
          setHasMoreProviders(false);
        } else {
          setHasMoreProviders(true);
        }
        if (page === 1) setProvidersLoaded(true);
      } catch (error) {
        setError(t('filters.error.fetch_providers'));
        setLoading(false);
      } finally {
        setLoading(false);
      }
    }
    getProviders();
  }, [page]);

  useEffect(() => {
    async function getGenres() {
      try {
        setLoading(true);
        const response = await GenreService.getAllGenres(genresPage);
        // response.data is the genre array, response.links is pagination
        const genreList = response.data.map((genre) => ({
          name: genre.genreName,
          id: genre.genreId,
        }));
        setGenresList((prev) => {
          const existingIds = new Set(prev.map((g) => g.id));
          const newGenres = genreList.filter((g) => !existingIds.has(g.id));
          return genresPage === 1 ? genreList : [...prev, ...newGenres];
        });
        // Check if there are more pages
        if (response?.links?.last?.pageNumber && genresPage >= response.links.last.pageNumber) {
          setHasMoreGenres(false);
        } else {
          setHasMoreGenres(true);
        }
        if (genresPage === 1) setGenresLoaded(true);
      } catch (error) {
        setError(t('filters.error.fetch_genres'));
        setLoading(false);
      } finally {
        setLoading(false);
      }
    }

    getGenres();
  }, [genresPage]);

  useEffect(() => {
    if (genresLoaded && providersLoaded) {
      setInitialLoading(false);
    }
  }, [genresLoaded, providersLoaded]);

  const handleChipRemove = (setFunction, item) => {
    setFunction((prev) => prev.filter((i) => i !== item));
  };

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    submitCallback({
      type: mediaTypeInput,
      sortOrder: sortOrderInput,
      orderBy: mediaOrderByInput,
      search: queryInput,
      selectedProviders: selectedProviders,
      selectedGenres: selectedGenres,
    });
  };

  const handleReset = () => {
    setSelectedGenres([]);
    setSelectedProviders([]);
    setQueryInput('');
    setSortOrderInput(SortOrder.DESC);
    setMediaTypeInput(mediaTypes.TYPE_ALL);
    setMediaOrderByInput(mediaOrderBy.TOTAL_RATING);
  };

  const loadMoreProviders = () => {
    if (!loading && hasMoreProviders) {
      setPage((prev) => prev + 1);
    }
  };

  const loadMoreGenres = () => {
    if (!loading && hasMoreGenres) {
      setGenresPage((prev) => prev + 1);
    }
  };

  if (initialLoading) return <CircularProgress />;
  if (error) return <div>{error}</div>;
  console.log('providersRes', providersRes);

  return (
    <div style={{ maxHeight: '85vh', width: '30vw', overflowY: 'auto' }}>
      <ChipsDisplay
        title={t('filters.genres')}
        items={selectedGenres}
        onRemove={(genre) => handleChipRemove(setSelectedGenres, genre)}
      />
      <ChipsDisplay
        title={t('filters.providers')}
        items={selectedProviders}
        onRemove={(provider) => handleChipRemove(setSelectedProviders, provider)}
      />

      {query && (
        <h4>
          {t('filters.results_for')}: {query}
        </h4>
      )}

      <div className="m-1 flex-column" id="filters">
        <form id="filter-form" onSubmit={handleFilterSubmit} className="mb-2 d-flex flex-column">
          {query && <input type="hidden" name="query" value={query} />}

          <div className="d-flex flex-row m-1">
            <select
              name="type"
              className="form-select m-1"
              onChange={(e) => setMediaTypeInput(e.target.value)}
            >
              <option selected={mediaTypeInput === mediaTypes.TYPE_ALL} value={mediaTypes.TYPE_ALL}>
                {t('filters.all')}
              </option>
              <option
                selected={mediaTypeInput === mediaTypes.TYPE_TVSERIE}
                value={mediaTypes.TYPE_TVSERIE}
              >
                {t('filters.series')}
              </option>
              <option
                selected={mediaTypeInput === mediaTypes.TYPE_MOVIE}
                value={mediaTypes.TYPE_MOVIE}
              >
                {t('filters.movies')}
              </option>
            </select>

            <select
              name="orderBy"
              className="form-select m-1"
              onChange={(e) => setMediaOrderByInput(e.target.value)}
            >
              <option selected={mediaOrderByInput === mediaOrderBy.NAME} value={mediaOrderBy.NAME}>
                {t('filters.title')}
              </option>
              <option
                selected={mediaOrderByInput === mediaOrderBy.TOTAL_RATING}
                value={mediaOrderBy.TOTAL_RATING}
              >
                {t('filters.total_rating')}
              </option>
              <option
                selected={mediaOrderByInput === mediaOrderBy.TMDB_RATING}
                value={mediaOrderBy.TMDB_RATING}
              >
                {t('filters.tmdb_rating')}
              </option>
              <option
                selected={mediaOrderByInput === mediaOrderBy.RELEASE_DATE}
                value={mediaOrderBy.RELEASE_DATE}
              >
                {t('filters.release_date')}
              </option>
            </select>
          </div>

          {searchBar && (
            <div className="m-1">
              <input
                type="search"
                className="form-control m-1"
                placeholder={t('filters.search_placeholder')}
                value={queryInput}
                onChange={(e) => setQueryInput(e.target.value)}
              />
            </div>
          )}
          <FilterSection
            title={t('filters.genres')}
            isOpen={openGenres}
            toggleOpen={() => setOpenGenres(!openGenres)}
          >
            <InfiniteScrollList
              loadMore={loadMoreGenres}
              hasMore={hasMoreGenres}
              loading={loading}
              maxHeight="250px"
            >
              <FilterList
                searchValue={searchGenre}
                onSearchChange={setSearchGenre}
                items={genresList}
                selectedItems={selectedGenres}
                onToggleItem={(genre) => {
                  setSelectedGenres((prev) =>
                    prev.some((g) => g.id === genre.id)
                      ? prev.filter((g) => g.id !== genre.id)
                      : [...prev, genre]
                  );
                }}
              />
            </InfiniteScrollList>
          </FilterSection>

          <FilterSection
            title={t('filters.providers')}
            isOpen={openProviders}
            toggleOpen={() => setOpenProviders(!openProviders)}
          >
            <InfiniteScrollList
              loadMore={loadMoreProviders}
              hasMore={hasMoreProviders}
              loading={loading}
              maxHeight="250px"
            >
              <FilterList
                searchValue={searchProvider}
                onSearchChange={setSearchProvider}
                items={providersList}
                selectedItems={selectedProviders}
                onToggleItem={(provider) =>
                  setSelectedProviders((prev) =>
                    prev.some((p) => p.id === provider.id)
                      ? prev.filter((p) => p.id !== provider.id)
                      : [...prev, provider]
                  )
                }
              />
            </InfiniteScrollList>

          </FilterSection>
          <FormButtons onApply={handleFilterSubmit} onReset={handleReset} />
        </form>
      </div>
    </div>
  );
};

export default FiltersGroup;
