import React from "react";
import "./profileImage.css";

const ProfileImage = ({image, size}) => {
    if(size){
        return(
            <img id="profile-image" className="profileImage" style={{height: size, width: size}} src={image || "../../images/defaultProfilePicture.png"} alt="profile image"/>
        )
    }
    return(
        <img id="profile-image" className="profileImage" src={image || "../../images/defaultProfilePicture.png"} alt="profile image"/>
    )
}

export default ProfileImage;