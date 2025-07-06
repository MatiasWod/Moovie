import React from 'react';
import { useTranslation } from 'react-i18next';

export default function ReportActionsButtons({ onResolve, onDelete, onBan, resolveKey = 'resolve', deleteKey = 'delete', banKey = 'banUser' }) {
  const { t } = useTranslation();

  return (
    <div className="flex justify-end gap-3">
      <button
        onClick={onResolve}
        className="flex items-center gap-2 px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors duration-200 shadow-sm hover:shadow-md"
      >
        <i className="bi bi-check2-circle"></i>
        <span>{t(resolveKey)}</span>
      </button>
      <button
        onClick={onDelete}
        className="flex items-center gap-2 px-4 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 transition-colors duration-200 shadow-sm hover:shadow-md"
      >
        <i className="bi bi-trash"></i>
        <span>{t(deleteKey)}</span>
      </button>
      <button
        onClick={onBan}
        className="flex items-center gap-2 px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors duration-200 shadow-sm hover:shadow-md"
      >
        <i className="bi bi-person-x"></i>
        <span>{t(banKey)}</span>
      </button>
    </div>
  );
} 