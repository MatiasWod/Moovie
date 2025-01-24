import React, { useEffect, useState } from 'react';
import './mediaCard.css';
import { useNavigate } from "react-router-dom";
import "../mainStyle.css";
import mediaService from "../../../services/MediaService";
import { useSelector } from "react-redux";
import WatchlistWatched from "../../../api/values/WatchlistWatched";
import { Tooltip as ReactTooltip } from 'react-tooltip'
import {useTranslation} from "react-i18next";

const MediaCard = ({ media }) => {
    const releaseDate = new Date(media.releaseDate).getFullYear();

    const [ww, setWW] = useState({ watched: false, watchlist: false });
    const { isLoggedIn, user } = useSelector(state => state.auth);
    const [ping, setPing] = useState(false);
    const { t } = useTranslation();

    useEffect(() => {
        const fetchWW = async () => {
            try {
                const WW = await mediaService.currentUserWWStatus(media.id, user.username);
                setWW(WW);
             } catch (error) {
            }
        };

        fetchWW();
    }, [media.id, ping]);

    const navigate = useNavigate();
    const [hovered, setHovered] = useState(false);

    const handleMouseEnter = () => {
        setHovered(true);
    };

    const handleMouseLeave = () => {
        setHovered(false);
    };

    const handleClick = () => {
        navigate(`/details/${media.id}`);
    };

    const handleWatched = async () => {
        try {
            if (ww.watched) {
                await mediaService.removeMediaFromWW(WatchlistWatched.Watched, media.id, user.username);
            } else {
                await mediaService.insertMediaIntoWW(WatchlistWatched.Watched, media.id, user.username);
            }
            setPing(!ping)
        }catch(error){
        }
    };

    const handleWatchlist = async () => {
        try {
            if (ww.watchlist) {
                await mediaService.removeMediaFromWW(WatchlistWatched.Watchlist, media.id, user.username);
            } else {
                await mediaService.insertMediaIntoWW(WatchlistWatched.Watchlist, media.id, user.username);
            }
            setPing(!ping)
        }catch(error){
        }
    };

    return (
        <div
            className={`media-card shadow ${hovered ? 'hovered' : ''}`}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            onClick={handleClick}
        >
            <div className="media-card-border">
                <img className="media-card-image" src={media.posterPath} alt={media.name} onClick={handleClick} />
                <ReactTooltip id={`watched-tooltip-${media.id}`} place="bottom" type="dark" effect="solid" />
                <ReactTooltip id={`watchlist-tooltip-${media.id}`} place="bottom" type="dark" effect="solid" />                {hovered && (
                    <div className="media-card-overlay">
                        <h4 className="media-card-title">{media.name}</h4>
                        <h5>{releaseDate}</h5>
                        <h5>{media.tmdbRating}⭐</h5>

                        <div className="media-card-buttons">
                            <button className="media-card-button"
                                    data-tooltip-id={`watched-tooltip-${media.id}`}
                                    data-tooltip-content={ww.watched ? t('mediaCard.removeFromWatched') : t('mediaCard.addToWatched')}
                                    onClick={(e) => { e.stopPropagation(); handleWatched(); }}>
                                {ww.watched ? "👁️" : "X👁️"}
                            </button>
                            <button className="media-card-button"
                                    data-tooltip-id={`watchlist-tooltip-${media.id}`}
                                    data-tooltip-content={t('mediaCard.addToWatchlist')}
                                    onClick={(e) => { e.stopPropagation(); handleWatchlist(); }}>
                                {ww.watchlist ? "☝️" : "X️☝️"}
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default MediaCard;
