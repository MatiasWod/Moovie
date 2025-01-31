import React from 'react'
import { Card } from "react-bootstrap";
import {formatDate, truncateText} from "../../../../utils/FormatUtils";


const MediaCard = ({ media, pageName, onClick, isSelected }) => {
    return (
        <div onClick={onClick} className={'poster card text-bg-dark m-1'}>
            <div id={media.id} className={'card-img-container'}>
                <img className={'crop-center'} loading='lazy' src={media.posterPath} alt={''} />
                <div className={'card-img-overlay'}>
                    <h6 className={'card-title text-center'}>
                        {truncateText(media.name, 20)}
                    </h6>
                    <div className={'d-flex flex-column justify-evenly align-items-center'}>
                        <Card.Text>
                            <i className={'bi bi-star-fill'} />
                            {media.tmdbRating}
                        </Card.Text>
                        <Card.Text>
                            {formatDate(media.releaseDate)}
                        </Card.Text>
                    </div>
                    <div id={'genres'} className={'d-flex justify-evenly flex-wrap'}>
                        {media.genres.data.slice(0, 2).map((genre, index) => (
                            <span key={index} className={'mt-1 badge text-bg-dark'}>
                                {genre.genreName}
                            </span>
                        ))}
                    </div>
                    <div id={'providers'} className={'d-flex mt-3 justify-evenly flex-wrap'}>
                        {media.providers.data.slice(0, 2).map((provider, index) => (
                            <span key={index} className={'mt-1 badge text-bg-light border border-black'}>
                                <img
                                    src={provider.logoPath}
                                    alt="provider logo"
                                    style={{ height: '1.5em', marginRight: '5px' }}
                                />
                            </span>
                        ))}
                    </div>
                </div>
                <div className={'interaction-img-overlay'}>
                    {pageName === 'createList' &&
                        (isSelected ? (
                            <div className={'d-flex justify-center'}>
                                <Card.Title>
                                    <i className={'bi bi-check-circle-fill'} style={{ color: "green" }} />
                                </Card.Title>
                            </div>
                        ) : (
                            <div></div>
                        ))}
                </div>
            </div>
        </div>
    );
};

export default MediaCard;
