import React, { useEffect, useState } from 'react';
import mediaService from "../../../services/MediaService";
import ActorCard from "./ActorCard";

const ActorCardList = ({ mediaId }) => {
    const [actors, setActors] = useState([]);
    const [actorsLoading, setActorsLoading] = useState(true);
    const [actorsError, setActorsError] = useState(null);



    useEffect(() => {
        async function fetchActors()  {
            try {
                const response = await mediaService.getActorsByMediaId(mediaId);
                setActors(response.data);

                console.log(response.data.length)

            } catch (err) {
                setActorsError(err);

            } finally {
                setActorsLoading(false);

            }
        }

        fetchActors();
    }, [mediaId]);

    if (actorsLoading) {
        return <div>Cargando actores...</div>;
    }

    if (actorsError) {
        return <div>Error al cargar actores: {actorsError.message}</div>;
    }

    return (
        <div style={{ display: 'flex', gap: '16px', overflowX: 'auto' }}>
            {actors.map((actor) => (
                <ActorCard
                    key={actor.actorId}
                    name={actor.actorName}
                    image={actor.profilePath}
                />
            ))}
        </div>
    );
};

export default ActorCardList;
