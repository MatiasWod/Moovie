import React from 'react';

const FilterList = ({ searchValue, onSearchChange, items, selectedItems, onToggleItem }) => (
    <>
        <input
            type="text"
            value={searchValue}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder="Search..."
            className="form-control mb-2"
        />
        {items
            .filter((item) => item.toLowerCase().includes(searchValue.toLowerCase()))
            .map((item, index) => (
                <div key={index} className="form-check">
                    <input
                        type="checkbox"
                        className="form-check-input"
                        id={`item-${index}`}
                        checked={selectedItems.includes(item)}
                        onChange={() => onToggleItem(item)}
                    />
                    <label className="form-check-label" htmlFor={`item-${index}`}>
                        {item}
                    </label>
                </div>
            ))}
    </>
);

export default FilterList;
