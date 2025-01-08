import React, {useEffect, useState} from 'react';
import './details.css';
import "../components/mainStyle.css"
import {useNavigate, useParams} from "react-router-dom";
import mediaApi from "../../api/MediaApi";
import Reviews from "../components/ReviewsSection/Reviews";
import AddMediaToListButton from "../components/buttons/addMediaToListButton/AddMediaToListButton";
import CreateReviewButton from "../components/buttons/createReviewButton/CreateReviewButton";
import ReviewForm from "../components/forms/reviewForm/ReviewForm";
import ActorCardList from "../components/actorCards/ActorCardList";
import {useSelector} from "react-redux";
import MediaTag from "../components/detailsSection/mediaTag/mediaTag";
import Popover from 'react-bootstrap/Popover';
import {OverlayTrigger} from "react-bootstrap";
import genreApi from "../../api/GenreApi";
import providerApi from "../../api/ProviderApi";
import MediaService from "../../services/MediaService";
import GenreService from "../../services/GenreService";
import ProviderService from "../../services/ProviderService";
import mediaService from "../../services/MediaService";

function Details() {
    const {id} = useParams();

    //GET VALUES FOR Media
    const [media, setMedia] = useState({});
    const [mediaLoading, setMediaLoading] = useState(true);
    const [mediaError, setMediaError] = useState(null);

    //GET VALUES FOR Genres
    const [genres, setGenres] = useState({});
    const [genresLoading, setGenresLoading] = useState(true);
    const [genreError, setGenreError] = useState(null);

    //GET VALUES FOR PROVIDERS
    const [providers, setProviders] = useState({});
    const [providersLoading, setProvidersLoading] = useState(true);
    const [providersError, setProvidersError] = useState(null);

    //GET VALUES FOR TVCREATORS
    const [tvCreators, setTvCreators] = useState([]);
    const [tvCreatorsLoading, setTvCreatorsLoading] = useState(true);
    const [tvCreatorsError, setTvCreatorsError] = useState(null);


    const [reload, setReload] = useState(false)
    const {isLoggedIn, user} = useSelector(state => state.auth);

    const [reloadReviews, setReloadReviews] = useState(false); // state to trigger re-render of Reviews component


    const popoverMoovieRating = (<Popover id="popover-basic">
        <Popover.Body as="h6">Moovie rating</Popover.Body>
    </Popover>);

    const popoverUserRating = (<Popover id="popover-basic">
        <Popover.Body as="h6">Users rating</Popover.Body>
    </Popover>);


    const fetchMedia = async () => {
        try {
            const response = await MediaService.getMediaById(id);
            setMedia(response.data);
        } catch (err) {
            setMediaError(err);
        } finally {
            setMediaLoading(false);
        }
    };

    const fetchGenres = async () => {
        try {
            const response = await GenreService.getGenresForMedia(id);
            setGenres(response.data);
        } catch (err) {
            setGenreError(err)
        } finally {
            setGenresLoading(false)
        }
    }

    const fetchProviders = async () => {
        try {
            const response = await ProviderService.getProvidersForMedia(id);
            setProviders(response.data)
        } catch (err) {
            setProvidersError(err);
        } finally {
            setProvidersLoading(false)
        }
    }


    const fetchTvCreators = async () => {
        try {
            const response = await mediaService.getTvCreatorsByMediaId(id);
            setTvCreators(response.data);
            console.log(response)
        } catch (err) {
            setTvCreatorsError(err);

        } finally {
            setTvCreatorsLoading(false);

        }
    }

    useEffect(() => {
        fetchMedia();
    }, [id, reload]);

    useEffect(() => {

        if (media.type !== "Movie") {
            fetchTvCreators();
        }
        fetchGenres();
        fetchProviders();
    }, [id]);

    const trailerLink = (media.trailerLink === 'None' ? null : media.trailerLink);
    const releaseYear = new Date(media.releaseDate).getFullYear();

    let detailsColumn = <div/>;
    let info = <div/>;

    if (media.type === "Movie") {
        detailsColumn = <span>

                    <div className="d-flex flex-row align-items-center ">
                        <h5>Director:</h5>
                        <MediaTag link={'cast/director'} text={media.director}/>
                    </div>

                    <div className="d-flex flex-row align-items-center ">
                        <h5>Budget: </h5>
                        <MediaTag text={new Intl.NumberFormat('en-US', {
                            style: 'currency', currency: 'USD', maximumFractionDigits: 0
                        }).format(media.budget)}/>
                    </div>

                    <div className="d-flex flex-row align-items-center ">
                        <h5>Revenue: </h5>
                        <MediaTag text={new Intl.NumberFormat('en-US', {
                            style: 'currency', currency: 'USD', maximumFractionDigits: 0
                        }).format(media.revenue)}/>
                    </div>
        </span>

        info = <h5>{releaseYear} • {media.runtime} m • Movie</h5>


    } else {
        detailsColumn =
            <span>

                <div className="d-flex flex-row align-items-center ">
                        {tvCreators.length > 1 ? <h5>Creators:</h5> : <h5>Creator:</h5>}
                    {tvCreators.length > 0 ? tvCreators.map((creator) =>
                        <MediaTag link={`tvcreators`} text={creator.creatorName} id={creator.id}/>
                    ) : (<MediaTag text="No creators available"/>)}
                </div>

                {media.lastAirDate && (
                    <div className="d-flex flex-row align-items-center">
                        <h5>Last Air Date: </h5>
                        <MediaTag
                            text={new Date(media.lastAirDate)
                                .toISOString()
                                .split('T')[0]} // Esto toma solo la parte YYYY-MM-DD
                        />
                    </div>
                )}

                {media.nextEpisodeToAir && (
                    <div className="d-flex flex-row align-items-center ">
                        <h5>Next Episode to Air: </h5>
                        <MediaTag
                            text={new Date(media.nextEpisodeToAir)
                                .toISOString()
                                .split('T')[0]} // Esto toma solo la parte YYYY-MM-DD
                        />
                    </div>
                )
                }
        </span>

        info = <h5>{releaseYear} • Serie • {media.numberOfSeasons} Temporadas • {media.numberOfEpisodes} Episodios</h5>

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
        setReload(!reload);
        handleCloseReviewForm(); // Close the review form after submission
    };

    const handleParentReload = () => {
        setReload(!reload);
    };


    return (
        <div className="container my-1">
            <div className="row align-items-center justify-content-center" style={{marginBottom: '20px'}}>

                {/* POSTER COLUMN */}
                <div className="col text-center">
                    <img src={media.posterPath} className="posterStyle" alt={`${media.name} poster`}/>
                </div>

                {/* MEDIA DETAILS COLUMN */}
                <div className="col" style={{marginBottom: '10px'}}>
                    <h1>{media.name}
                        <MediaTag style={{fontSize: '14px'}} link={'status'} text={media.status}/>
                        <MediaTag style={{fontSize: '14px'}} link={'l'} text={media.originalLanguage}/>
                    </h1>

                    {info}

                    <div>
                        <h1 className="d-flex flex-row align-items-center ">
                            <OverlayTrigger trigger="hover" placement="right" overlay={popoverMoovieRating}>
                                <div className="me-3"><i className={"bi bi-star-fill"}/> {media.tmdbRating}</div>
                            </OverlayTrigger>

                            {(media.voteCount > 0) && (
                                <>
                                    <span>•</span>
                                    <OverlayTrigger trigger="hover" placement="right" overlay={popoverUserRating}>
                                        <div className="m-3"><i className={"bi bi-star"}/> {media.totalRating}</div>
                                    </OverlayTrigger>
                                </>
                            )
                            }

                        </h1>
                    </div>

                    <div className="d-flex flex-row align-items-center" style={{marginBottom: '10px'}}>
                        {providers.length > 0 ? providers.map((provider) => <MediaTag link={`providers`}
                                                                                      text={provider.providerName}
                                                                                      image={provider.logoPath}
                                                                                      id={provider.providerId}/>) :
                            <MediaTag text="No providers available"/>}
                    </div>

                    <div className="d-flex flex-row align-items-center ">
                        <h5>Genres:</h5>
                        {genres.length > 0 ? genres.map((genre) => <MediaTag darkmode={true} link={`genres`}
                                                                             text={genre.genreName}
                                                                             id={genre.genreId}/>) :
                            <MediaTag darkmode={true} text="No genres available"/>}
                    </div>
                    {detailsColumn}

                    {trailerLink && (<div style={{marginBottom: '5px', marginTop: '5px'}}>
                        <iframe style={{width: '85%', height: '315px'}}
                                src={trailerLink.replace("watch?v=", "embed/")}/>
                    </div>)}
                    <span></span>
                    <p> {media.overview} </p>

                    <div className="flex-row d-flex">
                        <AddMediaToListButton currentId={id}/>

                        <CreateReviewButton handleOpenReviewForm={handleOpenReviewForm} rated={null}/>
                    </div>
                </div>


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

            <div className="row my-8">
                <h2>Cast</h2>
                <hr className="my-8"/>
                <ActorCardList mediaId={id}/>
            </div>

            {/* Pass reloadReviews as a key to force re-render */}
            <div className="row">
                <h2>Reseñas</h2>
                <hr className="my-8 mb-4"/>
                <Reviews key={reloadReviews ? id : undefined} id={id} handleParentReload={handleParentReload}
                         source="media"/>
            </div>
        </div>
    );
}

export default Details;
