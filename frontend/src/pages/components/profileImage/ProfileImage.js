import React from "react";
import "./profileImage.css";
import defaultProfilePicture from "../../../images/defaultProfilePicture.png";
import {Link} from "react-router-dom";

const ProfileImage = ({ image, username , size }) => {
    const imgSrc = image || defaultProfilePicture;

    return (
        
        <img
            id="profile-image"
            className="profileImage"
            style={{ height: size, width: size }}
            src={imgSrc}
            alt="Profile"
        />
    );
};

export default ProfileImage;