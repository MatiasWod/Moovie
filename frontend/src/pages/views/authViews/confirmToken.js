import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import userApi from '../../../api/UserApi';
import Loader from '../../components/loader/Loader';
import { useDispatch } from 'react-redux';
import { attemptReconnect } from '../../../features/authSlice';

export default function ConfirmToken() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        const token = new URLSearchParams(window.location.search).get('token');
        if (!token) {
            navigate('/login');
            return;
        }

        const confirmToken = async () => {
            try {

                const response = await userApi.confirmToken(token);

                if (response.status === 500) {
                    navigate('/register/verify?error=token_expired');
                    return;
                }

                if (response.status >= 400) {
                    throw new Error(`Unexpected response status: ${response.status}`);
                }

                const jwtToken = response.headers?.authorization;

                if (!jwtToken) {
                    throw new Error('No token received');
                }

                const username = localStorage.getItem('username');
                if (!username) {
                    throw new Error('No username found');
                }

                sessionStorage.setItem('jwtToken', jwtToken);
                sessionStorage.setItem('username', username);



                await dispatch(attemptReconnect());
                navigate('/');
            } catch (error) {
                console.error('Error confirming token:', error);
                navigate('/login');
            }
        };

        confirmToken();
    }, [dispatch, navigate]);

    return <Loader />;
}
