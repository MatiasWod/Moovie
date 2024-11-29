import React, { useEffect, useState } from "react";
import mediaApi from "../../../api/MediaApi";
import userApi from "../../../api/UserApi";

function Reviews({ id, source }) {
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchReviews = async () => {
        try {
            let response;
            setLoading(true)

            switch (source) {
                case 'media':
                    response = await mediaApi.getReviewsByMediaId(id);
                    break;
                case 'user':
                    response = await userApi.getMovieReviewsFromUser(id);
                    break;
                default:
                    throw new Error(`Unsupported source: ${source}`);
            }
            setReviews(response.data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if ((source === 'media' && id) || (source === 'user' && id)) {
            fetchReviews();
        }
    }, [id,source]);

    if (loading) return <div>Cargando reseñas...</div>;
    if (error) return <div>Error al cargar reseñas: {error}</div>;
    if (reviews.length === 0) return <div>No hay reseñas disponibles.</div>;

    return (
        <div className="reviews-container">
            <h2>Reseñas</h2>
            {reviews.map((review) => (
                <div key={review.id} className="review">
                    <div className="review-header">
                        <strong>{review.username}</strong> - <small>{review.rating}</small>
                    </div>
                    <div className="review-content">{review.reviewContent}</div>
                </div>
            ))}
        </div>
    );
}

export default Reviews;
