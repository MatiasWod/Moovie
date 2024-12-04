import React, { useState, useEffect } from 'react'
import styled from 'styled-components'
import { useTranslation } from 'react-i18next'
import { useDispatch, useSelector } from 'react-redux'
import BackgroundPosters from '../../components/backgroundPosters'
import MediaService from "../../../services/MediaService";
import pagingSizes from "../../../api/values/PagingSizes";
import {loginUser} from "../../../features/authSlice";
import {useLocation, useNavigate} from "react-router-dom";

const LoginPage = styled.div`
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    overflow: hidden;
`

const LoginContainer = styled.div`
    position: relative;
    z-index: 1;
    width: 100%;
    max-width: 500px;
    margin: 0 auto;
    padding: 2rem;
`

const LoginFormWrapper = styled.div`
    background-color: rgba(255, 255, 255, 0.9);
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    padding: 2rem;
`

const Login = () => {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [rememberMe, setRememberMe] = useState(false)
    const [mediaList, setMediaList] = useState(undefined)
    const { t } = useTranslation()

    const dispatch = useDispatch()
    const navigate = useNavigate();
    const location = useLocation();
    const { status, error } = useSelector((state) => state.auth);

    const from = location.state?.from?.pathname || '/';

    useEffect(() => {
        fetchMediaList()
    }, [])

    const fetchMediaList = async () => {
        try {
            const data = await MediaService.getMedia({
                pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
            });
            setMediaList(data);
        } catch (error) {
            console.error('Failed to fetch media list', error);
        }
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        dispatch(loginUser({username, password}))
            .unwrap()
            .then(() => {
                navigate(from, {replace: true});
            })
            .catch(() => {
                // Error handling is already done in the slice
            });
    };

    if (!mediaList) return null

    return (
        <LoginPage>
            <BackgroundPosters mediaList={mediaList} />
            <LoginContainer>
                <LoginFormWrapper>
                    <h1 className="text-center mb-4">{t('login.title')}</h1>
                    {status === 'failed' && (
                        <div className="alert alert-danger mb-4" role="alert">
                            {error}
                        </div>
                    )}
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="username" className="form-label">{t('login.username')}</label>
                            <input
                                id="username"
                                type="text"
                                className="form-control"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="password" className="form-label">{t('login.password')}</label>
                            <input
                                id="password"
                                type="password"
                                className="form-control"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <div className="mb-3 form-check">
                            <input
                                type="checkbox"
                                className="form-check-input"
                                id="rememberMe"
                                checked={rememberMe}
                                onChange={(e) => setRememberMe(e.target.checked)}
                            />
                            <label className="form-check-label" htmlFor="rememberMe">
                                {t('login.rememberMe')}
                            </label>
                        </div>
                        <button
                            type="submit"
                            className="btn btn-primary w-100"
                            disabled={status === 'loading'}
                        >
                            {status === 'loading' ? t('login.submitting') : t('login.login')}
                        </button>
                    </form>
                    <div className="mt-4 text-center">
                        <p>{t('login.noAccount')} <a href="/register" className="text-primary">{t('login.signUp')}</a></p>
                        <p>{t('login.continue')} <a href="#" onClick={() => window.history.back()} className="text-primary">{t('login.without')}</a></p>
                    </div>
                </LoginFormWrapper>
            </LoginContainer>
        </LoginPage>
    )
}

export default Login