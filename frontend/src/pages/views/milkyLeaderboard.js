import React, { useEffect, useState } from 'react';
import { Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { FaInfoCircle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import api from '../../api/api';
import PagingSizes from '../../api/values/PagingSizes';
import userService from '../../services/UserService';
import { parsePaginatedResponse } from '../../utils/ResponseUtils';
import '../components/mainStyle.css';
import ProfileImage from '../components/profileImage/ProfileImage';
import './milkyLeaderboard.css';

function MilkyLeaderboard() {
  const [milkyLeaderboard, setMilkyLeaderboard] = useState([]);
  const [milkyLeaderboardLoading, setMilkyLeaderboardLoading] = useState(true);
  const [milkyLeaderboardError, setMilkyLeaderboardError] = useState(null);
  const { t } = useTranslation();

  const fetchMilkyLeaderboard = async () => {
    try {
      const response = await userService.getMilkyLeaderboard({
        page: 1,
        pageSize: PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE,
      });
      setMilkyLeaderboard(response.data);
    } catch (err) {
      setMilkyLeaderboardError(err);
    } finally {
      setMilkyLeaderboardLoading(false);
    }
  };

  useEffect(() => {
    fetchMilkyLeaderboard();
  }, []);

  //Alguien que tenga buenos gustos dieñativos que ponga esto en algun lugar
  //<img style={{width: "100px", height: "100px"}} src={logo}
  //                  alt="Milky Logo"/>

  if (milkyLeaderboardLoading)
    return (
      <div className={'mt-6 d-flex justify-content-center'}>
        <Spinner />
      </div>
    );

  return (
    <div className="moovie-default default-container">
      <div className="title bold-title"> {t('mpl.title')}</div>

      <div className="milky-leaderboard-table-container">
        <table className="milky-leaderboard-table">
          <thead>
            <tr className="milky-leaderboard-header">
              <th className="col"></th>
              <th className="col bold-letters">{t('mpl.username')}</th>
              <th className="col bold-letters">{t('mpl.moovieListCount')}</th>
              <th className="col bold-letters">{t('mpl.reviewsCount')}</th>
              <th className="col bold-letters">
                {t('mpl.points')}
                <FaInfoCircle title={t('mpl.pointsTooltip')} style={{ marginLeft: '5px' }} />
              </th>
            </tr>
          </thead>

          <tbody>
            {milkyLeaderboard.map((profile) => (
              <MilkyLeaderboardProfile profile={profile} key={profile.username} />
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default MilkyLeaderboard;

function MilkyLeaderboardProfile({ profile }) {
  const [reviewsCount, setReviewsCount] = useState(0);
  const [moovieListCount, setMoovieListCount] = useState(0);
  const navigate = useNavigate();

  const handleUsernameClick = (username) => {
    navigate(`/profile/${username}`);
  };

  useEffect(() => {
    const fetchReviewsCount = async () => {
      const response = await api.get(profile.reviewsUrl);
      const res = parsePaginatedResponse(response);
      setReviewsCount(res.totalElements);
    };
    const fetchMoovieListCount = async () => {
      const response = await api.get(profile.moovieListsUrl);
      const res = parsePaginatedResponse(response);
      setMoovieListCount(res.totalElements);
    };
    Promise.all([fetchReviewsCount(), fetchMoovieListCount()]);
  }, [profile.reviewsUrl, profile.moovieListsUrl]);

  return (
    <tr className="milky-leaderboard-profile">
      <td className="col">
        <ProfileImage
          style={{ cursor: 'pointer' }}
          onClick={() => handleUsernameClick(profile.username)}
          image={profile.imageUrl}
          size={75}
        />
      </td>
      <td
        className="col"
        style={{ cursor: 'pointer' }}
        onClick={() => handleUsernameClick(profile.username)}
      >
        {profile.username}
      </td>
      <td className="col">{moovieListCount}</td>
      <td className="col">{reviewsCount}</td>
      <td className="col">{profile.milkyPoints}</td>
    </tr>
  );
}
