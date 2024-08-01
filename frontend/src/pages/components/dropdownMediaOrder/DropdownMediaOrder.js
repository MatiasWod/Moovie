import React, {useState} from "react";
import NavDropdown from "react-bootstrap/NavDropdown";
import OrderBy from "../../../api/values/OrderBy";
import SortOrder from "../../../api/values/SortOrder";
import Button from "react-bootstrap/Button";


const DropdownMediaOrder = ({setOrderBy, setSortOrder, currentSortOrder}) => {

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
                {Object.values(OrderBy).map((value) => (
                    <NavDropdown.Item onClick={ ()=>handleSelect(value)}>{value}</NavDropdown.Item>
                ))}
            </NavDropdown>
            <Button onClick={handleClick}>{(btnState===SortOrder.DESC) ? '↑' : '↓' }</Button>
        </div>
    );
}

export default DropdownMediaOrder;