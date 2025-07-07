import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { useTranslation } from 'react-i18next';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import SortOrder from '../../../api/values/SortOrder';

const DropdownMenu = ({ setOrderBy, setSortOrder, currentSortOrder, values, labels }) => {
  const [btnState, setBtnState] = useState(currentSortOrder);
  const { t } = useTranslation();

  useEffect(() => {
    setBtnState(currentSortOrder);
  }, [currentSortOrder]);

  const handleSelect = (selectedValue) => {
    setOrderBy(selectedValue);
  };

  const handleClick = () => {
    const newSortOrder = btnState === SortOrder.DESC ? SortOrder.ASC : SortOrder.DESC;
    setBtnState(newSortOrder);
    setSortOrder(newSortOrder);
  };

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
      <NavDropdown
        title={t('dropdownMenu.orderBy')}
        style={{ minWidth: 120 }}
        menuVariant="light"
      >
        {values.map((value) => (
          <NavDropdown.Item key={value} onClick={() => handleSelect(value)}>
            {labels[value] ? t(labels[value]) : value}
          </NavDropdown.Item>
        ))}
      </NavDropdown>
      <ReactTooltip id="tooltip-id" place="bottom" type="dark" effect="solid" />
      <Button
        className="z-0"
        onClick={handleClick}
        data-tooltip-id={'tooltip-id'}
        data-tooltip-content={t('dropdownMenu.invertOrder')}
        aria-label={t('dropdownMenu.invertOrder')}
      >
        {btnState === SortOrder.DESC ? '↑' : '↓'}
      </Button>
    </div>
  );
};

export default DropdownMenu;
