import React, { useState } from "react";
import "../formsStyle.css";
import mediaService from "../../../../services/MediaService";

const ReviewForm = ({mediaName, closeReview, mediaId, onReviewSubmit }) => {
    const [error, setError] = useState(null);
    const [rating, setRating] = useState(0);
    const [review, setReview] = useState("");

    const handleStarClick = (value) => {
        setRating(value);
    };

    const handleSubmit = async () => {
        try {
            const response = await mediaService.createReview(mediaId, rating, review);

            if (response.status === 200 || response.status === 201) {
                // Call onReviewSubmit if provided to reload reviews
                onReviewSubmit?.();
                // Close the pop-up
                closeReview();
                return;
            } else {
                // Set error if response is not successful
                setError(response.data.message || "Error submitting review");
            }
        } catch (error) {
            // Set error for any network or other errors
            setError(error.response?.data?.message || "Error making request");
        }
    };

    return (
        <div className="overlay">
            <div className="box-review">
                {!error ? (
                    <>
                        <h2>Tu review de {mediaName}</h2>
                        <div className="stars">
                            {[1, 2, 3, 4, 5].map((value) => (
                                <span
                                    key={value}
                                    className={`star ${value <= rating ? "selected" : ""}`}
                                    onClick={() => handleStarClick(value)}
                                >
                                ★
                            </span>
                            ))}
                        </div>
                        <textarea
                            placeholder="Tu reseña (Opcional)"
                            value={review}
                            onChange={(e) => setReview(e.target.value)}
                            maxLength="500"
                        ></textarea>
                        <p>{review.length}/500</p>
                        <div className="buttons">
                            <button className="cancel" onClick={closeReview}>
                                Cancelar
                            </button>
                            <button
                                className="submit"
                                onClick={handleSubmit}
                                disabled={rating === 0}
                            >
                                Enviar
                            </button>
                        </div>
                    </>
                ) : (
                    <>
                        <h2 style={{ color: "red" }}>{error}</h2>
                        <button className="cancel" onClick={() => setError(null)}>
                            Volver
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}

export default ReviewForm;