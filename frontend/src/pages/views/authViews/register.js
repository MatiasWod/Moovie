import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../components/mainStyle.css'
import userApi from "../../../api/UserApi";
import {useTranslation} from "react-i18next";


const RegisterForm = () => {
    const [form, setForm] = useState({
        username: '',
        email: '',
        password: '',
        repeatPassword: '',
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const {t} = useTranslation();

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
            localStorage.setItem('username', form.username);
            sessionStorage.setItem('username', form.username);
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
                <h1>{t('register.signUp')}</h1>
                {error && <div className="alert alert-danger">{error}</div>}
                {success && <div className="alert alert-success">{success}</div>}

                <form onSubmit={handleSubmit} className="">
                    <div className="me-5 d-flex flex-column">
                        <label htmlFor="username">{t('register.username')}</label>
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

                        <label htmlFor="email">{t('register.email')}</label>
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

                        <label htmlFor="password">{t('register.password')}</label>
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

                        <label htmlFor="repeatPassword">{t('register.repeatPassword')}</label>
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
                            {t('register.register')}
                        </button>

                        <div className="mt-3">
                            {t('register.alreadyHaveAnAccount')} <a href="/login">{t('register.login')}</a>
                        </div>
                        <div className="mt-1">
                            {t('register.continueWithoutRegistering')}{' '}
                            <a href="#" onClick={() => window.history.back()}>
                                {t('register.goBack')}
                            </a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RegisterForm;
