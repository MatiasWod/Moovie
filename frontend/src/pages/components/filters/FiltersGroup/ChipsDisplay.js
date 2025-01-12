import React from 'react';

const ChipsDisplay = ({ title, items, onRemove }) => (
    <div className="container d-flex justify-content-start p-0 flex-wrap">
        {items.length > 0 && (
            <>
                <h4>{title}:</h4>
                {items.map((item, index) => (
                    <div key={index} className="m-1 badge text-bg-dark">
                        <span className="text-bg-dark">{item}</span>
                        <i className="btn bi bi-trash-fill" onClick={() => onRemove(item)} />
                    </div>
                ))}
            </>
        )}
    </div>
);

export default ChipsDisplay;
