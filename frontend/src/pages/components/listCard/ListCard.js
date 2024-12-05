import React, { useState, useEffect } from "react";
import defaultPoster from "../../../images/defaultPoster.png";
import { Link } from "react-router-dom";
import "./listCard.css";
import ProfileImage from "../profileImage/ProfileImage";
import listService from "../../../services/ListService";

const ListCard = ({ listCard }) => {
    const [hasLikedAndFollowed, setHasLikedAndFollowed] = useState({liked:false,followed:false});

    let images = [...listCard.images];
    while (images.length < 4) {
        images.push(defaultPoster);
    }

    useEffect(() => {
        const fetchHasLikedAndFollowed = async () => {
            try {
                const likedAndFollowed = await listService.currentUserLikeFollowStatus(listCard.id);
                setHasLikedAndFollowed(likedAndFollowed);
            } catch (error) {
            }
        };

        fetchHasLikedAndFollowed();
    }, [listCard.id]);

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
                        <span>
                            por {listCard.createdBy} <ProfileImage username={listCard.createdBy} />
                        </span>
                        <span className="list-card-likes">
                            👍 {listCard.likes} {hasLikedAndFollowed.liked ? "(You liked this)" : ""}
                        </span>
                    </div>
                </div>
            </div>
        </Link>
    );
};

export default ListCard;
