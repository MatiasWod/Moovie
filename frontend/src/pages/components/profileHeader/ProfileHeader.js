import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import api from '../../../api/api';
import UserRoles from '../../../api/values/UserRoles';
import { parsePaginatedResponse } from '../../../utils/ResponseUtils';
import ChangePfpForm from '../forms/changePfpForm/changePfpForm';
import ProfileImage from '../profileImage/ProfileImage';
import RoleBadge from '../RoleBadge/RoleBadge';
import './profileHeader.css';

const ProfileHeader = ({ profile, handleBanUser, handleUnbanUser, handleMakeModerator }) => {
  const { t } = useTranslation();
  const [showPfpPopup, setShowPfpPopup] = useState(false);
  const navigate = useNavigate();
  const { isLoggedIn, user } = useSelector((state) => state.auth);
  const [reviewsCount, setReviewsCount] = useState(0);
  const [moovieListCount, setMoovieListCount] = useState(0);

  const handleShowPfpPopup = () => {
    if (!isLoggedIn) {
      navigate('/login');
    }
    if (user.username === profile.username) {
      setShowPfpPopup(true);
    }
  };

  useEffect(() => {
    const fetchReviewsCount = async () => {
      const response = await api.get(profile.reviewsUrl);
      const res = parsePaginatedResponse(response);
      setReviewsCount(res.totalElements);
    };
    const fetchMoovieListCount = async () => {
      const response = await api.get(profile.moovieListsUrl);
      const res = parsePaginatedResponse(response);
      setMoovieListCount(res.totalElements);
    };
    Promise.all([fetchReviewsCount(), fetchMoovieListCount()]);
  }, [profile.reviewsUrl, profile.moovieListsUrl]);

  const handleClosePfpPopup = () => setShowPfpPopup(false);

  const isModerator = user?.role === UserRoles.MODERATOR;
  const isNotOwnProfile = user?.username !== profile.username;
  const showModActions = isModerator && isNotOwnProfile;
  const canBeMadeModerator =
    showModActions && profile.role !== UserRoles.MODERATOR && profile.role !== UserRoles.BANNED;

  return (
    <div className="bg-gradient-to-r from-blue-600 to-blue-700 px-8 py-8">
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
        <div className="flex items-center gap-6">
          <div
            className="relative cursor-pointer group"
            onClick={handleShowPfpPopup}
          >
            <div className="w-24 h-24 md:w-28 md:h-28 rounded-full overflow-hidden border-4 border-white/20 shadow-xl transition-transform duration-200 group-hover:scale-105">
              <ProfileImage image={profile.imageUrl} size="112px" noBorder={true} />
            </div>
            {user?.username === profile.username && (
              <div className="absolute inset-0 bg-black/40 rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-200 flex items-center justify-center">
                <i className="bi bi-camera text-white text-xl"></i>
              </div>
            )}
          </div>
          <div className="text-white">
            <div className="flex flex-wrap items-center gap-3 mb-2">
              <h1 className="text-2xl md:text-3xl font-bold">
                {profile.username}
              </h1>
              {profile.hasBadge && (
                <span 
                  title={t('tooltips.badgeTooltip')}
                  className="text-2xl animate-pulse"
                > 
                  🏆
                </span>
              )}
              {showModActions && profile.role === UserRoles.MODERATOR && (
                <RoleBadge role={profile.role} size={'50px'} />
              )}
            </div>
            <p className="text-blue-100 text-sm md:text-base opacity-90">{profile.email}</p>
            
            {showModActions && profile.role !== UserRoles.MODERATOR && (
              <div className="flex flex-wrap gap-2 mt-3">
                {profile.role === UserRoles.BANNED_NOT_REGISTERED ||
                profile.role === UserRoles.BANNED ? (
                  <button
                    className="px-4 py-2 bg-green-500 hover:bg-green-600 text-white rounded-lg text-sm font-medium transition-colors duration-200 flex items-center gap-2"
                    onClick={handleUnbanUser}
                  >
                    <i className="bi bi-check-circle"></i>
                    {t('profile.unbanUser')}
                  </button>
                ) : (
                  <button
                    className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg text-sm font-medium transition-colors duration-200 flex items-center gap-2"
                    onClick={handleBanUser}
                  >
                    <i className="bi bi-x-circle"></i>
                    {t('profile.banUser')}
                  </button>
                )}
                {canBeMadeModerator && (
                  <button
                    className="px-4 py-2 bg-purple-500 hover:bg-purple-600 text-white rounded-lg text-sm font-medium transition-colors duration-200 flex items-center gap-2"
                    onClick={handleMakeModerator}
                  >
                    <i className="bi bi-shield-check"></i>
                    {t('profile.makeUserModerator')}
                  </button>
                )}
              </div>
            )}
          </div>
        </div>
        
        <div className="flex flex-wrap gap-4 md:gap-6">
          <div className="bg-white/10 backdrop-blur-sm rounded-xl px-4 py-3 text-center border border-white/20">
            <div className="text-2xl mb-1">📋</div>
            <div className="text-white font-bold text-lg">{moovieListCount}</div>
            <div className="text-blue-100 text-xs" title={t('tooltips.moovieListTooltip')}>
              Lists
            </div>
          </div>
          <div className="bg-white/10 backdrop-blur-sm rounded-xl px-4 py-3 text-center border border-white/20">
            <div className="text-2xl mb-1">⭐</div>
            <div className="text-white font-bold text-lg">{reviewsCount}</div>
            <div className="text-blue-100 text-xs" title={t('tooltips.reviewsTooltip')}>
              Reviews
            </div>
          </div>
          <div className="bg-white/10 backdrop-blur-sm rounded-xl px-4 py-3 text-center border border-white/20">
            <div className="text-2xl mb-1">🐵</div>
            <div className="text-white font-bold text-lg">{profile.milkyPoints}</div>
            <div className="text-blue-100 text-xs" title={t('tooltips.milkyPointsTooltip')}>
              Points
            </div>
          </div>
        </div>
      </div>
      {showPfpPopup && <ChangePfpForm onCancel={handleClosePfpPopup} />}
    </div>
  );
};

export default ProfileHeader;
