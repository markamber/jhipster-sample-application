import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Project from './project';
import OrderLineItem from './order-line-item';
import Manufacture from './manufacture';
import Product from './product';
import Vendor from './vendor';
import PurchaseOrder from './purchase-order';
import PurchaseOrderLineItem from './purchase-order-line-item';
import ReceivedItem from './received-item';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}project`} component={Project} />
        <ErrorBoundaryRoute path={`${match.url}order-line-item`} component={OrderLineItem} />
        <ErrorBoundaryRoute path={`${match.url}manufacture`} component={Manufacture} />
        <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
        <ErrorBoundaryRoute path={`${match.url}vendor`} component={Vendor} />
        <ErrorBoundaryRoute path={`${match.url}purchase-order`} component={PurchaseOrder} />
        <ErrorBoundaryRoute path={`${match.url}purchase-order-line-item`} component={PurchaseOrderLineItem} />
        <ErrorBoundaryRoute path={`${match.url}received-item`} component={ReceivedItem} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
