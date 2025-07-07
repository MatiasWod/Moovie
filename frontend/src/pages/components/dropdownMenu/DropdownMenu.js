import React from 'react';
import Button from 'react-bootstrap/Button';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { useTranslation } from 'react-i18next';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import SortOrder from '../../../api/values/SortOrder';

const DropdownMenu = ({ setOrderBy, setSortOrder, currentSortOrder, currentOrderBy, values, labels }) => {
  const { t } = useTranslation();

  // Add validation for required props
  if (!Array.isArray(values)) {
    console.error('DropdownMenu: values prop must be an array');
    return null;
  }

  if (!labels || typeof labels !== 'object') {
    console.error('DropdownMenu: labels prop must be an object');
    return null;
  }

  if (typeof setOrderBy !== 'function' || typeof setSortOrder !== 'function') {
    console.error('DropdownMenu: setOrderBy and setSortOrder must be functions');
    return null;
  }

  const handleSelect = (selectedValue) => {
    setOrderBy(selectedValue);
  };

  const handleClick = () => {
    const newSortOrder = currentSortOrder === SortOrder.DESC ? SortOrder.ASC : SortOrder.DESC;
    setSortOrder(newSortOrder);
  };

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
      <NavDropdown
        title={currentOrderBy && labels[currentOrderBy] ? t(labels[currentOrderBy]) : t('dropdownMenu.orderBy')}
        style={{ minWidth: 120 }}
        menuVariant="light"
      >
        {values.map((value) => (
          <NavDropdown.Item key={value} onClick={() => handleSelect(value)}>
            {labels[value] ? t(labels[value]) : value}
          </NavDropdown.Item>
        ))}
      </NavDropdown>
      <ReactTooltip 
        id="tooltip-id" 
        place="bottom"
        variant="dark"
        style={{ zIndex: 1000 }}
      />
      <Button
        onClick={handleClick}
        data-tooltip-id="tooltip-id"
        data-tooltip-content={t('dropdownMenu.invertOrder')}
        aria-label={t('dropdownMenu.invertOrder')}
      >
        {currentSortOrder === SortOrder.ASC ? '↑' : '↓'}
      </Button>
    </div>
  );
};

export default DropdownMenu;
