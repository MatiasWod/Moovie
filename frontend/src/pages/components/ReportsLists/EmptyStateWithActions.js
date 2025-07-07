import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function EmptyStateWithActions({ title, message, actions = [] }) {
  const navigate = useNavigate();

  const handleActionClick = (path) => {
    navigate(path);
  };

  return (
    <div className="text-center py-16">
      <div className="p-4 bg-blue-50 rounded-full w-20 h-20 mx-auto mb-4 flex items-center justify-center">
        <i className="bi bi-plus-circle text-blue-500 text-3xl"></i>
      </div>
      <h3 className="text-lg font-medium text-gray-700 mb-2">{title}</h3>
      <p className="text-gray-500 mb-6">{message}</p>
      
      {actions.length > 0 && (
        <div className="flex flex-wrap justify-center gap-3">
          {actions.map((action, index) => (
            <button
              key={index}
              onClick={() => handleActionClick(action.path)}
              className={`
                flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition-all duration-200 transform hover:scale-105
                ${action.primary 
                  ? 'bg-gradient-to-r from-blue-600 to-blue-700 text-white shadow-lg shadow-blue-500/25' 
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200 border border-gray-300'
                }
              `}
            >
              <i className={`${action.icon} text-lg`}></i>
              <span>{action.label}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
} 