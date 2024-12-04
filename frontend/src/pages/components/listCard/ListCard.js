import React from "react";
import defaultPoster from "../../../images/defaultPoster.png";
import { Link } from "react-router-dom";
import "./listCard.css";
import ProfileImage from "../profileImage/ProfileImage";

const ListCard = ({ listCard }) => {
    let images = [...listCard.images]; // Copy the array to avoid mutating the original
    let imgLength = images.length;

    while (imgLength < 4) {
        images.push(defaultPoster);
        imgLength++;
    }

    return (
        <Link to={"/list/" + listCard.id} className="not-link">
            <div className="list-card">
                <div className="image-grid">
                    {images.slice(0, 4).map((image, index) => (
                        <img className="list-card-image" src={image} alt={`Poster ${index}`} key={index} />
                    ))}
                </div>
                <div className="list-card-body">
                    <div className="list-card-title">{listCard.name}</div>
                    <div className="list-card-details">
                        <span>{listCard.movieCount} Películas</span> • <span>{listCard.mediaCount - listCard.movieCount} Series</span>
                    </div>
                    <div className="list-card-footer">
                        <span>por {listCard.createdBy} <ProfileImage username={listCard.createdBy}/> </span>
                        <span className="list-card-likes">
                            👍 {listCard.likes}
                        </span>
                    </div>
                </div>
            </div>
        </Link>
    );
};

export default ListCard;