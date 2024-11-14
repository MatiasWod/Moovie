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
                <Nav.Link eventKey="visto">Visto</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="listasPublicas">Listas públicas</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="listasPrivadas">Listas privadas</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="listasQueTeGustaron">Listas que te gustaron</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="listasSeguidas">Listas seguidas</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="resenas">Reseñas</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="porVer">Por ver</Nav.Link>
            </Nav.Item>
        </Nav>
    );
}

export default ProfileTabNavigation;

