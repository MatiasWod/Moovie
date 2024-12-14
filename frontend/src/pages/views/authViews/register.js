import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../components/mainStyle.css'
import userApi from "../../../api/UserApi";


const RegisterForm = () => {
    const [form, setForm] = useState({
        username: '',
        email: '',
        password: '',
        repeatPassword: '',
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (form.password !== form.repeatPassword) {
            setError('Passwords do not match');
            return;
        }

        try {
            await userApi.register({
                username: form.username,
                email: form.email,
                password: form.password,
            });
            setSuccess('Registration successful! You can now log in.');
            setError('');
            setForm({
                username: '',
                email: '',
                password: '',
                repeatPassword: '',
            });
        } catch (err) {
            setError(err.message || 'An error occurred during registration.');
            setSuccess('');
        }
    };

    return (
        <div
            style={{ background: 'whitesmoke', overflow: 'hidden', minHeight: '100vh' }}
            className="d-flex flex-column align-items-center"
        >
            <div
                style={{ border: 'solid black', width: 'fit-content' }}
                className="container-gray container d-flex flex-column p-3 mt-5"
            >
                <h1>Sign Up</h1>
                {error && <div className="alert alert-danger">{error}</div>}
                {success && <div className="alert alert-success">{success}</div>}

                <form onSubmit={handleSubmit} className="">
                    <div className="me-5 d-flex flex-column">
                        <label htmlFor="username">Username</label>
                        <div>
                            <input
                                type="text"
                                id="username"
                                name="username"
                                value={form.username}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>

                        <label htmlFor="email">Email</label>
                        <div>
                            <input
                                type="email"
                                id="email"
                                name="email"
                                value={form.email}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>

                        <label htmlFor="password">Password</label>
                        <div>
                            <input
                                type="password"
                                id="password"
                                name="password"
                                value={form.password}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>

                        <label htmlFor="repeatPassword">Repeat Password</label>
                        <div>
                            <input
                                type="password"
                                id="repeatPassword"
                                name="repeatPassword"
                                value={form.repeatPassword}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>

                        <button type="submit" className="mt-2 btn btn-outline-success">
                            Register
                        </button>

                        <div className="mt-3">
                            Already have an account? <a href="/login">Login</a>
                        </div>
                        <div className="mt-1">
                            Continue without registering?{' '}
                            <a href="#" onClick={() => window.history.back()}>
                                Go Back
                            </a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RegisterForm;
