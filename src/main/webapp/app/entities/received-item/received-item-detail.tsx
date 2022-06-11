import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
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
        <h2 data-cy="receivedItemDetailsHeading">ReceivedItem</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{receivedItemEntity.id}</dd>
          <dt>
            <span id="note">Note</span>
          </dt>
          <dd>{receivedItemEntity.note}</dd>
          <dt>
            <span id="location">Location</span>
          </dt>
          <dd>{receivedItemEntity.location}</dd>
          <dt>
            <span id="assetIdSerial">Asset Id Serial</span>
          </dt>
          <dd>{receivedItemEntity.assetIdSerial}</dd>
          <dt>
            <span id="assetIdMAC">Asset Id MAC</span>
          </dt>
          <dd>{receivedItemEntity.assetIdMAC}</dd>
          <dt>
            <span id="receivedDate">Received Date</span>
          </dt>
          <dd>
            {receivedItemEntity.receivedDate ? (
              <TextFormat value={receivedItemEntity.receivedDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Fufill</dt>
          <dd>{receivedItemEntity.fufill ? receivedItemEntity.fufill.id : ''}</dd>
          <dt>Po Line Item</dt>
          <dd>{receivedItemEntity.poLineItem ? receivedItemEntity.poLineItem.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/received-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/received-item/${receivedItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReceivedItemDetail;
