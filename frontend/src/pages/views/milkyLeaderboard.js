import React, {useEffect, useState} from 'react';
import userApi from "../../api/UserApi";
import PagingSizes from "../../api/values/PagingSizes";
import "../components/mainStyle.css"
import "./milkyLeaderboard.css"
import ProfileImage from "../components/profileImage/ProfileImage";

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
            <div className="title bold-title ">Milky Leaderboard</div>

            <div className="milky-leaderboard-table-container">

                <div className="table table-striped">
                    <tr className={"milky-leaderboard-profile"}>
                        <td className="col"></td>
                        <td className="col">User</td>
                        <td className="col">Lists</td>
                        <td className="col">Reviews</td>
                        <td className="col">Points</td>
                    </tr>

                    {milkyLeaderboard.map(profile => (
                        <MilkyLeaderboardProfile profile={profile}/>
                    ))}
                </div>

            </div>
        </div>
    )
}

export default MilkyLeaderboard;

//Aux functions for milkyLeaderboard

function MilkyLeaderboardProfile({profile}) {
    return (
        <tr className={"milky-leaderboard-profile"}>
            <ProfileImage image={profile.pictureUrl}/>
            <td className="col">{profile.username}</td>
            <td className="col">{profile.moovieListCount}</td>
            <td className="col">{profile.reviewsCount}</td>
            <td className="col">{profile.milkyPoints}</td>
        </tr>
    )
}


