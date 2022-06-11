import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './received-item.reducer';

export const ReceivedItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const receivedItemEntity = useAppSelector(state => state.receivedItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="receivedItemDetailsHeading">
          <Translate contentKey="myApp.receivedItem.detail.title">ReceivedItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{receivedItemEntity.id}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="myApp.receivedItem.note">Note</Translate>
            </span>
          </dt>
          <dd>{receivedItemEntity.note}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="myApp.receivedItem.location">Location</Translate>
            </span>
          </dt>
          <dd>{receivedItemEntity.location}</dd>
          <dt>
            <span id="assetIdSerial">
              <Translate contentKey="myApp.receivedItem.assetIdSerial">Asset Id Serial</Translate>
            </span>
          </dt>
          <dd>{receivedItemEntity.assetIdSerial}</dd>
          <dt>
            <span id="assetIdMAC">
              <Translate contentKey="myApp.receivedItem.assetIdMAC">Asset Id MAC</Translate>
            </span>
          </dt>
          <dd>{receivedItemEntity.assetIdMAC}</dd>
          <dt>
            <span id="receivedDate">
              <Translate contentKey="myApp.receivedItem.receivedDate">Received Date</Translate>
            </span>
          </dt>
          <dd>
            {receivedItemEntity.receivedDate ? (
              <TextFormat value={receivedItemEntity.receivedDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="tracked">
              <Translate contentKey="myApp.receivedItem.tracked">Tracked</Translate>
            </span>
          </dt>
          <dd>{receivedItemEntity.tracked ? 'true' : 'false'}</dd>
          <dt>
            <span id="forInventory">
              <Translate contentKey="myApp.receivedItem.forInventory">For Inventory</Translate>
            </span>
          </dt>
          <dd>{receivedItemEntity.forInventory ? 'true' : 'false'}</dd>
          <dt>
            <span id="bundleQuantity">
              <Translate contentKey="myApp.receivedItem.bundleQuantity">Bundle Quantity</Translate>
            </span>
          </dt>
          <dd>{receivedItemEntity.bundleQuantity}</dd>
          <dt>
            <Translate contentKey="myApp.receivedItem.fufill">Fufill</Translate>
          </dt>
          <dd>{receivedItemEntity.fufill ? receivedItemEntity.fufill.id : ''}</dd>
          <dt>
            <Translate contentKey="myApp.receivedItem.poLineItem">Po Line Item</Translate>
          </dt>
          <dd>{receivedItemEntity.poLineItem ? receivedItemEntity.poLineItem.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/received-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/received-item/${receivedItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReceivedItemDetail;
