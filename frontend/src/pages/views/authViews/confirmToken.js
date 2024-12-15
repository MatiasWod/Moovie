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
        if (token) {
            const confirmToken = async () => {
                try {
                    const username = sessionStorage.getItem('username');
                    if (!username) {
                        throw new Error('No username found');
                    }

                    // Confirm token
                    const response = await userApi.confirmToken(token);
                    const jwtToken = response.headers['authorization'];
                    
                    if (jwtToken) {
                        sessionStorage.setItem('jwtToken', jwtToken);
                        sessionStorage.setItem('username', username);

                        await dispatch(attemptReconnect()).unwrap();
                        navigate('/');
                    } else {
                        throw new Error('No token received');
                    }
                } catch (error) {
                    console.error('Error confirming token:', error);
                    navigate('/login');
                }
            }
            confirmToken();
        } else {
            navigate('/login');
        }
    }, [dispatch, navigate]);

    return <Loader/>
}