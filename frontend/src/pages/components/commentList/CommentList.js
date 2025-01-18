import React, { useState, useEffect } from 'react';
import commentApi from '../../../api/CommentApi';
import { useSelector } from 'react-redux';
import ConfirmationForm from '../forms/confirmationForm/confirmationForm';
import ReportForm from '../forms/reportForm/reportForm';
import reportApi from '../../../api/ReportApi';
import { useNavigate } from 'react-router-dom';
import {useTranslation} from "react-i18next";

export default function CommentList({ reviewId }) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { isLoggedIn, user } = useSelector(state => state.auth);
    const [comments, setComments] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedCommentId, setSelectedCommentId] = useState(null);
    const [reportedCommentId, setReportedCommentId] = useState(null);
    const [isExpanded, setIsExpanded] = useState(() => {
        const stored = localStorage.getItem(`comment-expanded-${reviewId}`);
        return stored ? JSON.parse(stored) : false;
    });

    useEffect(() => {
        fetchComments();
    }, [reviewId]);

    const fetchComments = async () => {
        try {
            const response = await commentApi.getReviewComments(reviewId);
            setComments(response.data || []);
            setIsLoading(false);
        } catch (err) {
            setError(err.message);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        localStorage.setItem(`comment-expanded-${reviewId}`, JSON.stringify(isExpanded));
    }, [isExpanded, reviewId]);

    const handleOpenConfirmationDelete = (commentId) => {
        if (!isLoggedIn) {
            navigate('/login');
            return;
        }
        setSelectedCommentId(commentId);
    };

    const handleReportComment = (commentId) => {
        if (!isLoggedIn) {
            navigate('/login');
            return;
        }
        setReportedCommentId(commentId);
    };

    const handleReportCommentSubmit = async (reportReason, additionalInfo) => {
        await reportApi.reportComment({
            commentId: reportedCommentId,
            reportedBy: user.username,
            content: additionalInfo,
            type: reportReason
        });
        setReportedCommentId(null);
    };

    const handleConfirmDelete = () => {
        fetchComments();
        setSelectedCommentId(null);
    };

    const CommentItem = ({ comment, index }) => (
        <div key={index} className="bg-gray-50 p-3 rounded-lg">
            <div className="flex justify-between items-start">
                <div className="flex-grow">
                    <p className="text-sm text-gray-700">{comment.content}</p>
                    <span className="text-xs text-gray-500">
                        {t('commentList.by')} {comment.userUrl.split('/').pop()}
                    </span>
                </div>
                {isLoggedIn && (
                    <div className="flex gap-2">
                        <button
                            onClick={() => handleReportComment(comment.id)}
                            className="text-yellow-600 hover:text-yellow-700 transition-colors"
                            title={t('commentList.reportComment')}
                        >
                            <i className="bi bi-flag text-sm"></i>
                        </button>
                        <button
                            onClick={() => handleOpenConfirmationDelete(comment.id)}
                            className="text-red-600 hover:text-red-700 transition-colors"
                            title={t('commentList.deleteComment')}
                        >
                            <i className="bi bi-trash text-sm"></i>
                        </button>
                    </div>
                )}
            </div>
            
            {selectedCommentId === comment.id && (
                <ConfirmationForm
                    service={commentApi.deleteComment}
                    serviceParams={[comment.id]}
                    actionName={t('commentList.deleteYourComment')}
                    onConfirm={handleConfirmDelete}
                    onCancel={() => setSelectedCommentId(null)}
                />
            )}
            
            {reportedCommentId === comment.id && (
                <ReportForm
                    onReportSubmit={handleReportCommentSubmit}
                    onCancel={() => setReportedCommentId(null)}
                />
            )}
        </div>
    );

    if (isLoading) return <div className="text-gray-500 text-sm">{t('commentList.loadingComments')}</div>;
    if (error) return <div className="text-red-500 text-sm">{t('commentList.error',{error: error})}</div>;
    if (comments.length === 0) return null;

    return (
        <div className="mt-4 space-y-2">
            <CommentItem comment={comments[0]} index={0} />

            {comments.length > 1 && (
                <div className="text-center">
                    <button
                        onClick={() => setIsExpanded(!isExpanded)}
                        className="text-blue-500 hover:text-blue-700 text-sm font-medium flex items-center justify-center gap-1 w-full"
                    >
                        {isExpanded ? (
                            <>
                                <span>{t('commentList.showLess')}</span>
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 15l7-7 7 7" />
                                </svg>
                            </>
                        ) : (
                            <>
                                <span>{t('commentList.showMore', { commentsQty: comments.length - 1 })}</span>
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                                </svg>
                            </>
                        )}
                    </button>
                </div>
            )}

            {isExpanded && (
                <div className="space-y-2 animate-fadeIn">
                    {comments.slice(1).map((comment, index) => (
                        <CommentItem comment={comment} index={index + 1} key={index} />
                    ))}
                </div>
            )}
        </div>
    );
}