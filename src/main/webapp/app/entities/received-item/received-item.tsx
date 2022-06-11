import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReceivedItem } from 'app/shared/model/received-item.model';
import { getEntities } from './received-item.reducer';

export const ReceivedItem = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const receivedItemList = useAppSelector(state => state.receivedItem.entities);
  const loading = useAppSelector(state => state.receivedItem.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="received-item-heading" data-cy="ReceivedItemHeading">
        <Translate contentKey="myApp.receivedItem.home.title">Received Items</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.receivedItem.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/received-item/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.receivedItem.home.createLabel">Create new Received Item</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {receivedItemList && receivedItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.receivedItem.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.note">Note</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.assetIdSerial">Asset Id Serial</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.assetIdMAC">Asset Id MAC</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.receivedDate">Received Date</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.tracked">Tracked</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.forInventory">For Inventory</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.bundleQuantity">Bundle Quantity</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.fufill">Fufill</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.receivedItem.poLineItem">Po Line Item</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {receivedItemList.map((receivedItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/received-item/${receivedItem.id}`} color="link" size="sm">
                      {receivedItem.id}
                    </Button>
                  </td>
                  <td>{receivedItem.note}</td>
                  <td>{receivedItem.location}</td>
                  <td>{receivedItem.assetIdSerial}</td>
                  <td>{receivedItem.assetIdMAC}</td>
                  <td>
                    {receivedItem.receivedDate ? (
                      <TextFormat type="date" value={receivedItem.receivedDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{receivedItem.tracked ? 'true' : 'false'}</td>
                  <td>{receivedItem.forInventory ? 'true' : 'false'}</td>
                  <td>{receivedItem.bundleQuantity}</td>
                  <td>
                    {receivedItem.fufill ? <Link to={`/order-line-item/${receivedItem.fufill.id}`}>{receivedItem.fufill.id}</Link> : ''}
                  </td>
                  <td>
                    {receivedItem.poLineItem ? (
                      <Link to={`/purchase-order-line-item/${receivedItem.poLineItem.id}`}>{receivedItem.poLineItem.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/received-item/${receivedItem.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/received-item/${receivedItem.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/received-item/${receivedItem.id}/delete`}
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
              <Translate contentKey="myApp.receivedItem.home.notFound">No Received Items found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ReceivedItem;
