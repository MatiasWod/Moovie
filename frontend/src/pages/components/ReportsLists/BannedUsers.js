import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import api from '../../../api/api';
import userApi from '../../../api/UserApi';
import UserRoles from '../../../api/values/UserRoles';
import { parsePaginatedResponse } from '../../../utils/ResponseUtils';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import PaginationButton from '../paginationButton/PaginationButton';
import EmptyState from './EmptyState';
import LoadingState from './LoadingState';

export default function BannedUsers() {
  const [users, setUsers] = useState({ users: [], links: {} });
  const [usersLoading, setUsersLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const [page, setPage] = useState(1);
  const { t } = useTranslation();

  useEffect(() => {
    fetchUsers();
  }, [page]);

  const fetchUsers = async () => {
    setUsersLoading(true);
    try {
      const res = await userApi.listUsers({ role: UserRoles.BANNED, pageNumber: page });
      const response = parsePaginatedResponse(res);
      const usersData = response.data || [];

      const userPromises = usersData.map(async (user) => {
        try {
          const banMessage = await api.get(user.banMessageUrl);
          return { ...user, banMessage: banMessage.data };
        } catch (error) {
          console.error('Error fetching ban message for user:', user.username, error);
          return { ...user, banMessage: null };
        }
      });

      const usersWithDetails = await Promise.all(userPromises);
      console.log(usersWithDetails);

      setUsers({
        users: usersWithDetails,
        links: response.links,
      });
    } catch (error) {
      console.error('Error fetching banned users:', error);
      setUsers({ users: [], links: {} });
    } finally {
      setUsersLoading(false);
    }
  };

  const handleUnban = async (user) => {
    try {
      await userApi.unbanUser(user.url);
      await fetchUsers();
    } catch (error) {
      console.error('Error unbanning user:', error);
    }
  };

  return (
    <div className="w-full">
      <div className="flex items-center gap-3 mb-6">
        <div className="p-2 bg-red-100 rounded-lg">
          <i className="bi bi-person-x text-red-600 text-xl"></i>
        </div>
        <h2 className="text-2xl font-bold text-gray-800">{t('bannedUsers.bannedUsers')}</h2>
      </div>

      {usersLoading ? (
        <LoadingState message={t('reports.loading.bannedUsers')} />
      ) : users.users.length === 0 ? (
        <EmptyState
          title={t('reports.empty.bannedUsers')}
          message={t('bannedUsers.noBannedUsers')}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {users.users.map((user, index) => (
            <div
              key={index}
              className="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200"
            >
              <div className="p-6">
                <div className="flex items-start gap-4 mb-4">
                  <div className="relative">
                    <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center">
                      <i className="bi bi-person text-red-600 text-2xl"></i>
                    </div>
                    <div className="absolute -top-1 -right-1 w-6 h-6 bg-red-500 rounded-full flex items-center justify-center">
                      <i className="bi bi-x text-white text-sm font-bold"></i>
                    </div>
                  </div>
                  <div className="flex-1">
                    <a
                      className="text-lg font-bold text-gray-800 mb-1"
                      href={process.env.PUBLIC_URL + `/profile/${user.username}`}
                    >
                      {user.username}
                    </a>
                    <div className="flex items-center gap-2 mb-2">
                      <span className="px-2 py-1 bg-red-100 text-red-700 text-xs font-medium rounded-full">
                        {t('reports.status.banned')}
                      </span>
                    </div>
                  </div>
                </div>

                <div className="bg-gradient-to-r from-red-50 to-red-100 rounded-lg p-4 mb-4 border-l-4 border-red-300">
                  <div className="flex items-center gap-2 mb-2">
                    <i className="bi bi-exclamation-triangle text-red-500"></i>
                    <span className="text-sm font-medium text-red-600">
                      {t('reports.content.banReason')}
                    </span>
                  </div>
                  <p className="text-red-800 text-sm leading-relaxed">
                    {user.banMessage.banMessage}
                  </p>
                </div>

                <div className="flex justify-end">
                  <button
                    onClick={() => setSelectedAction({ type: 'unban', item: user })}
                    className="flex items-center gap-2 px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors duration-200 shadow-sm hover:shadow-md"
                  >
                    <i className="bi bi-person-check"></i>
                    <span>{t('profile.unbanUser')}</span>
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {selectedAction && (
        <ConfirmationModal
          title={t('bannedUsers.confirmationModalTitle')}
          message={t('bannedUsers.confirmationModalMessage')}
          onConfirm={async () => {
            if (selectedAction.type === 'unban') await handleUnban(selectedAction.item);
            setSelectedAction(null);
          }}
          onCancel={() => setSelectedAction(null)}
        />
      )}

      {!usersLoading && users?.links?.last?.pageNumber > 1 && (
        <div className="flex justify-center mt-8">
          <PaginationButton page={page} lastPage={users.links.last.pageNumber} setPage={setPage} />
        </div>
      )}
    </div>
  );
}
