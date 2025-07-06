import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import api from '../../../api/api';
import commentApi from '../../../api/CommentApi';
import reportApi from '../../../api/ReportApi';
import userApi from '../../../api/UserApi';
import { parsePaginatedResponse } from "../../../utils/ResponseUtils";
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import PaginationButton from "../paginationButton/PaginationButton";
import EmptyState from './EmptyState';
import LoadingState from './LoadingState';
import ReportActionsButtons from './ReportActionsButtons';
import ReportCountsCard from './ReportCountsCard';

export default function CommentReports() {
  const [comments, setComments] = useState({ comments: [], links: {} });
  const [commentsLoading, setCommentsLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const [page, setPage] = useState(1);
  const { t } = useTranslation();

  useEffect(() => {
    fetchComments();
  }, [page]);

  const fetchComments = async () => {
    setCommentsLoading(true);
    try {
      const res = await reportApi.getReports({ contentType: 'comment', pageNumber: page });
      const response = parsePaginatedResponse(res)
      const reportsData = response.data || [];

      const uniqueCommentUrls = [...new Set(reportsData.map((report) => report.commentUrl))];

      try {
        const commentPromises = uniqueCommentUrls.map((url) => api.get(url));
        const commentResponses = await Promise.all(commentPromises);
        const comments = commentResponses.map((response) => response.data);

        try {
          const allPromises = comments.flatMap((comment) => {
            console.log('comment', comment);
            const promises = [
              reportApi.getCountFromUrl(comment.spamReportsUrl),
              reportApi.getCountFromUrl(comment.hateReportsUrl),
              reportApi.getCountFromUrl(comment.abuseReportsUrl),
              reportApi.getCountFromUrl(comment.privacyReportsUrl),
              api.get(comment.reviewUrl),
            ];
            return promises;
          });

          const allResults = await Promise.all(allPromises);
          console.log('allResults', allResults);

          const commentsWithDetails = await Promise.all(comments.map(async (comment, index) => {
            const baseIndex = index * 5;

            const commentReports = reportsData.filter(report => report.commentUrl === comment.url);
            const reportIds = commentReports.map(report => report.reportId);

            const media = allResults[baseIndex + 4]?.data?.mediaId ? await api.get(allResults[baseIndex + 4]?.data?.mediaUrl) : null;

            console.log(allResults[baseIndex + 4]?.data);
            return {
              ...comment,
              reportIds: reportIds,
              abuseReports: allResults[baseIndex] || 0,
              hateReports: allResults[baseIndex + 1] || 0,
              spamReports: allResults[baseIndex + 2] || 0,
              privacyReports: allResults[baseIndex + 3] || 0,
              totalReports:
                (Number(allResults[baseIndex]) || 0) +
                (Number(allResults[baseIndex + 1]) || 0) +
                (Number(allResults[baseIndex + 2]) || 0) +
                (Number(allResults[baseIndex + 3]) || 0),
              reviewDetails: allResults[baseIndex + 4]?.data,
              mediaDetails: media?.data,
            };
          }));

          setComments({
            comments: commentsWithDetails,
            links: response.links,
          });
        } catch (error) {
          console.error('Error fetching additional details:', error);
          setComments(comments);
        }
      } catch (error) {
        console.error('Error fetching comments:', error);
        setComments([]);
      }
    } catch (error) {
      console.error('Error fetching reports:', error);
      setComments([]);
    } finally {
      setCommentsLoading(false);
    }
  };

  const handleDelete = async (comment) => {
    try {
      await commentApi.deleteComment(comment.id);
      await fetchComments();
    } catch (error) {
      console.error('Error deleting comment:', error);
    }
  };

  const handleBan = async (comment) => {
    try {
      const response = await api.get(comment.userUrl);
      const user = response.data;
      await userApi.banUser(user.username);
      await fetchComments();
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleResolve = async (comment) => {
    try {
      if (comment.reportIds && comment.reportIds.length > 0) {
        await Promise.all(comment.reportIds.map(reportId =>
          reportApi.commentReports.resolveReport(reportId)
        ));
      }
      await fetchComments();
    } catch (error) {
      console.error('Error resolving report:', error);
    }
  };

  return (
    <div className="w-full">
      <div className="flex items-center gap-3 mb-6">
        <div className="p-2 bg-blue-100 rounded-lg">
          <i className="bi bi-chat-dots text-blue-600 text-xl"></i>
        </div>
        <h2 className="text-2xl font-bold text-gray-800">{t('commentReports.commentReports')}</h2>
      </div>
      
      {commentsLoading ? (
        <LoadingState message={t('reports.loading.comments')} />
      ) : comments?.comments?.length === 0 ? (
        <EmptyState 
          title={t('reports.empty.comments')} 
          message={t('commentReports.noCommentReports')} 
        />
      ) : (
        <div className="space-y-6">
          {comments?.comments?.map((comment, index) => (
            <div key={index} className="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200">
              <div className="p-6">
                <div className="flex justify-between items-start mb-4">
                  <div className="flex-1 space-y-3">
                    <div className="flex items-center gap-2 flex-wrap">
                      <div className="flex items-center gap-2">
                        <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                          <i className="bi bi-person text-blue-600 text-sm"></i>
                        </div>
                        <a
                          href={process.env.PUBLIC_URL + `/profile/${comment.username}`}
                          className="font-semibold text-blue-600 hover:text-blue-800 hover:underline transition-colors"
                        >
                          {comment.username}
                        </a>
                      </div>
                      <span className="text-gray-400">•</span>
                      <span className="text-gray-600 text-sm">{t('reviews.onMedia')}</span>
                      <a
                        href={process.env.PUBLIC_URL + `/details/${comment.mediaDetails?.id}`}
                        className="font-medium text-blue-600 hover:text-blue-800 hover:underline transition-colors"
                      >
                        {comment.mediaDetails?.name}
                      </a>
                    </div>

                    <div className="bg-gray-50 rounded-lg p-3 border-l-4 border-blue-200">
                      <div className="text-xs text-gray-500 mb-1 flex items-center gap-1">
                        <i className="bi bi-quote"></i>
                        {t('reviews.onMedia')} {t('reviews.reviews')}:
                      </div>
                      <p className="text-sm text-gray-700 italic">
                        "{comment.reviewDetails?.reviewContent?.length > 50
                          ? `${comment.reviewDetails.reviewContent.substring(0, 50)}...`
                          : comment.reviewDetails?.reviewContent}"
                      </p>
                    </div>

                    <div className="flex items-center gap-4 text-sm text-gray-600">
                      <div className="flex items-center gap-1">
                        <i className="bi bi-hand-thumbs-up text-green-500"></i>
                        <span>{comment.commentLikes}</span>
                      </div>
                      <div className="flex items-center gap-1">
                        <i className="bi bi-hand-thumbs-down text-red-500"></i>
                        <span>{comment.commentDislikes}</span>
                      </div>
                    </div>
                  </div>

                  <div className="ml-6 text-right">
                    <ReportCountsCard
                      totalReports={comment.totalReports}
                      spamReports={comment.spamReports}
                      hateReports={comment.hateReports}
                      abuseReports={comment.abuseReports}
                      privacyReports={comment.privacyReports}
                    />
                  </div>
                </div>

                <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-lg p-4 mb-4 border-l-4 border-gray-300">
                  <div className="flex items-center gap-2 mb-2">
                    <i className="bi bi-chat-dots text-gray-500"></i>
                    <span className="text-sm font-medium text-gray-600">{t('reports.content.reportedComment')}</span>
                  </div>
                  <p className="text-gray-800 leading-relaxed">{comment.content}</p>
                </div>

                <ReportActionsButtons
                  onResolve={() => setSelectedAction({ type: 'resolve', item: comment })}
                  onDelete={() => setSelectedAction({ type: 'delete', item: comment })}
                  onBan={() => setSelectedAction({ type: 'ban', item: comment })}
                  resolveKey="commentReports.resolve"
                  deleteKey="commentReports.delete"
                  banKey="commentReports.banUser"
                />
              </div>
            </div>
          ))}
        </div>
      )}

      {selectedAction && (
        <ConfirmationModal
          title={
            selectedAction.type === 'delete'
              ? t('commentReports.confirmCommentDeletionTitle')
              : selectedAction.type === 'ban'
                ? t('commentReports.confirmUserBanTitle')
                : t('commentReports.resolveReport')
          }
          message={
            selectedAction.type === 'delete'
              ? t('commentReports.confirmCommentDeletionMessage')
              : selectedAction.type === 'ban'
                ? t('commentReports.confirmUserBanMessage')
                : t('commentReports.confirmResolveReportMessage')
          }
          onConfirm={async () => {
            if (selectedAction.type === 'delete') await handleDelete(selectedAction.item);
            if (selectedAction.type === 'ban') await handleBan(selectedAction.item);
            if (selectedAction.type === 'resolve') await handleResolve(selectedAction.item);
            setSelectedAction(null);
          }}
          onCancel={() => setSelectedAction(null)}
        />
      )}

      {!commentsLoading && comments?.links?.last?.pageNumber > 1 && (
        <div className="flex justify-center mt-8">
          <PaginationButton
            page={page}
            lastPage={comments.links.last.pageNumber}
            setPage={setPage}
          />
        </div>
      )}
    </div>
  );
}

