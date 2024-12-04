import React, { useEffect, useState } from 'react';
import userApi from "../../api/UserApi";
import PagingSizes from "../../api/values/PagingSizes";
import "../components/mainStyle.css";
import "./milkyLeaderboard.css";
import ProfileImage from "../components/profileImage/ProfileImage";
import logo from "../../images/logo.png"

function MilkyLeaderboard() {
    const [milkyLeaderboard, setMilkyLeaderboard] = useState([]);
    const [milkyLeaderboardLoading, setMilkyLeaderboardLoading] = useState(true);
    const [milkyLeaderboardError, setMilkyLeaderboardError] = useState(null);

    const fetchMilkyLeaderboard = async () => {
        try {
            const response = await userApi.getMilkyLeaderboard({
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

    return (
        <div className="moovie-default default-container">
            <div className="title bold-title"> Milky Leaderboard</div>

            <div className="milky-leaderboard-table-container">
                <table className="milky-leaderboard-table">
                    <thead>
                    <tr className="milky-leaderboard-header">
                        <th className="col"></th>
                        <th className="col bold-letters">User</th>
                        <th className="col bold-letters">Lists</th>
                        <th className="col bold-letters">Reviews</th>
                        <th className="col bold-letters">Points</th>
                    </tr>
                    </thead>

                    <tbody>
                    {milkyLeaderboard.map(profile => (
                        <MilkyLeaderboardProfile profile={profile} key={profile.username}/>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default MilkyLeaderboard;

function MilkyLeaderboardProfile({profile}) {
    return (
        <tr className="milky-leaderboard-profile">
            <td className="col">
                <ProfileImage image={profile.pictureUrl} username={profile.username} size={75}/>
            </td>
            <td className="col">{profile.username}</td>
            <td className="col">{profile.moovieListCount}</td>
            <td className="col">{profile.reviewsCount}</td>
            <td className="col">{profile.milkyPoints}</td>
        </tr>
    );
}
