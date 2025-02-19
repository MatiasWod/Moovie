import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import api from '../../../api/api';
import userApi from '../../../api/UserApi';
import commentApi from '../../../api/CommentApi';
import {useTranslation} from "react-i18next";
import ReportTypes from '../../../api/values/ReportTypes';
import { Tooltip } from "react-tooltip";

export default function CommentReports() {
  const [comments, setComments] = useState([]);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();
  // selectedAction = {type: 'delete'|'ban'|'resolve', item: comment}

  useEffect(() => {
    fetchComments();
  }, []);

  const fetchComments = async () => {
    const response = await reportApi.getReports({ contentType: 'comment' });
    const reportsData = response.data || [];
    
    // Get unique URLs
    const uniqueUrls = [...new Set(reportsData.map(report => report.url))];
    
    // Fetch all comments in parallel
    const commentPromises = uniqueUrls.map(url => api.get(url));
    const commentResponses = await Promise.all(commentPromises);
    const comments = commentResponses.map(response => response.data);
    
    // Fetch all report counts in parallel
    const reportCountPromises = comments.flatMap(comment => {
      const params = { contentType: 'comment', resourceId: comment.id };
      return [
        reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy })
      ];
    });
    
    const reportCounts = await Promise.all(reportCountPromises);
    
    // Add report counts to comments
    const commentsWithReports = comments.map((comment, index) => {
      const baseIndex = index * 4;
      return {
        ...comment,
        abuseReports: reportCounts[baseIndex].data.count,
        hateReports: reportCounts[baseIndex + 1].data.count,
        spamReports: reportCounts[baseIndex + 2].data.count,
        privacyReports: reportCounts[baseIndex + 3].data.count,
        totalReports: reportCounts[baseIndex].data.count + 
                     reportCounts[baseIndex + 1].data.count + 
                     reportCounts[baseIndex + 2].data.count + 
                     reportCounts[baseIndex + 3].data.count
      };
    });

    setComments(commentsWithReports);
  };

  const handleDelete = async (comment) => {
    await commentApi.deleteComment(comment.id);
    fetchComments();
  };

  const handleBan = async (comment) => {
    const response = await api.get(comment.userUrl);
    const user = response.data;
    await userApi.banUser(user.username);
    fetchComments();
  };

  const handleResolve = async (comment) => {
    console.log('resolving report', comment);
    await reportApi.resolveCommentReport(comment.id);
    fetchComments();
  };

  return (
    <div>
      <h3 className="text-xl font-semibold mb-4">{t('commentReports.commentReports')}</h3>
      {comments.length === 0 ? (
        <div className="text-center text-gray-500">{t('commentReports.noCommentReports')}</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {comments.map((comment, index) => (
            <div key={index} className="bg-white p-4 rounded shadow">
              <div className="flex justify-between items-center mb-2">
                <a href={comment.userUrl} className="text-blue-600 font-bold hover:underline">
                  {comment.userUrl?.split('/').pop()}
                </a>
                <div className="text-sm text-gray-600 flex space-x-2">
                  <span className="flex items-center" data-tooltip-id="total-reports-tooltip"><i className="bi bi-flag mr-1"></i>{comment.totalReports}</span>
                  <span className="flex items-center" data-tooltip-id="spam-reports-tooltip"><i className="bi bi-envelope-exclamation mr-1"></i>{comment.spamReports}</span>
                  <span className="flex items-center" data-tooltip-id="hate-reports-tooltip"><i className="bi bi-emoji-angry mr-1"></i>{comment.hateReports}</span>
                  <span className="flex items-center" data-tooltip-id="abuse-reports-tooltip"><i className="bi bi-slash-circle mr-1"></i>{comment.abuseReports}</span>
                  <Tooltip id="total-reports-tooltip" place="bottom" effect="solid">
                    {t('commentReports.totalReports')}
                  </Tooltip>
                  <Tooltip id="spam-reports-tooltip" place="bottom" effect="solid">
                    {t('commentReports.spamReports')}
                  </Tooltip>
                  <Tooltip id="hate-reports-tooltip" place="bottom" effect="solid">
                    {t('commentReports.hateReports')}
                  </Tooltip>
                  <Tooltip id="abuse-reports-tooltip" place="bottom" effect="solid">
                    {t('commentReports.abuseReports')}
                  </Tooltip>
                </div>
              </div>
              <p className="mb-4 text-gray-700">{comment.content}</p>
              <div className="flex justify-evenly">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:comment})}
                  className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                >
                  {t('commentReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:comment})}
                  className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                >
                  {t('commentReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:comment})}
                  className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                >
                  {t('commentReports.resolve')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationModal
          title={
            selectedAction.type === 'delete' ? t('commentReports.confirmCommentDeletionTitle') :
            selectedAction.type === 'ban' ? t('commentReports.confirmUserBanTitle') :
            t('commentReports.resolveReport')
          }
          message={
            selectedAction.type === 'delete' ? t('commentReports.confirmCommentDeletionMessage') :
            selectedAction.type === 'ban' ? t('commentReports.confirmUserBanMessage') :
            t('commentReports.confirmResolveReportMessage')
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
    </div>
  );
}
