import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import reviewApi from '../../../api/ReviewApi';
import userApi from '../../../api/UserApi';
import ConfirmationForm from '../../components/forms/confirmationForm/confirmationForm';
import api from '../../../api/api';
import {useTranslation} from "react-i18next";

export default function ReviewReports() {
  const [reviews, setReviews] = useState([]);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    fetchReports();
  }, []);

  const fetchReports = async () => {
    const response = await reportApi.getReports({ contentType: 'review' });
    const reportsData = response.data || [];
    const reviewsToSet = [];
    const checkedUrls = [];
    
    for (const report of reportsData) {
      if (checkedUrls.includes(report.url)) continue;
      checkedUrls.push(report.url);
      const response = await api.get(report.url);
      reviewsToSet.push(response.data);
    }
    setReviews(reviewsToSet);
  };

  const handleDelete = async (review) => {
    await reviewApi.deleteReview(review.id);
    fetchReports();
  };

  const handleBan = async (review) => {
    const response = await api.get(review.userUrl);
    const user = response.data;
    await userApi.banUser(user.username);
    fetchReports();
  };

  const handleResolve = async (review) => {
    await reportApi.resolveReviewReport(review.id);
    fetchReports();
  };

  return (
    <div>
      <h3 className="text-xl font-semibold mb-4">
        {t('reviewReports.reviewReports')}
      </h3>
      {reviews.length === 0 ? (
        <div className="text-center text-gray-500">{t('reviewReports.noReviewReports')}</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {reviews.map((review, index) => (
            <div key={index} className="bg-white p-4 rounded shadow">
              <div className="flex justify-between items-center mb-2">
                <div className="flex items-center space-x-4">
                  <a href={`/profile/${review.userUrl?.split('/').pop()}`} className="text-blue-600 font-bold hover:underline">
                    {review.userUrl?.split('/').pop()}
                  </a>
                  <span className="text-gray-600">{review.rating}/5 <i className="bi bi-star-fill text-yellow-500"></i></span>
                </div>
                <div className="text-sm text-gray-600 flex space-x-2">
                  <span className="flex items-center"><i className="bi bi-flag mr-1"></i>{review.totalReports}</span>
                  <span className="flex items-center"><i className="bi bi-envelope-exclamation mr-1"></i>{review.spamReports}</span>
                  <span className="flex items-center"><i className="bi bi-emoji-angry mr-1"></i>{review.hateReports}</span>
                  <span className="flex items-center"><i className="bi bi-slash-circle mr-1"></i>{review.abuseReports}</span>
                  <span className="flex items-center"><i className="bi bi-incognito mr-1"></i>{review.privacyReports}</span>
                </div>
              </div>
              <p className="mb-4 text-gray-700">{review.reviewContent}</p>
              <div className="flex justify-evenly">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:review})}
                  className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                >
                  {t('reviewReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:review})}
                  className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                >
                  {t('reviewReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:review})}
                  className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                >
                  {t('reviewReports.resolve')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationForm
          service={selectedAction.type === 'delete' ? reviewApi.deleteReview :
            selectedAction.type === 'ban' ? userApi.banUser :
            reportApi.resolveReviewReport}
          actionName={
            selectedAction.type === 'delete' ? 'Delete Review' :
            selectedAction.type === 'ban' ? 'Ban User' : 
            'Resolve Report'
          }
          serviceParams={
            selectedAction.type === 'delete' ? [selectedAction.item.id] :
            selectedAction.type === 'ban' ? [selectedAction.item.userUrl] :
            [selectedAction.item.id]
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
