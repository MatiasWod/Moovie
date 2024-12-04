import React, { useState } from "react";
import "../formsStyle.css";
import mediaService from "../../../../services/MediaService";

const ReviewForm = ({mediaName, closeReview, mediaId }) => {
    const [finished, setFinished] = useState(false);
    const [postResponse, setPostResponse] = useState("");
    const [responseColor, setResponseColor] = useState("");


    const [rating, setRating] = useState(0);
    const [review, setReview] = useState("");
    const [showModal, setShowModal] = useState(true);

    const handleStarClick = (value) => {
        setRating(value);
    };

    const handleSubmit = async () => {
        try{
            console.log(mediaId);
            const response = await mediaService.createReview(mediaId, rating, review);

            console.log(response.data.message);

            if (response.status === 200) {
                setPostResponse("success");
                setResponseColor("green");
            } else {
                setPostResponse(response.data.message);
                setResponseColor("red");
            }
        } catch (error) {
            setPostResponse("Error making request.");
            setResponseColor("red");
        }
        setFinished(true);
        setShowModal(false);
    };


    if (!finished) {
        return (
            <div className="overlay">
                <div className="box-review">
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
                        <button className="submit" onClick={handleSubmit}>
                            Enviar
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="overlay">
            <div className="box-review">
                <h2 style={{ color: responseColor }}>{postResponse}</h2>
                <button className="cancel" onClick={closeReview}>
                    Close
                </button>
            </div>
        </div>
    );



}

export default ReviewForm;
