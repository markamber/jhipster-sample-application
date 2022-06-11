import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { getEntities } from './purchase-order.reducer';

export const PurchaseOrder = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const purchaseOrderList = useAppSelector(state => state.purchaseOrder.entities);
  const loading = useAppSelector(state => state.purchaseOrder.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="purchase-order-heading" data-cy="PurchaseOrderHeading">
        <Translate contentKey="myApp.purchaseOrder.home.title">Purchase Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.purchaseOrder.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/purchase-order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.purchaseOrder.home.createLabel">Create new Purchase Order</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {purchaseOrderList && purchaseOrderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.purchaseOrder.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.purchaseOrder.shipTo">Ship To</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.purchaseOrder.notes">Notes</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.purchaseOrder.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.purchaseOrder.poLineItems">Po Line Items</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {purchaseOrderList.map((purchaseOrder, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/purchase-order/${purchaseOrder.id}`} color="link" size="sm">
                      {purchaseOrder.id}
                    </Button>
                  </td>
                  <td>{purchaseOrder.shipTo}</td>
                  <td>{purchaseOrder.notes}</td>
                  <td>
                    {purchaseOrder.date ? <TextFormat type="date" value={purchaseOrder.date} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {purchaseOrder.poLineItems ? (
                      <Link to={`/purchase-order-line-item/${purchaseOrder.poLineItems.id}`}>{purchaseOrder.poLineItems.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/purchase-order/${purchaseOrder.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/purchase-order/${purchaseOrder.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/purchase-order/${purchaseOrder.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="myApp.purchaseOrder.home.notFound">No Purchase Orders found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PurchaseOrder;
