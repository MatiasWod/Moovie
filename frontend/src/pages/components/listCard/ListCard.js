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
        <div className="listCard">
            <div className="imageGrid">
                {images.map((image, index) => (
                    <img className="listCardImages" src={image}/>
                ))}
            </div>
            <div>
                <div>{listCard.name}</div>
            </div>
        </div>
    );
};

export default ListCard;
