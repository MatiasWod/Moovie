import React, { useState } from "react";
import "../buttonStyles.css";

const CreateReviewButton = ({handleOpenReviewForm}) => {
    return (
        <button className={"dropdown-button"}  onClick={handleOpenReviewForm}>
            + Review
        </button>
    );
}

export default CreateReviewButton;

