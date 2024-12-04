import React, { useEffect, useState } from "react";
import "../buttonStyles.css";
import listService from "../../../../services/ListService";
import MoovieListTypes from "../../../../api/values/MoovieListTypes";
import CardsListOrderBy from "../../../../api/values/CardsListOrderBy";
import SortOrder from "../../../../api/values/SortOrder";
import ResponsePopup from "../reponsePopup/ReponsePopup";

const AddMediaToListButton = ({ currentId }) => {
    const [lists, setLists] = useState([]);
    const [listsLoading, setListsLoading] = useState(true);
    const [listsError, setListsError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [popupMessage, setPopupMessage] = useState("");
    const [popupType, setPopupType] = useState("");
    const [popupVisible, setPopupVisible] = useState(false);
    const [options, setOptions] = useState([]);

    const fetchCurrentUserLists = async () => {
        try {
            const response0 = await listService.getLists({
                search: null,
                ownerUsername: "Wancho",
                type: MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type,
                orderBy: CardsListOrderBy.MOOVIE_LIST_ID,
                order: SortOrder.DESC,
                pageNumber: 1,
                pageSize: 3,
            });
            const response1 = await listService.getLists({
                search: null,
                ownerUsername: "Wancho",
                type: MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type,
                orderBy: CardsListOrderBy.MOOVIE_LIST_ID,
                order: SortOrder.DESC,
                pageNumber: 1,
                pageSize: 10,
            });
            const response2 = await listService.getLists({
                search: null,
                ownerUsername: "Wancho",
                type: MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.type,
                orderBy: CardsListOrderBy.MOOVIE_LIST_ID,
                order: SortOrder.DESC,
                pageNumber: 1,
                pageSize: 10,
            });

            const combinedLists = [...response0.data, ...response1.data, ...response2.data];
            setLists(combinedLists);
            const listOptions = combinedLists.map((list) => ({
                name: list.name,
                id: list.id,
            }));
            setOptions(listOptions);
        } catch (err) {
            setListsError(err);
        } finally {
            setListsLoading(false);
        }
    };

    useEffect(() => {
        fetchCurrentUserLists();
    }, []);

    const [isOpen, setIsOpen] = useState(false);
    const handleToggle = () => {
        setIsOpen(!isOpen);
    };

    const handleOptionClick = async (option) => {
        setPopupVisible(true);
        setIsOpen(false);
        setLoading(true);
        setPopupMessage("");
        setPopupType("loading");

        try {
            const response = await listService.insertMediaIntoMoovieList({
                id: option.id,
                mediaIds: [Number(currentId)],
            });

            if (response.status === 200) {
                setPopupType("success");
                setPopupMessage("Media successfully added to the list.");
            } else {
                setPopupType("error");
                setPopupMessage(response.data.message);
            }
        } catch (error) {

            setPopupType("error");
            setPopupMessage("Error making request.");
        } finally {
            setLoading(false);
        }
    };

    const handleClosePopup = () => {
        setPopupVisible(false);
    };

    return (
        <div className="dropdown">
            <button className="dropdown-button" onClick={handleToggle}>
                + Añadir a la lista
            </button>
            {isOpen && (
                <div className="dropdown-content scrollable-options">
                    {options.map((option, index) => (
                        <div
                            key={index}
                            className="dropdown-item"
                            onClick={() => handleOptionClick(option)}
                        >
                            {option.name}
                        </div>
                    ))}
                </div>
            )}
            {popupVisible && (
                <ResponsePopup
                    message={popupMessage}
                    type={popupType}
                    isLoading={loading}
                    onClose={handleClosePopup}
                />
            )}
        </div>
    );
};

export default AddMediaToListButton;
