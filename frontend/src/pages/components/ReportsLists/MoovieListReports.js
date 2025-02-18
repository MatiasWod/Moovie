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
    const response = await reportApi.getReports({ contentType: 'list' });
    const reportsData = response.data || [];
    const listsToSet = [];
    const checkedUrls = [];

    for (const report of reportsData) {
      if (checkedUrls.includes(report.url)) continue;
      checkedUrls.push(report.url);
      
      const listResponse = await api.get(report.url);
      const list = listResponse.data;
      
      // Fetch report counts for each type
      const params = { contentType: 'moovieList', resourceId: list.id };
      
      const [abuseReports, hateReports, spamReports, privacyReports] = await Promise.all([
        reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
        reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy })
      ]);

      // Add report counts to the list object
      list.abuseReports = abuseReports.data.count;
      list.hateReports = hateReports.data.count;
      list.spamReports = spamReports.data.count;
      list.privacyReports = privacyReports.data.count;
      list.totalReports = list.abuseReports + list.hateReports + list.spamReports + list.privacyReports;

      listsToSet.push(list);
    }
    setLists(listsToSet);
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
    <div>
      <h3 className="text-xl font-semibold mb-4">{t('moovieListReports.moovieListReports')}</h3>
      {lists.length === 0 ? (
        <div className="text-center text-gray-500">{t('moovieListReports.noMoovieListReports')}</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {lists.map((ml, index) => (
            <div key={index} className="bg-white p-4 rounded shadow">
              <div className="flex justify-between items-center mb-2">
                <a href={ml.creatorUrl} className="text-blue-600 font-bold hover:underline">
                  {ml.creatorUrl?.split('/').pop()}
                </a>
                <div className="text-sm text-gray-600 flex space-x-2">
                  <span className="flex items-center"><i className="bi bi-flag mr-1"></i>{ml.totalReports}</span>
                  <span className="flex items-center"><i className="bi bi-envelope-exclamation mr-1"></i>{ml.spamReports}</span>
                  <span className="flex items-center"><i className="bi bi-emoji-angry mr-1"></i>{ml.hateReports}</span>
                  <span className="flex items-center"><i className="bi bi-slash-circle mr-1"></i>{ml.abuseReports}</span>
                  <span className="flex items-center"><i className="bi bi-incognito mr-1"></i>{ml.privacyReports}</span>
                </div>
              </div>
              <h4 className="text-lg font-bold text-blue-600 hover:underline mb-2">
                <a href={ml.url}>{ml.name}</a>
              </h4>
              <p className="text-gray-700 mb-4">{ml.description}</p>
              <div className="flex justify-evenly">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:ml})}
                  className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                >
                  {t('moovieListReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:ml})}
                  className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                >
                  {t('moovieListReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:ml})}
                  className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                >
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
