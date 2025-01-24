import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationForm';
import api from '../../../api/api';
import userApi from '../../../api/UserApi';
import commentApi from '../../../api/CommentApi';
import {useTranslation} from "react-i18next";

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
    const commentsToSet = [];
    const checkedUrls = [];

    for (const report of reportsData) {
      if (checkedUrls.includes(report.url)) continue;
      checkedUrls.push(report.url);
      const response = await api.get(report.url);
      commentsToSet.push(response.data);
    }
    setComments(commentsToSet);
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
                  <span className="flex items-center"><i className="bi bi-flag mr-1"></i>{comment.totalReports}</span>
                  <span className="flex items-center"><i className="bi bi-envelope-exclamation mr-1"></i>{comment.spamReports}</span>
                  <span className="flex items-center"><i className="bi bi-emoji-angry mr-1"></i>{comment.hateReports}</span>
                  <span className="flex items-center"><i className="bi bi-slash-circle mr-1"></i>{comment.abuseReports}</span>
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
            selectedAction.type === 'delete' ? 'Confirm Comment Deletion' :
            selectedAction.type === 'ban' ? 'Confirm User Ban' : 
            'Resolve Report'
          }
          message={
            selectedAction.type === 'delete' ? 'Are you sure you want to delete this comment?' :
            selectedAction.type === 'ban' ? 'Are you sure you want to ban this user?' :
            'Are you sure you want to mark this report as resolved?'
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
