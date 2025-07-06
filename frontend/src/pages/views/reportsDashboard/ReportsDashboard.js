import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import BannedUsers from '../../components/ReportsLists/BannedUsers';
import CommentReports from '../../components/ReportsLists/CommentReports';
import MoovieListReports from '../../components/ReportsLists/MoovieListReports';
import MoovieListReviewReports from '../../components/ReportsLists/MoovieListReviewReports';
import ReviewReports from '../../components/ReportsLists/ReviewReports';

export default function ReportsDashboard() {
  const [selectedType, setSelectedType] = useState('comments');
  const { t } = useTranslation();

  const tabs = [
    {
      id: 'comments',
      label: t('reportsDashboard.comments'),
      component: CommentReports,
      icon: 'bi-chat-dots',
    },
    {
      id: 'ml',
      label: t('reportsDashboard.moovieLists'),
      component: MoovieListReports,
      icon: 'bi-collection',
    },
    {
      id: 'mlReviews',
      label: t('reportsDashboard.moovieListReviews'),
      component: MoovieListReviewReports,
      icon: 'bi-star',
    },
    {
      id: 'reviews',
      label: t('reportsDashboard.reviews'),
      component: ReviewReports,
      icon: 'bi-chat-square-text',
    },
    {
      id: 'banned',
      label: t('reportsDashboard.bannedUsers'),
      component: BannedUsers,
      icon: 'bi-person-x',
    },
  ];

  const ActiveComponent = tabs.find((tab) => tab.id === selectedType)?.component;

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100">
      <div className="container mx-auto px-4 py-8">
        <div className="bg-white shadow-xl rounded-2xl overflow-hidden">
          <div className="bg-gradient-to-r from-blue-600 to-blue-700 px-8 py-6">
            <h1 className="text-3xl font-bold text-white text-center flex items-center justify-center gap-3">
              <i className="bi bi-shield-exclamation text-4xl"></i>
              {t('reportsDashboard.title')}
            </h1>
            <p className="text-blue-100 text-center mt-2 text-sm">
              {t('reportsDashboard.subtitle')}
            </p>
          </div>

          <div className="p-8">
            <div className="flex flex-wrap justify-center gap-2 mb-8">
              {tabs.map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setSelectedType(tab.id)}
                  className={`
                    flex items-center gap-2 px-6 py-3 rounded-xl font-medium transition-all duration-200 transform hover:scale-105
                    ${
                      selectedType === tab.id
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

            <div className="bg-gray-50 rounded-xl p-6 min-h-[400px]">
              {ActiveComponent && <ActiveComponent />}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
