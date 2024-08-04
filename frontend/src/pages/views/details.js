import React, {useEffect, useState} from 'react';
import Navbar from "../components/navBar/navbar";
import {useParams} from "react-router-dom";
import mediaApi from "../../api/MediaApi";

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


    return (
        <div className={"moovie-default"}>
            <h1>Details Page</h1>
            <h1>{media.name}</h1>
        </div>
    );
}

export default Details;