import React, { useState } from 'react';
import {Form, Button, Card, Container} from 'react-bootstrap';
import {truncateText} from "../../../../utils/FormatUtils";

const CreateListForm = ({ selectedMedia, onDeleteCallback, onSubmitCallback }) => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("List Created:", { name, description });
    };

    return (
        <Card className="shadow p-3 border border-dark" style={{ height: "83vh" }}>
            <Card.Body>
                <Card.Title className="text-center">Create a List</Card.Title>
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
                            style={{maxHeight: "max-content"}}
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

                    <div style={{maxHeight: "35vh", overflowY: "auto"}}>
                        {selectedMedia.map((media)=>(
                            <button type={"button"} onClick={()=>onDeleteCallback(media)}  className={'btn btn-light w-100 mb-2 d-flex flex-row align-items-center justify-content-between'}>
                                {truncateText(media.name, 10)}
                                <i className={'bi bi-trash'}/>
                            </button>
                        ))}
                    </div>
                </Form>
            </Card.Body>
        </Card>
    );
};

export default CreateListForm;
