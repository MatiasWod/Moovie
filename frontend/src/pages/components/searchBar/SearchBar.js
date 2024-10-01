import React, { useState } from "react";
import { Form, Button, InputGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const SearchBar = () => {
    const [query, setQuery] = useState("");
    const navigate = useNavigate();

    const handleSearchSubmit = (e) => {
        e.preventDefault();
        if (query.trim()) {

            navigate(`/search/${query}`);
        }
    };

    return (
        <Form inline onSubmit={handleSearchSubmit}> {}
            <InputGroup className={"InputGroup"}>
                <Form.Control
                    type="search"
                    placeholder="Search"
                    className="me-2"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                />
                <Button variant="outline-success" type="submit">
                    <i className={"bi bi-search"} /> Search
                </Button>
            </InputGroup>
        </Form>
    );
};

export default SearchBar;
