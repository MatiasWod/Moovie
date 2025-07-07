import React from 'react';
import { useTranslation } from 'react-i18next';
import Loader from '../../Loader';
import MediaCard from '../mediaCard/MediaCard';
import './cardsHorizontalContainer.css';

const CardsHorizontalContainer = ({ mediaList, loading, error, watchedUrl, watchlistUrl }) => {
  const { t } = useTranslation();

  if (loading) {
    return <Loader />;
  }

  if (error) {
    return <div>{t('cardsHorizontalContainer.errorLoadingMedia')}</div>;
  }

  return (
    <div className="cardsHorizontalContainer">
      {Array.isArray(mediaList) && mediaList.length > 0 ? (
        mediaList.map((media) => <MediaCard key={media.id} media={media} watchedUrl={watchedUrl} watchlistUrl={watchlistUrl} />)
      ) : (
        <div>{t('cardsHorizontalContainer.noMediaFound')}</div>
      )}
    </div>
  );
};

export default CardsHorizontalContainer;
