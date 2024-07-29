// src/routes/Router.js
import React from 'react';
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import App from './../App';
import Login from './../pages/login';
import ErrorPage from "../pages/404";
import Healthcheck from "../pages/healthcheck";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        errorElement: <ErrorPage />,
    },
    {
        path: "/login",
        element: <Login />,
        errorElement: <ErrorPage />,
    },
    {
        path: "/healthcheck",
        element: <Healthcheck />,
        errorElement: <ErrorPage />,
    }
]);

function Router() {
    return (
        <RouterProvider router={router} />
    );
}

export default Router;
