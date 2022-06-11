import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PurchaseOrder from './purchase-order';
import PurchaseOrderDetail from './purchase-order-detail';
import PurchaseOrderUpdate from './purchase-order-update';
import PurchaseOrderDeleteDialog from './purchase-order-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PurchaseOrderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PurchaseOrderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PurchaseOrderDetail} />
      <ErrorBoundaryRoute path={match.url} component={PurchaseOrder} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PurchaseOrderDeleteDialog} />
  </>
);

export default Routes;
