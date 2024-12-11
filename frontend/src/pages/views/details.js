import React, { useEffect, useState } from 'react';
import './details.css';
import "../components/mainStyle.css"
import { useNavigate, useParams } from "react-router-dom";
import mediaApi from "../../api/MediaApi";
import SearchableMediaTag from "../components/searchableMediaTag/searchableMediaTag";
import MediaTypes from "../../api/values/MediaTypes";
import Reviews from "../components/ReviewsSection/Reviews";
import AddMediaToListButton from "../components/buttons/addMediaToListButton/AddMediaToListButton";
import CreateReviewButton from "../components/buttons/createReviewButton/CreateReviewButton";
import ReviewForm from "../components/forms/reviewForm/ReviewForm";
import ActorCardList from "../components/actorCards/ActorCardList";
import { useDispatch, useSelector } from "react-redux";

function Details() {
    const { id } = useParams();

    //GET VALUES FOR Media
    const [media, setMedia] = useState([]);
    const [mediaLoading, setMediaLoading] = useState(true);
    const [mediaError, setMediaError] = useState(null);

    const { isLoggedIn, user } = useSelector(state => state.auth);

    const [reloadReviews, setReloadReviews] = useState(false); // state to trigger re-render of Reviews component

    const fetchMedia = async () => {
        try {
            const response = await mediaApi.getMediaById(id);
            setMedia(response.data);
        } catch (err) {
            setMediaError(err);
        } finally {
            setMediaLoading(false);
        }
    };

    useEffect(() => {
        fetchMedia();
    }, [id]);

    const trailerLink = (media.trailerLink === 'None' ? null : media.trailerLink);
    const releaseYear = new Date(media.releaseDate).getFullYear();

    let detailsColumn = <div />;

    if (media.type === "Movie") {
        detailsColumn =
            <div className="col">
                <h1>{media.name}
                    <SearchableMediaTag link={'status'} text={media.status} />
                    <SearchableMediaTag link={'l'} text={media.originalLanguage} />
                </h1>
                <h1>{releaseYear} • Película • {media.runtime} m </h1>

                <div>Generos:</div>
                <div>Director: <SearchableMediaTag link={'cast/director'} text={media.director} /></div>
                <div>Presupuesto: {media.budget}</div>
                <div>Ingresos: {media.revenue}</div>

                {trailerLink && (
                    <iframe src={trailerLink.replace("watch?v=", "embed/")} />
                )}

                <div>{media.overview}</div>
            </div>

    } else {
        detailsColumn =
            <div className="col">
                <h1>{media.name}
                    <SearchableMediaTag link={'status'} text={media.status} />
                    <SearchableMediaTag link={'l'} text={media.originalLanguage} />
                </h1>
                <h1>{releaseYear} • Serie • {media.numberOfSeasons} Temporadas • {media.numberOfEpisodes} Episodios</h1>

                <div>Generos:</div>
                <div>Creadores:</div>

                {media.lastAirDate && (
                    <div>Fecha de Ultima Emisión: {media.lastAirDate}</div>
                )}

                {media.nextEpisodeToAir && (
                    <div>Próximo Episodio a Emitir: {media.nextEpisodeToAir}</div>
                )}

                {trailerLink && (
                    <iframe src={trailerLink.replace("watch?v=", "embed/")} />
                )}

                <div>{media.overview}</div>
            </div>
    }

    // Buttons for creating reviews
    const [showReviewForm, setShowReviewForm] = useState(false);
    const navigate = useNavigate();

    const handleOpenReviewForm = () => {
        if (!isLoggedIn) {
            navigate('/login');
        }
        setShowReviewForm(true);
    };

    const handleCloseReviewForm = () => {
        setShowReviewForm(false);
    };

    const handleReviewSubmit = () => {
        setReloadReviews(prevState => !prevState); // Toggle the state to force re-render of Reviews component
        handleCloseReviewForm(); // Close the review form after submission
    };

    return (
        <div className="moovie-default default-container">
            <div className="row">

                {/* POSTER COLUMN */}
                <div className="col">
                    <img src={media.posterPath} className="posterStyle" />
                </div>

                {/* MEDIA DETAILS COLUMN */}
                {detailsColumn}

                <AddMediaToListButton currentId={id} />

                <CreateReviewButton handleOpenReviewForm={handleOpenReviewForm} />

                {showReviewForm && (
                    <div className="overlay">
                        <ReviewForm
                            mediaName={media.name}
                            mediaId={id}
                            closeReview={handleCloseReviewForm}
                            onReviewSubmit={handleReviewSubmit} // Pass the submit handler
                        />
                    </div>
                )}
            </div>

            <ActorCardList mediaId={id} />

            {/* Pass reloadReviews as a key to force re-render */}
            <Reviews key={reloadReviews ? id : undefined} id={id} source="media" />
        </div>
    );
}

export default Details;
