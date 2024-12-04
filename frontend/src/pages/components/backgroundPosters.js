import React from 'react'
import styled, { keyframes } from 'styled-components'

const moveRightToLeft = keyframes`
    from { transform: translateX(0); }
    to { transform: translateX(-100%); }
`

const BackgroundPostersWrapper = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    z-index: -1;
    overflow: hidden;
`

const PosterColumn = styled.div`
    display: flex;
    flex-direction: column;
    width: 50%;
`

const PosterRow = styled.div`
    display: flex;
    height: 33.33%;
    margin-bottom: 4px;
    animation: ${moveRightToLeft} ${props => props.animationDuration || '60s'} linear ${props => props.reverse ? 'reverse' : 'normal'} infinite;
    white-space: nowrap;
`

const PosterContainer = styled.div`
    display: inline-flex;
    height: 100%;
`

const Poster = styled.img`
  height: 100%;
  width: auto;
  object-fit: cover;
  object-position: center;
  margin-right: 4px;
`

const BackgroundPosters = ({ mediaList }) => {
    // Duplicate the media list to create a seamless infinite loop
    const extendedMediaList = [
        ...mediaList.data,
        ...mediaList.data,
        ...mediaList.data
    ];

    return (
        <BackgroundPostersWrapper>
            {[0, 1].map((_, columnIndex) => (
                <PosterColumn key={columnIndex}>
                    {[false, true, false].map((isReverse, rowIndex) => (
                        <PosterRow
                            key={rowIndex}
                            reverse={isReverse}
                            animationDuration={isReverse ? '120s' : '60s'}
                        >
                            <PosterContainer>
                                {extendedMediaList
                                    .slice(
                                        columnIndex * 7 + rowIndex * 7,
                                        columnIndex * 7 + rowIndex * 7 + 14
                                    )
                                    .map((media, i) => (
                                        <Poster
                                            key={i}
                                            src={media.posterPath}
                                            alt="poster"
                                        />
                                    ))
                                }
                            </PosterContainer>
                        </PosterRow>
                    ))}
                </PosterColumn>
            ))}
        </BackgroundPostersWrapper>
    )
}

export default BackgroundPosters