import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Form from "react-bootstrap/Form";
import {Image, InputGroup} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import Logo from '../../../images/logo.png';
import NavbarStyle from './navbarStyle.css';
import {NavLink} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {logout} from "../../../features/authSlice";
import ProfileImage from "../profileImage/ProfileImage";
import React from "react";

function NavbarComponent() {

    const dispatch = useDispatch();
    const {isLoggedIn, user} = useSelector(state => state.auth);

    const handleLogout = () => {
        dispatch(logout());
    };

    return (
        <Navbar expand="lg" className="sticky-top navbar navbar-expand-lg navbar-light container-nav mb-4">
            <Container fluid>
                <Navbar.Brand href="/" className="d-flex align-items-center">
                    <Image src={Logo} width={50} className="me-2"/> Moovie
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={NavLink} to="/discover" activeClassName="active">Discover</Nav.Link>
                        <Nav.Link as={NavLink} to="/moovieLists" activeClassName="active">Browse Lists</Nav.Link>
                        <NavDropdown title="Top Rated" id="basic-nav-dropdown">
                            <NavDropdown.Item to="#action/3.1" activeClassName="active">Media</NavDropdown.Item>
                            <NavDropdown.Item to="#action/3.2" activeClassName="active">Movies</NavDropdown.Item>
                            <NavDropdown.Item to="#action/3.3" activeClassName="active">Series</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title="Most Popular" id="basic-nav-dropdown">
                            <NavDropdown.Item to="#action/3.1" activeClassName="active">Media</NavDropdown.Item>
                            <NavDropdown.Item to="#action/3.2" activeClassName="active">Movies</NavDropdown.Item>
                            <NavDropdown.Item to="#action/3.3" activeClassName="active">Series</NavDropdown.Item>
                        </NavDropdown>
                        <Nav.Link as={NavLink} to="/leaderboard" activeClassName="active">Leaderboard</Nav.Link>
                    </Nav>
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
                    <Nav className="d-flex nav-item justify-content-center userPic-login">
                        <ProfileImage image={user ? "http://localhost:8080/users/" + user.username + "/image" : null}/>

                        {isLoggedIn ? (
                            <NavDropdown title={`${user.username} (${user.role})`} id="basic-nav-dropdown">
                                <NavDropdown.Item as={NavLink} to="/profile">Profile</NavDropdown.Item>
                                <NavDropdown.Item onClick={handleLogout}>Logout</NavDropdown.Item>
                            </NavDropdown>
                        ) : (
                            <Nav.Link as={NavLink} to="/login" activeClassName="active"
                                      className={'link-primary'}>Login</Nav.Link>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavbarComponent;