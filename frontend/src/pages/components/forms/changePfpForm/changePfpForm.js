import React, { useState } from "react";
import "../formsStyle.css";
import UserService from "../../../../services/UserService";
import { useSelector } from "react-redux";

const ChangePfpForm = ({ onCancel }) => {
    const { user } = useSelector((state) => state.auth); // Fetch logged-in user's details
    const [selectedFile, setSelectedFile] = useState(null);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null); // State to track success message

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!selectedFile) {
            alert("Please select a file first.");
            return;
        }

        try {
            let form = new FormData();
            form.append("image", selectedFile);
            const response = await UserService.setPfp({
                username: user.username,
                pfp: form
            });
            console.log(response);
            if (response.status === 200) {
                setSuccess("Profile picture updated successfully!");

            } else{
                throw new Error();
            }
        } catch (error) {
            setError(error.message || "An error occurred while uploading the profile picture.");
        }
    };

    return (
        <div className="overlay">
            <div className="box-review">
                {!error ? (
                    success ? (
                        <div>
                            <h2 style={{color: "green"}}>{success}</h2>
                            <button type="button" className="cancel" onClick={onCancel}>
                                Close
                            </button>
                        </div>

                    ) : (
                        <>
                            <h2>Change Profile Picture</h2>
                            <form onSubmit={handleSubmit}>
                            <input
                                    type="file"
                                    accept="image/*"
                                    onChange={handleFileChange}
                                />
                                <div className="buttons">
                                    <button type="button" className="cancel" onClick={onCancel}>
                                        Cancel
                                    </button>
                                    <button type="submit" className="submit">
                                        Submit
                                    </button>
                                </div>
                            </form>
                        </>
                    )
                ) : (
                    <>
                        <h2 style={{ color: "red" }}>{error}</h2>
                        <button className="cancel" onClick={() => setError(null)}>
                            Back
                        </button>
                    </>
                )}
            </div>
        </div>
    );
};

export default ChangePfpForm;
