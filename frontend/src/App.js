import {HelmetProvider} from "react-helmet-async";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import React, {Suspense,lazy} from "react";
import Loader from "./pages/Loader";

const views = './pages/views'


const Home = lazy(() => import(views + '/home'));
const Login = lazy(() => import(views + '/login'));
const Healthcheck = lazy(() => import(views + '/healthcheck'));
const Error404 = lazy(() => import(views + '/errorViews/error404'));

export default function App() {
    const helmetContext = {};

      return (
          <HelmetProvider context={helmetContext}>
              <BrowserRouter>
                  <Suspense fallback={<Loader/>}>
                      <Routes>
                        <Route path='/' element={<Home/>}/>
                        <Route path='/login' element={<Login/>}/>
                        <Route path='/healthcheck' element={<Healthcheck/>}/>
                        <Route path='*' element={<Error404/>}/>
                      </Routes>
                  </Suspense>
              </BrowserRouter>
          </HelmetProvider>
      );
}
