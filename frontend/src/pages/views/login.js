// src/views/login.js
import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import {useNavigate, useLocation} from 'react-router-dom';
import {loginUser} from '../../features/authSlice';
import "../components/mainStyle.css"



function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const location = useLocation();
    const {status, error} = useSelector((state) => state.auth);

    const from = location.state?.from?.pathname || '/';

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

    return (
        <main className="m-1 moovie-default default-container">
            <div className="text-3xl font-bold underline">This is the login page. Powered by Tailwind CSS</div>
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formBasicUsername">
                    <Form.Label>Username</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Enter username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </Form.Group>

                <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="Keep me logged in"/>
                </Form.Group>
                {status === 'failed' && <div className="text-red-500">{error}</div>}
                <Button variant="primary" type="submit" disabled={status === 'loading'}>
                    {status === 'loading' ? 'Submitting...' : 'Submit'}
                </Button>
            </Form>
        </main>
    );
}

export default Login;
