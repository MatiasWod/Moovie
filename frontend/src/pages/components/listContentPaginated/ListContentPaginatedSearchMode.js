import "../forms/formsStyle.css";
import React, { useState } from "react";
import mediaService from "../../../services/MediaService";
import MediaService from "../../../services/MediaService";
import pagingSizes from "../../../api/values/PagingSizes";
import "../../../api/values/MediaTypes";
import MediaTypes from "../../../api/values/MediaTypes";
import SortOrder from "../../../api/values/SortOrder";
import MediaOrderBy from "../../../api/values/MediaOrderBy";
import MediaCard from "../mediaCard/MediaCard";
import moovieListReviewService from "../../../services/MoovieListReviewService";
import listService from "../../../services/ListService";

const ListContentPaginatedSearchMode = ({ moovieListId, handleCloseSearchMode }) => {
    const [searchQuery, setSearchQuery] = useState('');
    const [mediaList, setMediaList] = useState(null);

    const handleInputChange = (event) => {
        const query = event.target.value;
        setSearchQuery(query);

        if (query.trim() === "") {
            setMediaList(null);
            return;
        }
        fetchMedia(query);
    };

    const fetchMedia = async (query) => {
        try {
            const mediasResponse = await MediaService.getMedia({
                type: MediaTypes.TYPE_MOVIE,
                page: 1,
                pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                sortOrder: SortOrder.DESC,
                orderBy: MediaOrderBy.VOTE_COUNT,
                search: query
            });
            setMediaList(mediasResponse);
        } catch (e) {
            setMediaList(null);
        }
    };

    const handleMediaClick = async (media) => {
        try{
            const response = await listService.insertMediaIntoMoovieList({
                id: moovieListId,
                mediaIds: [media.id]
            });

        } catch(e){
            console.log("error while adding the media")
        }
    };

    return (
        <div className="overlay">
            <button onClick={handleCloseSearchMode} className="btn btn-danger btn-sm">X</button>
            <a>Click on media to add to list</a>
            <input
                type="text"
                value={searchQuery}
                onChange={handleInputChange}
                placeholder="Type something..."
            />
            <div>
                {mediaList?.data?.map((media) => (
                    <div                         onClick={() => handleMediaClick(media)}>
                        <MediaCard
                        key={media.id}
                        media={media}
                        size="small"
                        showWWButtons={false}
                        disableOnClick={true}/>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ListContentPaginatedSearchMode;
