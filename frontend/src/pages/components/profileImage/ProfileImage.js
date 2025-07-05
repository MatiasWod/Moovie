import React, { useEffect, useState } from 'react';
import './profileImage.css';
import defaultProfilePicture from '../../../images/defaultProfilePicture.png';
import userApi from '../../../api/UserApi';

const ProfileImage = ({ image, username, size, onClick }) => {
    const [imageSrc, setImageSrc] = useState(image || defaultProfilePicture);

    useEffect(() => {
        const fetchImage = async () => {
            if (!image && username) {
                try {
                    const response = await userApi.getUserByUsername(username);
                    const imageUrl = response?.data?.imageUrl;

                    if (imageUrl) {
                        setImageSrc(imageUrl);
                    } else {
                        setImageSrc(defaultProfilePicture);
                    }
                } catch (e) {
                    console.error('Failed to load user image', e);
                    setImageSrc(defaultProfilePicture);
                }
            }
        };

        fetchImage();
    }, [image, username]);

    return (
        <img
            id="profile-image"
            className="profileImage"
            style={{ height: size, width: size, cursor: 'pointer' }}
            src={imageSrc + "?size=100"}
            alt="Profile"
            onClick={onClick}
            onError={(e) => {
                e.target.src = defaultProfilePicture;
            }}
        />
    );
};

export default ProfileImage;
