import React from 'react';
import Navbar from "../components/navbar";

function Healthcheck() {
    return (
        <div className={"moovie-default"}>
            <Navbar/>
            <h1>Moovie is up and running!</h1>
        </div>
    );
}

export default Healthcheck;