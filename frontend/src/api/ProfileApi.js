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

    const setPfp = (username, pfp) => {
        return api.put(`/profiles/${username}/image`, pfp);
    }

    return {
        getProfileByUsername,
        getMilkyLeaderboard,
        getSearchedUsers,
        setPfp
    }

}
);

export default profileApi;
