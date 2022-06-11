import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Vendor from './vendor';
import VendorDetail from './vendor-detail';
import VendorUpdate from './vendor-update';
import VendorDeleteDialog from './vendor-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VendorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VendorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VendorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Vendor} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VendorDeleteDialog} />
  </>
);

export default Routes;
