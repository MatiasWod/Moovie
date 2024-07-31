import React, {useState, useEffect, useCallback} from 'react';
import mediaApi from '../../api/MediaApi'; // Adjust the path if needed
import Navbar from "../components/navBar/navbar";
import mainStyle from "../components/mainStyle.css";
import Loader from "../Loader";
import MediaCard from "../components/mediaCard/MediaCard";
import CardsHorizontalContainer from "../components/cardsHorizontalContainer/CardsHorizontalContainer";
import MediaTypes from "../../api/values/MediaTypes";
import MediaFilters from "../../api/values/MediaFilters";

function Home() {

    //GET VALUES FOT Top Rated Movies
    const [topRatedMovies, setTopRatedMovies] = useState([]);
    const [topRatedMoviesLoading, setTopRatedMoviesLoading] = useState(true);
    const [topRatedMoviesError, setTopRatedMoviesError] = useState(null);

    const fetchTopRatedMovies = async () => {
        try {
            const response = await mediaApi
                .getMedia({ type: MediaTypes.TYPE_MOVIE, orderBy: MediaFilters.TMDB_RATING, sortOrder: MediaFilters.DESC, page: 1, pageSize: 5});
            setTopRatedMovies(response.data);
        } catch (err) {
            setTopRatedMoviesError(err);
        } finally {
            setTopRatedMoviesLoading(false);
        }
    };

    useEffect(() => {
        fetchTopRatedMovies();
    }, []);


    //GET VALUES FOT Mos Popular Movies
    const [mostPopularMovies, setMostPopularMovies] = useState([]);
    const [mostPopularMoviesLoading, setMostPopularMoviesLoading] = useState(true);
    const [mostPopularMoviesError, setMostPopularMoviesError] = useState(null);

    const fetchMostPopularMovies = async () => {
        try {
            const response = await mediaApi
                .getMedia({ type: MediaTypes.TYPE_MOVIE, orderBy: MediaFilters.VOTE_COUNT, sortOrder: MediaFilters.DESC, page: 1, pageSize: 5});
            setMostPopularMovies(response.data);
        } catch (err) {
            setMostPopularMoviesError(err);
        } finally {
            setMostPopularMoviesLoading(false);
        }
    };

    useEffect(() => {
        fetchMostPopularMovies();
    }, []);


    //GET VALUES FOT Top Rated Series
    const [topRatedSeries, setTopRatedSeries] = useState([]);
    const [topRatedSeriesLoading, setTopRatedSeriesLoading] = useState(true);
    const [topRatedSeriesError, setTopRatedSeriesError] = useState(null);

    const fetchTopRatedSeries = async () => {
        try {
            const response = await mediaApi
                .getMedia({ type: MediaTypes.TYPE_TVSERIE, orderBy: MediaFilters.TMDB_RATING, sortOrder: MediaFilters.DESC, page: 1, pageSize: 5});
            setTopRatedSeries(response.data);
        } catch (err) {
            setTopRatedSeriesError(err);
        } finally {
            setTopRatedSeriesLoading(false);
        }
    };

    useEffect(() => {
        fetchTopRatedSeries();
    }, []);


    //GET VALUES FOT Most Popular Series
    const [mostPopularSeries, setMostPopularSeries] = useState([]);
    const [mostPopularSeriesLoading, setMostPopularSeriesLoading] = useState(true);
    const [mostPopularSeriesError, setMostPopularSeriesError] = useState(null);

    const fetchMostPopularSeries = async () => {
        try {
            const response = await mediaApi
                .getMedia({ type: MediaTypes.TYPE_TVSERIE, orderBy: MediaFilters.VOTE_COUNT, sortOrder: MediaFilters.DESC, page: 1, pageSize: 5});
            setMostPopularSeries(response.data);
        } catch (err) {
            setMostPopularSeriesError(err);
        } finally {
            setMostPopularSeriesLoading(false);
        }
    };

    useEffect(() => {
        fetchMostPopularSeries();
    }, []);


    return (
        <main>
            <Navbar/>
            <div>Sumérgete en Películas y Series,
                Descubre tu Próxima Experiencia Favorita.
            </div>
            <div>Home Page of Moovie, Welcome!</div>
            <div>
                <div>Top Rated Movies by IMDB</div>
                <CardsHorizontalContainer mediaList={topRatedMovies} loading={topRatedMoviesLoading}
                                          error={topRatedMoviesError}/>
            </div>

            <div>
                <div>Most Popular Movies</div>
                <CardsHorizontalContainer mediaList={mostPopularMovies} loading={mostPopularMoviesLoading}
                                          error={mostPopularMoviesError}/>
            </div>

            <div>
                <div>Top Rated Series by IMDB</div>
                <CardsHorizontalContainer mediaList={topRatedSeries} loading={topRatedSeriesLoading}
                                          error={topRatedSeriesError}/>
            </div>

            <div>
                <div>Most Popular Series</div>
                <CardsHorizontalContainer mediaList={mostPopularSeries} loading={mostPopularSeriesLoading}
                                          error={mostPopularSeriesError}/>
            </div>


        </main>
    );
}

export default Home;
