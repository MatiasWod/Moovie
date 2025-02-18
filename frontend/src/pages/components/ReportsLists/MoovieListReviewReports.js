import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import api from '../../../api/api';
import moovieListReviewApi from '../../../api/MoovieListReviewApi';
import userApi from '../../../api/UserApi';
import {useTranslation} from "react-i18next";
import ReportTypes from '../../../api/values/ReportTypes';

export default function MoovieListReviewReports() {
  const [reviews, setReviews] = useState([]);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    fetchReviews();
  }, []);

  const fetchReviews = async () => {
    const response = await reportApi.getReports({ contentType: 'listReview' });
    const reportsData = response.data || [];
    const reviewsToSet = [];
    const checkedUrls = [];

    for (const report of reportsData) {
      if (checkedUrls.includes(report.url)) continue;
      checkedUrls.push(report.url);
      
      const reviewResponse = await api.get(report.url);
      const review = reviewResponse.data;
      
      // Fetch report counts for each type
      const params = { contentType: 'moovieListReview', resourceId: review.id };
      
      const [abuseReports, hateReports, spamReports, privacyReports] = await Promise.all([
        reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy })
      ]);

      // Add report counts to the review object
      review.abuseReports = abuseReports.data.count;
      review.hateReports = hateReports.data.count;
      review.spamReports = spamReports.data.count;
      review.privacyReports = privacyReports.data.count;
      review.totalReports = review.abuseReports + review.hateReports + review.spamReports + review.privacyReports;

      reviewsToSet.push(review);
    }
    setReviews(reviewsToSet);
  };

  const handleDelete = async (review) => {
    await moovieListReviewApi.deleteMoovieListReviewById(review.id);
    fetchReviews();
  };

  const handleBan = async (review) => {
    const response = await api.get(review.creatorUrl);
    const user = response.data;
    await userApi.banUser(user.username);
    fetchReviews();
  };

  const handleResolve = async (review) => {
    await reportApi.resolveMoovieListReviewReport(review.id);
    fetchReviews();
  };

  return (
    <div>
      <h3 className="text-xl font-semibold mb-4">{t('moovieListReviewReports.moovieListReviewReports')}</h3>
      {reviews.length === 0 ? (
        <div className="text-center text-gray-500">{t('moovieListReviewReports.noMoovieListReviewReports')}</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {reviews.map((review, index) => (
            <div key={index} className="bg-white p-4 rounded shadow">
              <div className="flex justify-between items-center mb-2">
                <a href={review.creatorUrl} className="text-blue-600 font-bold hover:underline">
                  {review.creatorUrl?.split('/').pop()}
                </a>
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
                  {t('moovieListReviewReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:review})}
                  className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                >
                  {t('moovieListReviewReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:review})}
                  className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                >
                  {t('moovieListReviewReports.resolve')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationModal
          title={
            selectedAction.type === 'delete' ? 'Confirm Review Deletion' :
            selectedAction.type === 'ban' ? 'Confirm User Ban' : 
            'Resolve Report'
          }
          message={
            selectedAction.type === 'delete' ? 'Are you sure you want to delete this review?' :
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
