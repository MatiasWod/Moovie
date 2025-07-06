import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import api from '../../../api/api';
import { default as ListApi, default as listApi } from '../../../api/ListApi';
import reportApi from '../../../api/ReportApi';
import userApi from '../../../api/UserApi';
import { parsePaginatedResponse } from "../../../utils/ResponseUtils";
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import PaginationButton from "../paginationButton/PaginationButton";
import EmptyState from './EmptyState';
import LoadingState from './LoadingState';
import ReportActionsButtons from './ReportActionsButtons';
import ReportCountsCard from './ReportCountsCard';

export default function MoovieListReports() {
  const [page, setPage] = useState(1);
  const [lists, setLists] = useState({ lists: [], links: {} });
  const [listsLoading, setListsLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    fetchLists();
  }, [page]);

  const fetchLists = async () => {
    setListsLoading(true);
    try {
      const res = await listApi.getReportedLists(page);
      const response = parsePaginatedResponse(res)
      const lists = response.data || [];

      try {
        try {
          const reportCountPromises = lists.flatMap((list) => {
            return [
              reportApi.getCountFromUrl(list.abuseReportsUrl),
              reportApi.getCountFromUrl(list.hateReportsUrl),
              reportApi.getCountFromUrl(list.spamReportsUrl),
              reportApi.getCountFromUrl(list.privacyReportsUrl),
            ];
          });

          const reportCounts = await Promise.all(reportCountPromises);

          const listsWithReports = lists.map((list, index) => {
            const baseIndex = index * 4;

            return {
              ...list,
              abuseReports: reportCounts[baseIndex] || 0,
              hateReports: reportCounts[baseIndex + 1] || 0,
              spamReports: reportCounts[baseIndex + 2] || 0,
              privacyReports: reportCounts[baseIndex + 3] || 0,
              totalReports:
                (Number(reportCounts[baseIndex]) || 0) +
                (Number(reportCounts[baseIndex + 1]) || 0) +
                (Number(reportCounts[baseIndex + 2]) || 0) +
                (Number(reportCounts[baseIndex + 3]) || 0),
            };
          });

          setLists({
            lists: listsWithReports,
            links: response.links,
          });
        } catch (error) {
          console.error('Error fetching report counts:', error);
          setLists(lists);
        }
      } catch (error) {
        console.error('Error fetching lists:', error);
        setLists([]);
      }
    } catch (error) {
      console.error('Error fetching reports:', error);
      setLists([]);
    } finally {
      setListsLoading(false);
    }
  };

  const handleDelete = async (ml) => {
    try {
      await ListApi.deleteList(ml.id);
      await fetchLists();
    } catch (error) {
      console.error('Error deleting list:', error);
    }
  };

  const handleBan = async (ml) => {
    try {
      const response = await api.get(ml.creatorUrl);
      const user = response.data;
      await userApi.banUser(user.username);
      await fetchLists();
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleResolve = async (ml) => {
    try {
      if (ml.reportIds && ml.reportIds.length > 0) {
        await Promise.all(ml.reportIds.map(reportId =>
          reportApi.moovieListReports.resolveReport(reportId)
        ));
      }
      await fetchLists();
    } catch (error) {
      console.error('Error resolving report:', error);
    }
  };

  return (
    <div className="w-full">
      <div className="flex items-center gap-3 mb-6">
        <div className="p-2 bg-purple-100 rounded-lg">
          <i className="bi bi-collection text-purple-600 text-xl"></i>
        </div>
        <h2 className="text-2xl font-bold text-gray-800">{t('moovieListReports.moovieListReports')}</h2>
      </div>
      
      {listsLoading ? (
        <LoadingState message={t('reports.loading.lists')} />
      ) : lists.lists.length === 0 ? (
        <EmptyState 
          title={t('reports.empty.lists')} 
          message={t('moovieListReports.noMoovieListReports')} 
        />
      ) : (
        <div className="space-y-6">
          {lists.lists.map((ml, index) => (
            <div key={index} className="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200">
              <div className="p-6">
                <div className="flex justify-between items-start mb-6">
                  <div className="flex-1 space-y-3">
                    <div className="flex items-start gap-3">
                      <div className="w-10 h-10 bg-purple-100 rounded-full flex items-center justify-center flex-shrink-0">
                        <i className="bi bi-collection text-purple-600"></i>
                      </div>
                      <div>
                        <h3 className="text-xl font-bold">
                          <a
                            href={process.env.PUBLIC_URL + `/list/${ml.id}`}
                            className="text-blue-600 hover:text-blue-800 hover:underline transition-colors"
                          >
                            {ml.name}
                          </a>
                        </h3>
                        <div className="flex items-center gap-2 text-sm text-gray-600 mt-1">
                          <span>{t('listHeader.by')}</span>
                          <a
                            href={process.env.PUBLIC_URL + `/profile/${ml.createdBy}`}
                            className="font-medium text-blue-600 hover:text-blue-800 hover:underline transition-colors"
                          >
                            {ml.createdBy}
                          </a>
                        </div>
                      </div>
                    </div>

                    <div className="flex items-center gap-6 text-sm text-gray-600 bg-gray-50 rounded-lg p-3">
                      <div className="flex items-center gap-2">
                        <i className="bi bi-film text-blue-500"></i>
                        <span className="font-medium">{ml.movieCount}</span>
                        <span>{t('listCard.movies')}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <i className="bi bi-tv text-green-500"></i>
                        <span className="font-medium">{ml.mediaCount - ml.movieCount}</span>
                        <span>{t('listCard.series')}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <i className="bi bi-people text-purple-500"></i>
                        <span className="font-medium">{ml.followers}</span>
                        <span>{t('listHeader.followers')}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <i className="bi bi-hand-thumbs-up text-red-500"></i>
                        <span className="font-medium">{ml.likes}</span>
                        <span>{t('reports.text.likes')}</span>
                      </div>
                    </div>
                  </div>

                  <div className="ml-6 text-right">
                    <ReportCountsCard
                      totalReports={ml.totalReports}
                      spamReports={ml.spamReports}
                      hateReports={ml.hateReports}
                      abuseReports={ml.abuseReports}
                      privacyReports={ml.privacyReports}
                    />
                  </div>
                </div>

                {ml.images && ml.images.length > 0 && (
                  <div className="mb-4">
                    <div className="flex items-center gap-2 mb-3">
                      <i className="bi bi-images text-gray-500"></i>
                      <span className="text-sm font-medium text-gray-600">{t('reports.content.previewImages')}</span>
                    </div>
                    <div className="flex gap-3 overflow-x-auto py-2">
                      {ml.images.slice(0, 4).map((image, imgIndex) => (
                        <img
                          key={imgIndex}
                          src={image}
                          alt={`List preview ${imgIndex + 1}`}
                          className="h-24 w-40 object-cover rounded-lg shadow-sm border border-gray-200 flex-shrink-0"
                        />
                      ))}
                    </div>
                  </div>
                )}

                <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-lg p-4 mb-4 border-l-4 border-purple-300">
                  <div className="flex items-center gap-2 mb-2">
                    <i className="bi bi-text-paragraph text-gray-500"></i>
                    <span className="text-sm font-medium text-gray-600">{t('reports.content.description')}</span>
                  </div>
                  <p className="text-gray-800 leading-relaxed">{ml.description}</p>
                </div>

                <ReportActionsButtons
                  onResolve={() => setSelectedAction({ type: 'resolve', item: ml })}
                  onDelete={() => setSelectedAction({ type: 'delete', item: ml })}
                  onBan={() => setSelectedAction({ type: 'ban', item: ml })}
                  resolveKey="moovieListReports.resolve"
                  deleteKey="moovieListReports.delete"
                  banKey="moovieListReports.banUser"
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
              ? t('reports.confirmListDeletionTitle')
              : selectedAction.type === 'ban'
                ? t('reports.confirmUserBanTitle')
                : t('reports.resolveReport')
          }
          message={
            selectedAction.type === 'delete'
              ? t('reports.confirmListDeletionMessage')
              : selectedAction.type === 'ban'
                ? t('reports.confirmUserBanMessage')
                : t('reports.confirmResolveReportMessage')
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

      {!listsLoading && lists?.links?.last?.pageNumber > 1 && (
        <div className="flex justify-center mt-8">
          <PaginationButton
            page={page}
            lastPage={lists.links.last.pageNumber}
            setPage={setPage}
          />
        </div>
      )}
    </div>
  );
}
