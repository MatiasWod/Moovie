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

function Details() {
    const { id } = useParams();

    //GET VALUES FOR Media
    const [media, setMedia] = useState({});
    const [mediaLoading, setMediaLoading] = useState(true);
    const [mediaError, setMediaError] = useState(null);
    const [reload, setReload] = useState(false)

    const { isLoggedIn, user } = useSelector(state => state.auth);

    const [reloadReviews, setReloadReviews] = useState(false); // state to trigger re-render of Reviews component

    const popoverMoovieRating = (<Popover id="popover-basic">
        <Popover.Body as="h6">Moovie rating</Popover.Body>
    </Popover>);

    const popoverUserRating = (<Popover id="popover-basic">
        <Popover.Body as="h6">Users rating</Popover.Body>
    </Popover>);


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
    }, [id, reload]);

    const trailerLink = (media.trailerLink === 'None' ? null : media.trailerLink);
    const releaseYear = new Date(media.releaseDate).getFullYear();

    let detailsColumn = <div />;
    let info = <div/>;

    if (media.type === "Movie") {
        detailsColumn = <span><div className="d-flex flex-row align-items-center ">
                    </div>

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
            <div className="col">

                <div>Creadores:</div>

                {media.lastAirDate && (
                    <div>Fecha de Ultima Emisión: {media.lastAirDate}</div>
                )}

                {media.nextEpisodeToAir && (
                    <div>Próximo Episodio a Emitir: {media.nextEpisodeToAir}</div>
                )}

            </div>

        info = <h1>{releaseYear} • Serie • {media.numberOfSeasons} Temporadas • {media.numberOfEpisodes} Episodios</h1>

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
        <div className="moovie-default default-container">
            <div className="row">

                {/* POSTER COLUMN */}
                <div className="col">
                    <img src={media.posterPath} className="posterStyle"/>
                </div>

                {/* MEDIA DETAILS COLUMN */}
                <div className="col">
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

                    <h5>Genres:</h5>
                    {detailsColumn}

                    {trailerLink && (<div style={{marginBottom: '5px', marginTop: '5px'}}>
                        <iframe style={{width: '85%', height: '315px'}}
                                src={trailerLink.replace("watch?v=", "embed/")}/>
                    </div>)}
                    <span></span>
                    <p> {media.overview} </p>

                    <div Class="flex-row d-flex">
                        <AddMediaToListButton currentId={id}/>

                        <CreateReviewButton handleOpenReviewForm={handleOpenReviewForm} rated={}/>
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

            <ActorCardList mediaId={id}/>

            {/* Pass reloadReviews as a key to force re-render */}
            <Reviews key={reloadReviews ? id : undefined} id={id} handleParentReload={handleParentReload}
                     source="media"/>
        </div>
    );
}

export default Details;
