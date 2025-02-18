import React, { useState, useEffect } from 'react';
import commentApi from '../../../api/CommentApi';
import { useSelector } from 'react-redux';
import reportApi from '../../../api/ReportApi';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from "react-i18next";
import ReportForm from "../forms/reportForm/reportForm";
import ConfirmationForm from "../forms/confirmationForm/confirmationForm";
import reviewService from "../../../services/ReviewService";
import ConfirmationModal from "../forms/confirmationForm/confirmationModal";
import profileService from "../../../services/ProfileService";
import profileApi from "../../../api/ProfileApi";
import CommentStatusEnum from "../../../api/values/CommentStatusEnum";

export default function CommentList({ reviewId, reload }) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { isLoggedIn, user } = useSelector(state => state.auth);
    const [comments, setComments] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isExpanded, setIsExpanded] = useState(() => {
        const stored = localStorage.getItem(`comment-expanded-${reviewId}`);
        return stored ? JSON.parse(stored) : false;
    });

    const [refreshComments, setRefreshComments] = useState(false);
    const handleRefreshComments = () =>{
        setRefreshComments(!refreshComments);
    }

    useEffect(() => {
        fetchComments();
    }, [reviewId, refreshComments, reload]);

    const fetchComments = async () => {
        try {
            const response = await commentApi.getReviewComments(reviewId, 1);
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

    const handleDeleteComment = async (commentId) => {
        await commentApi.deleteComment(commentId);
        fetchComments();
    };

    const handleReportComment = async (commentId, reportReason, additionalInfo) => {
        await reportApi.reportComment({
            commentId,
            reportedBy: user.username,
            content: additionalInfo,
            type: reportReason
        });
    };

    if (isLoading) return <div className="text-gray-500 text-sm">{t('commentList.loadingComments')}</div>;
    if (error) return <div className="text-red-500 text-sm">{t('commentList.error', { error: error })}</div>;
    if (comments.length === 0) return null;

    return (
        <div className="mt-4 space-y-2">
            <CommentItem
                comment={comments[0]}
                isLoggedIn={isLoggedIn}
                user={user}
                onDelete={handleDeleteComment}
                onReport={handleReportComment}
                reload = {handleRefreshComments}
            />

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
                        <CommentItem
                            comment={comment}
                            key={comment.id}
                            isLoggedIn={isLoggedIn}
                            user={user}
                            onDelete={handleDeleteComment}
                            onReport={handleReportComment}
                            reload = {reload}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}


function CommentItem({ comment, isLoggedIn, user, onDelete, reload, onReport }) {
    const { t } = useTranslation();
    const [isReporting, setIsReporting] = useState(false);
    const [showDeleteComment, setShowDeleteComment] = useState(false);

    const handleToggleDelete = () => {
        setShowDeleteComment(!showDeleteComment)
    }

    const handleDelete = async () =>{
        try{
            await commentApi.deleteComment(comment.id);
            setShowDeleteComment(!showDeleteComment);
            reload();
        } catch (e){

        }
    }

    const handleReportSubmit = async (reportReason, additionalInfo) => {
        await onReport(comment.id, reportReason, additionalInfo);
        setIsReporting(false);
    };


    const [currentLikeStatus, setCurrentLikeStatus] = useState(false);
    const [currentDislikeStatus, setCurrentDislikeStatus] = useState(false);
    const [refreshLikeStatus, setRefreshLikeStatus] = useState(false);
    useEffect( async () => {
        try{
            const feedback = await profileService.currentUserCommentFeedback(comment.id, user.username);
            setCurrentLikeStatus(false);
            setCurrentDislikeStatus(false);
            if(feedback === CommentStatusEnum.LIKE){
                setCurrentLikeStatus(true);
            } else if (feedback === CommentStatusEnum.DISLIKE){
                setCurrentDislikeStatus(true);
            }
        } catch(e){

        }
    }, [currentLikeStatus, currentDislikeStatus, refreshLikeStatus]);

    const handleLikeComment = async () => {
        try{
            await commentApi.commentFeedback(comment.id,"LIKE");
            setRefreshLikeStatus(!refreshLikeStatus);
        } catch (e) {

        }
    }

    const handleDislikeComment = async () => {
        try{
            await commentApi.commentFeedback(comment.id,"DISLIKE");
            setRefreshLikeStatus(!refreshLikeStatus);
        } catch (e) {

        }
    }

    return (
        <div className="bg-gray-50 p-3 rounded-lg">
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
                            className="btn btn-success btn-sm"
                            onClick={handleLikeComment}
                            data-bs-toggle="tooltip"
                            data-bs-placement="top"
                            title={currentLikeStatus ? t('review.unlike') : t('review.like')}>
                            <i className={currentLikeStatus ? "bi bi-hand-thumbs-up-fill" : "bi bi-hand-thumbs-up"}></i>
                        </button>

                        <button
                            className="btn btn-danger btn-sm"
                            onClick={handleDislikeComment}
                            data-bs-toggle="tooltip"
                            data-bs-placement="top"
                            title={currentLikeStatus ? t('review.unlike') : t('review.like')}>
                            <i className={currentDislikeStatus ? "bi bi-hand-thumbs-down-fill" : "bi bi-hand-thumbs-down"}></i>
                        </button>

                        <button
                            onClick={() => setIsReporting(true)}
                            className="text-yellow-600 hover:text-yellow-700 transition-colors"
                            title={t('commentList.reportComment')}
                        >
                            <i className="bi bi-flag text-sm"></i>
                        </button>
                        {user.username === comment.username && (
                            <button
                                onClick={handleToggleDelete}
                                className="text-red-600 hover:text-red-700 transition-colors"
                                title={t('commentList.deleteComment')}
                            >
                                <i className="bi bi-trash text-sm"></i>
                            </button>
                        )}
                    </div>
                )}
            </div>

            {showDeleteComment && (
                <div className="overlay">
                    <ConfirmationModal onConfirm={handleDelete} onCancel={handleToggleDelete}
                                       message={t('reviews.aboutToDelete')} title={t('review.confirmDelete')}/>
                </div>
            )}

            {isReporting && (
                <ReportForm
                    onReportSubmit={handleReportSubmit}
                    onCancel={() => setIsReporting(false)}
                />
            )}
        </div>
    );
}
