import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationForm';
import {useTranslation} from "react-i18next";

export default function BannedUsers() {
  const [users, setUsers] = useState([]);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    fetchBannedUsers();
  }, []);

  const fetchBannedUsers = async () => {
    const response = await reportApi.getReports({ contentType: 'banned' });
    setUsers(response.data || []);
  };

  const handleUnban = async (item) => {
    // Assume we can unban using userUrl:
    await reportApi.unbanUser(item.userUrl);
    fetchBannedUsers();
  };

  return (
    <div>
      <h3 className="text-xl font-semibold mb-4">{t('bannedUsers.bannedUsers')}</h3>
      {users.length === 0 ? (
        <div className="text-center text-gray-500">{t('bannedUsers.noBannedUsers')}</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {users.map((user, index) => (
            <div key={index} className="bg-white p-4 rounded shadow flex items-center justify-between">
              <div className="flex items-center space-x-4">
                {/* Assuming user has an imageUrl */}
                <img src={user.imageUrl || '/resources/defaultProfile.jpg'} alt="profile" className="h-12 w-12 rounded-full border" />
                <a href={user.userUrl} className="text-blue-600 font-bold hover:underline">{user.userUrl?.split('/').pop()}</a>
              </div>
              <button
                onClick={() => setSelectedAction({type:'unban', item:user})}
                className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
              >
                Unban User
              </button>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationModal
          title={t('bannedUsers.confirmationModalTitle')}
          message={t('bannedUsers.confirmationModalMessage')}
          onConfirm={async () => {
            await handleUnban(selectedAction.item);
            setSelectedAction(null);
          }}
          onCancel={() => setSelectedAction(null)}
        />
      )}
    </div>
  );
}
