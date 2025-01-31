import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import "./listContent.css";
import ListService from "../../../services/ListService";
import SortOrder from "../../../api/values/SortOrder";
import PagingSizes from "../../../api/values/PagingSizes";
import listService from "../../../services/ListService";
import Button from "react-bootstrap/Button";

const MediaRow = ({
                      position, media, handleClick, handleMouseEnter, handleMouseLeave,
                      index, moveItem, editMode, pageChange, removeFromList
                  }) => {
    const handleDragStart = (e) => {
        e.dataTransfer.setData("index", index);
    };

    const handleDragOver = (e) => {
        e.preventDefault();
    };

    const handleDrop = (e) => {
        const fromIndex = e.dataTransfer.getData("index");
        moveItem(Number(fromIndex), index);
    };

    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <div
            className="media-row"
            onMouseEnter={() => handleMouseEnter(media.id)}
            onMouseLeave={handleMouseLeave}
            onClick={() => !editMode && handleClick(media.id)}
            draggable={editMode}
            onDragStart={handleDragStart}
            onDragOver={handleDragOver}
            onDrop={handleDrop}
            style={{ cursor: editMode ? "grab" : "pointer", position: "relative" }}
        >
            <span className="media-position">{position}</span>
            <img className="media-image" src={media.posterPath} alt={media.name} />
            <div className="media-info">
                <span className="media-name">{media.name}</span>
                <span className="media-type">{media.type}</span>
            </div>
            <span className="media-score">{media.tmdbRating} ★</span>
            <span className="media-score">{media.totalRating} ☆</span>
            <span className="media-release">{new Date(media.releaseDate).getFullYear()}</span>

            {editMode && (
                <div className="menu-container">
                    <button className="menu-button" onClick={() => setMenuOpen(!menuOpen)}>⋮</button>
                    {menuOpen && (
                        <div className="menu-dropdown">
                            <Button onClick={() => pageChange(1, media.id)}>Send to next page</Button>
                            <Button onClick={() => pageChange(-1, media.id)}>Send to prev page</Button>
                            <Button onClick={() => removeFromList(media.id)}>Delete from list</Button>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

const ListContent = ({ listContent, editMode, Refresh, listId, currentPage}) => {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const [hoveredId, setHoveredId] = useState(null);

    const handleClick = (id) => {
        if (!editMode) {
            navigate(`/details/${id}`);
        }
    };

    const handleMouseEnter = (id) => setHoveredId(id);
    const handleMouseLeave = () => setHoveredId(null);

    //to === -1 if to prev to === 1 if next page
    const pageChange = async (to, mId) => {
        console.log(currentPage);
        console.log((currentPage - 1 + to) * PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT);
        if(to===1){
            await ListService.editListContent({
                mediaId: mId,
                listId: listId,
                customOrder: (currentPage) * PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT + 1
            });
        } else if (to===-1){
            await ListService.editListContent({
                mediaId: mId,
                listId: listId,
                customOrder: (currentPage-1) * PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT
            });
        }

        Refresh();
    }

    const removeFromList = async (mediaId) => {
        await listService.deleteMediaFromMoovieList({
            id: listId,
            mediaId: mediaId
        });
        Refresh();

    }

    const moveItem = async (fromIndex, toIndex) => {
        await ListService.editListContent({
            mediaId: listContent[fromIndex].id,
            listId: listId,
            customOrder: listContent[toIndex].customOrder
        }
        );
        Refresh();
    };

    if (!listContent || listContent.length === 0) {
        return <div className="empty-list">{t("listContent.emptyMessage")}</div>;
    }

    return (
        <div className="list-content">
            <div className="media-header">
                <span>#</span>
                <span>{t("listContent.title")}</span>
                <span>{t("listContent.type")}</span>
                <span>{t("listContent.score")}</span>
                <span>{t("listContent.usersScore")}</span>
                <span>{t("listContent.releaseDate")}</span>
            </div>
            <div className="media-list">
                {listContent.map((media, index) => (
                    <MediaRow
                        key={media.id}
                        position={(editMode) ? media.customOrder : index + 1}
                        media={media}
                        handleClick={handleClick}
                        handleMouseEnter={handleMouseEnter}
                        handleMouseLeave={handleMouseLeave}
                        index={index}
                        moveItem={moveItem}
                        editMode={editMode}
                        pageChange={pageChange}
                        removeFromList={removeFromList}
                    />
                ))}
            </div>
        </div>
    );
};

export default ListContent;
