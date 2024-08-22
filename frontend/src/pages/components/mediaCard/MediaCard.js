import React from 'react';
import './mediaCard.css';
import {Link} from "react-router-dom";
import "../mainStyle.css"

const MediaCard = ({ media }) => {
    const releaseDate = new Date(media.releaseDate).getFullYear()
    return (
        <div className="card shadow" >
            <div className="card-body" style={{ borderRadius: '5%' }}>
                <span data-toggle="tooltip" data-placement="top" title={`(${media.title})`}>
                    <Link to={"/details/" + media.id}> <img className="card-img-top" style={{borderRadius: '5%'}} src={media.posterPath}/></Link>
                </span>
                <Link to={"/details/" + media.id} className="not-link"> <h4 className="card-title">{media.name}</h4></Link>
                <h5>{releaseDate}</h5>
            </div>
        </div>
    );
};

export default MediaCard;