import React, { useEffect, useState } from 'react';
import SortOrder from '../../../api/values/SortOrder';
import OrderBy from '../../../api/values/MediaOrderBy';
import UserApi from '../../../api/UserApi';
import ListService from '../../../services/ListService';
import pagingSizes from '../../../api/values/PagingSizes';
import ListContentPaginated from '../listContentPaginated/ListContentPaginated';
import CardsListOrderBy from '../../../api/values/CardsListOrderBy';
import MoovieListTypes from '../../../api/values/MoovieListTypes';
import ListCardsPaginated from '../ListCardsPaginated/ListCardsPaginated';
import UserService from '../../../services/UserService';
import { Spinner } from 'react-bootstrap';
import userApi from '../../../api/UserApi';
import { parsePaginatedResponse } from '../../../utils/ResponseUtils';

function ProfileTabMoovieLists({ user, search }) {
  const [orderBy, setOrderBy] = useState(CardsListOrderBy.LIKE_COUNT);
  const [sortOrder, setSortOrder] = useState(SortOrder.DESC);
  const [page, setPage] = useState(1);

  const [lists, setLists] = useState(undefined);
  const [listsLoading, setListsLoading] = useState(true);
  const [listError, setListError] = useState(false);

  useEffect(() => {
    async function getData() {
      try {
        console.log('ProfileTabMoovieLists', user, search);
        setListsLoading(true);
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
        setListError(true);
      } finally {
        setListsLoading(false);
      }
    }
    getData();
  }, [orderBy, sortOrder, page, user, search, pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS]);

  if (listsLoading)
    return (
      <div className={'mt-6 d-flex justify-content-center'}>
        <Spinner />
      </div>
    );

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
