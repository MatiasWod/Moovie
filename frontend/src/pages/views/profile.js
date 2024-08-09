import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import userApi from "../../api/UserApi";

function Profile(){

    const {username} = useParams();

    //GET VALUES FOT Top Rated Movies
    const [profile, setProfile] = useState([]);
    const [profileLoading, setProfileLoading] = useState(true);
    const [profileError, setProfileError] = useState(null);

    const fetchProfile = async () => {
        try {
            const response = await userApi.getProfileByUsername(username);
            setProfile(response.data);
        } catch (err) {
            setProfileError(err);
        } finally {
            setProfileLoading(false);
        }
    };

    useEffect(() => {
        fetchProfile();
    }, []);

    return (
        <div>
            <div>Perfil de {profile.username}</div>
            <div><img alt="profile_picture" src={profile.pictureUrl}/></div>
        </div>
    )
}

export default Profile;
