import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-line-item.reducer';

export const OrderLineItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const orderLineItemEntity = useAppSelector(state => state.orderLineItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderLineItemDetailsHeading">
          <Translate contentKey="myApp.orderLineItem.detail.title">OrderLineItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderLineItemEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="myApp.orderLineItem.description">Description</Translate>
            </span>
          </dt>
          <dd>{orderLineItemEntity.description}</dd>
          <dt>
            <span id="expectedCostUnit">
              <Translate contentKey="myApp.orderLineItem.expectedCostUnit">Expected Cost Unit</Translate>
            </span>
          </dt>
          <dd>{orderLineItemEntity.expectedCostUnit}</dd>
          <dt>
            <span id="sellPriceUnit">
              <Translate contentKey="myApp.orderLineItem.sellPriceUnit">Sell Price Unit</Translate>
            </span>
          </dt>
          <dd>{orderLineItemEntity.sellPriceUnit}</dd>
          <dt>
            <span id="numberUnits">
              <Translate contentKey="myApp.orderLineItem.numberUnits">Number Units</Translate>
            </span>
          </dt>
          <dd>{orderLineItemEntity.numberUnits}</dd>
          <dt>
            <span id="room">
              <Translate contentKey="myApp.orderLineItem.room">Room</Translate>
            </span>
          </dt>
          <dd>{orderLineItemEntity.room}</dd>
          <dt>
            <span id="system">
              <Translate contentKey="myApp.orderLineItem.system">System</Translate>
            </span>
          </dt>
          <dd>{orderLineItemEntity.system}</dd>
          <dt>
            <Translate contentKey="myApp.orderLineItem.product">Product</Translate>
          </dt>
          <dd>{orderLineItemEntity.product ? orderLineItemEntity.product.id : ''}</dd>
          <dt>
            <Translate contentKey="myApp.orderLineItem.project">Project</Translate>
          </dt>
          <dd>{orderLineItemEntity.project ? orderLineItemEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-line-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-line-item/${orderLineItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderLineItemDetail;
