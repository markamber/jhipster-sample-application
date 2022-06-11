import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVendor } from 'app/shared/model/vendor.model';
import { getEntities } from './vendor.reducer';

export const Vendor = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const vendorList = useAppSelector(state => state.vendor.entities);
  const loading = useAppSelector(state => state.vendor.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="vendor-heading" data-cy="VendorHeading">
        <Translate contentKey="myApp.vendor.home.title">Vendors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.vendor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/vendor/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.vendor.home.createLabel">Create new Vendor</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {vendorList && vendorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.vendor.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.vendor.legalEntity">Legal Entity</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.vendor.nickname">Nickname</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.vendor.billingAddress">Billing Address</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.vendor.manufactures">Manufactures</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {vendorList.map((vendor, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/vendor/${vendor.id}`} color="link" size="sm">
                      {vendor.id}
                    </Button>
                  </td>
                  <td>{vendor.legalEntity}</td>
                  <td>{vendor.nickname}</td>
                  <td>{vendor.billingAddress}</td>
                  <td>
                    {vendor.manufactures
                      ? vendor.manufactures.map((val, j) => (
                          <span key={j}>
                            <Link to={`/manufacture/${val.id}`}>{val.id}</Link>
                            {j === vendor.manufactures.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/vendor/${vendor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/vendor/${vendor.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/vendor/${vendor.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="myApp.vendor.home.notFound">No Vendors found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Vendor;
