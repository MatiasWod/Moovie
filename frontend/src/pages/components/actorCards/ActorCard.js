import React from 'react';

const ActorCard = ({ name, image }) => (
    <div style={{
        border: '1px solid #ccc',
        borderRadius: '8px',
        padding: '16px',
        textAlign: 'center',
        width: '200px'
    }}>
        <img
            src={image}
            alt={name}
            style={{ borderRadius: '8px', width: '100%' }}
        />
        <h3 style={{ margin: '8px 0' }}>{name}</h3>
    </div>
);

export default ActorCard;