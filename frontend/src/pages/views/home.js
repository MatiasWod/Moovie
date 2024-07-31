import React, {useState, useEffect, useCallback} from 'react';
import mediaApi from '../../api/MediaApi'; // Adjust the path if needed
import MediaCard from '../components/mediaCard';

function Home() {
    const [topRatedMedia, setTopRatedMedia] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchMedia = async () => {
            try {
                const response = await mediaApi.getMedia({ page: 1, pageSize: 5 });
                setTopRatedMedia(response.data);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchMedia();
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <main>
            <div>Home Page of Moovie, Welcome!</div>
            <div>
                <div>Top Rating by IMDB</div>
                <div className="cards-container">
                    {topRatedMedia.map((movie) => (
                        <MediaCard
                            key={movie.id}
                            releaseDate={movie.releaseDate}
                            posterPath={movie.posterPath}
                            mediaName={movie.name}
                        />
                    ))}
                </div>
            </div>
        </main>
    );
}

export default Home;
