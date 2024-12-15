import { useState } from 'react';

export default function CommentField({ onSubmit, isLoading }) {
    const [comment, setComment] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (comment.trim()) {
            onSubmit(comment);
            setComment('');
        }
    };

    return (
        <form onSubmit={handleSubmit} className="mt-2">
            <div className="flex items-center gap-2 w-32">
                <input
                    type="text"
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    placeholder="Añadir un comentario..."
                    className="flex-grow px-3 py-1 text-sm text-gray-700 border rounded-full 
                             focus:outline-none focus:border-blue-500"
                />
                <button
                    type="submit"
                    disabled={isLoading || !comment.trim()}
                    className={`px-3 py-1 text-xs font-medium text-white rounded-full
                        ${isLoading || !comment.trim() 
                        ? 'bg-gray-300 cursor-not-allowed' 
                        : 'bg-blue-500 hover:bg-blue-600'}`}
                >
                    {isLoading ? '...' : 'Enviar'}
                </button>
            </div>
        </form>
    );
}