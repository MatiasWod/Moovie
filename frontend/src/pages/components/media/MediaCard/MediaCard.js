import React from 'react'
import {Card} from "react-bootstrap";

const MediaCard = ({ media, pageName, onClick, isSelected }) => {
    const placeholderGenre = Array.from({length: 10})
    return <div onClick={onClick} className={'poster card text-bg-dark m-1'}>
        <div id={media.id} className={'card-img-container'}>
            <img className={'crop-center'} loading='lazy' src={media.posterPath} alt={''}/>
            <div className={'card-img-overlay'}>
                <h6 className={'card-title text-center'}>{media.name}</h6>
                <div className={'d-flex justify-evenly'}>
                    <Card.Text>
                        <i className={'bi bi-star-fill'}/>
                        7.5
                    </Card.Text>
                    <Card.Text>
                        10/12/2025
                    </Card.Text>
                </div>
                <div id={'genres'} className={'d-flex justify-evenly flex-wrap'}>
                    {placeholderGenre.slice(0, 5).map((_, __) => (
                        <span className={'mt-1 badge text-bg-dark'}>
                        horror
                    </span>
                    ))}
                </div>
                <div id={'providers'} className={'d-flex mt-3 justify-evenly flex-wrap'}>
                    {placeholderGenre.slice(0, 2).map((_, __) => (
                        <span className={'mt-1 badge text-bg-light border border-black'}>
                        <img
                            src={'https://png.pngtree.com/png-vector/20230420/ourmid/pngtree-movie-vector-design-illustration-background-sign-equipment-vector-png-image_51588342.jpg'}
                            alt="provider logo"
                            style={{height: '1.5em', marginRight: '5px'}}/>
                    </span>
                    ))}
                </div>
            </div>
            <div className={'interaction-img-overlay'}>
                {pageName === 'createList'
                    ? isSelected
                        ?
                        <div className={'mt-4 d-flex justify-center'}>
                            <Card.Title>
                                <i className={'bi bi-check-circle-fill'} style={{color: "green"}}/>
                            </Card.Title>
                        </div>
                        : <div></div>
                    : <div></div>
                }
            </div>
        </div>
    </div>
}

export default MediaCard