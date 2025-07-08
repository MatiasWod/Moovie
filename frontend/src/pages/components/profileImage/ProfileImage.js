import React, { useEffect, useState } from 'react';
import userApi from '../../../api/UserApi';
import defaultProfilePicture from '../../../images/defaultProfilePicture.png';
import './profileImage.css';

const ProfileImage = ({ image, userUrl, size, onClick, noBorder }) => {
  const [imageSrc, setImageSrc] = useState(image || defaultProfilePicture);

  useEffect(() => {
    const fetchImage = async () => {
      if (!image && userUrl) {
        try {
          const response = await userApi.getUserByUsername(userUrl);
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
      } else if (image) {
        setImageSrc(image);
      }
    };

    fetchImage();
  }, [image, userUrl]);

  const buildImageUrl = (baseUrl) => {
    if (!baseUrl || baseUrl === defaultProfilePicture) {
      return defaultProfilePicture;
    }
    if (baseUrl.includes('size=')) {
      return baseUrl;
    }
    
    const hasQueryParams = baseUrl.includes('?');
    const separator = hasQueryParams ? '&' : '?';
    return `${baseUrl}${separator}size=100`;
  };

  return (
    <img
      id="profile-image"
      className={`profileImage${noBorder ? ' no-border' : ''}`}
      style={{ height: size, width: size, cursor: 'pointer' }}
      src={buildImageUrl(imageSrc)}
      alt="Profile"
      onClick={onClick}
      onError={(e) => {
        e.target.src = defaultProfilePicture;
      }}
    />
  );
};

export default ProfileImage;
