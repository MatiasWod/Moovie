import React from 'react';
import { useTranslation } from 'react-i18next';

export default function ReportCountsCard({ totalReports, spamReports, hateReports, abuseReports, privacyReports }) {
  const { t } = useTranslation();

  return (
    <div className="bg-red-50 rounded-lg p-3 min-w-[120px]">
      <div className="flex items-center justify-center gap-2 mb-2">
        <i className="bi bi-flag-fill text-red-500"></i>
        <span className="font-bold text-red-700">{totalReports}</span>
        <span className="text-xs text-red-600">{t('reports.text.reports')}</span>
      </div>
      <div className="grid grid-cols-2 gap-2 text-xs">
        <div className="flex items-center gap-1 justify-center" title={t('reports.spamReports')}>
          <i className="bi bi-envelope-exclamation text-orange-500"></i>
          <span>{spamReports}</span>
        </div>
        <div className="flex items-center gap-1 justify-center" title={t('reports.hateReports')}>
          <i className="bi bi-emoji-angry text-red-500"></i>
          <span>{hateReports}</span>
        </div>
        <div className="flex items-center gap-1 justify-center" title={t('reports.abuseReports')}>
          <i className="bi bi-slash-circle text-purple-500"></i>
          <span>{abuseReports}</span>
        </div>
        <div className="flex items-center gap-1 justify-center" title={t('reports.privacyReports')}>
          <i className="bi bi-incognito text-gray-500"></i>
          <span>{privacyReports}</span>
        </div>
      </div>
    </div>
  );
} 