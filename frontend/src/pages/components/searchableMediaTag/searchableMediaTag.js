import React from "react";

const SearchableMediaTag = ({ image, text, link }) => {
    return (
        <div>
            <a href={link + '=' + text}>
                {image && <img src={image}/>}
                {text}
            </a>
        </div>
    );
}

export default SearchableMediaTag;