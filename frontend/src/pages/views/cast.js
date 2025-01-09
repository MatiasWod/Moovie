import React, {useCallback, useEffect, useState} from 'react';
import {createSearchParams, useNavigate, useParams, useSearchParams, useLocation} from "react-router-dom";
import CastService from "../../services/CastService";
import MediaCard from "../components/mediaCard/MediaCard";
import TVCreatorsService from "../../services/TVCreatorsService";

function Cast(){
    const navigate = useNavigate();
    const location = useLocation();
    const [searchParams] = useSearchParams();
    const {id} = useParams();

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
                    const data = await CastService.getMediasForActor({id});
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
                    const data = await TVCreatorsService.getMediasForTVCreator({id});
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
                    const data = await CastService.getMediasForDirector({id});
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
                        <h3>Medias for: {selectedActor}</h3>
                        {actorMedias.data.map((media) => (
                            <div className="discover-media-card" key={media.id}>
                                <MediaCard media={media} />
                            </div>
                        ))}
                    </>
                ) : (
                    <p>No medias found.</p>
                )}
            </>
        </div>
    )
        ;
}

export default Cast;