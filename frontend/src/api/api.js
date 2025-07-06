import axios from 'axios';
import Qs from 'qs';

const getTokenFromStorage = (key) => localStorage.getItem(key) || sessionStorage.getItem(key);

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  timeout: 50000,
  paramsSerializer: (params) => Qs.stringify(params, { arrayFormat: 'repeat' }),
});

api.interceptors.request.use(
  (config) => {
    if (getTokenFromStorage('jwt') && !config.retried) {
      const jwt = getTokenFromStorage('jwt');
      config.headers['Authorization'] = `Bearer ${jwt}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const previousRequest = error?.config;
    if (
      error?.response?.status === 401 &&
      !previousRequest?.retried &&
      !previousRequest?.skipRetry
    ) {
      previousRequest.retried = true;
      const refreshToken = getTokenFromStorage('refreshToken');
      previousRequest.headers['Authorization'] = `Bearer ${refreshToken}`;
      try {
        const retryResponse = await api.request(previousRequest);
        const newJwt = retryResponse.headers['moovie-authtoken'];
        const newRefresh = retryResponse.headers['moovie-refreshtoken'];

        if (newJwt && newRefresh) {
          if (localStorage.getItem('jwt') && localStorage.getItem('refreshToken')) {
            localStorage.setItem('jwt', newJwt);
            localStorage.setItem('refreshToken', newRefresh);
          } else {
            sessionStorage.setItem('jwt', newJwt);
            sessionStorage.setItem('refreshToken', newRefresh);
          }
        }

        return retryResponse;
      } catch (retryError) {
        if (retryError?.response?.status === 401) {
          console.log('clearing storage');
          localStorage.clear();
          sessionStorage.clear();
        }
        throw retryError;
      }
    }

    return Promise.reject(error);
  }
);

export default api;
