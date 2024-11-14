import {HelmetProvider} from "react-helmet-async";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import React, {lazy, Suspense, useEffect} from "react";
import Loader from "./pages/Loader";
import Nav from "./pages/components/navBar/navbar";
import {useDispatch} from "react-redux";
import {attemptReconnect} from "./features/authSlice";
import 'bootstrap/dist/css/bootstrap.min.css';

const views = './pages/views';


const Home = lazy(() => import(views + '/home'));
const Login = lazy(() => import(views + '/login'));
const Details = lazy(() => import(views + '/details'));
const List = lazy(() => import(views + '/list'));
const BrowseLists = lazy(() => import(views + '/browseLists'));
const Discover = lazy(() => import(views + '/discover'));
const FeaturedLists = lazy(() => import(views + '/featuredLists'));
const MilkyLeaderboard = lazy(() => import(views + '/milkyLeaderboard'));
const Search = lazy(() => import(views + '/search'));
const Profile = lazy(() => import(views + '/profile'));
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
                        <Route path='/discover' element={<Discover/>}/>
                        <Route path='/browseLists' element={<BrowseLists/>}/>
                        <Route path='/featuredLists/:type' element={<FeaturedLists/>}/>
                        <Route path='/leaderboard' element={<MilkyLeaderboard/>}/>
                        <Route path='/profile/:username' element={<Profile/>}/>
                        <Route path='/search/:search' element={<Search/>}/>
                        <Route path='/healthcheck' element={<Healthcheck/>}/>
                        <Route path='/authtest' element={<AuthTest/>}/> {/* Add AuthTest route */}
                        <Route path='*' element={<Error404/>}/>
                    </Routes>
                </Suspense>
            </BrowserRouter>
        </HelmetProvider>
    );
}
