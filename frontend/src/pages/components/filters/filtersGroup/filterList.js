import React from 'react';

const FilterList = ({ searchValue, onSearchChange, items, selectedItems, onToggleItem }) => (

    <>
        <input
            type="text"
            value={searchValue}
            onChange={(e) => onSearchChange(e.target.value)}
            className="form-control mb-2"
        />
        {items
            .filter((item) => item.name.toLowerCase().includes(searchValue.toLowerCase()))
            .map((item, index) => (
                <div key={index} className="form-check">
                    <input
                        type="checkbox"
                        className="form-check-input"
                        id={`item-${index}`}
                        checked={selectedItems.includes(item)}
                        onClick={() => onToggleItem(item)}
                    />
                    <label className="form-check-label" htmlFor={`item-${index}`}>
                        {item.name}
                    </label>
                </div>
            ))}
    </>
);

export default FilterList;
