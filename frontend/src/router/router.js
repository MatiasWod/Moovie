import App from './../App';
import Login from './../pages/login'


import {
    createBrowserRouter,
} from "react-router-dom";

import ErrorPage from "../pages/404";

function Router(){
    return(
    createBrowserRouter([
        {
            path: "/",
            element: App(),
            errorElement: <ErrorPage />,
        },
        {
            path: "/login",
            element: Login(),
            errorElement: <ErrorPage />,
        },
    ])
    )
}

export default Router;