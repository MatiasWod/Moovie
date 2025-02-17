import React, { useState } from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import userApi from "../../../api/UserApi";

const AwaitEmailValidation = () => {
    const { user } = useSelector((state) => state.auth);
    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from || '/';

    const searchParams = new URLSearchParams(location.search);
    const error = searchParams.get('error');

    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    if (user) navigate(from);

    const handleResendVerificationEmail = async () => {
        setLoading(true);
        setMessage(""); // Clear previous messages

        try {
            // Assuming the token from the URL or some other method
            const token = searchParams.get('token');
            if (!token) {
                setMessage("No token provided.");
                setLoading(false);
                return;
            }

            // Call the resend email method
            const response = await userApi.resendVerificationEmail(token);

            if (response.status === 200) {
                setMessage("Verification email resent successfully!");
            } else {
                setMessage("Failed to resend verification email.");
            }
        } catch (error) {
            setMessage("Error resending email. Please try again.");
        }

        setLoading(false);
    };

    return (
        <Container fluid className="d-flex justify-content-center align-items-center" style={{ minHeight: '60vh', width: 'max-content' }}>
            <Row className="justify-content-center">
                <Col md={12} lg={10}>
                    <Card className="text-center shadow">
                        <Card.Body>
                            <Card.Title>Account Verification</Card.Title>
                            {error === "token_expired" ? (
                                <Card.Text>
                                    Your verification link has expired. Please request a new one.
                                </Card.Text>
                            ) : (
                                <Card.Text>
                                    Please check your email and verify your account to continue.
                                </Card.Text>
                            )}
                            {message && <Card.Text>{message}</Card.Text>}
                            {error === "token_expired" ?
                                <Button
                                variant="success"
                                onClick={handleResendVerificationEmail}
                                disabled={loading}
                                >
                                    {loading ? 'Sending...' : 'Resend Verification Email'}
                                </Button> :
                                <Button variant={"success"} onClick={navigate('/')}>
                                    Go Home
                                </Button>
                            }
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}

export default AwaitEmailValidation;
