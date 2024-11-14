import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import userApi from "../../api/UserApi";
import ProfileImage from "../components/profileImage/ProfileImage";
import "../components/mainStyle.css";
import ProfileTabNavigation from "../components/profileTabNavigation/profileTabNavigation";

function Profile() {
    const { username } = useParams();

    // GET Profile Data
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

    // For tracking the selected tab
    const [selectedTab, setSelectedTab] = useState("visto");
    const handleTabSelect = (tab) => {
        setSelectedTab(tab);
    };

    return (
        <div className="default-container moovie-default">
            <div>Perfil de {profile.username}</div>
            <ProfileImage image={profile.pictureUrl} size={100} />
            <ProfileTabNavigation selectedTab={selectedTab} onTabSelect={handleTabSelect} />
        </div>
    );
}

export default Profile;
