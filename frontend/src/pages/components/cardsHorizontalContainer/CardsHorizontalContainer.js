import React from 'react';
import MediaCard from '../mediaCard/MediaCard';
import './cardsHorizontalContainer.css';
import Loader from "../../Loader";

const CardsHorizontalContainer = ({ mediaList, loading, error}) => {
    if (loading) {
        return <Loader />;
    }

    if (error) {
        return <div>Error loading Media.</div>;
    }

    return (
        <div className="cardsHorizontalContainer">
            {Array.isArray(mediaList) && mediaList.length > 0 ? (
                mediaList.map((media) => (
                    <MediaCard key={media.id} media={media} />
                ))
            ) : (
                <div>No media found</div>
            )}
        </div>
    );
};

export default CardsHorizontalContainer;
