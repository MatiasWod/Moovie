import React, { useState } from 'react';
import { Container, Form, Button, Alert, Col } from 'react-bootstrap';
import {useNavigate, useLocation, useSearchParams} from 'react-router-dom';
import { useTranslation } from "react-i18next";
import userApi from "../../../api/UserApi";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../components/mainStyle.css';

const ResetPassword = () => {
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);

    const { t } = useTranslation();
    const navigate = useNavigate();
    const location = useLocation();
    const [searchParams] = useSearchParams();

    const token = searchParams.get('token');

    if (!token) {
        navigate('/login');
        return;
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (password !== repeatPassword) {
            setError("register.passwordMismatch");
            return;
        }

        try {
            setLoading(true);
            await userApi.resetPassword(token,password)
            setError('');
            setSuccess(true)
        } catch (error) {
            setLoading(false);
            setError(error.message);
        }
    };

    return (
        <div className="p-5 vh-100" style={{ background: "whitesmoke" }}>
            <Container className="d-flex align-items-center justify-content-center">
                <Col xs={12} md={6} className="p-4 bg-light shadow rounded">
                    <h2 className="text-center mb-3">Reset your Password</h2>
                    <p style={{fontWeight: "bold"}} className="text-center mb-3">Enter your new password!</p>

                    {success && <Alert variant={"success"}>Password reset, yo may now login!</Alert>}

                    {error && <Alert variant="danger">{error}</Alert>}

                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>{t("login.password")}</Form.Label>
                            <Form.Control
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>{t("login.password")}</Form.Label>
                            <Form.Control
                                type="password"
                                value={repeatPassword}
                                onChange={(e) => setRepeatPassword(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Button variant="success" type="submit" className="w-100" disabled={loading}>
                            Set new password
                        </Button>
                    </Form>
                </Col>
            </Container>
        </div>
    );
};

export default ResetPassword;
