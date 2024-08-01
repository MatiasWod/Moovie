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
                <MediaCard media={media} />
            ))}
        </div>
    );
};

export default CardsHorizontalContainer;