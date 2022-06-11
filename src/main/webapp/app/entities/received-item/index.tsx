import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ReceivedItem from './received-item';
import ReceivedItemDetail from './received-item-detail';
import ReceivedItemUpdate from './received-item-update';
import ReceivedItemDeleteDialog from './received-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReceivedItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReceivedItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReceivedItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={ReceivedItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ReceivedItemDeleteDialog} />
  </>
);

export default Routes;
