import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import userApi from '../../api/UserApi';
import { useNavigate, useLocation } from 'react-router-dom';

function Login(){
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();

    const from = location.state?.from?.pathname || '/';

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await userApi.login({ username, password });
            console.log('Login successful:', response);
            // Redirect to the previous location or default to '/'
            navigate(from, { replace: true });
        } catch (error) {
            setError('Login failed. Please check your username and password.');
        }
    };

    return(
        <main className="m-1 moovie-default">
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
                    <Form.Check type="checkbox" label="Keep me logged in" />
                </Form.Group>
                {error && <div className="text-red-500">{error}</div>}
                <Button variant="primary" type="submit">
                    Submit
                </Button>
            </Form>
        </main>
    )
}

export default Login;
