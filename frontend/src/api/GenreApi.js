import api from "./api";

const genreApi = (() => {

    const getAllGenres = () => {
        return api.get('/genres');
    }

    return{
        getAllGenres
    }
})();

export default genreApi;