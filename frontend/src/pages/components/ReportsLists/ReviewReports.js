import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import api from '../../../api/api';
import mediaApi from '../../../api/MediaApi';
import reportApi from '../../../api/ReportApi';
import userApi from '../../../api/UserApi';
import { parsePaginatedResponse } from '../../../utils/ResponseUtils';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import PaginationButton from '../paginationButton/PaginationButton';
import EmptyState from './EmptyState';
import LoadingState from './LoadingState';
import ReportActionsButtons from './ReportActionsButtons';
import ReportCountsCard from './ReportCountsCard';

export default function ReviewReports() {
  const [reviews, setReviews] = useState({ reviews: [], links: {} });
  const [reviewsLoading, setReviewsLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const [page, setPage] = useState(1);
  const { t } = useTranslation();

  useEffect(() => {
    fetchReports();
  }, [page]);

  const fetchReports = async () => {
    setReviewsLoading(true);
    try {
      const res = await api.get('/reviews', {
        params: {
          isReported: true,
          pageNumber: page,
        },
      });
      const response = parsePaginatedResponse(res);
      const reviews = response.data || [];

      try {
        const allPromises = reviews.flatMap((review) => {
          const promises = [
            reportApi.getCountFromUrl(review.abuseReportsUrl),
            reportApi.getCountFromUrl(review.hateReportsUrl),
            reportApi.getCountFromUrl(review.spamReportsUrl),
            reportApi.getCountFromUrl(review.privacyReportsUrl),
          ];

          if (review.mediaId) {
            promises.push(mediaApi.getMediaById(review.mediaId));
          } else {
            promises.push(Promise.resolve({ data: null }));
          }

          return promises;
        });

        const allResults = await Promise.all(allPromises);

        const reviewsWithReports = reviews.map((review, index) => {
          const baseIndex = index * 5;

          return {
            ...review,
            abuseReports: allResults[baseIndex] || 0,
            hateReports: allResults[baseIndex + 1] || 0,
            spamReports: allResults[baseIndex + 2] || 0,
            privacyReports: allResults[baseIndex + 3] || 0,
            totalReports:
              (Number(allResults[baseIndex]) || 0) +
              (Number(allResults[baseIndex + 1]) || 0) +
              (Number(allResults[baseIndex + 2]) || 0) +
              (Number(allResults[baseIndex + 3]) || 0),
            mediaDetails: allResults[baseIndex + 4].data,
          };
        });

        setReviews({
          reviews: reviewsWithReports,
          links: response.links,
        });
      } catch (error) {
        console.error('Error fetching reviews:', error);
        setReviews([]);
      }
    } catch (error) {
      console.error('Error fetching reports:', error);
      setReviews([]);
    } finally {
      setReviewsLoading(false);
    }
  };

  const handleDelete = async (review) => {
    try {
      await api.delete(review.url);
      await fetchReports();
    } catch (error) {
      console.error('Error deleting review:', error);
    }
  };

  const handleBan = async (review) => {
    try {
      const response = await api.get(review.userUrl);
      const user = response.data;
      await userApi.banUser(user.url);
      await fetchReports();
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleResolve = async (review) => {
    try {
      await reportApi.resolveReports(review.reportsUrl);
      await fetchReports();
    } catch (error) {
      console.error('Error resolving report:', error);
    }
  };

  return (
    <div className="w-full">
      <div className="flex items-center gap-3 mb-6">
        <div className="p-2 bg-indigo-100 rounded-lg">
          <i className="bi bi-chat-square-text text-indigo-600 text-xl"></i>
        </div>
        <h2 className="text-2xl font-bold text-gray-800">{t('reviewReports.reviewReports')}</h2>
      </div>

      {reviewsLoading ? (
        <LoadingState message={t('reports.loading.reviews')} />
      ) : reviews.reviews.length === 0 ? (
        <EmptyState
          title={t('reports.empty.reviews')}
          message={t('reviewReports.noReviewReports')}
        />
      ) : (
        <div className="space-y-6">
          {reviews.reviews.map((review, index) => (
            <div
              key={index}
              className="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200"
            >
              <div className="p-6">
                <div className="flex justify-between items-start mb-4">
                  <div className="flex-1 space-y-3">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center">
                        <i className="bi bi-person text-indigo-600"></i>
                      </div>
                      <div>
                        <div className="flex items-center gap-2 flex-wrap">
                          <a
                            href={process.env.PUBLIC_URL + `/profile/${review.username}`}
                            className="font-semibold text-blue-600 hover:text-blue-800 hover:underline transition-colors"
                          >
                            {review.username}
                          </a>
                          {review.mediaDetails && (
                            <>
                              <span className="text-gray-400">•</span>
                              <span className="text-gray-600 text-sm">{t('reviews.onMedia')}</span>
                              <a
                                href={process.env.PUBLIC_URL + `/details/${review.mediaDetails.id}`}
                                className="font-medium text-blue-600 hover:text-blue-800 hover:underline transition-colors"
                              >
                                {review.mediaDetails.name}
                              </a>
                            </>
                          )}
                        </div>
                        <div className="flex items-center gap-2 mt-1">
                          <div className="flex items-center">
                            {[...Array(5)].map((_, i) => (
                              <i
                                key={i}
                                className={`bi bi-star${i < review.rating ? '-fill' : ''} text-yellow-400 text-sm`}
                              ></i>
                            ))}
                          </div>
                          <span className="text-gray-500 text-sm">({review.rating}/5)</span>
                          {review.lastModified && (
                            <>
                              <span className="text-gray-400">•</span>
                              <span className="text-gray-500 text-sm">
                                {t('reviews.lastModified')} {review.lastModified}
                              </span>
                            </>
                          )}
                        </div>
                      </div>
                    </div>

                    <div className="flex items-center gap-4 text-sm text-gray-600">
                      <div className="flex items-center gap-1">
                        <i className="bi bi-hand-thumbs-up text-green-500"></i>
                        <span>{review.likes || 0}</span>
                      </div>
                      <div className="flex items-center gap-1">
                        <i className="bi bi-hand-thumbs-down text-red-500"></i>
                        <span>{review.dislikes || 0}</span>
                      </div>
                    </div>
                  </div>

                  <div className="ml-6 text-right">
                    <ReportCountsCard
                      totalReports={review.totalReports}
                      spamReports={review.spamReports}
                      hateReports={review.hateReports}
                      abuseReports={review.abuseReports}
                      privacyReports={review.privacyReports}
                    />
                  </div>
                </div>

                <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-lg p-4 mb-4 border-l-4 border-indigo-300">
                  <div className="flex items-center gap-2 mb-2">
                    <i className="bi bi-chat-square-text text-gray-500"></i>
                    <span className="text-sm font-medium text-gray-600">
                      {t('reports.content.reportedReview')}
                    </span>
                  </div>
                  <p className="text-gray-800 leading-relaxed">{review.reviewContent}</p>
                </div>

                <ReportActionsButtons
                  onResolve={() => setSelectedAction({ type: 'resolve', item: review })}
                  onDelete={() => setSelectedAction({ type: 'delete', item: review })}
                  onBan={() => setSelectedAction({ type: 'ban', item: review })}
                  resolveKey="reviewReports.resolve"
                  deleteKey="reviewReports.delete"
                  banKey="reviewReports.banUser"
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
              ? t('reviewReports.confirmReviewDeletionTitle')
              : selectedAction.type === 'ban'
                ? t('reviewReports.confirmUserBanTitle')
                : t('reviewReports.resolveReport')
          }
          message={
            selectedAction.type === 'delete'
              ? t('reviewReports.confirmReviewDeletionMessage')
              : selectedAction.type === 'ban'
                ? t('reviewReports.confirmUserBanMessage')
                : t('reviewReports.confirmResolveReportMessage')
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

      {!reviewsLoading && reviews?.links?.last?.pageNumber > 1 && (
        <div className="flex justify-center mt-8">
          <PaginationButton
            page={page}
            lastPage={reviews.links.last.pageNumber}
            setPage={setPage}
          />
        </div>
      )}
    </div>
  );
}
