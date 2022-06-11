import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './purchase-order-line-item.reducer';

export const PurchaseOrderLineItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const purchaseOrderLineItemEntity = useAppSelector(state => state.purchaseOrderLineItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="purchaseOrderLineItemDetailsHeading">PurchaseOrderLineItem</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{purchaseOrderLineItemEntity.id}</dd>
          <dt>
            <span id="note">Note</span>
          </dt>
          <dd>{purchaseOrderLineItemEntity.note}</dd>
          <dt>
            <span id="estimatedShipDate">Estimated Ship Date</span>
          </dt>
          <dd>
            {purchaseOrderLineItemEntity.estimatedShipDate ? (
              <TextFormat value={purchaseOrderLineItemEntity.estimatedShipDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Product</dt>
          <dd>{purchaseOrderLineItemEntity.product ? purchaseOrderLineItemEntity.product.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/purchase-order-line-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/purchase-order-line-item/${purchaseOrderLineItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PurchaseOrderLineItemDetail;
