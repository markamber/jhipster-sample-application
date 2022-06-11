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
        Purchase Orders
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/purchase-order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Purchase Order
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {purchaseOrderList && purchaseOrderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Ship To</th>
                <th>Notes</th>
                <th>Date</th>
                <th>Po Line Items</th>
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
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/purchase-order/${purchaseOrder.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/purchase-order/${purchaseOrder.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Purchase Orders found</div>
        )}
      </div>
    </div>
  );
};

export default PurchaseOrder;
