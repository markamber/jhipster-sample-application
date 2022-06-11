import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vendor.reducer';

export const VendorDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const vendorEntity = useAppSelector(state => state.vendor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vendorDetailsHeading">
          <Translate contentKey="myApp.vendor.detail.title">Vendor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vendorEntity.id}</dd>
          <dt>
            <span id="legalEntity">
              <Translate contentKey="myApp.vendor.legalEntity">Legal Entity</Translate>
            </span>
          </dt>
          <dd>{vendorEntity.legalEntity}</dd>
          <dt>
            <span id="nickname">
              <Translate contentKey="myApp.vendor.nickname">Nickname</Translate>
            </span>
          </dt>
          <dd>{vendorEntity.nickname}</dd>
          <dt>
            <span id="billingAddress">
              <Translate contentKey="myApp.vendor.billingAddress">Billing Address</Translate>
            </span>
          </dt>
          <dd>{vendorEntity.billingAddress}</dd>
          <dt>
            <Translate contentKey="myApp.vendor.manufactures">Manufactures</Translate>
          </dt>
          <dd>
            {vendorEntity.manufactures
              ? vendorEntity.manufactures.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {vendorEntity.manufactures && i === vendorEntity.manufactures.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/vendor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vendor/${vendorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VendorDetail;
