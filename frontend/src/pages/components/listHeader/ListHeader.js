import React from "react";
import './listHeader.css';

const ListHeader = ({ list }) => {
    return (
        <div className="list-header">
            {list.images && list.images.length > 0 ? (
                <div
                    className="list-header-image"
                    style={{ backgroundImage: `url(${list.images[0]})` }}
                ></div>
            ) : null}
            <div className="list-header-content">
                <h1 className="list-header-title">{list.name}</h1>
                <p className="list-header-description">{list.description}</p>
                <span className="list-header-username">por {list.username}</span>
            </div>
        </div>
    );
};

export default ListHeader;
