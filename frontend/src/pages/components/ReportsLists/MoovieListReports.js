import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import api from '../../../api/api';
import ListApi from '../../../api/ListApi';
import userApi from '../../../api/UserApi';
import {useTranslation} from "react-i18next";
import ReportTypes from '../../../api/values/ReportTypes';

export default function MoovieListReports() {
  const [lists, setLists] = useState([]);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    fetchLists();
  }, []);

  const fetchLists = async () => {
    const response = await reportApi.getReports({ contentType: 'moovieList' });
    const reportsData = response.data || [];
    
    // Get unique URLs
    const uniqueUrls = [...new Set(reportsData.map(report => report.url))];
    
    // Fetch all lists in parallel
    const listPromises = uniqueUrls.map(url => api.get(url));
    const listResponses = await Promise.all(listPromises);
    const lists = listResponses.map(response => response.data);
    
    // Fetch all report counts in parallel
    const reportCountPromises = lists.flatMap(list => {
      const params = { contentType: 'moovieList', resourceId: list.id };
      return [
        reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy })
      ];
    });
    
    const reportCounts = await Promise.all(reportCountPromises);
    
    // Add report counts to lists
    const listsWithReports = lists.map((list, index) => {
      const baseIndex = index * 4;
      return {
        ...list,
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

    setLists(listsWithReports);
  };

  const handleDelete = async (ml) => {
    await ListApi.deleteList(ml.id);
    fetchLists();
  };

  const handleBan = async (ml) => {
    const response = await api.get(ml.creatorUrl);
    const user = response.data;
    await userApi.banUser(user.username);
    fetchLists();
  };

  const handleResolve = async (ml) => {
    await reportApi.resolveMoovieListReport(ml.id);
    fetchLists();
  };

  return (
    <div className="container-fluid">
      <h3 className="text-xl font-semibold mb-4">{t('moovieListReports.moovieListReports')}</h3>
      {lists.length === 0 ? (
        <div className="text-center text-gray-500">{t('moovieListReports.noMoovieListReports')}</div>
      ) : (
        <div className="space-y-4">
          {lists.map((ml, index) => (
            <div key={index} className="container-fluid bg-white my-3 p-4 rounded shadow">
              <div className="review-header d-flex align-items-center justify-between">
                <div>
                  <div className="flex items-center space-x-4">
                    <a href={ml.creatorUrl} className="text-blue-600 font-bold hover:underline">
                      {ml.creatorUrl?.split('/').pop()}
                    </a>
                  </div>
                  <h4 className="text-lg font-bold text-blue-600 hover:underline">
                    <a href={ml.url}>{ml.name}</a>
                  </h4>
                </div>
                <div>
                  <div className="text-sm text-gray-600 flex space-x-3">
                    <span className="flex items-center" title={t('reports.total')}>
                      <i className="bi bi-flag mr-1"></i>{ml.totalReports}
                    </span>
                    <span className="flex items-center" title={t('reports.spam')}>
                      <i className="bi bi-envelope-exclamation mr-1"></i>{ml.spamReports}
                    </span>
                    <span className="flex items-center" title={t('reports.hate')}>
                      <i className="bi bi-emoji-angry mr-1"></i>{ml.hateReports}
                    </span>
                    <span className="flex items-center" title={t('reports.abuse')}>
                      <i className="bi bi-slash-circle mr-1"></i>{ml.abuseReports}
                    </span>
                    <span className="flex items-center" title={t('reports.privacy')}>
                      <i className="bi bi-incognito mr-1"></i>{ml.privacyReports}
                    </span>
                  </div>
                </div>
              </div>
              <div className="review-content my-4 text-gray-700">
                {ml.description}
              </div>
              <div className="flex justify-end space-x-3">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:ml})}
                  className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600"
                >
                  <i className="bi bi-trash mr-2"></i>
                  {t('moovieListReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:ml})}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                >
                  <i className="bi bi-person-x mr-2"></i>
                  {t('moovieListReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:ml})}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  <i className="bi bi-check2-circle mr-2"></i>
                  {t('moovieListReports.resolve')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationModal
          title={
            selectedAction.type === 'delete' ? 'Confirm List Deletion' :
            selectedAction.type === 'ban' ? 'Confirm User Ban' : 
            'Resolve Report'
          }
          message={
            selectedAction.type === 'delete' ? 'Are you sure you want to delete this list?' :
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
