import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/project">
        <Translate contentKey="global.menu.entities.project" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-line-item">
        <Translate contentKey="global.menu.entities.orderLineItem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/manufacture">
        <Translate contentKey="global.menu.entities.manufacture" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vendor">
        <Translate contentKey="global.menu.entities.vendor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/purchase-order">
        <Translate contentKey="global.menu.entities.purchaseOrder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/purchase-order-line-item">
        <Translate contentKey="global.menu.entities.purchaseOrderLineItem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/received-item">
        <Translate contentKey="global.menu.entities.receivedItem" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
