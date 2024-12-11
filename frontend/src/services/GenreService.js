import genreApi from "../api/GenreApi";

const GenreService = (() => {

    const getAllGenres = async () => {
        return await genreApi.getAllGenres();
    }

    return{
        getAllGenres
    }
})();

export default GenreService;