import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import userApi from '../../../api/UserApi';
import Loader from '../../components/loader/Loader';
import { useDispatch } from 'react-redux';
import { attemptReconnect } from '../../../features/authSlice';

export default function ConfirmToken() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const token = searchParams.get('token');
    const username = searchParams.get('user');

    if (!token || !username) {
      navigate('/login');
      return;
    }

    const confirmToken = async () => {
      try {
        const response = await userApi.confirmToken(username, token);
        console.log(response)
        if (response.status === 500) {
          navigate(`/register/verify?error=token_expired&token=${token}`);
          return;
        }

        if (response.status >= 400) {
          throw new Error(`Unexpected response status: ${response.status}`);
        }

        const authToken = response.headers['moovie-authtoken'];
        const refreshToken = response.headers['moovie-refreshtoken'];

        if (!authToken || !refreshToken) {
          throw new Error('No tokens received');
        }

        sessionStorage.setItem('jwt', authToken);
        sessionStorage.setItem('refreshToken', refreshToken);
        sessionStorage.setItem('username', username);

        await new Promise((resolve) => setTimeout(resolve, 10));

        await dispatch(attemptReconnect());
        navigate('/');
      } catch (error) {
        console.error('Error confirming token:', error);
        const to = '/login?error=user_verified';
        navigate(to);
      }
    };

    confirmToken();
  }, [dispatch, navigate, searchParams]);

  return <Loader />;
}
