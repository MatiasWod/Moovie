import {HelmetProvider} from "react-helmet-async";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import React, {lazy, Suspense} from "react";
import Loader from "./pages/Loader";
import Nav from "./pages/components/navBar/navbar";

const views = './pages/views';


const Home = lazy(() => import(views + '/home'));
const Login = lazy(() => import(views + '/login'));
const Details = lazy(() => import(views + '/details'));
const List = lazy(() => import(views + '/list'));
const MilkyLeaderboard = lazy(() => import(views + '/milkyLeaderboard'));
const Healthcheck = lazy(() => import(views + '/healthcheck'));
const Error404 = lazy(() => import(views + '/errorViews/error404'));
const AuthTest = lazy(() => import(views + '/AuthTest')); // Import AuthTest


export default function App() {
    const helmetContext = {};

    return (
        <HelmetProvider context={helmetContext}>
            <BrowserRouter>
                <Suspense fallback={<Loader/>}>
                    <Nav/>
                    <Routes>
                        <Route path='/' element={<Home/>}/>
                        <Route path='/login' element={<Login/>}/>
                        <Route path='/details/:id' element={<Details/>}/>
                        <Route path='/list/:id' element={<List/>}/>
                        <Route path='/milkyLeaderboard' element={<MilkyLeaderboard/>}/>
                        <Route path='/healthcheck' element={<Healthcheck/>}/>
                        <Route path='/authtest' element={<AuthTest/>}/> {/* Add AuthTest route */}
                        <Route path='*' element={<Error404/>}/>
                    </Routes>
                </Suspense>
            </BrowserRouter>
        </HelmetProvider>
    );
}
