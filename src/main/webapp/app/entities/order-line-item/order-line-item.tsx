import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrderLineItem } from 'app/shared/model/order-line-item.model';
import { getEntities } from './order-line-item.reducer';

export const OrderLineItem = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const orderLineItemList = useAppSelector(state => state.orderLineItem.entities);
  const loading = useAppSelector(state => state.orderLineItem.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="order-line-item-heading" data-cy="OrderLineItemHeading">
        <Translate contentKey="myApp.orderLineItem.home.title">Order Line Items</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.orderLineItem.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/order-line-item/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.orderLineItem.home.createLabel">Create new Order Line Item</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {orderLineItemList && orderLineItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.orderLineItem.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.orderLineItem.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.orderLineItem.expectedCostUnit">Expected Cost Unit</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.orderLineItem.sellPriceUnit">Sell Price Unit</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.orderLineItem.numberUnits">Number Units</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.orderLineItem.room">Room</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.orderLineItem.system">System</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.orderLineItem.product">Product</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.orderLineItem.project">Project</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {orderLineItemList.map((orderLineItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/order-line-item/${orderLineItem.id}`} color="link" size="sm">
                      {orderLineItem.id}
                    </Button>
                  </td>
                  <td>{orderLineItem.description}</td>
                  <td>{orderLineItem.expectedCostUnit}</td>
                  <td>{orderLineItem.sellPriceUnit}</td>
                  <td>{orderLineItem.numberUnits}</td>
                  <td>{orderLineItem.room}</td>
                  <td>{orderLineItem.system}</td>
                  <td>
                    {orderLineItem.product ? <Link to={`/product/${orderLineItem.product.id}`}>{orderLineItem.product.id}</Link> : ''}
                  </td>
                  <td>
                    {orderLineItem.project ? <Link to={`/project/${orderLineItem.project.id}`}>{orderLineItem.project.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/order-line-item/${orderLineItem.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/order-line-item/${orderLineItem.id}/edit`}
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
                        to={`/order-line-item/${orderLineItem.id}/delete`}
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
              <Translate contentKey="myApp.orderLineItem.home.notFound">No Order Line Items found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default OrderLineItem;
