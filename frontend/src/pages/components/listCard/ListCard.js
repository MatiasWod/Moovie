import React from "react";
import defaultPoster from "../../../images/defaultPoster.png";
import "./listCard.css";

const ListCard = ({ listCard }) => {
    let images = [...listCard.images]; // Copy the array to avoid mutating the original
    let imgLength = images.length;

    while (imgLength < 4) {
        images.push(defaultPoster);
        imgLength++;
    }

    return (
        <div className="list-card">
            <div className="image-grid">
                {images.map((image, index) => (
                    <img className="list-card-images" src={image}/>
                ))}
            </div>
            <div className="list-card-body">
                <div>{listCard.name}</div>
                <div>{listCard.createdBy}</div>
                <div>{listCard.likes}</div>
                <div>{listCard.mediaCount}</div>
            </div>
        </div>
    );
};

export default ListCard;
