import React from 'react';

const FormButtons = ({ onApply, onReset }) => (
    <div className="d-flex flex-column m-1">
        <button type="submit" className="btn btn-outline-success m-1" onClick={onApply}>
            Apply
        </button>
        <button type="button" className="btn btn-outline-secondary m-1" onClick={onReset}>
            Reset
        </button>
    </div>
);

export default FormButtons;
