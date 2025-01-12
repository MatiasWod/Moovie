import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import {Image} from "react-bootstrap";
import Logo from '../../../images/logo.png';
import {NavLink} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {logout} from "../../../features/authSlice";
import ProfileImage from "../profileImage/ProfileImage";
import React from "react";
import SearchBar from "../searchBar/SearchBar";
import RoleBadge from "../RoleBadge/RoleBadge";

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
                        <Nav.Link as={NavLink} to="/browseLists" activeClassName="active">Browse Lists</Nav.Link>
                        <Nav.Link as={NavLink} to="/createList" activeClassName="active">Create List</Nav.Link>
                        <NavDropdown title="Top Rated" id="basic-nav-dropdown">
                            <NavDropdown.Item as={NavLink} to="featuredLists/topRatedMedia" activeClassName="active">Media</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="featuredLists/topRatedMovies" activeClassName="active">Movies</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="featuredLists/topRatedSeries" activeClassName="active">Series</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title="Most Popular" id="basic-nav-dropdown">
                            <NavDropdown.Item as={NavLink} to="featuredLists/mostPopularMedia" activeClassName="active">Media</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="featuredLists/mostPopularMovies" activeClassName="active">Movies</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="featuredLists/mostPopularSeries" activeClassName="active">Series</NavDropdown.Item>
                        </NavDropdown>
                        <Nav.Link as={NavLink} to="/leaderboard" activeClassName="active">Leaderboard</Nav.Link>
                    </Nav>
                    <SearchBar/>
                    <Nav className="d-flex nav-item justify-content-center userPic-login">
                        {user && (
                            <div style={{display: 'flex', alignItems: 'center'}}>
                            <RoleBadge role={user.role} size={"50px"}></RoleBadge>
                            <ProfileImage
                                image={`http://localhost:8080/users/${user.username}/image`}
                                size="60px" // Adjust size as needed
                                defaultProfilePicture="https://example.com/default-profile.jpg" // Your default image URL
                            />
                            </div>
                        )}
                        {/*(${user.role})*/}
                        {isLoggedIn ? (
                            <div style={{display: 'flex', alignItems: 'center'}}>
                                <NavDropdown
                                    title={`${user.username} `}
                                    id="basic-nav-dropdown"
                                    drop="down"
                                    className="custom-dropdown"
                                >
                                    <NavDropdown.Item as={NavLink}
                                                      to={`/profile/${user.username}`}>Profile</NavDropdown.Item>
                                    <NavDropdown.Item as={NavLink} to={`/reports`}>Reports</NavDropdown.Item>
                                    <NavDropdown.Item onClick={handleLogout}>Logout</NavDropdown.Item>
                                </NavDropdown>
                            </div>
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