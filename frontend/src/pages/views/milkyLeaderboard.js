import React, {useEffect, useState} from 'react';
import mediaApi from "../../api/MediaApi";
import MediaTypes from "../../api/values/MediaTypes";
import OrderBy from "../../api/values/OrderBy";
import SortOrder from "../../api/values/SortOrder";
import userApi from "../../api/UserApi";

function MilkyLeaderboard(){
    const [milkyLeaderboard, setMilkyLeaderboard] = useState([]);
    const [milkyLeaderboardLoading, setMilkyLeaderboardLoading] = useState(true);
    const [milkyLeaderboardError, setMilkyLeaderboardError] = useState(null);

    const fetchMilkyLeaderboard = async () => {
        try {
            const response = await userApi
                .getMilkyLeaderboard({page: 0, pageSize: 25});
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
        <div>
            <div>
                {milkyLeaderboard.map(profile => (
                    <div>{profile.username}, {profile.milkyPoints}</div>
                ))}
            </div>
        </div>
    )
}

export default MilkyLeaderboard;