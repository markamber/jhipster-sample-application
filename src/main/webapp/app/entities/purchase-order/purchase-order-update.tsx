import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPurchaseOrderLineItem } from 'app/shared/model/purchase-order-line-item.model';
import { getEntities as getPurchaseOrderLineItems } from 'app/entities/purchase-order-line-item/purchase-order-line-item.reducer';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { getEntity, updateEntity, createEntity, reset } from './purchase-order.reducer';

export const PurchaseOrderUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const purchaseOrderLineItems = useAppSelector(state => state.purchaseOrderLineItem.entities);
  const purchaseOrderEntity = useAppSelector(state => state.purchaseOrder.entity);
  const loading = useAppSelector(state => state.purchaseOrder.loading);
  const updating = useAppSelector(state => state.purchaseOrder.updating);
  const updateSuccess = useAppSelector(state => state.purchaseOrder.updateSuccess);
  const handleClose = () => {
    props.history.push('/purchase-order');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPurchaseOrderLineItems({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...purchaseOrderEntity,
      ...values,
      poLineItems: purchaseOrderLineItems.find(it => it.id.toString() === values.poLineItems.toString()),
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
          ...purchaseOrderEntity,
          poLineItems: purchaseOrderEntity?.poLineItems?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.purchaseOrder.home.createOrEditLabel" data-cy="PurchaseOrderCreateUpdateHeading">
            Create or edit a PurchaseOrder
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="purchase-order-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Ship To" id="purchase-order-shipTo" name="shipTo" data-cy="shipTo" type="text" />
              <ValidatedField label="Notes" id="purchase-order-notes" name="notes" data-cy="notes" type="text" />
              <ValidatedField label="Date" id="purchase-order-date" name="date" data-cy="date" type="date" />
              <ValidatedField id="purchase-order-poLineItems" name="poLineItems" data-cy="poLineItems" label="Po Line Items" type="select">
                <option value="" key="0" />
                {purchaseOrderLineItems
                  ? purchaseOrderLineItems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/purchase-order" replace color="info">
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

export default PurchaseOrderUpdate;
