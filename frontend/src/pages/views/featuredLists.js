import React, { useEffect, useState } from 'react';
import { Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { createSearchParams, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import api from '../../api/api';
import { MediaOrderBy as OrderBy } from '../../api/values/MediaOrderBy';
import MediaTypes from '../../api/values/MediaTypes';
import pagingSizes from '../../api/values/PagingSizes';
import SortOrder from '../../api/values/SortOrder';
import useErrorStatus from '../../hooks/useErrorStatus';
import MediaService from '../../services/MediaService';
import ListContent from '../components/listContent/ListContent';
import '../components/mainStyle.css';
import PaginationButton from '../components/paginationButton/PaginationButton';

function FeaturedLists() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { t } = useTranslation();
  const { setErrorStatus } = useErrorStatus();
  const { isLoggedIn, user } = useSelector((state) => state.auth);


  //GET VALUES FOR FEATURED MEDIA
  const { type } = useParams();
  const [featuredMedia, setFeaturedMedia] = useState(undefined);
  const [featuredMediaLoading, setFeaturedMediaLoading] = useState(true);
  const [featuredMediaError, setFeaturedMediaError] = useState(null);
  const [page, setPage] = useState(Number(searchParams.get('page')) || 1);
  const [watchedUrl, setWatchedUrl] = useState(null);

  let featuredListTypeName;
  let mediaType;
  let orderBy;
  let typeSubtext;

  switch (type) {
    case 'topRatedMedia':
      featuredListTypeName = 'topRatedMedia';
      mediaType = MediaTypes.TYPE_ALL;
      orderBy = OrderBy.TMDB_RATING;
      typeSubtext = t('featuredLists.topRatedMedia');
      break;
    case 'topRatedMovies':
      featuredListTypeName = 'topRatedMovies';
      mediaType = MediaTypes.TYPE_MOVIE;
      orderBy = OrderBy.TMDB_RATING;
      typeSubtext = t('featuredLists.topRatedMovies');
      break;

    case 'topRatedSeries':
      featuredListTypeName = 'topRatedSeries';
      mediaType = MediaTypes.TYPE_TVSERIE;
      orderBy = OrderBy.TMDB_RATING;
      typeSubtext = t('featuredLists.topRatedSeries');
      break;

    case 'mostPopularMedia':
      featuredListTypeName = 'mostPopularMedia';
      mediaType = MediaTypes.TYPE_ALL;
      orderBy = OrderBy.VOTE_COUNT;
      typeSubtext = t('featuredLists.mostPopularMedia');
      break;

    case 'mostPopularMovies':
      featuredListTypeName = 'mostPopularMovies';
      mediaType = MediaTypes.TYPE_MOVIE;
      orderBy = OrderBy.VOTE_COUNT;
      typeSubtext = t('featuredLists.mostPopularMovies');
      break;

    case 'mostPopularSeries':
      featuredListTypeName = 'mostPopularSeries';
      mediaType = MediaTypes.TYPE_TVSERIE;
      orderBy = OrderBy.VOTE_COUNT;
      typeSubtext = t('featuredLists.mostPopularSeries');
      break;

    default:
      setErrorStatus(404);
  }

  const handlePageChange = (newPage) => {
    setPage(newPage);
    navigate({
      pathname: `/featuredLists/${featuredListTypeName}`,
      search: createSearchParams({
        type: mediaType,
        orderBy: orderBy,
        sortOrder: SortOrder.DESC,
        page: newPage.toString(),
      }).toString(),
    });
  };

  useEffect(() => {
    if (!user.defaultPrivateMoovieListsUrl) return;
    const fetchWatchedUrl = async () => {
      const res = await api.get(user.defaultPrivateMoovieListsUrl, {
        params: {
          search: 'Watched',
        },
      }).then((res) => res.data?.[0]?.url);
      setWatchedUrl(res);
    };
    fetchWatchedUrl();
  }, [user.defaultPrivateMoovieListsUrl]);

  useEffect(() => {
    async function getData() {
      try {
        const data = await MediaService.getMedia({
          type: mediaType,
          page: page,
          pageSize: pagingSizes.FEATURED_MOOVIE_LIST_DEFAULT_TOTAL_CONTENT,
          sortOrder: SortOrder.DESC,
          orderBy: orderBy,
        });
        setFeaturedMedia(data);
        setFeaturedMediaLoading(false);
      } catch (error) {
        setFeaturedMediaError(error);
        setErrorStatus(error.response.status);
        setFeaturedMediaLoading(false);
      }
    }

    getData();
  }, [type, page, setErrorStatus]);

  if (featuredMediaLoading)
    return (
      <div className={'mt-6 d-flex justify-content-center'}>
        <Spinner />
      </div>
    );

  return (
    <div className="default-container moovie-default">
      <h1>{t('featuredLists.featuredList')}</h1>
      <h3>{typeSubtext}</h3>

      <ListContent
        listContent={featuredMedia?.data || []}
        isLoggedIn={isLoggedIn}
        editMode={false}
        username={isLoggedIn ? user.username : null}
        watchedUrl={watchedUrl}
      />
      <div className="flex justify-center pt-4">
        {featuredMedia?.data?.length > 0 && featuredMedia.links?.last?.pageNumber > 1 && (
          <PaginationButton
            page={page}
            lastPage={featuredMedia.links.last.pageNumber}
            setPage={handlePageChange}
          />
        )}
      </div>
    </div>
  );
}

export default FeaturedLists;
