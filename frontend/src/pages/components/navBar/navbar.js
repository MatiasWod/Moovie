import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Form from "react-bootstrap/Form";
import {Col, Image, InputGroup, Row} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import Logo from '../../../images/logo.png';
import NavbarStyle from './navbarStyle.css';

function navbar() {
    return (
        <Navbar expand="lg" className="sticky-top navbar navbar-expand-lg navbar-light container-nav mb-4">
            <Container fluid>
                <Navbar.Brand href="home" className="d-flex align-items-center">
                    <Image src={Logo} width={60} className="me-2"/> Moovie
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href="discover">Discover</Nav.Link>
                        <Nav.Link href="moovieLists">Browse Lists</Nav.Link>
                        <NavDropdown title="Top Rated" id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1">Media</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2">Movies</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.3">Series</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title="Top Rated" id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1">Media</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2">Movies</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.3">Series</NavDropdown.Item>
                        </NavDropdown>
                        <Nav.Link href="leaderboard">Leaderboard</Nav.Link>
                    </Nav>
                    <Form inline>
                        <InputGroup className={"InputGroup"}>
                                <Form.Control
                                    type="search"
                                    placeholder="Search"
                                    className="me-2"
                                />
                                <Button variant="outline-success" type="submit">
                                    <i className={"bi bi-search"}></i>
                                    Search
                                </Button>
                        </InputGroup>
                    </Form>
                    <Nav className="d-flex nav-item justify-content-center userPic-login">
                        <Nav.Link href="login">Login</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default navbar;