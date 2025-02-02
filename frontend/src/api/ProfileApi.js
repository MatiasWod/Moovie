import api from "./api";

const profileApi = (() => {

    const getProfileByUsername = (username) =>{
        return api.get(`/profiles/${username}`);
    }



    const getMilkyLeaderboard = ({page, pageSize}) => {
        return api.get('/profiles/milkyLeaderboard',
            {
                params: {
                    'page': page,
                    'pageSize': pageSize
                }
            });
    }


    const getSearchedUsers = ({username,orderBy,sortOrder,page}) => {
        return api.get(`/profiles`,
            {
                params: {
                    'username': username,
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
                    'pageNumber': page
                }
            }
        );
    }


    const getSpecialListFromUser = (username, type, orderBy, order, pageNumber = 1) => {
            return api.get(`/profiles/${username}/${type}?orderBy=${orderBy}&order=${order}&pageNumber=${pageNumber}`);
    };



        const setPfp = (username, pfp) => {
        return api.put(`/profiles/${username}/image`, pfp);
    }

    return {
        getProfileByUsername,
        getMilkyLeaderboard,
        getSpecialListFromUser,
        getSearchedUsers,
        setPfp
    }

}
);

export default profileApi;
