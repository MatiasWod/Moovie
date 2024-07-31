import React from 'react';
import './mediaCard.css';

const MediaCard = ({ title, releaseDate, posterPath, mediaName }) => {
    return (
        <div className="card shadow">
            <div className="card-body" style={{ borderRadius: '5%' }}>
                <span data-toggle="tooltip" data-placement="top" title={`${title} (${releaseDate})`}>
                    <img className="card-img-top" style={{ borderRadius: '5%' }} src={posterPath} alt={title} />
                </span>
                <h4 className="card-title">{mediaName}</h4>
                <h5>{releaseDate}</h5>
            </div>
        </div>
    );
};

export default MediaCard;