import React, { useState } from "react";
import "../formsStyle.css";
import ReportTypes from "../../../../api/values/ReportTypes";

const ReportForm = ({onCancel, onReportSubmit }) => {
    const [error, setError] = useState(null);
    const [reportReason, setReportReason] = useState("");
    const [additionalInfo, setAdditionalInfo] = useState("");

    const reportReasons = [
        "Hate",
        "Abuse & Harassment",
        "Privacy",
        "Spam"
    ];

    const handleSubmit = async () => {
        try {
            const reportType = ReportTypes[reportReason];
            onReportSubmit?.(reportType, additionalInfo);
        } catch (error) {
            setError(error.response?.data?.message || "Error making request");
        }
    };

    return (
        <div className="overlay">
            <div className="box-review">
                {!error ? (
                    <>
                        <h2>Report</h2>
                        <div className="radio-group">
                            {reportReasons.map((reason) => (
                                <div key={reason} className="radio-option">
                                    <input
                                        type="radio"
                                        id={reason}
                                        name="reportReason"
                                        value={reason}
                                        checked={reportReason === reason}
                                        onChange={(e) => setReportReason(e.target.value)}
                                    />
                                    <label htmlFor={reason}>{reason}</label>
                                </div>
                            ))}
                        </div>
                        <textarea
                            placeholder="Additional information (Optional)"
                            value={additionalInfo}
                            onChange={(e) => setAdditionalInfo(e.target.value)}
                            maxLength="500"
                        ></textarea>
                        <p>{additionalInfo.length}/500</p>
                        <div className="buttons">
                            <button className="cancel" onClick={onCancel}>
                                Cancel
                            </button>
                            <button
                                className="submit"
                                onClick={handleSubmit}
                                disabled={!reportReason}
                            >
                                Submit Report
                            </button>
                        </div>
                    </>
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
}

export default ReportForm;
