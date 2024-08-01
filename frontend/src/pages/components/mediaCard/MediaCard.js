import React from 'react';
import './mediaCard.css';

const MediaCard = ({ media }) => {
    return (
        <div className="card shadow">
            <div className="card-body" style={{ borderRadius: '5%' }}>
                <span data-toggle="tooltip" data-placement="top" title={`(${media.releaseDate})`}>
                    <img className="card-img-top" style={{ borderRadius: '5%' }} src={media.posterPath} />
                </span>
                <h4 className="card-title">{media.name}</h4>
                <h5>{media.releaseDate}</h5>
            </div>
        </div>
    );
};

export default MediaCard;