import React from "react";
import {createSearchParams, useNavigate} from 'react-router-dom';

const SearchableMediaTag = ({ image, text, link, id }) => {

    const navigate = useNavigate();

    const handleClick = () => {
        if (link === `providers` || link === `genres`) {
            const providersParam = JSON.stringify([id]);

            navigate({
                pathname: `/discover`,
                search: `?${link}=${encodeURIComponent(providersParam)}`,
            });
        }
        if (link === `tvcreators` || link === `cast/director`) {
            console.log(id);
            navigate(`/${link}/${id}`,{ state: { actorName: text } });
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