import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPurchaseOrderLineItem } from 'app/shared/model/purchase-order-line-item.model';
import { getEntities } from './purchase-order-line-item.reducer';

export const PurchaseOrderLineItem = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const purchaseOrderLineItemList = useAppSelector(state => state.purchaseOrderLineItem.entities);
  const loading = useAppSelector(state => state.purchaseOrderLineItem.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="purchase-order-line-item-heading" data-cy="PurchaseOrderLineItemHeading">
        Purchase Order Line Items
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link
            to="/purchase-order-line-item/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Purchase Order Line Item
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {purchaseOrderLineItemList && purchaseOrderLineItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Note</th>
                <th>Estimated Ship Date</th>
                <th>Product</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {purchaseOrderLineItemList.map((purchaseOrderLineItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/purchase-order-line-item/${purchaseOrderLineItem.id}`} color="link" size="sm">
                      {purchaseOrderLineItem.id}
                    </Button>
                  </td>
                  <td>{purchaseOrderLineItem.note}</td>
                  <td>
                    {purchaseOrderLineItem.estimatedShipDate ? (
                      <TextFormat type="date" value={purchaseOrderLineItem.estimatedShipDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {purchaseOrderLineItem.product ? (
                      <Link to={`/product/${purchaseOrderLineItem.product.id}`}>{purchaseOrderLineItem.product.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/purchase-order-line-item/${purchaseOrderLineItem.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/purchase-order-line-item/${purchaseOrderLineItem.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/purchase-order-line-item/${purchaseOrderLineItem.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Purchase Order Line Items found</div>
        )}
      </div>
    </div>
  );
};

export default PurchaseOrderLineItem;
