import React, {useState} from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import {useLocation, useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";

const AwaitEmailValidation = () => {

    const { user, isLoggedIn } = useSelector((state) => state.auth);
    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from || '/';

    if (user) navigate(from);

    return (
        <Container fluid className="d-flex justify-content-center align-items-center" style={{ minHeight: '60vh', width: 'max-content' }}>
            <Row className="justify-content-center">
                <Col md={12} lg={10}>
                    <Card className="text-center shadow">
                        <Card.Body>
                            <Card.Title>Account Verification</Card.Title>
                            <Card.Text>
                                Please check your email and verify your account to continue.
                            </Card.Text>
                            <Button variant="success" onClick={() => alert('Verification link sent!')}>
                                Verify Account
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}

export default AwaitEmailValidation;
