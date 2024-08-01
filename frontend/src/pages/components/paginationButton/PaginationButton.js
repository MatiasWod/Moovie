import React from "react";

const PaginationButton = (({currentPage, setCurrentPage, totalPages}) =>{
    const handleNext = () => {
        if(currentPage<totalPages){
            setCurrentPage(currentPage + 1);
        }
    };

    const handlePrevious = () => {
        if(currentPage>1){
            setCurrentPage(currentPage - 1);
        }
    };


    return(
        <div style={{display: "flex"}}>
            <button onClick={handlePrevious}>←</button>
            <div>{currentPage}</div>
        </div>
    )
});

export default PaginationButton;