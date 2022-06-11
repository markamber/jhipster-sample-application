import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/project">
        Project
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-line-item">
        Order Line Item
      </MenuItem>
      <MenuItem icon="asterisk" to="/manufacture">
        Manufacture
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        Product
      </MenuItem>
      <MenuItem icon="asterisk" to="/vendor">
        Vendor
      </MenuItem>
      <MenuItem icon="asterisk" to="/purchase-order">
        Purchase Order
      </MenuItem>
      <MenuItem icon="asterisk" to="/purchase-order-line-item">
        Purchase Order Line Item
      </MenuItem>
      <MenuItem icon="asterisk" to="/received-item">
        Received Item
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
