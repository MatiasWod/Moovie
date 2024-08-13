import React from "react";
import {InputGroup} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

const SearchBar = () =>{
    return(
        <Form inline>
            <InputGroup className={"InputGroup"}>
                <Form.Control
                    type="search"
                    placeholder="Search"
                    className="me-2"
                />
                <Button variant="outline-success" type="submit">
                    <i className={"bi bi-search"}/> Search
                </Button>
            </InputGroup>
        </Form>
    )
}

export default SearchBar;