import {BrowserRouter, Route, Routes} from "react-router-dom";
import React from "react";
import Error404 from "./pages/404";
import Login from "./pages/login";
import Healthcheck from "./pages/healthcheck";

export default function App() {
  return (
      <BrowserRouter>
          <Routes>
            <Route path='/login' element={<Login/>}/>
            <Route path='/healthcheck' element={<Healthcheck/>}/>
            <Route element={<Error404/>}/>
          </Routes>
      </BrowserRouter>
  );
}
