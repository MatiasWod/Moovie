import React, { useRef, useEffect } from 'react';
import { CircularProgress } from '@mui/material';

const InfiniteScrollList = ({
    children,
    loadMore,
    hasMore,
    loading,
    maxHeight = '250px',
    threshold = 100,
}) => {
    const listRef = useRef(null);

    useEffect(() => {
        const handleScroll = () => {
            if (!listRef.current || loading || !hasMore) return;
            const { scrollTop, scrollHeight, clientHeight } = listRef.current;
            if (scrollHeight - scrollTop - clientHeight < threshold) {
                loadMore();
            }
        };
        const node = listRef.current;
        if (node) {
            node.addEventListener('scroll', handleScroll);
        }
        return () => {
            if (node) {
                node.removeEventListener('scroll', handleScroll);
            }
        };
    }, [loading, hasMore, loadMore, threshold]);

    return (
        <div
            ref={listRef}
            style={{ overflowY: 'auto', maxHeight, position: 'relative' }}
        >
            {children}
            {loading && (
                <div style={{ display: 'flex', justifyContent: 'center', padding: 8 }}>
                    <CircularProgress size={24} />
                </div>
            )}
        </div>
    );
};

export default InfiniteScrollList; 