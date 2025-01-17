import React,{useState} from "react";
import "./listContent.css";
import SortOrder from "../../../api/values/SortOrder";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";

const ListContent = ({ listContent }) => {

    const navigate = useNavigate();
    const { t } = useTranslation();

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

    if(listContent === null){
        return <div></div>
    }

    return (
        <div className="list-content">
            <table className="media-table">
                <thead>
                <tr>
                    <th>{t('listContent.title')}</th>
                    <th>{t('listContent.type')}</th>
                    <th>{t('listContent.score')}</th>
                    <th>{t('listContent.usersScore')}</th>
                    <th>{t('listContent.releaseDate')}</th>
                </tr>
                </thead>
                <tbody>
                {listContent.map((media, index) => (
                    <tr key={index}>
                        <td className="media-title">
                            <div
                                className="image-container"
                                onMouseEnter={() => handleMouseEnter(media.id)}
                                onMouseLeave={handleMouseLeave}
                            >
                                <img
                                    className="list-card-images"
                                    src={media.posterPath}
                                    alt={media.name}
                                    onClick={() => handleClick(media.id)}
                                />
                            </div>
                            <span
                                className="media-name"
                                onClick={() => handleClick(media.id)}>
                                {media.name}
                            </span>
                        </td>
                        <td>{media.type}</td>
                        <td>
                            {media.tmdbRating} <span className="star">★</span>
                        </td>
                        <td>
                            {media.totalRating} <span className="star">☆</span>
                        </td>
                        <td>{new Date(media.releaseDate).getFullYear()}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ListContent;
