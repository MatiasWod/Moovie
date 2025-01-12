import {HelmetProvider} from "react-helmet-async";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import React, {lazy, Suspense, useEffect, useState} from "react";
import Loader from "./pages/Loader";
import Nav from "./pages/components/navBar/navbar";
import {useDispatch} from "react-redux";
import {attemptReconnect} from "./features/authSlice";
import 'bootstrap/dist/css/bootstrap.min.css';

const views = './pages/views';


const Home = lazy(() => import(views + '/home'));
const Login = lazy(() => import(views + '/authViews/login'));
const Register = lazy(() => import(views + '/authViews/register'));
const Details = lazy(() => import(views + '/details'));
const List = lazy(() => import(views + '/list'));
const CreateList = lazy(() => import(views + '/CreateListView/CreateListView'));
const BrowseLists = lazy(() => import(views + '/browseLists'));
const Discover = lazy(() => import(views + '/discover'));
const FeaturedLists = lazy(() => import(views + '/featuredLists'));
const MilkyLeaderboard = lazy(() => import(views + '/milkyLeaderboard'));
const Search = lazy(() => import(views + '/search'));
const Profile = lazy(() => import(views + '/profile'));
const Cast = lazy(() => import(views + '/cast'));
const Healthcheck = lazy(() => import(views + '/healthcheck'));
const Error404 = lazy(() => import(views + '/errorViews/error404'));
const AuthTest = lazy(() => import(views + '/AuthTest')); // Import AuthTest
const ReportsDashboard = lazy(() => import(views + '/reportsDashboard/ReportsDashboard'));
const ConfirmToken = lazy(() => import(views + '/authViews/confirmToken'));
const AwaitEmailValidation = lazy(() => import(views + '/authViews/awaitEmailValidation'));

export default function App() {
    const helmetContext = {};
    const dispatch = useDispatch();
    const [isInitialized, setIsInitialized] = useState(false);

    useEffect(() => {
        dispatch(attemptReconnect())
            .finally(() => {
                setIsInitialized(true);
            });
    }, [dispatch]);

    if (!isInitialized) {
        return <Loader />;
    }

    return (
        <HelmetProvider context={helmetContext}>
            <BrowserRouter>
                <Suspense fallback={<Loader/>}>
                    <Nav/>
                    <Routes>
                        <Route path='/' element={<Home/>}/>
                        <Route path='/login' element={<Login/>}/>
                        <Route path='/register' element={<Register/>}/>
                        <Route path='/register/confirm' element={<ConfirmToken/>}/>
                        <Route path='/details/:id' element={<Details/>}/>
                        <Route path='/list/:id' element={<List/>}/>
                        <Route path='/discover' element={<Discover/>}/>
                        <Route path='/browseLists' element={<BrowseLists/>}/>
                        <Route path='/createList' element={<CreateList/>}/>
                        <Route path='/featuredLists/:type' element={<FeaturedLists/>}/>
                        <Route path='/leaderboard' element={<MilkyLeaderboard/>}/>
                        <Route path='/profile/:username' element={<Profile/>}/>
                        <Route path='/search/:search' element={<Search/>}/>
                        <Route path='/cast/actor/:id' element={<Cast/>}/>
                        <Route path='/cast/director/:id' element={<Cast/>}/>
                        <Route path='/tvcreators/:id' element={<Cast/>}/>
                        <Route path='/healthcheck' element={<Healthcheck/>}/>
                        <Route path='/authtest' element={<AuthTest/>}/> {/* Add AuthTest route */}
                        <Route path='/reports' element={<ReportsDashboard/>}/>
                        <Route path='*' element={<Error404/>}/>
                    </Routes>
                </Suspense>
            </BrowserRouter>
        </HelmetProvider>
    );
}
