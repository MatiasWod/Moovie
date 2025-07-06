import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import api from '../../../api/api';
import reportApi from '../../../api/ReportApi';
import reviewApi from '../../../api/ReviewApi';
import userApi from '../../../api/UserApi';
import { parsePaginatedResponse } from "../../../utils/ResponseUtils";
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import PaginationButton from "../paginationButton/PaginationButton";
import EmptyState from './EmptyState';
import LoadingState from './LoadingState';
import ReportActionsButtons from './ReportActionsButtons';
import ReportCountsCard from './ReportCountsCard';

export default function MoovieListReviewReports() {
  const [page, setPage] = useState(1);
  const [reviews, setReviews] = useState({ reviews: [], links: {} });
  const [reviewsLoading, setReviewsLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    fetchReviews();
  }, [page]);

  const fetchReviews = async () => {
    setReviewsLoading(true);
    try {
      const res = await reportApi.getReports({ contentType: 'moovieListReview', pageNumber: page });
      const response = parsePaginatedResponse(res)
      const reportsData = response.data || [];

      const uniqueReviewUrls = [...new Set(reportsData.map((report) => report.moovieListReviewUrl))];

      try {
        const reviewPromises = uniqueReviewUrls.map((url) => api.get(url));
        const reviewResponses = await Promise.all(reviewPromises);
        const reviews = reviewResponses.map((response) => response.data);

        try {
          const allPromises = reviews.flatMap((review) => [
            reportApi.getCountFromUrl(review.abuseReportsUrl),
            reportApi.getCountFromUrl(review.hateReportsUrl),
            reportApi.getCountFromUrl(review.spamReportsUrl),
            reportApi.getCountFromUrl(review.privacyReportsUrl),
            api.get(review.moovieListUrl),
          ]);

          const allResults = await Promise.all(allPromises);

          const reviewsWithDetails = reviews.map((review, index) => {
            const baseIndex = index * 5;

            const reviewReports = reportsData.filter(report => report.moovieListReviewUrl === review.url);
            const reportIds = reviewReports.map(report => report.reportId);

            return {
              ...review,
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
              moovieListDetails: allResults[baseIndex + 4]?.data,
            };
          });

          setReviews({
            reviews: reviewsWithDetails,
            links: response.links,
          });
        } catch (error) {
          console.error('Error fetching additional details:', error);
          setReviews(reviews);
        }
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
      await reviewApi.deleteReview(review.id);
      await fetchReviews();
    } catch (error) {
      console.error('Error deleting review:', error);
    }
  };

  const handleBan = async (review) => {
    try {
      const response = await api.get(review.userUrl);
      const user = response.data;
      await userApi.banUser(user.username);
      await fetchReviews();
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleResolve = async (review) => {
    try {
      if (review.reportIds && review.reportIds.length > 0) {
        await Promise.all(review.reportIds.map(reportId =>
          reportApi.moovieListReviewReports.resolveReport(reportId)
        ));
      }
      await fetchReviews();
    } catch (error) {
      console.error('Error resolving report:', error);
    }
  };

  return (
    <div className="w-full">
      <div className="flex items-center gap-3 mb-6">
        <div className="p-2 bg-yellow-100 rounded-lg">
          <i className="bi bi-star text-yellow-600 text-xl"></i>
        </div>
        <h2 className="text-2xl font-bold text-gray-800">{t('moovieListReviewReports.moovieListReviewReports')}</h2>
      </div>
      
      {reviewsLoading ? (
        <LoadingState message={t('reports.loading.reviews')} />
      ) : reviews.reviews.length === 0 ? (
        <EmptyState 
          title={t('reports.empty.reviews')} 
          message={t('moovieListReviewReports.noMoovieListReviewReports')} 
        />
      ) : (
        <div className="space-y-6">
          {reviews.reviews.map((review, index) => (
            <div key={index} className="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200">
              <div className="p-6">
                <div className="flex justify-between items-start mb-4">
                  <div className="flex-1 space-y-3">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 bg-yellow-100 rounded-full flex items-center justify-center">
                        <i className="bi bi-star text-yellow-600"></i>
                      </div>
                      <div>
                        <div className="flex items-center gap-2">
                          <a
                            href={process.env.PUBLIC_URL + `/profile/${review.username}`}
                            className="font-semibold text-blue-600 hover:text-blue-800 hover:underline transition-colors"
                          >
                            {review.username}
                          </a>
                          <span className="text-gray-400">•</span>
                          <span className="text-gray-600 text-sm">{t('reviews.onMedia')}</span>
                          <a
                            href={process.env.PUBLIC_URL + `/list/${review.moovieListDetails?.id}`}
                            className="font-medium text-blue-600 hover:text-blue-800 hover:underline transition-colors"
                          >
                            {review.moovieListDetails?.name}
                          </a>
                        </div>
                      </div>
                    </div>

                    <div className="flex items-center gap-4 text-sm text-gray-600">
                      <div className="flex items-center gap-1">
                        <i className="bi bi-hand-thumbs-up text-green-500"></i>
                        <span>{review.reviewLikes}</span>
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

                {review.moovieListDetails?.images && review.moovieListDetails.images.length > 0 && (
                  <div className="mb-4">
                    <div className="flex items-center gap-2 mb-3">
                      <i className="bi bi-collection text-gray-500"></i>
                      <span className="text-sm font-medium text-gray-600">{t('reports.content.listPreview')}</span>
                    </div>
                    <div className="flex gap-3 overflow-x-auto py-2">
                      {review.moovieListDetails.images.slice(0, 3).map((image, imgIndex) => (
                        <img
                          key={imgIndex}
                          src={image}
                          alt={`List preview ${imgIndex + 1}`}
                          className="h-20 w-32 object-cover rounded-lg shadow-sm border border-gray-200 flex-shrink-0"
                        />
                      ))}
                    </div>
                  </div>
                )}

                <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-lg p-4 mb-4 border-l-4 border-yellow-300">
                  <div className="flex items-center gap-2 mb-2">
                    <i className="bi bi-star text-gray-500"></i>
                    <span className="text-sm font-medium text-gray-600">{t('reports.content.reportedReview')}</span>
                  </div>
                  <p className="text-gray-800 leading-relaxed">{review.reviewContent}</p>
                </div>

                <ReportActionsButtons
                  onResolve={() => setSelectedAction({ type: 'resolve', item: review })}
                  onDelete={() => setSelectedAction({ type: 'delete', item: review })}
                  onBan={() => setSelectedAction({ type: 'ban', item: review })}
                  resolveKey="moovieListReviewReports.resolve"
                  deleteKey="moovieListReviewReports.delete"
                  banKey="moovieListReviewReports.banUser"
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
