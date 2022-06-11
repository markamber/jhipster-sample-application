import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrderLineItem from './order-line-item';
import OrderLineItemDetail from './order-line-item-detail';
import OrderLineItemUpdate from './order-line-item-update';
import OrderLineItemDeleteDialog from './order-line-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrderLineItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrderLineItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrderLineItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrderLineItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OrderLineItemDeleteDialog} />
  </>
);

export default Routes;
