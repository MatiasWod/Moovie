import React from "react";

const ListHeader = ({list}) => {
    return(
        <div>
            <div>{list.name}</div>
            <div>{list.username}</div>
            <div>{list.createdBy}</div>
            <div>{list.description}</div>
        </div>
    )
}

export default ListHeader;