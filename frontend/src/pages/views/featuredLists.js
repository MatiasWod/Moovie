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
import {createSearchParams, useNavigate, useParams, useSearchParams} from "react-router-dom";
import Error404 from "./errorViews/error404";
import MediaService from "../../services/MediaService";
import pagingSizes from "../../api/values/PagingSizes";
import {useTranslation} from "react-i18next";


function FeaturedLists() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const { t } = useTranslation();

    //GET VALUES FOR FEATURED MEDIA
    const {type} = useParams();
    const [featuredMedia, setFeaturedMedia] = useState(undefined);
    const [featuredMediaLoading, setFeaturedMediaLoading] = useState(true);
    const [featuredMediaError, setFeaturedMediaError] = useState(null);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);

    let featuredListTypeName;
    let mediaType;
    let orderBy;

    switch (type) {
        //TODO Reviasr que parece que la media es lo mismo que para los movie
        case "topRatedMedia":
            featuredListTypeName = "topRatedMedia";
            mediaType = MediaTypes.TYPE_ALL
            orderBy = OrderBy.TMDB_RATING
            break;
        case "topRatedMovies":
            featuredListTypeName = "topRatedMovies";
            mediaType = MediaTypes.TYPE_MOVIE
            orderBy = OrderBy.TMDB_RATING
            break;

        case "topRatedSeries":
            featuredListTypeName = "topRatedSeries";
            mediaType = MediaTypes.TYPE_TVSERIE
            orderBy = OrderBy.TMDB_RATING
            break;

        case "mostPopularMedia":
            featuredListTypeName = "mostPopularMedia";
            mediaType = MediaTypes.TYPE_ALL
            orderBy = OrderBy.VOTE_COUNT
            break;

        case "mostPopularMovies":
            featuredListTypeName = "mostPopularMovies";
            mediaType = MediaTypes.TYPE_MOVIE
            orderBy = OrderBy.VOTE_COUNT
            break;

        case "mostPopularSeries":
            featuredListTypeName = "mostPopularSeries";
            mediaType = MediaTypes.TYPE_TVSERIE
            orderBy = OrderBy.VOTE_COUNT
            break;

        default:
            featuredListTypeName = null
            mediaType = null;
            orderBy = null;
    }

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/featuredLists/${featuredListTypeName}`,
            search: createSearchParams({
                type:mediaType,
                orderBy:orderBy,
                sortOrder: SortOrder.DESC,
                page: newPage.toString(),
            }).toString(),
        });
    };


    useEffect(() => {
        async function getData() {
            try {
                const data = await MediaService.getMedia({
                    type: mediaType,
                    page: page,
                    pageSize: pagingSizes.FEATURED_MOOVIE_LIST_DEFAULT_TOTAL_CONTENT,
                    sortOrder: SortOrder.DESC,
                    orderBy: orderBy,
                });
                setFeaturedMedia(data);
                setFeaturedMediaLoading(false);
            } catch (error) {
                setFeaturedMediaError(error);
                setFeaturedMediaLoading(false);
            }
        }

        getData();
    }, [type, page]);

    return (
        <div className="default-container moovie-default">
            <h1>{t('featuredLists.featuredList')}</h1>

            <ListContent listContent={featuredMedia?.data || []} />
            <div className="flex justify-center pt-4">
                {featuredMedia?.data?.length > 0 && featuredMedia.links?.last?.page > 1 && (
                    <PaginationButton
                        page={page}
                        lastPage={featuredMedia.links.last.page}
                        setPage={handlePageChange}
                    />
                )}
            </div>

        </div>
    );
}

export default FeaturedLists;