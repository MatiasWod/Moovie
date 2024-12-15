import React, { useState, useEffect } from 'react'
import {
    Container,
    Form,
    Button,
    Alert,
    Row,
    Col
} from 'react-bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import { useTranslation } from 'react-i18next'
import { useDispatch, useSelector } from 'react-redux'
import { useLocation, useNavigate } from 'react-router-dom'
import BackgroundPosters from '../../components/backgroundPosters/backgroundPosters'
import MediaService from "../../../services/MediaService"
import pagingSizes from "../../../api/values/PagingSizes"
import { loginUser } from "../../../features/authSlice"

const Login = () => {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [rememberMe, setRememberMe] = useState(false)
    const [mediaList, setMediaList] = useState(undefined)
    const { t } = useTranslation()

    const dispatch = useDispatch()
    const navigate = useNavigate()
    const location = useLocation()
    const { status, error } = useSelector((state) => state.auth)

    const from = location.state?.from?.pathname || '/'

    useEffect(() => {
        fetchMediaList()
    }, [])

    const fetchMediaList = async () => {
        try {
            const data = await MediaService.getMedia({
                pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
            })
            setMediaList(data)
        } catch (error) {
            console.error('Failed to fetch media list', error)
        }
    }

    const handleSubmit = async (event) => {
        event.preventDefault()
        dispatch(loginUser({username, password}))
            .unwrap()
            .then(() => {
                navigate(from, {replace: true})
            })
            .catch(() => {
                // Error handling is already done in the slice
            })
    }

    if (!mediaList) return null

    return (
        <div
            style={{
                background: 'whitesmoke',
                height: '100vh',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                position: 'relative',
                overflow: 'hidden'
            }}
        >
            {/*<BackgroundPosters mediaList={mediaList.data} />*/}

            <Container
                className="container-gray"
                style={{
                    border: 'solid black',
                    width: 'fit-content',
                    minHeight: '60%',
                    padding: '5%',
                    position: 'relative',
                    zIndex: 1,
                    backgroundColor: 'rgba(255, 255, 255, 0.9)'
                }}
            >
                <Form onSubmit={handleSubmit}>
                    <h1 className="text-center mb-4">{t('login.title')}</h1>

                    {status === 'failed' && (
                        <Alert variant="danger">
                            {error}
                        </Alert>
                    )}

                    <Form.Group className="mb-3">
                        <Form.Label>{t('login.username')}</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder={t('login.username')}
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>{t('login.password')}</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder={t('login.password')}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Check
                            type="checkbox"
                            label={t('login.rememberMe')}
                            checked={rememberMe}
                            onChange={(e) => setRememberMe(e.target.checked)}
                        />
                    </Form.Group>

                    <Button
                        variant="outline-success"
                        type="submit"
                        className="w-100 mb-3"
                        disabled={status === 'loading'}
                    >
                        {status === 'loading' ? t('login.submitting') : t('login.login')}
                    </Button>

                    <div className="mt-3 text-center">
                        <p>
                            {t('login.noAccount')}
                            <a href="/register" className="ms-2">{t('login.signUp')}</a>
                        </p>
                        <p>
                            {t('login.continue')}
                            <a href="#" onClick={() => window.history.back()} className="ms-2">
                                {t('login.without')}
                            </a>
                        </p>
                    </div>
                </Form>
            </Container>
        </div>
    )
}

export default Login