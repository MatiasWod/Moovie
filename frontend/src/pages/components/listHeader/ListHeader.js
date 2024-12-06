import React, { useEffect, useState } from "react";
import "./listHeader.css";
import listService from "../../../services/ListService";

const ListHeader = ({ list }) => {
    const [hasLikedAndFollowed, setHasLikedAndFollowed] = useState({
        liked: false,
        followed: false,
    });

    const [ping, setPing] = useState(false);

    useEffect(() => {
        const fetchHasLikedAndFollowed = async () => {
            try {
                const likedAndFollowed = await listService.currentUserLikeFollowStatus(list.id);
                setHasLikedAndFollowed(likedAndFollowed);
            } catch (error) {
            }
        };

        fetchHasLikedAndFollowed();
    }, [list.id, ping]);

    const handleLike = async () => {
        try {
            if (hasLikedAndFollowed.liked) {
                await listService.unlikeList(list.id);
            } else {
                await listService.likeList(list.id);
            }
            setPing(!ping)
        } catch (error) {
        }
    };

    const handleFollow = async () => {
        try {
            if (hasLikedAndFollowed.followed) {
                await listService.unfollowList(list.id);
            } else {
                await listService.followList(list.id);
            }
            setPing(!ping)
        } catch (error) {
        }
    };

    return (
        <div className="list-header">
            {list.images && list.images.length > 0 ? (
                <div
                    className="list-header-image"
                    style={{ backgroundImage: `url(${list.images[0]})` }}
                ></div>
            ) : null}
            <div className="list-header-content">
                <h1 className="list-header-title">{list.name}</h1>
                <p className="list-header-description">{list.description}</p>
                <span className="list-header-username">por {list.createdBy}</span>
                <div className="list-header-buttons">
                    <button
                        className={`like-button ${hasLikedAndFollowed.liked ? "liked" : ""}`}
                        onClick={handleLike}
                    >
                        {hasLikedAndFollowed.liked ? "Unlike" : "Like"}
                    </button>
                    <button
                        className={`follow-button ${hasLikedAndFollowed.followed ? "followed" : ""}`}
                        onClick={handleFollow}
                    >
                        {hasLikedAndFollowed.followed ? "Unfollow" : "Follow"}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ListHeader;
