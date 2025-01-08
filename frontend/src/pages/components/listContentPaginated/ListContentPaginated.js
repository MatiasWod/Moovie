// src/components/listContentPaginated/ListContentPaginated.js

import React from "react";
import ListContent from "../listContent/ListContent";
import PaginationButton from "../paginationButton/PaginationButton";
import DropdownMenu from "../dropdownMenu/DropdownMenu";
import MediaOrderBy from "../../../api/values/MediaOrderBy";

const ListContentPaginated = ({
                                  listContent,
                                  page,
                                  lastPage,
                                  handlePageChange,
                                  currentOrderBy,
                                  setOrderBy,
                                  currentSortOrder,
                                  setSortOrder
                              }) => {
    return (
        <div>
            <DropdownMenu
                setOrderBy={setOrderBy}
                setSortOrder={setSortOrder}
                currentOrderDefault={currentSortOrder}
                values={Object.values(MediaOrderBy)}
            />

            <ListContent listContent={listContent?.data ?? []} />


            <div className="flex justify-center pt-4">
                {listContent?.data?.length > 0 && listContent.links?.last?.page > 1 && (
                    <PaginationButton
                        page={page}
                        lastPage={lastPage}
                        setPage={handlePageChange}
                    />
                )}
            </div>
        </div>
    );
};

export default ListContentPaginated;
