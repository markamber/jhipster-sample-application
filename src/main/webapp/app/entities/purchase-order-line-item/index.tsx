import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PurchaseOrderLineItem from './purchase-order-line-item';
import PurchaseOrderLineItemDetail from './purchase-order-line-item-detail';
import PurchaseOrderLineItemUpdate from './purchase-order-line-item-update';
import PurchaseOrderLineItemDeleteDialog from './purchase-order-line-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PurchaseOrderLineItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PurchaseOrderLineItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PurchaseOrderLineItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={PurchaseOrderLineItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PurchaseOrderLineItemDeleteDialog} />
  </>
);

export default Routes;
