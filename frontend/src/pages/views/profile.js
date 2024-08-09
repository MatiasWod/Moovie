import React from "react";
import {useParams} from "react-router-dom";

function Profile(){

    const {username} = useParams();

    return (
        <div>
            <div>Perfil de {username}</div>
            <div><img alt="profile_picture" src="http://localhost:8080/users/Wancho/image"/></div>
        </div>
)
}

export default Profile;
