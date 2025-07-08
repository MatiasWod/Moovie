import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { BsBookmark, BsBookmarkDash, BsEye, BsEyeSlash } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import api from '../../../api/api';
import WatchlistWatched from '../../../api/values/WatchlistWatched';
import UserService from '../../../services/UserService';
import '../mainStyle.css';
import './mediaCard.css';

const MediaCard = ({ media, size = 'normal', showWWButtons = true, disableOnClick = false, watchedUrl, watchlistUrl }) => {
  const releaseDate = new Date(media.releaseDate).getFullYear();

  const [ww, setWW] = useState({ watched: false, watchlist: false });
  const { isLoggedIn, user } = useSelector((state) => state.auth);
  const [ping, setPing] = useState(false);
  const { t } = useTranslation();

  const fetchStatus = async (axiosPromise) => {
    try {
      const response = await axiosPromise;
      return response.status === 200;
    } catch(e){
      return false;
    }
  }

  useEffect(() => {
    const fetchWW = async () => {
      try {
        if (watchedUrl && watchlistUrl){
          if (watchedUrl === 'loading' || watchlistUrl === 'loading') return;
          let watched = false;
          let watchlist = false;
          const [watchedResp, watchlistResp] = await Promise.all([
            fetchStatus(api.get(watchedUrl + '/content/' + media.id)),
            fetchStatus(api.get(watchlistUrl + '/content/' + media.id))
          ]);
          if (watchedResp) {
            watched = true;
          }
          if (watchlistResp) {
            watchlist = true;
          }
          console.log('WW', { watched, watchlist });
          setWW({ watched, watchlist });
          return;
        }
        
        const WW = await UserService.currentUserWWStatus(
          media.id,
          user.defaultPrivateMoovieListsUrl
        );
        setWW(WW);
      } catch (error) {}
    };

    fetchWW();
  }, [media.id, ping, watchedUrl, watchlistUrl]);

  const navigate = useNavigate();
  const [hovered, setHovered] = useState(false);

  const handleMouseEnter = () => {
    setHovered(true);
  };

  const handleMouseLeave = () => {
    setHovered(false);
  };

  const handleClick = () => {
    if (!disableOnClick) {
      navigate(`/details/${media.id}`);
    }
  };

  const handleWatched = async () => {
    try {
      if (!isLoggedIn) {
        navigate('/login');
        return;
      }

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
      setPing(!ping);
    } catch (error) {}
  };

  const handleWatchlist = async () => {
    try {
      if (!isLoggedIn) {
        navigate('/login');
        return;
      }

      if (ww.watchlist) {
        await UserService.removeMediaFromWW(
          user.defaultPrivateMoovieListsUrl,
          media.id,
          WatchlistWatched.Watchlist
        );
      } else {
        await UserService.insertMediaIntoWW(
          user.defaultPrivateMoovieListsUrl,
          media.id,
          WatchlistWatched.Watchlist
        );
      }
      setPing(!ping);
    } catch (error) {}
  };

  const sizeClasses = {
    normal: 'media-card',
    small: 'media-card media-card-small',
  };

  return (
    <div
      className={`${sizeClasses[size]} shadow ${hovered ? 'hovered' : ''}`}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
      onClick={handleClick}
    >
      <div className="media-card-border">
        <img
          className="media-card-image"
          src={media.posterPath}
          alt={media.name}
          onClick={handleClick}
        />
        <ReactTooltip
          id={`watched-tooltip-${media.id}`}
          place="bottom"
          type="dark"
          effect="solid"
        />
        <ReactTooltip
          id={`watchlist-tooltip-${media.id}`}
          place="bottom"
          type="dark"
          effect="solid"
        />{' '}
        {hovered && (
          <div className="media-card-overlay">
            <h4 className="media-card-title">{media.name}</h4>
            <h5>{releaseDate}</h5>
            <h5>{media.tmdbRating}⭐</h5>

            {showWWButtons && (
              <div className="media-card-buttons">
                <button
                  className="media-card-button"
                  data-tooltip-id={`watched-tooltip-${media.id}`}
                  data-tooltip-content={
                    ww.watched ? t('mediaCard.removeFromWatched') : t('mediaCard.addToWatched')
                  }
                  onClick={(e) => {
                    e.stopPropagation();
                    handleWatched();
                  }}
                >
                  {ww.watched ? <BsEyeSlash className="fs-5" /> : <BsEye className="fs-5" />}
                </button>
                <button
                  className="media-card-button"
                  data-tooltip-id={`watchlist-tooltip-${media.id}`}
                  data-tooltip-content={
                    ww.watchlist
                      ? t('mediaCard.removeFromWatchlist')
                      : t('mediaCard.addToWatchlist')
                  }
                  onClick={(e) => {
                    e.stopPropagation();
                    handleWatchlist();
                  }}
                >
                  {ww.watchlist ? (
                    <BsBookmarkDash className="fs-5" />
                  ) : (
                    <BsBookmark className="fs-5" />
                  )}
                </button>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default MediaCard;
