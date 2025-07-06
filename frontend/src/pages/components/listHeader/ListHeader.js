import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import api from '../../../api/api';
import MoovieListTypes from '../../../api/values/MoovieListTypes';
import EditListForm from '../forms/editListForm/editListForm';
import './listHeader.css';

const ListHeader = ({
  list,
  updateHeader,
  onDelete,
  onReport,
  showDeleteConfirmation,
  setShowDeleteConfirmation,
  showReportForm,
  setShowReportForm,
}) => {
  const { t } = useTranslation();
  const { isLoggedIn, user } = useSelector((state) => state.auth);
  const navigate = useNavigate();

  const [hasLikedAndFollowed, setHasLikedAndFollowed] = useState({
    liked: false,
    followed: false,
  });

  const [ping, setPing] = useState(false);

  useEffect(() => {
    const fetchHasLikedAndFollowed = async () => {
      try {
        const [liked, followed] = await Promise.all([
          api.get(list.likesUrl + '/' + user.username),
          api.get(list.followersUrl + '/' + user.username),
        ]);
        setHasLikedAndFollowed({
          liked: liked.status === 200,
          followed: followed.status === 200,
        });
      } catch (error) {
        setHasLikedAndFollowed({
          liked: false,
          followed: false,
        });
      }
    };

    fetchHasLikedAndFollowed();
  }, [list.id, ping]);

  const handleLike = async () => {
    try {
      if (!isLoggedIn) {
        navigate('/login');
      }
      if (hasLikedAndFollowed.liked) {
        await api.delete(list.likesUrl);
      } else {
        await api.post(list.likesUrl);
      }
      setPing(!ping);
    } catch (error) {
      // Handle error
    }
  };

  const handleFollow = async () => {
    try {
      if (!isLoggedIn) {
        navigate('/login');
      }
      if (hasLikedAndFollowed.followed) {
        await api.delete(list.followersUrl);
      } else {
        await api.post(list.followersUrl);
      }
      setPing(!ping);
    } catch (error) {
      // Handle error
    }
  };

  const [editList, setEditList] = useState(false);
  const handleOpenEdit = () => {
    setEditList(true);
  };

  const handleCloseEdit = () => {
    setEditList(false);
  };

  const handleCloseEditSucccess = () => {
    setEditList(false);
    updateHeader();
  };

  return (
    <div className="list-header">
      {list.images && list.images.length > 0 ? (
        <div
          className="list-header-image"
          style={{ backgroundImage: `url(${list.images[0]})` }}
        ></div>
      ) : null}
      <div className="list-header-content">
        <div className="list-header-actions">
          {isLoggedIn && (
            <>
              {user.username !== list.createdBy && (
                <button className="report-button" onClick={() => setShowReportForm(true)}>
                  <i className="bi bi-flag"></i>
                </button>
              )}
              {user.username === list.createdBy && (
                <>
                  <button className="delete-button" onClick={() => setShowDeleteConfirmation(true)}>
                    <i className="bi bi-trash"></i>
                  </button>
                  <button className="edit-list-button" onClick={handleOpenEdit}>
                    {t('listHeader.edit')}
                  </button>
                </>
              )}
            </>
          )}
        </div>
        <h1 className="list-header-title">{list.name}</h1>
        <p className="list-header-description">{list.description}</p>
        <span className="list-header-username">
          {t('listHeader.by')}{' '}
          <div
            style={{ cursor: 'pointer', color: 'aliceblue' }}
            onClick={() => navigate(`/profile/${list.createdBy}`)}
          >
            {list.createdBy}
          </div>
        </span>
        <div className="list-header-buttons">
          {list.type !== MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type &&
            list.type !== MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type && (
              <button
                className={`like-button ${hasLikedAndFollowed.liked ? t('listHeader.liked') : ''}`}
                onClick={handleLike}
              >
                {hasLikedAndFollowed.liked ? t('listHeader.dislike') : t('listHeader.like')}
              </button>
            )}
          {list.type !== MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type &&
            list.type !== MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type && (
              <button
                className={`follow-button ${hasLikedAndFollowed.followed ? t('listHeader.followed') : ''}`}
                onClick={handleFollow}
              >
                {hasLikedAndFollowed.followed ? t('listHeader.unfollow') : t('listHeader.follow')}
              </button>
            )}
        </div>
      </div>
      {editList && (
        <div className="overlay">
          <EditListForm
            listName={list.name}
            listUrl={list.url}
            listDescription={list.description}
            closeEdit={handleCloseEdit}
            closeEditSuccess={handleCloseEditSucccess}
          />
        </div>
      )}
    </div>
  );
};

export default ListHeader;
