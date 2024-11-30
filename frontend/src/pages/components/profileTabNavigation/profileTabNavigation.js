import React, {useState} from "react";
import Nav from "react-bootstrap/Nav";
import "./profileTabNavigation.css"

function ProfileTabNavigation({ selectedTab, onTabSelect }) {
    return (
        <Nav
            variant="tabs"
            activeKey={selectedTab}
            onSelect={(selectedKey) => onTabSelect(selectedKey)}
            className="custom-nav"
        >
            <Nav.Item>
                <Nav.Link eventKey="watched">Watched</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="public-lists">Public</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="private-lists">Private</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="liked-lists">Liked</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="followed-lists">Followed</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="reviews">Reviews</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="watchlist">Watchlist</Nav.Link>
            </Nav.Item>
        </Nav>
    );
}

export default ProfileTabNavigation;

