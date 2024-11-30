import React from 'react';
import './profileHeader.css';
import ProfileImage from "../profileImage/ProfileImage";

const ProfileHeader = ({ profile }) => {
    return (
        <div className="profile-header">
            <div className="profile-header-info">
                <div className="profile-header-avatar">
                    <ProfileImage username={profile.username} image={profile.pictureUrl}/>
                </div>
                <div>
                    <h1 className="profile-header-username">{profile.username} {profile.hasBadge ? '🏆' : ''}</h1>
                    <p className="profile-header-email">{profile.email}</p>
                </div>
            </div>
            <div className="profile-header-stats">
                <span>📋 {profile.moovieListCount}</span>
                <span>⭐ {profile.reviewsCount}</span>
                <span>🐵 {profile.milkyPoints}</span>
            </div>
        </div>
    );
};

export default ProfileHeader;
