import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IManufacture } from 'app/shared/model/manufacture.model';
import { getEntities as getManufactures } from 'app/entities/manufacture/manufacture.reducer';
import { IVendor } from 'app/shared/model/vendor.model';
import { getEntity, updateEntity, createEntity, reset } from './vendor.reducer';

export const VendorUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const manufactures = useAppSelector(state => state.manufacture.entities);
  const vendorEntity = useAppSelector(state => state.vendor.entity);
  const loading = useAppSelector(state => state.vendor.loading);
  const updating = useAppSelector(state => state.vendor.updating);
  const updateSuccess = useAppSelector(state => state.vendor.updateSuccess);
  const handleClose = () => {
    props.history.push('/vendor');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getManufactures({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...vendorEntity,
      ...values,
      manufactures: mapIdList(values.manufactures),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...vendorEntity,
          manufactures: vendorEntity?.manufactures?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.vendor.home.createOrEditLabel" data-cy="VendorCreateUpdateHeading">
            Create or edit a Vendor
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="vendor-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Legal Entity"
                id="vendor-legalEntity"
                name="legalEntity"
                data-cy="legalEntity"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Nickname" id="vendor-nickname" name="nickname" data-cy="nickname" type="text" />
              <ValidatedField
                label="Billing Address"
                id="vendor-billingAddress"
                name="billingAddress"
                data-cy="billingAddress"
                type="text"
              />
              <ValidatedField
                label="Manufactures"
                id="vendor-manufactures"
                data-cy="manufactures"
                type="select"
                multiple
                name="manufactures"
              >
                <option value="" key="0" />
                {manufactures
                  ? manufactures.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vendor" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VendorUpdate;
