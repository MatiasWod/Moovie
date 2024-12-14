import React from "react";

const SearchableMediaTag = ({ image, text, link }) => {
    return (
        <div>
            <a href={link + '=' + text} className="not-link" style={{display:'inline-flex',alignItems: 'center'}}>
                {image && <img src={image} style={{height:'1.6em',marginRight:'5px',verticalAlign:'middle'}} />}
                {text}
            </a>
        </div>
    );
}

export default SearchableMediaTag;