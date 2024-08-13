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
            <div className="title bold-title">Milky Leaderboard</div>
            <div>
                {milkyLeaderboard.map(profile => (
                    <MilkyLeaderboardProfile profile={profile}/>
                ))}
            </div>
        </div>
    )
}

export default MilkyLeaderboard;

//Aux functions for milkyLeaderboard

function MilkyLeaderboardProfile({profile}) {
    return(
        <div className="milky-leaderboard-profile">
            <div>{profile.username}</div>
            <div>{profile.milkyPoints}</div>
        </div>
    )
}


