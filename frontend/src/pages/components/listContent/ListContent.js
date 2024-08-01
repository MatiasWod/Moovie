import React from "react";

const ListHeader = ({listContent}) => {
    return(
        <div>
            <div>
                {listContent.map(media => (
                    <div>{media.name}</div>
                ))}
            </div>
        </div>
    )
}

export default ListHeader;