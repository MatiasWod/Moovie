import React from "react";
import "./listContent.css";

const ListContent = ({ listContent }) => {
    return (
        <div className="list-content">
            <table className="media-table">
                <thead>
                <tr>
                    <th>Título</th>
                    <th>Tipo</th>
                    <th>Puntuación</th>
                    <th>Puntuación de usuarios</th>
                    <th>Fecha de Lanzamiento</th>
                </tr>
                </thead>
                <tbody>
                {listContent.map((media, index) => (
                    <tr key={index}>
                        <td className="media-title">
                            <img className="list-card-images" src={media.posterPath} alt={media.name}/>
                            <span>{media.name}</span>
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
