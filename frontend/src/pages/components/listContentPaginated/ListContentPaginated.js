// src/components/listContentPaginated/ListContentPaginated.js

import React, {useState} from "react";
import ListContent from "../listContent/ListContent";
import PaginationButton from "../paginationButton/PaginationButton";
import DropdownMenu from "../dropdownMenu/DropdownMenu";
import MediaOrderBy from "../../../api/values/MediaOrderBy";
import Button from "react-bootstrap/Button";
import {useSelector} from "react-redux";
import SortOrder from "../../../api/values/SortOrder";
import {useTranslation} from "react-i18next";

const ListContentPaginated = ({
                                  listContent,
                                  page,
                                  lastPage,
                                  handlePageChange,
                                  currentOrderBy,
                                  setOrderBy,
                                  currentSortOrder,
                                  setSortOrder,
                                  setListContent,
                                  isOwner,
                                  listId
                              }) => {

    const [editMode, setEditMode] = useState(false);
    const {t} = useTranslation();

    const handleEditMode = () =>{
        setEditMode(!editMode);
        setOrderBy(MediaOrderBy.CUSTOM_ORDER);
        setSortOrder(SortOrder.ASC);
    }

    return (
        <div>
            {
                !editMode && (
                    <DropdownMenu
                        setOrderBy={setOrderBy}
                        setSortOrder={setSortOrder}
                        currentOrderDefault={currentSortOrder}
                        values={Object.values(MediaOrderBy)}
                    />
                )
            }



            {isOwner && (
                !editMode ? (<Button onClick={handleEditMode}>{t('listContentPaginated.edit')}</Button>) :
                    (<Button onClick={handleEditMode}>{t('listContentPaginated.save')}</Button>)
            ) }

            <ListContent listContent={listContent?.data ?? []} editMode={editMode}
                         setCurrentSortOrder={setSortOrder} listId={listId} setCurre/>

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
