import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationForm from '../../components/forms/confirmationForm/confirmationForm';
import api from '../../../api/api';
import moovieListApi from '../../../api/MoovieListApi.js';
import userApi from '../../../api/UserApi';
import {useTranslation} from "react-i18next";

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
      const response = await api.get(report.url);
      listsToSet.push(response.data);
    }
    setLists(listsToSet);
  };

  const handleDelete = async (ml) => {
    await moovieListApi.deleteMoovieList(ml.id);
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
        <ConfirmationForm
          service={selectedAction.type === 'delete' ? moovieListApi.deleteMoovieList :
            selectedAction.type === 'ban' ? userApi.banUser :
            reportApi.resolveMoovieListReport}
          actionName={
            selectedAction.type === 'delete' ? 'Delete MoovieList' :
            selectedAction.type === 'ban' ? 'Ban User' : 
            'Resolve Report'
          }
          serviceParams={
            selectedAction.type === 'delete' ? [selectedAction.item.id] :
            selectedAction.type === 'ban' ? [selectedAction.item.creatorUrl] :
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
