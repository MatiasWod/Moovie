import App from './../App';
import Login from './../pages/login'


import {
    createBrowserRouter,
} from "react-router-dom";


function Router(){
    return(
    createBrowserRouter([
        {
            path: "/",
            element: App(),
        },
        {
            path: "/login",
            element: Login(),
        },
    ])
    )
}

export default Router;