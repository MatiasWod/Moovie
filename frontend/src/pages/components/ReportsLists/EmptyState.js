import React from 'react';

export default function EmptyState({ title, message }) {
  return (
    <div className="text-center py-16">
      <div className="p-4 bg-green-50 rounded-full w-20 h-20 mx-auto mb-4 flex items-center justify-center">
        <i className="bi bi-check-circle text-green-500 text-3xl"></i>
      </div>
      <h3 className="text-lg font-medium text-gray-700 mb-2">{title}</h3>
      <p className="text-gray-500">{message}</p>
    </div>
  );
}
