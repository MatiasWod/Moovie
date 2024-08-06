import {HelmetProvider} from "react-helmet-async";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import React, {lazy, Suspense, useEffect} from "react";
import Loader from "./pages/Loader";
import Nav from "./pages/components/navBar/navbar";
import {useDispatch} from "react-redux";
import {attemptReconnect} from "./features/authSlice";

const views = './pages/views';


const Home = lazy(() => import(views + '/home'));
const Login = lazy(() => import(views + '/login'));
const Details = lazy(() => import(views + '/details'));
const List = lazy(() => import(views + '/list'));
const BrowseLists = lazy(() => import(views + '/browseLists'));
const MilkyLeaderboard = lazy(() => import(views + '/milkyLeaderboard'));
const Healthcheck = lazy(() => import(views + '/healthcheck'));
const Error404 = lazy(() => import(views + '/errorViews/error404'));
const AuthTest = lazy(() => import(views + '/AuthTest')); // Import AuthTest


export default function App() {
    const helmetContext = {};

    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(attemptReconnect());
    }, [dispatch]);

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
                        <Route path='/browseLists' element={<BrowseLists/>}/>
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
