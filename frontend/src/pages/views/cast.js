import React, {useCallback, useEffect, useState} from 'react';
import {createSearchParams, useNavigate, useParams, useSearchParams, useLocation} from "react-router-dom";
import CastService from "../../services/CastService";
import MediaCard from "../components/mediaCard/MediaCard";
import mediaService from "../../services/MediaService";
import {useTranslation} from "react-i18next";
import {Divider} from "@mui/material";

function Cast(){
    const navigate = useNavigate();
    const location = useLocation();
    const [searchParams] = useSearchParams();
    const {id} = useParams();
    const {t} = useTranslation();

    const [actorMedias, setActorMedias] = useState(undefined);
    const [actorMediasLoading, setActorMediasLoading] = useState(true);
    const [actorMediasError, setActorMediasError] = useState(null);
    const [selectedActor, setSelectedActor] = useState(location.state?.actorName || "Unknown Actor");

    const isActor = location.pathname.includes("/cast/actor/");
    const isTvCreator = location.pathname.includes("/tvcreators/");
    const isDirector = location.pathname.includes("/cast/director/");

    useEffect(() => {
        if (isActor) {

            async function getData() {
                try {
                    const data = await mediaService.getMediasForActor({id});
                    setActorMedias(data);
                } catch (error) {
                    console.error("Error fetching actor media:", error);
                    setActorMediasError(error);
                } finally {
                    setActorMediasLoading(false);
                }
            }

            getData();
        }
    }, [id]);

    useEffect(() => {
        if (isTvCreator) {

            async function getData() {
                try {
                    const data = await mediaService.getMediasForTVCreator({id});
                    setActorMedias(data);
                    console.log(data)
                } catch (error) {
                    console.error("Error fetching actor media:", error);
                    setActorMediasError(error);
                } finally {
                    setActorMediasLoading(false);
                }
            }

            getData();
        }
    }, [id]);

    useEffect(() => {
        if (isDirector) {

            async function getData() {
                try {
                    const data = await mediaService.getMediasForDirector({id});
                    setActorMedias(data);
                } catch (error) {
                    console.error("Error fetching actor media:", error);
                    setActorMediasError(error);
                } finally {
                    setActorMediasLoading(false);
                }
            }

            getData();
        }
    }, [id]);

    return (
        <div className="discover-media-card-container">
            <>
                {actorMedias?.data?.length > 0 ? (
                    <>
                        <h3>{t('cast.mediasFor',{selectedActor:selectedActor})}</h3>
                        <Divider sx={{
                            backgroundColor: "rgba(0, 0, 0, 0.8)",
                            height: "2px",
                        }} />
                        <div className="cards-container">
                            {actorMedias.data.map((media) => (
                                <div className="discover-media-card" key={media.id}>
                                    <MediaCard media={media} />
                                </div>
                            ))}
                        </div>
                    </>
                ) : (
                    <p>{t('cast.noMediasFound')}</p>
                )}
            </>
        </div>
    )
        ;
}

export default Cast;