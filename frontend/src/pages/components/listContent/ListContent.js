import React from "react";

const ListContent = ({listContent}) => {
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

export default ListContent;