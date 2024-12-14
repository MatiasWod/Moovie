import React from 'react';
import './profileHeader.css';
import ProfileImage from "../profileImage/ProfileImage";
import { Button } from 'react-bootstrap';
import userApi from '../../../api/UserApi';


const ProfileHeader = ({ profile, handleBanUser, handleUnbanUser }) => {

    return (
        <div className="profile-header">
            <div className="profile-header-info">
                <div className="profile-header-avatar">
                    <ProfileImage username={profile.username} image={profile.pictureUrl}/>
                </div>
                <div>
                    <h1 className="profile-header-username">
                        {profile.username} {profile.hasBadge ? '🏆' : ''}
                        {profile.role === -101 || profile.role === -2 ? (
                            <Button
                                variant="success"
                                size="sm"
                                className="ms-2"
                                onClick={handleUnbanUser}
                            >
                                Unban User
                            </Button>
                        ) : (
                            <Button 
                                variant="danger" 
                                size="sm" 
                                className="ms-2"
                                onClick={handleBanUser}
                            >
                                Ban User
                            </Button>
                        )}
                    </h1>
                    <p className="profile-header-email">{profile.email}</p>
                </div>
            </div>
            <div className="profile-header-stats">
                <span>📋 {profile.moovieListCount}</span>
                <span>⭐ {profile.reviewsCount}</span>
                <span>🐵 {profile.milkyPoints}</span>
            </div>
            <div>
                {profile.role}
            </div>
        </div>
    );
};

export default ProfileHeader;
