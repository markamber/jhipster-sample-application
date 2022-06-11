import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Manufacture from './manufacture';
import ManufactureDetail from './manufacture-detail';
import ManufactureUpdate from './manufacture-update';
import ManufactureDeleteDialog from './manufacture-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ManufactureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ManufactureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ManufactureDetail} />
      <ErrorBoundaryRoute path={match.url} component={Manufacture} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ManufactureDeleteDialog} />
  </>
);

export default Routes;
