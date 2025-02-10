import React from 'react';

const ChipsDisplay = ({ title, items, onRemove }) => (
    <div className="container d-flex justify-content-start p-0 flex-wrap">
        {items.length > 0 && (
            <div className={'d-flex flex-wrap align-items-center'}>
                <h4>{title}:</h4>
                {items.map((item, index) => (
                    <div key={index} className="m-1 badge text-bg-dark d-flex justify-content-between align-items-center">
                        <span className="text-bg-dark">{item.name}</span>
                        <i className="ms-1 p-1 btn btn-outline-light bi bi-trash-fill" onClick={() => onRemove(item)} />
                    </div>
                ))}
            </div>
        )}
    </div>
);

export default ChipsDisplay;
