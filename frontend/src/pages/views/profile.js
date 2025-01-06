import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import userApi from "../../api/UserApi";
import ProfileImage from "../components/profileImage/ProfileImage";
import "../components/mainStyle.css";
import ProfileTabNavigation from "../components/profileTabNavigation/profileTabNavigation";
import Reviews from "../components/ReviewsSection/Reviews";
import ProfileHeader from "../components/profileHeader/ProfileHeader";
import ProfileTabMediaLists from "../components/profileTab/ProfileTabMediaLists";
import ProfileTabMoovieLists from "../components/profileTab/ProfileTabMoovieLists";

function ProfileTab({selectedTab, profile}){
    switch (selectedTab.toLowerCase()) {
        case "watched":
            return <ProfileTabMediaLists username={profile.username} type={"watched"}/>
        case "watchlist":
            return <ProfileTabMediaLists username={profile.username} type={"watchlist"}/>
        case "public-lists":
            return <ProfileTabMoovieLists username={profile.username} type={"public-lists"}/>
        case "private-lists":
            return <ProfileTabMoovieLists username={profile.username} type={"private-lists"}/>
        case "liked-lists":
            return <ProfileTabMoovieLists username={profile.username} type={"followed-lists"}/>
        case "followed-lists":
            return <ProfileTabMoovieLists username={profile.username} type={"liked-lists"}/>
        case "reviews":
            return <Reviews username={profile.username} source="user" />;
        default:
            return <div>{selectedTab}</div>
    }

}

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

    const handleBanUser = async () => {
        await userApi.banUser(username);
        fetchProfile();
    };

    const handleUnbanUser = async () => {
        await userApi.unbanUser(username);
        fetchProfile();
    };

    useEffect(() => {
        fetchProfile();
    }, []);

    // For tracking the selected tab
    const [selectedTab, setSelectedTab] = useState("public-lists");
    const handleTabSelect = (tab) => {
        setSelectedTab(tab);
    };

    return (
        <div className="default-container moovie-default">
            <ProfileHeader profile={profile} handleBanUser={handleBanUser} handleUnbanUser={handleUnbanUser}/>
            <ProfileTabNavigation selectedTab={selectedTab} onTabSelect={handleTabSelect} />
            <ProfileTab selectedTab={selectedTab} profile={profile}></ProfileTab>
        </div>
    );
}

export default Profile;
