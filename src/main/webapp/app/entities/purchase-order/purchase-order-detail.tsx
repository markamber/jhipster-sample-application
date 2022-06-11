import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './purchase-order.reducer';

export const PurchaseOrderDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const purchaseOrderEntity = useAppSelector(state => state.purchaseOrder.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="purchaseOrderDetailsHeading">
          <Translate contentKey="myApp.purchaseOrder.detail.title">PurchaseOrder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{purchaseOrderEntity.id}</dd>
          <dt>
            <span id="shipTo">
              <Translate contentKey="myApp.purchaseOrder.shipTo">Ship To</Translate>
            </span>
          </dt>
          <dd>{purchaseOrderEntity.shipTo}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="myApp.purchaseOrder.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{purchaseOrderEntity.notes}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="myApp.purchaseOrder.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {purchaseOrderEntity.date ? <TextFormat value={purchaseOrderEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="myApp.purchaseOrder.poLineItems">Po Line Items</Translate>
          </dt>
          <dd>{purchaseOrderEntity.poLineItems ? purchaseOrderEntity.poLineItems.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/purchase-order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/purchase-order/${purchaseOrderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PurchaseOrderDetail;
