import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
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
        <h2 data-cy="orderLineItemDetailsHeading">OrderLineItem</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{orderLineItemEntity.id}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{orderLineItemEntity.description}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{orderLineItemEntity.type}</dd>
          <dt>
            <span id="expectedCostUnit">Expected Cost Unit</span>
          </dt>
          <dd>{orderLineItemEntity.expectedCostUnit}</dd>
          <dt>
            <span id="sellPriceUnit">Sell Price Unit</span>
          </dt>
          <dd>{orderLineItemEntity.sellPriceUnit}</dd>
          <dt>
            <span id="numberUnits">Number Units</span>
          </dt>
          <dd>{orderLineItemEntity.numberUnits}</dd>
          <dt>
            <span id="room">Room</span>
          </dt>
          <dd>{orderLineItemEntity.room}</dd>
          <dt>
            <span id="system">System</span>
          </dt>
          <dd>{orderLineItemEntity.system}</dd>
          <dt>Product</dt>
          <dd>{orderLineItemEntity.product ? orderLineItemEntity.product.id : ''}</dd>
          <dt>Project</dt>
          <dd>{orderLineItemEntity.project ? orderLineItemEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-line-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-line-item/${orderLineItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderLineItemDetail;
