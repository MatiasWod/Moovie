import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import userApi from "../../api/UserApi";
import ProfileImage from "../components/profileImage/ProfileImage";
import "../components/mainStyle.css"

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
        <div className="default-container moovie-default">
            <div>Perfil de {profile.username}</div>
            <ProfileImage image={profile.pictureUrl} size={100}/>
        </div>
    )
}

export default Profile;
