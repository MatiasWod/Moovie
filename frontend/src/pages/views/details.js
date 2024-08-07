import React, {useEffect, useState} from 'react';
import './details.css';
import {useParams} from "react-router-dom";
import mediaApi from "../../api/MediaApi";
import SearchableMediaTag from "../components/searchableMediaTag/searchableMediaTag";
import MediaTypes from "../../api/values/MediaTypes";

function Details() {

    const {id} = useParams();

    //GET VALUES FOT Top Rated Movies
    const [media, setMedia] = useState([]);
    const [mediaLoading, setMediaLoading] = useState(true);
    const [mediaError, setMediaError] = useState(null);

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
    }, []);

    const trailerLink = (media.trailerLink === 'None' ? null : media.trailerLink);
    const releaseYear = new Date(media.releaseDate).getFullYear();

    let detailsColumn = <div/>;

    if(media.type==="Movie") {

        detailsColumn =
            <div className="col">
                <h1>{media.name}
                    <SearchableMediaTag link={'status'} text={media.status}/>
                    <SearchableMediaTag link={'l'} text={media.originalLanguage}/>
                </h1>
                <h1>{releaseYear} • Pelicula • {media.runtime} m </h1>

                <div>Generos:</div>
                <div>Director: <SearchableMediaTag link={'cast/director'} text={media.director}/></div>
                <div>Presupuesto: {media.budget}</div>
                <div>Ingresos: {media.revenue}</div>

                {trailerLink && (
                    <iframe src={trailerLink.replace("watch?v=", "embed/")}/>
                )}

                <div>{media.overview}</div>
            </div>

    } else {

        detailsColumn =
            <div className="col">
                <h1>{media.name}
                    <SearchableMediaTag link={'status'} text={media.status}/>
                    <SearchableMediaTag link={'l'} text={media.originalLanguage}/>
                </h1>
                <h1>{releaseYear} • Serie • {media.numberOfSeasons} Temporadas • {media.numberOfEpisodes} Episodios</h1>

                <div>Generos:</div>
                <div>Creadores:</div>

                {media.lastAirDate && (
                    <div>Fecha de Ultima Emision: {media.lastAirDate}</div>
                )}

                {media.nextEpisodeToAir && (
                    <div>Proximo Episodio a Emitir:{media.nextEpisodeToAir}</div>
                )}

                {trailerLink && (
                    <iframe src={trailerLink.replace("watch?v=", "embed/")}/>
                )}

                <div>{media.overview}</div>
            </div>

    }


    return (
        <div className={"moovie-default"}>
        <div className="row">

                {/*POSTER COLUMN*/}
                <div className="col">
                    <img src={media.posterPath} className="posterStyle"/>
                </div>

                {/*MEDIA DETAILS COLUMN*/}
                {detailsColumn}

            </div>

        </div>
    );
}

export default Details;