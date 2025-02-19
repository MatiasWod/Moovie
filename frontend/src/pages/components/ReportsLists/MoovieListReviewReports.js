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
    const response = await reportApi.getReports({ contentType: 'moovieListReview' });
    const reportsData = response.data || [];
    
    // Get unique URLs
    const uniqueUrls = [...new Set(reportsData.map(report => report.url))];
    
    // Fetch all reviews in parallel
    const reviewPromises = uniqueUrls.map(url => api.get(url));
    const reviewResponses = await Promise.all(reviewPromises);
    const reviews = reviewResponses.map(response => response.data);
    
    // Fetch all report counts in parallel
    const reportCountPromises = reviews.flatMap(review => {
      const params = { contentType: 'moovieListReview', resourceId: review.id };
      return [
        reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy })
      ];
    });
    
    const reportCounts = await Promise.all(reportCountPromises);
    
    // Add report counts to reviews
    const reviewsWithReports = reviews.map((review, index) => {
      const baseIndex = index * 4;
      return {
        ...review,
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

    setReviews(reviewsWithReports);
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
    <div className="container-fluid">
      <h3 className="text-xl font-semibold mb-4">{t('moovieListReviewReports.moovieListReviewReports')}</h3>
      {reviews.length === 0 ? (
        <div className="text-center text-gray-500">{t('moovieListReviewReports.noMoovieListReviewReports')}</div>
      ) : (
        <div className="space-y-4">
          {reviews.map((review, index) => (
            <div key={index} className="container-fluid bg-white my-3 p-4 rounded shadow">
              <div className="review-header d-flex align-items-center justify-between">
                <div>
                  <div className="flex items-center space-x-4">
                    <a href={review.creatorUrl} className="text-blue-600 font-bold hover:underline">
                      {review.creatorUrl?.split('/').pop()}
                    </a>
                  </div>
                  {review.lastModified && (
                    <div className="text-sm text-gray-500">
                      {t('reviews.lastModified')} {review.lastModified}
                    </div>
                  )}
                </div>
                <div>
                  <div className="text-sm text-gray-600 flex space-x-3">
                    <span className="flex items-center" title={t('reports.total')}>
                      <i className="bi bi-flag mr-1"></i>{review.totalReports}
                    </span>
                    <span className="flex items-center" title={t('reports.spam')}>
                      <i className="bi bi-envelope-exclamation mr-1"></i>{review.spamReports}
                    </span>
                    <span className="flex items-center" title={t('reports.hate')}>
                      <i className="bi bi-emoji-angry mr-1"></i>{review.hateReports}
                    </span>
                    <span className="flex items-center" title={t('reports.abuse')}>
                      <i className="bi bi-slash-circle mr-1"></i>{review.abuseReports}
                    </span>
                    <span className="flex items-center" title={t('reports.privacy')}>
                      <i className="bi bi-incognito mr-1"></i>{review.privacyReports}
                    </span>
                  </div>
                </div>
              </div>
              <div className="review-content my-4 text-gray-700">
                {review.reviewContent}
              </div>
              <div className="flex justify-end space-x-3">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:review})}
                  className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600"
                >
                  <i className="bi bi-trash mr-2"></i>
                  {t('moovieListReviewReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:review})}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                >
                  <i className="bi bi-person-x mr-2"></i>
                  {t('moovieListReviewReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:review})}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  <i className="bi bi-check2-circle mr-2"></i>
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
