import React, { useEffect, useState } from 'react';
import userApi from '../../../api/UserApi';
import profileApi from '../../../api/ProfileApi';
import UserRoles from '../../../api/values/UserRoles';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import {useTranslation} from "react-i18next";
import defaultProfilePicture from "../../../images/defaultProfilePicture.png"

export default function BannedUsers() {
  const [users, setUsers] = useState([]);
  const [selectedAction, setSelectedAction] = useState(null);
  const [showUnbanModal, setShowUnbanModal] = useState(false);
  const { t } = useTranslation();

  useEffect(() => {
    fetchBannedUsers();
  }, []);

  const fetchBannedUsers = async () => {
    try {
      const response = await userApi.listUsers({ role: UserRoles.BANNED });
      const bannedUsers = response.data || [];
      
      // Fetch ban messages and profile info for each user
      const usersWithDetails = await Promise.all(
        bannedUsers.map(async (user) => {
          const [banMessageResponse, profileResponse] = await Promise.all([
            userApi.getBanMessage(user.username),
            profileApi.getProfileByUsername(user.username)
          ]);
          
          return {
            ...user,
            banInfo: banMessageResponse.data,
            profile: profileResponse.data
          };
        })
      );
      
      setUsers(usersWithDetails);
    } catch (error) {
      console.error('Error fetching banned users:', error);
    }
  };

  const handleUnban = async (user) => {
    try {
      await userApi.unbanUser(user.username);
      await fetchBannedUsers();
    } catch (error) {
      console.error('Error unbanning user:', error);
    } finally {
      setShowUnbanModal(false);
    }
  };

  return (
    <div>
      <h3 className="text-xl font-semibold mb-4">{t('bannedUsers.bannedUsers')}</h3>
      {users.length === 0 ? (
        <div className="text-center text-gray-500">{t('bannedUsers.noBannedUsers')}</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {users.map((user) => (
            <div key={user.username} className="bg-white p-4 rounded shadow">
              <div className="flex items-center justify-between mb-2">
                <div className="flex items-center space-x-4">
                  <img 
                    src={`http://localhost:8080/profiles/${user.username}/image`} 
                    alt={t('mpl.picture')} 
                    className="h-12 w-12 rounded-full border"
                    onError={(e) => {
                        e.target.src = defaultProfilePicture;
                    }}
                  />
                  <div>
                    <div className="font-bold">{user.username}</div>
                    <div className="text-sm text-gray-600">{user.banInfo?.banMessage}</div>
                    <div className="text-sm text-gray-500">{user.email}</div>
                  </div>
                </div>
                <button
                  onClick={() => setShowUnbanModal(user)}
                  className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                >
                  {t('profile.unbanUser')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {showUnbanModal && (
        <ConfirmationModal
          title={t('profile.unbanUser')}
          message={t('confirmationForm.prompt', { actionName: t('profile.unbanUser').toLowerCase() })}
          onConfirm={() => handleUnban(showUnbanModal)}
          onCancel={() => setShowUnbanModal(false)}
        />
      )}
    </div>
  );
}
