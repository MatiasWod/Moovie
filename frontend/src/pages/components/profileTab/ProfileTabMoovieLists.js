import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import userApi from '../../../api/UserApi';
import { CardsListOrderBy } from '../../../api/values/CardsListOrderBy';
import pagingSizes from '../../../api/values/PagingSizes';
import SortOrder from '../../../api/values/SortOrder';
import { parsePaginatedResponse } from '../../../utils/ResponseUtils';
import ListCardsPaginated from '../ListCardsPaginated/ListCardsPaginated';
import EmptyState from '../ReportsLists/EmptyState';
import EmptyStateWithActions from '../ReportsLists/EmptyStateWithActions';
import ErrorState from '../ReportsLists/ErrorState';
import LoadingState from '../ReportsLists/LoadingState';

function ProfileTabMoovieLists({ user, search, isMe }) {
  const { t } = useTranslation();
  const [orderBy, setOrderBy] = useState(CardsListOrderBy.LIKE_COUNT);
  const [sortOrder, setSortOrder] = useState(SortOrder.DESC);
  const [page, setPage] = useState(1);

  const [lists, setLists] = useState(undefined);
  const [listsLoading, setListsLoading] = useState(true);
  const [listError, setListError] = useState(false);

  const getLoadingMessage = () => {
    switch (search) {
      case 'public-lists': return t('profile.loading.publicLists');
      case 'private-lists': return t('profile.loading.privateLists');
      case 'liked-lists': return t('profile.loading.likedLists');
      case 'followed-lists': return t('profile.loading.followedLists');
      default: return t('loader.loading');
    }
  };

  const getEmptyTitle = () => {
    switch (search) {
      case 'public-lists': return t('profile.empty.publicLists');
      case 'private-lists': return t('profile.empty.privateLists');
      case 'liked-lists': return t('profile.empty.likedLists');
      case 'followed-lists': return t('profile.empty.followedLists');
      default: return t('list.emptyMessage');
    }
  };

  const getEmptyMessage = () => {
    if (isMe) {
      switch (search) {
        case 'public-lists': return t('profile.currentUser.emptyMessage.publicLists');
        case 'private-lists': return t('profile.currentUser.emptyMessage.privateLists');
        case 'liked-lists': return t('profile.currentUser.emptyMessage.likedLists');
        case 'followed-lists': return t('profile.currentUser.emptyMessage.followedLists');
        default: return t('listContent.emptyMessage');
      }
    } else {
      switch (search) {
        case 'public-lists': return t('profile.emptyMessage.publicLists');
        case 'private-lists': return t('profile.emptyMessage.privateLists');
        case 'liked-lists': return t('profile.emptyMessage.likedLists');
        case 'followed-lists': return t('profile.emptyMessage.followedLists');
        default: return t('listContent.emptyMessage');
      }
    }
  };

  const getErrorTitle = () => {
    switch (search) {
      case 'public-lists': return t('profile.error.publicLists');
      case 'private-lists': return t('profile.error.privateLists');
      case 'liked-lists': return t('profile.error.likedLists');
      case 'followed-lists': return t('profile.error.followedLists');
      default: return 'Error loading lists';
    }
  };

  const getActionsForCurrentUser = () => {
    switch (search) {
      case 'public-lists':
      case 'private-lists':
        return [
          { 
            label: t('profile.actions.createList'), 
            icon: 'bi-plus-circle', 
            path: '/createList', 
            primary: true 
          },
          { 
            label: t('profile.actions.browseLists'), 
            icon: 'bi-collection', 
            path: '/browseLists', 
            primary: false 
          }
        ];
      case 'liked-lists':
      case 'followed-lists':
        return [
          { 
            label: t('profile.actions.browseLists'), 
            icon: 'bi-collection', 
            path: '/browseLists', 
            primary: true 
          },
          { 
            label: t('profile.actions.discover'), 
            icon: 'bi-compass', 
            path: '/discover', 
            primary: false 
          }
        ];
      default:
        return [];
    }
  };

  useEffect(() => {
    async function getData() {
      try {
        console.log('ProfileTabMoovieLists', user, search);
        setListsLoading(true);
        setListError(false);
        let initialData, data;
        if (search === 'public-lists') {
          initialData = await userApi.getProfileListsFromUser(
            user.publicMoovieListsUrl,
            orderBy,
            sortOrder,
            page
          );
          console.log('Public lists data:', data);
        } else if (search === 'private-lists') {
          initialData = await userApi.getProfileListsFromUser(
            user.privateMoovieListsUrl,
            orderBy,
            sortOrder,
            page
          );
        } else if (search === 'liked-lists') {
          initialData = await userApi.getProfileListsFromUser(
            user.likedMoovieListsUrl,
            orderBy,
            sortOrder,
            page
          );
        } else if (search === 'followed-lists') {
          initialData = await userApi.getProfileListsFromUser(
            user.followedMoovieListsUrl,
            orderBy,
            sortOrder,
            page
          );
        }
        data = parsePaginatedResponse(initialData);
        console.log('Fetched lists:', initialData);
        setLists(data);
        setListError(false);
      } catch (error) {
        console.error('Error fetching data:', error);
        setLists(null);
        setListError(error);
      } finally {
        setListsLoading(false);
      }
    }
    getData();
  }, [orderBy, sortOrder, page, user, search, pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS]);

  if (listsLoading) {
    return <LoadingState message={getLoadingMessage()} />;
  }

  if (listError) {
    return (
      <ErrorState
        title={getErrorTitle()}
        message={listError.message || t('reports.content.description')}
      />
    );
  }

  if (!lists?.data || lists.data.length === 0) {
    if (isMe) {
      // Show enhanced empty state with CTAs for current user
      return (
        <EmptyStateWithActions
          title={getEmptyTitle()}
          message={getEmptyMessage()}
          actions={getActionsForCurrentUser()}
        />
      );
    } else {
      // Show regular empty state for other users
      return (
        <EmptyState
          title={getEmptyTitle()}
          message={getEmptyMessage()}
        />
      );
    }
  }

  return (
    <ListCardsPaginated
      mlcList={lists}
      page={page}
      lastPage={lists?.links?.last?.pageNumber}
      handlePageChange={setPage}
      currentOrderBy={orderBy}
      setOrderBy={setOrderBy}
      currentSortOrder={sortOrder}
      setSortOrder={setSortOrder}
    />
  );
}

export default ProfileTabMoovieLists;
