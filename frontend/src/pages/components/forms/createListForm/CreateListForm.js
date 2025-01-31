import React, { useState } from 'react';
import { truncateText } from "../../../../utils/FormatUtils";
import {Alert, Card, Form, OverlayTrigger, Tooltip} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {Link} from "react-router-dom";


const CreateListForm = ({
                            selectedMedia,
                            name,
                            setName,
                            description,
                            setDescription,
                            onDeleteCallback,
                            onSubmitCallback,
                            onResetCallback
}) => {

    const [showError, setShowError] = useState(false)
    const [showSuccess, setShowSuccess] = useState(false)

    const [listId, setListId] = useState(0)

    const handleSubmit = async  (e) => {
        e.preventDefault()
        const {success, listId} = await onSubmitCallback()
        setShowSuccess(success)
        setShowError(!success)
        setListId(listId)
    };

    const handleReset = (e) => {
        e.preventDefault();
        onResetCallback()
    }

    return (
        <Card className="shadow p-3 border border-dark" style={{ height: "83vh" }}>
            <Card.Body>
                {showError && <div className={'alert alert-danger alert-dismissible'} onClick={() => setShowError(false)}>
                    error <i className={'btn-close disabled'}/>
                </div>}
                {showSuccess && <div className={'alert alert-success alert-dismissible'} onClick={() => setShowError(false)}>
                    You created a list! find it here: <Link to={'/list/' + listId}>link</Link> <i className={'btn-close disabled'}/>
                </div>}
                <div className={'d-flex flex-row justify-content-between align-items-center'}>
                    <Card.Title className="text-center">
                        Create a List
                        <OverlayTrigger placement="top-end" overlay={<Tooltip id="tooltip-info">You can leave this page, we'll save your progress!</Tooltip>}>
                            <span style={{ cursor: 'pointer', marginLeft: '8px' }}>
                                <i className={'bi bi-info-circle-fill'}></i>
                            </span>
                        </OverlayTrigger>
                    </Card.Title>
                    <Button type={"reset"} variant={"outline-dark"} onClick={handleReset}>
                        <i className={'bi bi-arrow-counterclockwise'} />
                    </Button>
                </div>
                <Form onSubmit={handleSubmit}>
                    {/* Name Input */}
                    <Form.Group className="mb-3">
                        <Form.Label column={true}>Name</Form.Label>
                        <Form.Control
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="Enter list name"
                            required
                        />
                    </Form.Group>

                    {/* Description Input */}
                    <Form.Group className="mb-3">
                        <Form.Label column={true}>Description</Form.Label>
                        <Form.Control
                            style={{ maxHeight: "max-content" }}
                            as="textarea"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            placeholder="Enter list description"
                            rows={3}
                            required
                        />
                    </Form.Group>

                    <Button variant="success" type="submit" className="w-100 mb-3">
                        Create List
                    </Button>

                    <div style={{ maxHeight: "35vh", overflowY: "auto" }}>
                        {selectedMedia.map((media) => (
                            <button type={"button"} onClick={() => onDeleteCallback(media)} className={'btn btn-light w-100 mb-2 d-flex flex-row align-items-center justify-content-between'}>
                                {truncateText(media.name, 10)}
                                <i className={'bi bi-trash'} />
                            </button>
                        ))}
                    </div>
                </Form>
            </Card.Body>
        </Card>
    );
};

export default CreateListForm;
