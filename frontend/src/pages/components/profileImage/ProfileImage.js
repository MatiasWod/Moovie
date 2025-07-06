import React, { useEffect, useState } from 'react';
import './profileImage.css';
import defaultProfilePicture from '../../../images/defaultProfilePicture.png';
import userApi from '../../../api/UserApi';

const ProfileImage = ({ image, userUrl, size, onClick }) => {
  const [imageSrc, setImageSrc] = useState(image || defaultProfilePicture);

  useEffect(() => {
    const fetchImage = async () => {
      if (!image && userUrl) {
        try {
          // TODO do fetch with url. En vez de pasarle el username ctr + f y pasar el link que ta en todos las veces que se fetchea.
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
      }
    };

    fetchImage();
  }, [image, userUrl]);

  return (
    <img
      id="profile-image"
      className="profileImage"
      style={{ height: size, width: size, cursor: 'pointer' }}
      src={imageSrc + '?size=100'}
      alt="Profile"
      onClick={onClick}
      onError={(e) => {
        e.target.src = defaultProfilePicture;
      }}
    />
  );
};

export default ProfileImage;
