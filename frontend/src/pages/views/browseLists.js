import React, { useEffect, useState } from 'react';
import { Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { createSearchParams, useNavigate, useSearchParams } from 'react-router-dom';
import { CardsListOrderBy, CardsListOrderByLabels } from '../../api/values/CardsListOrderBy';
import pagingSizes from '../../api/values/PagingSizes';
import SortOrder from '../../api/values/SortOrder';
import ListService from '../../services/ListService';
import BrowseListsSearchBar from '../components/browseListsSearchBar/browseListsSearchBar';
import DropdownMenu from '../components/dropdownMenu/DropdownMenu';
import ListCard from '../components/listCard/ListCard';
import '../components/listContent/listContent.css';
import PaginationButton from '../components/paginationButton/PaginationButton';
import './../components/mainStyle.css';
import './browseLists.css';

function BrowseLists() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { t } = useTranslation();

  const [search, setSearch] = useState(searchParams.get('search') || '');
  const [ownerUsername, setOwnerUsername] = useState(null);
  const [type, setType] = useState(null);
  const [orderBy, setOrderBy] = useState(CardsListOrderBy.LIKE_COUNT);
  const [sortOrder, setSortOrder] = useState(SortOrder.DESC);
  const [page, setPage] = useState(Number(searchParams.get('page')) || 1);

  const [mlcList, setMlcList] = useState(undefined);
  const [mlcListLoading, setMlcListLoading] = useState(true);
  const [mlcListError, setMlcListError] = useState(null);

  useEffect(() => {
    const searchParam = searchParams.get('search') || '';
    const pageParam = searchParam !== search ? 1 : Number(searchParams.get('page')) || 1;

    setSearch(searchParam);
    setPage(pageParam);
  }, [searchParams]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
    navigate({
      pathname: `/browselists`,
      search: createSearchParams({
        search: search,
        orderBy: orderBy,
        sortOrder: sortOrder,
        page: newPage.toString(),
      }).toString(),
    });
  };

  useEffect(() => {
    navigate({
      pathname: `/browselists`,
      search: createSearchParams({
        search: search,
        orderBy: orderBy,
        sortOrder: sortOrder,
        page: page.toString(),
      }).toString(),
    });
  }, [search, orderBy, sortOrder, page]);

  useEffect(() => {
    async function getData() {
      try {
        const data = await ListService.getLists({
          orderBy: orderBy,
          ownerUsername: ownerUsername,
          pageNumber: page,
          pageSize: pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS,
          search: search,
          type: type,
          order: sortOrder,
        });
        setMlcList(data);
        setMlcListLoading(false);
      } catch (error) {
        setMlcListError(error);
        setMlcListLoading(false);
      }
    }
    getData();
  }, [search, orderBy, sortOrder, page]);

  if (mlcListLoading)
    return (
      <div className={'mt-6 d-flex justify-content-center'}>
        <Spinner />
      </div>
    );

  return (
    <div className="moovie-default default-container">
      <div className="browse-lists-header">
        <div className="title">{t('browseLists.communityLists')}</div>

        <div className="browse-list-header-searchable">
          <BrowseListsSearchBar />
          <div style={{ display: 'flex', float: 'right', marginLeft: '10px' }}>
            <DropdownMenu
              setOrderBy={setOrderBy}
              setSortOrder={setSortOrder}
              currentOrderDefault={sortOrder}
              values={Object.values(CardsListOrderBy)}
              labels={CardsListOrderByLabels}
            />
          </div>
        </div>
      </div>

      <div className="list-card-container">
        {mlcList?.data?.map((list) => (
          <ListCard listCard={list} />
        ))}
      </div>

      <div className="flex justify-center pt-4">
        {mlcList?.data?.length > 0 && mlcList.links?.last?.pageNumber > 1 && (
          <PaginationButton
            page={page}
            lastPage={mlcList.links.last.pageNumber}
            setPage={handlePageChange}
          />
        )}
      </div>
    </div>
  );
}

export default BrowseLists;
