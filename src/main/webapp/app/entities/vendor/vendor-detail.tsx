import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
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
        <h2 data-cy="vendorDetailsHeading">Vendor</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{vendorEntity.id}</dd>
          <dt>
            <span id="legalEntity">Legal Entity</span>
          </dt>
          <dd>{vendorEntity.legalEntity}</dd>
          <dt>
            <span id="nickname">Nickname</span>
          </dt>
          <dd>{vendorEntity.nickname}</dd>
          <dt>
            <span id="billingAddress">Billing Address</span>
          </dt>
          <dd>{vendorEntity.billingAddress}</dd>
          <dt>Manufactures</dt>
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
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vendor/${vendorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VendorDetail;
