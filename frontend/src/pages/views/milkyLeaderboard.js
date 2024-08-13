import React, {useEffect, useState} from 'react';
import userApi from "../../api/UserApi";
import PagingSizes from "../../api/values/PagingSizes";
import "../components/mainStyle.css"

function MilkyLeaderboard(){
    const [milkyLeaderboard, setMilkyLeaderboard] = useState([]);
    const [milkyLeaderboardLoading, setMilkyLeaderboardLoading] = useState(true);
    const [milkyLeaderboardError, setMilkyLeaderboardError] = useState(null);

    const fetchMilkyLeaderboard = async () => {
        try {
            const response = await userApi
                .getMilkyLeaderboard({page: 1, pageSize: PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE});
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

    return (
        <div className="moovie-default default-container">
            <div>
                {milkyLeaderboard.map(profile => (
                    <div>{profile.username}, {profile.milkyPoints}</div>
                ))}
            </div>
        </div>
    )
}

export default MilkyLeaderboard;