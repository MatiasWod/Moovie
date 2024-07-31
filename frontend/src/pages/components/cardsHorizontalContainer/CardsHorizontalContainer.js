import React from 'react';
import MediaCard from '../mediaCard/MediaCard';
import './cardsHorizontalContainer.css'
import Loader from "../../Loader";

const CardsHorizontalContainer = ({ mediaList, loading, error }) => {
    if(loading){
        return Loader();
    }

    if(error){
        return <div>Error loading Media.</div>
    }

    return (
        <div className="cardsHorizontalContainer">
            {mediaList.map((media) => (
                <MediaCard
                    key={media.id}
                    releaseDate={media.releaseDate}
                    posterPath={media.posterPath}
                    mediaName={media.name}
                />
            ))}
        </div>
    );
};

export default CardsHorizontalContainer;