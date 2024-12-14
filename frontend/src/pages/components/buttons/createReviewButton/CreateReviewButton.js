import React from "react";
import "../buttonStyles.css";

const CreateReviewButton = ({ handleOpenReviewForm, rated }) => (
    rated ? (
        <button
            type="button"
            className="btn btn-dark border border-black"
            onClick={handleOpenReviewForm}
        >
            <i className="bi bi-star-fill"></i> Rated
        </button>
    ) : (
        <button
            type="button"
            className="btn btn-light border border-black"
            onClick={handleOpenReviewForm}
        >
            <i className="bi bi-star-fill"></i> Rate
        </button>
    )
);

export default CreateReviewButton;
