import React from "react";
import {createSearchParams, useNavigate} from 'react-router-dom';

const SearchableMediaTag = ({ image, text, link, id }) => {

    const navigate = useNavigate();

    const handleClick = () => {
        if (link) {
            const providersParam = JSON.stringify([id]);

            navigate({
                pathname: `/discover`,
                search: `?${link}=${encodeURIComponent(providersParam)}`,
            });
        }
    };

    return (
        <div onClick={handleClick} style={{display:'inline-flex',alignItems: 'center', cursor: link ? 'pointer' : 'default' }}>
                {image && <img src={image} style={{height:'1.6em',marginRight:'5px',verticalAlign:'middle'}} />}
                {text}
        </div>
    );
}

export default SearchableMediaTag;