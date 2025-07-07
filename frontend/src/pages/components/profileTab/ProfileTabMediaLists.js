import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import userApi from '../../../api/UserApi';
import { MediaOrderBy as OrderBy } from '../../../api/values/MediaOrderBy';
import SortOrder from '../../../api/values/SortOrder';
import ListContentPaginated from '../listContentPaginated/ListContentPaginated';
import EmptyState from '../ReportsLists/EmptyState';
import EmptyStateWithActions from '../ReportsLists/EmptyStateWithActions';
import ErrorState from '../ReportsLists/ErrorState';
import LoadingState from '../ReportsLists/LoadingState';

function ProfileTabMediaLists({ user, search, isMe }) {
  const { t } = useTranslation();
  const [currentOrderBy, setOrderBy] = useState(OrderBy.CUSTOM_ORDER);
  const [currentSortOrder, setSortOrder] = useState(SortOrder.DESC);
  const [page, setPage] = useState(1);

  const [listContent, setListContent] = useState(undefined);
  const [listPagination, setListPagination] = useState(undefined);
  const [listContentLoading, setListContentLoading] = useState(true);
  const [listContentError, setListContentError] = useState(false);

  useEffect(() => {
    async function getData() {
      try {
        setListContentLoading(true);
        setListContentError(false);
        let response;
        if (search === 'watched' || search === 'watchlist') {
          response = await userApi.getSpecialListFromUser(
            user.defaultPrivateMoovieListsUrl,
            currentOrderBy,
            currentSortOrder,
            page,
            search
          );
        }
        setListPagination(response);
        setListContent(response);
        setListContentLoading(false);
      } catch (error) {
        setListContentError(error);
        setListContentLoading(false);
      }
    }
    getData();
  }, [search, user, currentOrderBy, currentSortOrder, page]);

  if (listContentLoading) {
    return (
      <LoadingState 
        message={search === 'watched' ? t('profile.loading.watched') : t('profile.loading.watchlist')} 
      />
    );
  }

  if (listContentError) {
    return (
      <ErrorState
        title={search === 'watched' ? t('profile.error.watched') : t('profile.error.watchlist')}
        message={listContentError.message || t('reports.content.description')}
      />
    );
  }

  if (!listContent?.data || listContent.data.length === 0) {
    if (isMe) {
      // Show enhanced empty state with CTAs for current user
      const actions = [];
      
      if (search === 'watched') {
        actions.push(
          { 
            label: t('profile.actions.discover'), 
            icon: 'bi-compass', 
            path: '/discover', 
            primary: true 
          }
        );
      } else if (search === 'watchlist') {
        actions.push(
          { 
            label: t('profile.actions.discover'), 
            icon: 'bi-compass', 
            path: '/discover', 
            primary: true 
          },
          { 
            label: t('profile.actions.browseLists'), 
            icon: 'bi-collection', 
            path: '/browseLists', 
            primary: false 
          }
        );
      }

      return (
        <EmptyStateWithActions
          title={search === 'watched' ? t('profile.empty.watched') : t('profile.empty.watchlist')}
          message={search === 'watched' ? t('profile.currentUser.emptyMessage.watched') : t('profile.currentUser.emptyMessage.watchlist')}
          actions={actions}
        />
      );
    } else {
      // Show regular empty state for other users
      return (
        <EmptyState
          title={search === 'watched' ? t('profile.empty.watched') : t('profile.empty.watchlist')}
          message={search === 'watched' ? t('profile.emptyMessage.watched') : t('profile.emptyMessage.watchlist')}
        />
      );
    }
  }

  return (
    <ListContentPaginated
      listContent={listContent}
      page={page}
      lastPage={listPagination?.last?.pageNumber}
      handlePageChange={setPage}
      currentOrderBy={currentOrderBy}
      setOrderBy={setOrderBy}
      currentSortOrder={currentSortOrder}
      setSortOrder={setSortOrder}
    />
  );
}

export default ProfileTabMediaLists;
