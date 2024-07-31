import React, {useState, useEffect, useCallback} from 'react';
import mediaApi from '../../api/MediaApi'; // Adjust the path if needed
import Navbar from "../components/navBar/navbar";
import mainStyle from "../components/mainStyle.css";
import Loader from "../Loader";
import MediaCard from "../components/mediaCard/MediaCard";
import CardsHorizontalContainer from "../components/cardsHorizontalContainer/CardsHorizontalContainer";

function Home() {
    const [topRatedMedia, setTopRatedMedia] = useState([]);
    const [topRatedMediaLoading, setTopRatedMediaLoading] = useState(true);
    const [topRatedMediaError, setTopRatedMediaError] = useState(null);

    useEffect(() => {
        const fetchMedia = async () => {
            try {
                const response = await mediaApi.getMedia({ page: 1, pageSize: 5 });
                setTopRatedMedia(response.data);
            } catch (err) {
                setTopRatedMediaError(err);
            } finally {
                setTopRatedMediaLoading(false);
            }
        };

        fetchMedia();
    }, []);




    return (
        <main>
            <Navbar/>
            <div>Sumérgete en Películas y Series,
                Descubre tu Próxima Experiencia Favorita.</div>
            <div>Home Page of Moovie, Welcome!</div>
            <div>
                <div>Top Rating by IMDB</div>
                <CardsHorizontalContainer mediaList={topRatedMedia} loading={topRatedMediaLoading} error={topRatedMediaError}/>
            </div>

        </main>
    );
}

export default Home;
