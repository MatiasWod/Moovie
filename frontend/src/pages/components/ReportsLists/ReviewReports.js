import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import reviewApi from '../../../api/ReviewApi';
import userApi from '../../../api/UserApi';
import ConfirmationForm from '../../components/forms/confirmationForm/confirmationForm';
import api from '../../../api/api';
import {useTranslation} from "react-i18next";
import ReportTypes from '../../../api/values/ReportTypes';
import mediaApi from '../../../api/MediaApi';

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
      
      const reviewResponse = await api.get(report.url);
      const review = reviewResponse.data;
      
      // Fetch report counts for each type
      const params = { contentType: 'review', resourceId: review.id };
      
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

      // Fetch media details if available
      if (review.mediaId) {
        const mediaResponse = await mediaApi.getMediaById(review.mediaId);
        review.mediaDetails = mediaResponse.data;
      }

      reviewsToSet.push(review);
    }
    console.log(reviewsToSet);
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
    <div className="container-fluid">
      <h3 className="text-xl font-semibold mb-4">
        {t('reviewReports.reviewReports')}
      </h3>
      {reviews.length === 0 ? (
        <div className="text-center text-gray-500">{t('reviewReports.noReviewReports')}</div>
      ) : (
        <div className="space-y-4">
          {reviews.map((review, index) => (
            <div key={index} className="review container-fluid bg-white my-3 p-4 rounded shadow">
              <div className="review-header d-flex align-items-center justify-between">
                <div>
                  <div className="flex items-center space-x-4">
                    <a href={`/profile/${review.username}`} className="text-blue-600 font-bold hover:underline">
                      {review.username}
                    </a>
                    {review.mediaDetails && (
                      <span className="text-gray-600">
                        {t('reviews.onMedia')} <a href={`/details/${review.mediaDetails.id}`} className="text-blue-600 hover:underline">
                          {review.mediaDetails.name}
                        </a>
                      </span>
                    )}
                  </div>
                  {review.lastModified && (
                    <div className="text-sm text-gray-500">
                      {t('reviews.lastModified')} {review.lastModified}
                    </div>
                  )}
                </div>
                <div>
                  <div className="text-sm text-gray-600 flex space-x-3 mb-2">
                    <span className="flex items-center">
                      <i className="bi bi-star-fill text-yellow-500 mr-1"></i>
                      {review.rating}/5
                    </span>
                    <span className="flex items-center">
                      <i className="bi bi-hand-thumbs-up mr-1"></i>
                      {review.likes || 0}
                    </span>
                  </div>
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
                  {t('reviewReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:review})}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                >
                  <i className="bi bi-person-x mr-2"></i>
                  {t('reviewReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:review})}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  <i className="bi bi-check2-circle mr-2"></i>
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
