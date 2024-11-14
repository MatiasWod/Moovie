import React, {useState, useEffect, useCallback} from 'react';
import mediaApi from '../../api/MediaApi'; // Adjust the path if needed
import CardsHorizontalContainer from "../components/cardsHorizontalContainer/CardsHorizontalContainer";
import MediaTypes from "../../api/values/MediaTypes";
import OrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";
import "../components/mainStyle.css"
import ListHeader from "../components/listHeader/ListHeader";
import DropdownMenu from "../components/dropdownMenu/DropdownMenu";
import ListContent from "../components/listContent/ListContent";
import PaginationButton from "../components/paginationButton/PaginationButton";
import PagingSizes from "../../api/values/PagingSizes";
import {useParams} from "react-router-dom";
import Error404 from "./errorViews/error404";


function FeaturedLists() {
    const {type} = useParams();

    //GET VALUES FOR FEATURED MEDIA
    const [featuredMedia, setFeaturedMedia] = useState([]);
    const [featuredMediaLoading, setFeaturedMediaLoading] = useState(true);
    const [featuredMediaError, setFeaturedMediaError] = useState(null);

    let mediaType;
    let orderBy;

    switch (type) {
        //TODO Reviasr que parece que la media es lo mismo que para los movie
        case "topRatedMedia":
            mediaType = MediaTypes.TYPE_ALL
            orderBy = OrderBy.TMDB_RATING
            break;
        case "topRatedMovies":
            mediaType = MediaTypes.TYPE_MOVIE
            orderBy = OrderBy.TMDB_RATING
            break;

        case "topRatedSeries":
            mediaType = MediaTypes.TYPE_TVSERIE
            orderBy = OrderBy.TMDB_RATING
            break;

        case "mostPopularMedia":
            mediaType = MediaTypes.TYPE_ALL
            orderBy = OrderBy.VOTE_COUNT
            break;

        case "mostPopularMovies":
            mediaType = MediaTypes.TYPE_MOVIE
            orderBy = OrderBy.VOTE_COUNT
            break;

        case "mostPopularSeries":
            mediaType = MediaTypes.TYPE_TVSERIE
            orderBy = OrderBy.VOTE_COUNT
            break;

        default:
            mediaType = null;
            orderBy = null;
    }

    const fetchFeaturedMedia = async () => {
        try {
            if (!mediaType || !orderBy) throw new Error("Invalid media type");

            const response = await mediaApi
                .getMedia({
                    type: mediaType,
                    orderBy: orderBy,
                    sortOrder: SortOrder.DESC,
                    page: 1,
                    pageSize: 25
                });
            setFeaturedMedia(response.data);
        } catch (err) {
            setFeaturedMediaLoading(err);
        } finally {
            setFeaturedMediaError(false);
        }
    };

    useEffect(() => {
        fetchFeaturedMedia();
    }, [type]);

    return (
        <div className="default-container moovie-default">
            <h1>Featured List </h1>
            <h1>------</h1>


            <ListContent listContent={featuredMedia}/>


        </div>
    );
}

export default FeaturedLists;