import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import { useTranslation } from 'react-i18next';
import { BsEye, BsEyeSlash } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import api from '../../../api/api';
import PagingSizes from '../../../api/values/PagingSizes';
import WatchlistWatched from '../../../api/values/WatchlistWatched';
import { default as ListService, default as listService } from '../../../services/ListService';
import UserService from '../../../services/UserService';
import './listContent.css';

const MediaRow = ({
  position,
  media,
  handleClick,
  handleMouseEnter,
  handleMouseLeave,
  index,
  moveItem,
  editMode,
  pageChange,
  removeFromList,
  isLoggedIn,
  username,
  watchedUrl,
  refresh,
}) => {
  const { user } = useSelector((state) => state.auth);
  const [isDragging, setIsDragging] = useState(false);

  const handleDragStart = (e) => {
    setIsDragging(true);
    e.dataTransfer.setData('mediaId', media.id);
    e.dataTransfer.setData('currentOrder', media.customOrder);
    e.target.classList.add('dragging');
  };

  const handleDragEnd = (e) => {
    setIsDragging(false);
    e.target.classList.remove('dragging');
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    e.currentTarget.classList.add('drag-over');
  };

  const handleDragLeave = (e) => {
    e.currentTarget.classList.remove('drag-over');
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.currentTarget.classList.remove('drag-over');
    const draggedMediaId = e.dataTransfer.getData('mediaId');
    const currentOrder = Number(e.dataTransfer.getData('currentOrder'));
    const targetOrder = media.customOrder;

    if (draggedMediaId !== media.id) {
      moveItem(draggedMediaId, currentOrder, targetOrder);
    }
  };

  const [menuOpen, setMenuOpen] = useState(false);
  const { t } = useTranslation();

  const [ww, setWW] = useState({ watched: false });
  const [refreshWatched, setRefreshWatched] = useState(false);
  useEffect(async () => {
    try {

      // const data = await UserService.currentUserWatchedStatus(
      //   media.id,
      //   user.defaultPrivateMoovieListsUrl
      // );
      if (!watchedUrl) {
        setWW({ watched: false });
        return;
      }
      const data = await api.get(watchedUrl + '/content/' + media.id);
      if (data.status === 200) {
        setWW({ watched: true });
      } else {
        setWW({ watched: false });
      }
    } catch (e) {
      setWW({ watched: false });
    }
  }, [media, refreshWatched, watchedUrl]);

  const handleWatched = async () => {
    try {
      if (ww.watched) {
        await UserService.removeMediaFromWW(
          user.defaultPrivateMoovieListsUrl,
          media.id,
          WatchlistWatched.Watched
        );
      } else {
        await UserService.insertMediaIntoWW(
          user.defaultPrivateMoovieListsUrl,
          media.id,
          WatchlistWatched.Watched
        );
      }
      refresh?.();
    } catch (e) {
      console.log('Error in handleWatched')
    } finally {
      setRefreshWatched(!refreshWatched);
    }
  };

  return (
    <div
      className={`media-row ${isDragging ? 'dragging' : ''}`}
      onMouseEnter={() => handleMouseEnter(media.id)}
      onMouseLeave={handleMouseLeave}
      onClick={() => !editMode && handleClick(media.id)}
      draggable={editMode}
      onDragStart={handleDragStart}
      onDragEnd={handleDragEnd}
      onDragOver={handleDragOver}
      onDragLeave={handleDragLeave}
      onDrop={handleDrop}
      style={{
        cursor: editMode ? 'grab' : 'pointer',
        position: 'relative',
        opacity: isDragging ? 0.5 : 1,
      }}
    >
      <span className="media-position">{position}</span>
      <div className="media-title-section">
        <img className="media-image" src={media.posterPath} alt={media.name} />
        <div className="media-info">
          <span className="media-name">{media.name}</span>
          <span className="media-type">{media.type}</span>
        </div>
      </div>
      <span className="media-score">
        <i className="fas fa-star text-warning me-1"></i>
        {media.tmdbRating}
      </span>
      <span className="media-score">
        <i className="fas fa-users text-info me-1"></i>
        {media.totalRating.toFixed(2)}
      </span>

      <span className="media-release">{new Date(media.releaseDate).getFullYear()}</span>

      {isLoggedIn && !editMode && (
        <span
          className="watched-status-button"
          onClick={(e) => {
            e.stopPropagation();
            handleWatched();
          }}
        >
          {ww.watched ? (
            <BsEye className="fs-5" style={{ color: 'green' }} />
          ) : (
            <BsEyeSlash className="fs-5" style={{ color: 'red' }} />
          )}
        </span>
      )}

      {editMode && (
        <span>
          <div className="menu-container">
            <button
              className="menu-button"
              onClick={(e) => {
                e.stopPropagation();
                setMenuOpen(!menuOpen);
              }}
            >
              <i className="bi bi-three-dots-vertical"></i>
            </button>
            {menuOpen && (
              <div className="menu-dropdown">
                <Button variant="outline-primary" onClick={() => pageChange(1, media.id)}>
                  <i className="fas fa-arrow-right me-2"></i>
                  {t('listContent.nextPage')}
                </Button>
                <Button variant="outline-primary" onClick={() => pageChange(-1, media.id)}>
                  <i className="fas fa-arrow-left me-2"></i>
                  {t('listContent.prevPage')}
                </Button>
                <Button variant="outline-danger" onClick={() => removeFromList(media.id)}>
                  <i className="fas fa-trash me-2"></i>
                  {t('listContent.remove')}
                </Button>
              </div>
            )}
          </div>
        </span>
      )}
    </div>
  );
};

const ListContent = ({
  listContent,
  editMode,
  setCurrentSortOrder,
  listId,
  currentPage,
  Refresh,
  isLoggedIn,
  username,
  listContentUrl,
  watchedUrl,
  refresh,
}) => {
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
    if (to === 1) {
      await ListService.editListContent({
        url: listContentUrl,
        mediaId: mId,
        customOrder: currentPage * PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT + 1,
      });
    } else if (to === -1) {
      await ListService.editListContent({
        url: listContentUrl,
        mediaId: mId,
        customOrder: (currentPage - 1) * PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT,
      });
    }

    Refresh();
  };

  const removeFromList = async (mediaId) => {
    await listService.deleteMediaFromMoovieList({
      url: listContentUrl,
      mediaId: mediaId,
    });
    Refresh();
  };

  const moveItem = async (mediaId, fromOrder, toOrder) => {
    try {
      console.log(listContentUrl)
      await ListService.editListContent({
        url: listContentUrl,
        mediaId: mediaId,
        customOrder: toOrder,
      });
      Refresh();
    } catch (error) {
      console.error('Error moving item:', error);
    }
  };

  if (!listContent || listContent.length === 0) {
    return <div className="empty-list">{t('listContent.emptyMessage')}</div>;
  }

  return (
    <div className="list-content">
      <div className="media-header">
        <span>#</span>
        <span>{t('listContent.title')}</span>
        <span>
          <i className="fas fa-star text-warning me-1"></i>
          {t('listContent.tmdbScore')}
        </span>
        <span>
          <i className="fas fa-users text-info me-1"></i>
          {t('listContent.userScore')}
        </span>
        <span>
          <i className="fas fa-calendar me-1"></i>
          {t('listContent.year')}
        </span>
        {isLoggedIn && !editMode && <span>{t('profile.watched')}</span>}
      </div>
      <div className="media-list">
        {listContent.map((media, index) => (
          <MediaRow
            key={media.id}
            position={editMode ? media.customOrder : index + 1}
            media={media}
            handleClick={handleClick}
            handleMouseEnter={handleMouseEnter}
            handleMouseLeave={handleMouseLeave}
            index={index}
            moveItem={moveItem}
            editMode={editMode}
            pageChange={pageChange}
            removeFromList={removeFromList}
            isLoggedIn={isLoggedIn}
            username={username}
            watchedUrl={watchedUrl}
            refresh={refresh}
          />
        ))}
      </div>
    </div>
  );
};

export default ListContent;
