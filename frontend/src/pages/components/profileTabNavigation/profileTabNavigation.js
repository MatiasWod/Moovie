import React from 'react';
import { useTranslation } from 'react-i18next';

function ProfileTabNavigation({ selectedTab, onTabSelect, isLoggedIn, isMe }) {
  const { t } = useTranslation();

  const tabs = [
    {
      id: 'public-lists',
      label: t('profile.public'),
      icon: 'bi-collection',
      show: true,
    },
    {
      id: 'private-lists',
      label: t('profile.private'),
      icon: 'bi-lock',
      show: isMe,
    },
    {
      id: 'liked-lists',
      label: t('profile.liked'),
      icon: 'bi-heart',
      show: isLoggedIn,
    },
    {
      id: 'followed-lists',
      label: t('profile.followed'),
      icon: 'bi-eye',
      show: isLoggedIn,
    },
    {
      id: 'reviews',
      label: t('profile.reviews'),
      icon: 'bi-chat-square-text',
      show: true,
    },
    {
      id: 'watched',
      label: t('profile.watched'),
      icon: 'bi-check-circle',
      show: isMe,
    },
    {
      id: 'watchlist',
      label: t('profile.watchlist'),
      icon: 'bi-clock',
      show: isMe,
    },
  ];

  const visibleTabs = tabs.filter(tab => tab.show);

  return (
    <div className="flex flex-wrap justify-center gap-2 mb-6">
      {visibleTabs.map((tab) => (
        <button
          key={tab.id}
          onClick={() => onTabSelect(tab.id)}
          className={`
            flex items-center gap-2 px-6 py-3 rounded-xl font-medium transition-all duration-200 transform hover:scale-105
            ${
              selectedTab === tab.id
                ? 'bg-gradient-to-r from-blue-600 to-blue-700 text-white shadow-lg shadow-blue-500/25'
                : 'bg-gray-50 text-gray-700 hover:bg-gray-100 hover:text-gray-900 border border-gray-200'
            }
          `}
        >
          <i className={`${tab.icon} text-lg`}></i>
          <span className="whitespace-nowrap">{tab.label}</span>
        </button>
      ))}
    </div>
  );
}

export default ProfileTabNavigation;
