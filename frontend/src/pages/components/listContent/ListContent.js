import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import "./listContent.css";
import ListService from "../../../services/ListService";

const MediaRow = ({
                      position, media, handleClick, handleMouseEnter, handleMouseLeave,
                      index, moveItem, editMode
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
            style={{ cursor: editMode ? "grab" : "pointer" }}
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
        </div>
    );
};

const ListContent = ({ listContent, editMode, setListContent, listId }) => {
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

    const moveItem = async (fromIndex, toIndex) => {
        await ListService.editListContent({
            mediaId: listContent[fromIndex].id,
            listId: listId,
            customOrder: listContent[toIndex].customOrder
        }
    )
        // const updatedList = [...listContent];
        // const [movedItem] = updatedList.splice(fromIndex, 1);
        // console.log(listContent);
        // updatedList.splice(toIndex, 0, movedItem);
        // console.log(listContent);
        // setListContent(updatedList)
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
                    />
                ))}
            </div>
        </div>
    );
};

export default ListContent;
