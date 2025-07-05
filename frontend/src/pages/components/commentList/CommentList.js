import React, { useState, useEffect } from 'react';
import commentApi from '../../../api/CommentApi';
import { useSelector } from 'react-redux';
import reportApi from '../../../api/ReportApi';
import {useNavigate, useSearchParams} from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import ReportForm from '../forms/reportForm/reportForm';
import ConfirmationForm from '../forms/confirmationForm/confirmationForm';
import reviewService from '../../../services/ReviewService';
import ConfirmationModal from '../forms/confirmationForm/confirmationModal';
import userApi from '../../../api/UserApi';
import CommentStatusEnum from '../../../api/values/CommentStatusEnum';
import NavDropdown from 'react-bootstrap/NavDropdown';
import './CommentList.css';
import api from '../../../api/api';
import {parsePaginatedResponse} from "../../../utils/ResponseUtils";
import PaginationButton from "../paginationButton/PaginationButton";
import UserService from "../../../services/UserService";
import CommentService from "../../../services/CommentService";

export default function CommentList({ reviewId, reload, commentsUrl }) {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { isLoggedIn, user } = useSelector((state) => state.auth);
  const [comments, setComments] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isExpanded, setIsExpanded] = useState(() => {
    const stored = localStorage.getItem(`comment-expanded-${reviewId}`);
    return stored ? JSON.parse(stored) : false;
  });
  const [pageComments, setPageComments] = useState(Number(searchParams.get('pageComments')) || 1);

  const [refreshComments, setRefreshComments] = useState(false);
  const handleRefreshComments = () => {
    setRefreshComments(!refreshComments);
  };

  useEffect(() => {
    fetchComments();
  }, [reviewId, refreshComments, reload?.reloadComments, commentsUrl, pageComments]);

  const fetchComments = async () => {
    try {
      let response;
      response = await CommentService.getCommentsFromUrl({ url: commentsUrl , pageNumber: pageComments });
      setComments(response);
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

  const handleReportComment = async (commentId, reportReason) => {
    try {
      const response = await reportApi.reportComment({
        commentId,
        reportedBy: user.username,
        type: reportReason,
      });
      return response;
    } catch (error) {
      console.error('Error reporting comment:', error);
      return error;
    }
  };

  if (isLoading)
    return <div className="text-gray-500 text-sm">{t('commentList.loadingComments')}</div>;
  if (error)
    return <div className="text-red-500 text-sm">{t('commentList.error', { error: error })}</div>;
  if (comments.length === 0) return null;

  return (
    <div className="mt-4 space-y-2">
      {comments?.data?.length > 0 && (
        <CommentItem
          comment={comments.data[0]}
          isLoggedIn={isLoggedIn}
          user={user}
          onDelete={handleDeleteComment}
          onReport={handleReportComment}
          reload={handleRefreshComments}
        />
      )}

      {comments?.data?.length > 1 && (
        <div className="text-center">
          <button
            onClick={() => setIsExpanded(!isExpanded)}
            className="text-blue-500 hover:text-blue-700 text-sm font-medium flex items-center justify-center gap-1 w-full"
          >
            {isExpanded ? (
              <>
                <span>{t('commentList.showLess')}</span>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M5 15l7-7 7 7"
                  />
                </svg>
              </>
            ) : (
              <>
                <span>{t('commentList.showMore')}</span>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M19 9l-7 7-7-7"
                  />
                </svg>
              </>
            )}
          </button>
        </div>
      )}

      {isExpanded && (
        <div className="space-y-2 animate-fadeIn">
          {comments?.data?.slice(1).map((comment, index) => (
            <CommentItem
              comment={comment}
              key={comment.id}
              isLoggedIn={isLoggedIn}
              user={user}
              onDelete={handleDeleteComment}
              onReport={handleReportComment}
              reload={handleRefreshComments}
            />
          ))}
          <div className="m-1 d-flex justify-content-center">
            {!isLoading && comments?.links?.last?.pageNumber > 1 && (
                <PaginationButton
                    page={pageComments}
                    lastPage={comments.links.last.pageNumber}
                    setPage={setPageComments}
                />
            )}
          </div>
        </div>

      )
      }

    </div>

  );
}

function CommentItem({ comment, isLoggedIn, user, onDelete, reload, onReport }) {
  const { t } = useTranslation();
  const [isReporting, setIsReporting] = useState(false);
  const [showDeleteComment, setShowDeleteComment] = useState(false);

  const handleToggleDelete = () => {
    setShowDeleteComment(!showDeleteComment);
  };

  const handleDelete = async () => {
    try {
      await commentApi.deleteComment(comment.id);
      setShowDeleteComment(!showDeleteComment);
      reload();
    } catch (e) {}
  };

  const handleReportSubmit = async (reportReason) => {
    return await onReport(comment.id, reportReason);
  };

  const [currentLikeStatus, setCurrentLikeStatus] = useState(false);
  const [currentDislikeStatus, setCurrentDislikeStatus] = useState(false);
  const [refreshLikeStatus, setRefreshLikeStatus] = useState(false);

  useEffect(() => {
    const fetchFeedbackStatus = async () => {
      if (!isLoggedIn || !user) return;

      try {
        const feedback = await UserService.currentUserCommentFeedback(comment?.id, user.username);
        setCurrentLikeStatus(feedback === CommentStatusEnum.LIKE);
        setCurrentDislikeStatus(feedback === CommentStatusEnum.DISLIKE);
      } catch (e) {
        console.error('Error fetching feedback status:', e);
      }
    };

    fetchFeedbackStatus();
  }, [comment?.id, user?.username, isLoggedIn, refreshLikeStatus]);

  const [localLikes, setLocalLikes] = useState(comment?.commentLikes);
  const [localDislikes, setLocalDislikes] = useState(comment?.commentDislikes);

  const handleLikeComment = async () => {
    try {
      await commentApi.commentFeedback(comment.id, 'LIKE');
      // Update counts based on previous state
      if (currentLikeStatus) {
        setLocalLikes((prev) => prev - 1);
      } else {
        setLocalLikes((prev) => prev + 1);
        if (currentDislikeStatus) {
          setLocalDislikes((prev) => prev - 1);
        }
      }
      setRefreshLikeStatus(!refreshLikeStatus);
    } catch (e) {
      // Revert to original counts if the API call fails
      setLocalLikes(comment?.commentLikes);
      setLocalDislikes(comment?.commentDislikes);
    }
  };

  const handleDislikeComment = async () => {
    try {
      await commentApi.commentFeedback(comment?.id, 'DISLIKE');
      // Update counts based on previous state
      if (currentDislikeStatus) {
        setLocalDislikes((prev) => prev - 1);
      } else {
        setLocalDislikes((prev) => prev + 1);
        if (currentLikeStatus) {
          setLocalLikes((prev) => prev - 1);
        }
      }
      setRefreshLikeStatus(!refreshLikeStatus);
    } catch (e) {
      // Revert to original counts if the API call fails
      setLocalLikes(comment?.commentLikes);
      setLocalDislikes(comment?.commentDislikes);
    }
  };

  // Update local counts when comment prop changes
  useEffect(() => {
    setLocalLikes(comment?.commentLikes);
    setLocalDislikes(comment?.commentDislikes);
  }, [comment?.commentLikes, comment?.commentDislikes]);

  return (
    <div className="bg-gray-50 p-3 rounded-lg">
      <div className="flex justify-between items-start">
        <div className="flex-grow">
          <p className="text-sm text-gray-700">{comment?.content}</p>
          <span className="text-xs text-gray-500">
            {t('commentList.by')} {comment?.userUrl.split('/').pop()}
          </span>
        </div>
        <div className="flex items-center gap-2">
          <div className="flex items-center gap-1">
            <span className="text-xs text-gray-600">{localLikes}</span>
            {isLoggedIn ? (
              <button
                className="btn btn-success btn-sm"
                onClick={handleLikeComment}
                data-bs-toggle="tooltip"
                data-bs-placement="top"
                title={currentLikeStatus ? t('comment.unlike') : t('comment.like')}
              >
                <i
                  className={
                    currentLikeStatus ? 'bi bi-hand-thumbs-up-fill' : 'bi bi-hand-thumbs-up'
                  }
                ></i>
              </button>
            ) : (
              <i className="bi bi-hand-thumbs-up text-gray-400 text-sm"></i>
            )}
          </div>

          <div className="flex items-center gap-1">
            <span className="text-xs text-gray-600">{localDislikes}</span>
            {isLoggedIn ? (
              <button
                className="btn btn-danger btn-sm"
                onClick={handleDislikeComment}
                data-bs-toggle="tooltip"
                data-bs-placement="top"
                title={currentDislikeStatus ? t('comment.undislike') : t('comment.dislike')}
              >
                <i
                  className={
                    currentDislikeStatus ? 'bi bi-hand-thumbs-down-fill' : 'bi bi-hand-thumbs-down'
                  }
                ></i>
              </button>
            ) : (
              <i className="bi bi-hand-thumbs-down text-gray-400 text-sm"></i>
            )}
          </div>

          {isLoggedIn && (
            <NavDropdown
              title={<i className="bi bi-three-dots-vertical"></i>}
              id="comment-actions-dropdown"
              className="custom-dropdown"
            >
              <NavDropdown.Item
                onClick={() => setIsReporting(true)}
                className="text-yellow-600 hover:text-yellow-700"
              >
                <i className="bi bi-flag text-sm me-2"></i>
                {t('comment.report')}
              </NavDropdown.Item>

              {user.username === comment?.username && (
                <NavDropdown.Item
                  onClick={handleToggleDelete}
                  className="text-red-600 hover:text-red-700"
                >
                  <i className="bi bi-trash text-sm me-2"></i>
                  {t('comment.delete')}
                </NavDropdown.Item>
              )}
            </NavDropdown>
          )}
        </div>
      </div>

      {showDeleteComment && (
        <div className="overlay">
          <ConfirmationModal
            onConfirm={handleDelete}
            onCancel={handleToggleDelete}
            message={t('comment.confirmDeleteMessage')}
            title={t('comment.confirmDelete')}
          />
        </div>
      )}

      {isReporting && (
        <ReportForm onReportSubmit={handleReportSubmit} onCancel={() => setIsReporting(false)} />
      )}
    </div>
  );
}
