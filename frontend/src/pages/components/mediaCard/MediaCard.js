import React, {useState} from 'react';
import './mediaCard.css';
import {Link, useNavigate} from "react-router-dom";
import "../mainStyle.css"

const MediaCard = ({ media }) => {

    const navigate = useNavigate();

    const handleClick = (id) => {
        navigate(`/details/${id}`);
    };

    const [hoveredId, setHoveredId] = useState(null);

    const handleMouseEnter = (id) => {
        setHoveredId(id);
    };

    const handleMouseLeave = () => {
        setHoveredId(null);
    };

    const releaseDate = new Date(media.releaseDate).getFullYear()
    return (
        <div className="card shadow" >
            <div className="card-body" style={{ borderRadius: '5%' }} onMouseEnter={() => handleMouseEnter(media.id)}
                 onMouseLeave={handleMouseLeave}>
                <span data-toggle="tooltip" data-placement="top" title={`(${media.title})`}>
                    <Link to={"/details/" + media.id}>
                        <img className="card-img-top" style={{borderRadius: '5%'}} src={media.posterPath} /></Link>
                </span>
                <Link to={"/details/" + media.id} className="not-link"> <h4 className="card-title">{media.name}</h4></Link>
                <h5>{releaseDate}</h5>
            </div>
        </div>
    );
};

export default MediaCard;