import React, { useEffect, useState } from 'react';
import SortOrder from '../../../api/values/SortOrder';
import OrderBy from '../../../api/values/MediaOrderBy';
import ListContentPaginated from '../listContentPaginated/ListContentPaginated';
import userApi from '../../../api/UserApi';
import { Spinner } from 'react-bootstrap';

function ProfileTabMediaLists({ user, search  }) {
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
        let response;
        if(search === 'watched' || search === 'watchlist') {
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

  if (listContentLoading)
    return (
      <div className={'mt-6 d-flex justify-content-center'}>
        <Spinner />
      </div>
    );
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
