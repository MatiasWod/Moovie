// src/components/listContentPaginated/ListContentPaginated.js

import React from 'react';
import { CardsListOrderBy, CardsListOrderByLabels } from '../../../api/values/CardsListOrderBy';
import '../../views/browseLists.css';
import DropdownMenu from '../dropdownMenu/DropdownMenu';
import ListCard from '../listCard/ListCard';
import PaginationButton from '../paginationButton/PaginationButton';

const ListCardsPaginated = ({
  mlcList,
  page,
  lastPage,
  handlePageChange,
  currentOrderBy,
  setOrderBy,
  currentSortOrder,
  setSortOrder,
}) => {
  return (
    <div>
      <DropdownMenu
        setOrderBy={setOrderBy}
        setSortOrder={setSortOrder}
        currentOrderDefault={currentSortOrder}
        values={Object.values(CardsListOrderBy)}
        labels={CardsListOrderByLabels}
      />

      <div className="list-card-container">
        {Array.isArray(mlcList?.data) && mlcList.data.length > 0
          ? mlcList.data.map((list) => <ListCard key={list.id} listCard={list} />)
          : null}
      </div>

      <div className="flex justify-center pt-4">
        {mlcList?.data?.length > 0 && mlcList.links?.last?.pageNumber > 1 && (
          <PaginationButton page={page} lastPage={lastPage} setPage={handlePageChange} />
        )}
      </div>
    </div>
  );
};

export default ListCardsPaginated;
