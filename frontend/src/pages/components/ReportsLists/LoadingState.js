import React from 'react';
import { Spinner } from 'react-bootstrap';

export default function LoadingState({ message }) {
  return (
    <div className="flex justify-center items-center py-16">
      <div className="text-center">
        <Spinner className="mb-4" />
        <p className="text-gray-500 text-sm">{message}</p>
      </div>
    </div>
  );
} 