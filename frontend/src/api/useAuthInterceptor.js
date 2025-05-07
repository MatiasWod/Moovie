import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import api from "./api";

const getTokenFromStorage = (key) => localStorage.getItem(key) || sessionStorage.getItem(key);

export const useAuthInterceptor = () => {
    const [jwt, setJwt] = useState(getTokenFromStorage("jwt"));
    const [refreshToken, setRefreshToken] = useState(getTokenFromStorage("refreshToken"));
    const [isAuthenticated, setIsAuthenticated] = useState(jwt !== null);


    useEffect(() => {
        const requestInterceptor = api.interceptors.request.use(

            (config) => {
                if (isAuthenticated && !config.retried) {
                    config.headers["Authorization"] = `Bearer ${jwt}`;
                }
                return config;
            },
            (error) => Promise.reject(error)
        );

        const responseInterceptor = api.interceptors.response.use(
            (response) => response,
            async (error) => {
                const previousRequest = error?.config;
                if (error?.response?.status === 401 && !previousRequest?.retried) {
                    previousRequest.retried = true;
                    previousRequest.headers["Authorization"] = `Bearer ${refreshToken}`;
                    try {
                        const retryResponse = await api.request(previousRequest);
                        const newJwt = retryResponse.headers["moovie-authtoken"];
                        const newRefresh = retryResponse.headers["moovie-refreshtoken"];

                        if (newJwt && newRefresh) {
                            setJwt(newJwt);
                            setRefreshToken(newRefresh);
                            setIsAuthenticated(true);

                            if (localStorage.getItem("jwt") && localStorage.getItem("refreshToken")) {
                                localStorage.setItem("jwt", newJwt);
                                localStorage.setItem("refreshToken", newRefresh);
                            } else {
                                sessionStorage.setItem("jwt", newJwt);
                                sessionStorage.setItem("refreshToken", newRefresh);
                            }
                        }

                        return retryResponse;
                    } catch (retryError) {
                        if (retryError?.response?.status === 401) {
                            setIsAuthenticated(false);
                            setJwt(null);
                            setRefreshToken(null);
                            localStorage.clear();
                            sessionStorage.clear();
                        }
                        throw retryError;
                    }
                }

                return Promise.reject(error);
            }
        );

        return () => {
            api.interceptors.request.eject(requestInterceptor);
            api.interceptors.response.eject(responseInterceptor);
        };
    }, [isAuthenticated, jwt, refreshToken]);

    return { jwt, refreshToken, isAuthenticated };
};
