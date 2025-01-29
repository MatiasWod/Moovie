import React, {useCallback, useEffect, useState} from 'react';
import {createSearchParams, useNavigate, useParams, useSearchParams} from "react-router-dom";
import SortOrder from "../../api/values/SortOrder";
import mediaOrderBy from "../../api/values/MediaOrderBy";
import MediaTypes from "../../api/values/MediaTypes";
import MediaCard from "../components/mediaCard/MediaCard";
import MediaService from "../../services/MediaService";
import pagingSizes from "../../api/values/PagingSizes";
import PaginationButton from "../components/paginationButton/PaginationButton";
import ListService from "../../services/ListService";
import cardsListOrderBy from "../../api/values/CardsListOrderBy";
import sortOrder from "../../api/values/SortOrder";
import ListCard from "../components/listCard/ListCard";
import CastService from "../../services/CastService";
import userService from "../../services/UserService";
import ActorCard from "../components/actorCards/ActorCard";
import MediaOrderBy from "../../api/values/MediaOrderBy";
import CardsListOrderBy from "../../api/values/CardsListOrderBy";
import {useTranslation} from "react-i18next";
import {Divider} from "@mui/material";
import './discover.css';

function Healthcheck() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const {search} = useParams();

    const [medias, setMedias] = useState(undefined);
    const [mediaLoading, setMediaLoading] = useState(true);
    const [mediaError, setMediaError] = useState(null);

    const [lists, setLists] = useState(undefined);
    const [listLoading, setListLoading] = useState(true);
    const [listError, setListError] = useState(null);

    const [actors, setActors] = useState(undefined);
    const [actorLoading, setActorLoading] = useState(true);
    const [actorError, setActorError] = useState(null);


    const [users, setUsers] = useState(undefined);
    const [userLoading, setUserLoading] = useState(true);
    const [userError, setUserError] = useState(null);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);

    const handlePageChange = useCallback((newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/search/${search}`,
            search: createSearchParams({ search, page: newPage.toString() }).toString(),
        });
    }, [navigate, search]);

    const handleUserCardClick = (user) =>{
        navigate(`/profile/${user.username}`);
    }

    const handleActorCardClick = (actor) => {
        navigate(`/cast/actor/${actor.actorId}`, { state: { actorName: actor.actorName } });
    };

    useEffect(() => {
        async function getData() {
            try {
                const data = await MediaService.getMedia({
                    type: MediaTypes.TYPE_ALL,
                    page: page,
                    pageSize: pagingSizes.MEDIA_SEARCH_PAGE_SIZE,
                    sortOrder: SortOrder.DESC,
                    orderBy: mediaOrderBy.RELEASE_DATE,
                    search:search
                });
                setMedias(data);
                setMediaLoading(false);
            } catch (error) {
                setMediaError(error);
                setMediaLoading(false);
            }
        }

        getData();
    }, [search,page]);

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getLists({
                    orderBy: cardsListOrderBy.LIKE_COUNT,
                    ownerUsername: null,
                    pageNumber: page,
                    pageSize: pagingSizes.MOOVIE_LIST_SEARCH_PAGE_SIZE,
                    search: search,
                    type: 1,
                    order: sortOrder.DESC
                });
                setLists(data);
                setListLoading(false);
            } catch (error) {
                setListError(error);
                setListLoading(false);
            }
        }

        getData();
    }, [search,page]);

    useEffect(() => {
        async function getData() {
            try {
                const data = await CastService.getActorsForQuery({
                    search: search
                });
                console.log(data)
                setActors(data.data);
                setActorLoading(false);
            } catch (error) {
                setActorError(error);
                setActorLoading(false);
            }
        }
        getData();
    }, [search]);


    //TODO ponerle un orderby
    useEffect(() => {
        async function getData() {
            try {
                console.log(search)
                const data = await userService.getSearchedUsers({
                    username: search,
                    orderBy: "username",
                    sortOrder: SortOrder.DESC,
                    page: page
                });
                setUsers(data);
                setUserLoading(false);
            } catch (error) {
                setUserError(error);
                setUserLoading(false);
            }
        }

        getData();
    }, [search,page]);

    return (
        <div className="discover-media-card-container">
            <>
                {medias?.data?.length > 0 ? (
                    <>
                        <h3>{t('search.mediasFor', {search: search})}</h3>
                        <Divider sx={{
                            backgroundColor: "rgba(0, 0, 0, 0.8)",
                            height: "2px",
                        }} />
                        <div className="cards-container">
                            {medias.data.map((media) => (
                                <div className="discover-media-card" key={media.id}>
                                    <MediaCard media={media} />
                                </div>
                            ))}
                        </div>
                    </>
                ) : (
                    <p>{t('search.noMediasFound')}</p>
                )}
            </>


            <>
                {lists?.data?.length > 0 ? (
                    <>
                        <h3>{t('search.listsFor', {search: search})}</h3>
                        <Divider sx={{
                            backgroundColor: "rgba(0, 0, 0, 0.8)",
                            height: "2px",
                        }} />
                        <div className="cards-container">
                            {lists.data.slice(0, 5).map((list) => (
                                <div className="discover-media-card" key={list.id}>
                                    <ListCard listCard={list} />
                                </div>
                            ))}
                        </div>
                    </>
                ) : (
                    <p>{t('search.noListsFound')}</p>
                )}
            </>

                {actors && actors.length > 0 ? (
                    <>
                        <h3>{t('search.actorsFor', {search: search})}</h3>
                        <Divider sx={{
                            backgroundColor: "rgba(0, 0, 0, 0.8)",
                            height: "2px",
                        }} />
                        <div className="cards-container">
                            {actors.slice(0,5).map((actor) => (
                                <div
                                    key={actor.actorId}
                                    onClick={() => handleActorCardClick(actor)}
                                    style={{ cursor: "pointer" }}
                                >
                                    <div className="discover-media-card">
                                        <ActorCard
                                            name={actor.actorName}
                                            image={actor.profilePath}
                                        />
                                    </div>
                                </div>
                            ))}
                        </div>
                    </>
                ) : (
                    <p>{t('search.noActorsFound')}</p>
                )}
                <div>
                    {users?.data?.length > 0 ? (
                        <>
                            <h3>{t('search.usersFor', {search: search})}</h3>
                            <Divider sx={{
                                backgroundColor: "rgba(0, 0, 0, 0.8)",
                                height: "2px",
                            }} />
                            <div className="cards-container">
                                {users.data.slice(0,5).map((user) => (
                                    <div
                                        key={user.username}
                                        onClick={() => handleUserCardClick(user)}
                                        style={{ cursor: "pointer" }}
                                    >
                                        <div className="discover-media-card">
                                            <ActorCard name={user.username} />
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </>
                    ) : (
                        <p>{t('search.noUsersFound')}</p>
                    )}
                </div>
        </div>
    )
    ;
}

export default Healthcheck;