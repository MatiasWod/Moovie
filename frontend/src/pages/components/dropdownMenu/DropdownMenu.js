import React, {useState} from "react";
import NavDropdown from "react-bootstrap/NavDropdown";
import OrderBy from "../../../api/values/MediaOrderBy";
import SortOrder from "../../../api/values/SortOrder";
import Button from "react-bootstrap/Button";


const DropdownMenu = ({setOrderBy, setSortOrder, currentSortOrder, values}) => {

    const handleSelect = (selectedValue) =>{
        setOrderBy(selectedValue);
    };

    const [btnState, setBtnState] = useState(currentSortOrder);
    const handleClick = () =>{
        setBtnState( (btnState===SortOrder.DESC) ? SortOrder.ASC : SortOrder.DESC );
        setSortOrder( btnState );

    }

    return (
        <div style={{display: "flex"}}>
            <NavDropdown title="Order By">
                {values.map((value) => (
                    <NavDropdown.Item onClick={ ()=>handleSelect(value)}>{value}</NavDropdown.Item>
                ))}
            </NavDropdown>
            <Button onClick={handleClick}>{(btnState===SortOrder.DESC) ? '↑' : '↓' }</Button>
        </div>
    );
}

export default DropdownMenu;