import React, { useState, useEffect } from "react";
import NavDropdown from "react-bootstrap/NavDropdown";
import OrderBy from "../../../api/values/MediaOrderBy";
import SortOrder from "../../../api/values/SortOrder";
import Button from "react-bootstrap/Button";

const DropdownMenu = ({ setOrderBy, setSortOrder, currentSortOrder, values }) => {
    const [btnState, setBtnState] = useState(currentSortOrder);

    useEffect(() => {
        setBtnState(currentSortOrder);
    }, [currentSortOrder]);

    const handleSelect = (selectedValue) => {
        setOrderBy(selectedValue);
    };

    const handleClick = () => {
        const newSortOrder = btnState === SortOrder.DESC ? SortOrder.ASC : SortOrder.DESC;
        setBtnState(newSortOrder);
        setSortOrder(newSortOrder);
    };

    return (
        <div style={{ display: "flex" }}>
            <NavDropdown title="Order By">
                {values.map((value) => (
                    <NavDropdown.Item key={value} onClick={() => handleSelect(value)}>
                        {value}
                    </NavDropdown.Item>
                ))}
            </NavDropdown>
            <Button onClick={handleClick}>
                {btnState === SortOrder.DESC ? "↑" : "↓"}
            </Button>
        </div>
    );
};

export default DropdownMenu;
