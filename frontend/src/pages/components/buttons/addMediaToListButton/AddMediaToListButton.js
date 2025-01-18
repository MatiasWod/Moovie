import React, {useEffect, useState} from "react";
import "../buttonStyles.css";
import listService from "../../../../services/ListService";
import MoovieListTypes from "../../../../api/values/MoovieListTypes";
import CardsListOrderBy from "../../../../api/values/CardsListOrderBy";
import SortOrder from "../../../../api/values/SortOrder";
import ResponsePopup from "../reponsePopup/ReponsePopup";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {Dropdown} from "react-bootstrap";
import {useTranslation} from "react-i18next";

const AddMediaToListButton = ({ currentId }) => {
    const { t } = useTranslation();
    const {isLoggedIn, user} = useSelector(state => state.auth);


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
                ownerUsername: user.username,
                type: MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type,
                orderBy: CardsListOrderBy.MOOVIE_LIST_ID,
                order: SortOrder.DESC,
                pageNumber: 1,
                pageSize: 3,
            });
            const response1 = await listService.getLists({
                search: null,
                ownerUsername: user.username,
                type: MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type,
                orderBy: CardsListOrderBy.MOOVIE_LIST_ID,
                order: SortOrder.DESC,
                pageNumber: 1,
                pageSize: 10,
            });
            const response2 = await listService.getLists({
                search: null,
                ownerUsername: user.username,
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
    const navigate = useNavigate();

    const handleToggle = () => {
        if(!isLoggedIn){
            navigate(`/login`);
        }
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

    const handleOnClick = () => {
        if (!isLoggedIn) {
            navigate('/login');
        }
    };

    return (
        <div className="dropdown">
            <Dropdown onClick={handleOnClick}>
                <Dropdown.Toggle className="btn btn-dark dropdown-toggle" id="dropdown-basic"
                                 style={{marginRight: '10px'}}>
                    <i className="bi bi-plus-circle-fill"></i> {t('addMediaToListButton.addToList')}
                </Dropdown.Toggle>

                { isLoggedIn && (
                    <Dropdown.Menu>
                        {options.map((option, index) => (
                            <Dropdown.Item key={index} onClick={ () => handleOptionClick(option)}>{option.name}</Dropdown.Item>
                        ))}
                        <Dropdown.Item onClick={() => navigate('/createList')}> <i
                            className="bi bi-plus-circle-fill"></i> {t('addMediaToListButton.createNewList')}</Dropdown.Item>
                    </Dropdown.Menu>
                )}
            </Dropdown>

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
