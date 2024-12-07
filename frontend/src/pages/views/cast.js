import React, {useCallback, useEffect, useState} from 'react';
import {createSearchParams, useNavigate, useParams, useSearchParams} from "react-router-dom";
import CastService from "../../services/CastService";
import MediaCard from "../components/mediaCard/MediaCard";

function Cast(){
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const {id} = useParams();

    const [actorMedias, setActorMedias] = useState(undefined);
    const [actorMediasLoading, setActorMediasLoading] = useState(true);
    const [actorMediasError, setActorMediasError] = useState(null);

    useEffect(() => {
        if (!id) {
            console.warn("Actor ID is undefined, skipping API call.");
            return;
        }

        console.log("Actor ID used for API call:", id); // Confirm it's passed correctly

        async function getData() {
            try {
                const data = await CastService.getMediasForActor({ id });
                setActorMedias(data);
            } catch (error) {
                console.error("Error fetching actor media:", error);
                setActorMediasError(error);
            } finally {
                setActorMediasLoading(false);
            }
        }

        getData();
    }, [id]);

    return (
        <div className="discover-media-card-container">
            <>
                {actorMedias?.data?.length > 0 ? (
                    <>
                        <h3>Medias for:</h3>
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