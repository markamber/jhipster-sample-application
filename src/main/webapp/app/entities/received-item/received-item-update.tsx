import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrderLineItem } from 'app/shared/model/order-line-item.model';
import { getEntities as getOrderLineItems } from 'app/entities/order-line-item/order-line-item.reducer';
import { IPurchaseOrderLineItem } from 'app/shared/model/purchase-order-line-item.model';
import { getEntities as getPurchaseOrderLineItems } from 'app/entities/purchase-order-line-item/purchase-order-line-item.reducer';
import { IReceivedItem } from 'app/shared/model/received-item.model';
import { getEntity, updateEntity, createEntity, reset } from './received-item.reducer';

export const ReceivedItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const orderLineItems = useAppSelector(state => state.orderLineItem.entities);
  const purchaseOrderLineItems = useAppSelector(state => state.purchaseOrderLineItem.entities);
  const receivedItemEntity = useAppSelector(state => state.receivedItem.entity);
  const loading = useAppSelector(state => state.receivedItem.loading);
  const updating = useAppSelector(state => state.receivedItem.updating);
  const updateSuccess = useAppSelector(state => state.receivedItem.updateSuccess);
  const handleClose = () => {
    props.history.push('/received-item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getOrderLineItems({}));
    dispatch(getPurchaseOrderLineItems({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...receivedItemEntity,
      ...values,
      fufill: orderLineItems.find(it => it.id.toString() === values.fufill.toString()),
      poLineItem: purchaseOrderLineItems.find(it => it.id.toString() === values.poLineItem.toString()),
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
          ...receivedItemEntity,
          fufill: receivedItemEntity?.fufill?.id,
          poLineItem: receivedItemEntity?.poLineItem?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.receivedItem.home.createOrEditLabel" data-cy="ReceivedItemCreateUpdateHeading">
            <Translate contentKey="myApp.receivedItem.home.createOrEditLabel">Create or edit a ReceivedItem</Translate>
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
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="received-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('myApp.receivedItem.note')} id="received-item-note" name="note" data-cy="note" type="text" />
              <ValidatedField
                label={translate('myApp.receivedItem.location')}
                id="received-item-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.receivedItem.assetIdSerial')}
                id="received-item-assetIdSerial"
                name="assetIdSerial"
                data-cy="assetIdSerial"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.receivedItem.assetIdMAC')}
                id="received-item-assetIdMAC"
                name="assetIdMAC"
                data-cy="assetIdMAC"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.receivedItem.receivedDate')}
                id="received-item-receivedDate"
                name="receivedDate"
                data-cy="receivedDate"
                type="date"
              />
              <ValidatedField
                label={translate('myApp.receivedItem.tracked')}
                id="received-item-tracked"
                name="tracked"
                data-cy="tracked"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('myApp.receivedItem.forInventory')}
                id="received-item-forInventory"
                name="forInventory"
                data-cy="forInventory"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('myApp.receivedItem.bundleQuantity')}
                id="received-item-bundleQuantity"
                name="bundleQuantity"
                data-cy="bundleQuantity"
                type="text"
              />
              <ValidatedField
                id="received-item-fufill"
                name="fufill"
                data-cy="fufill"
                label={translate('myApp.receivedItem.fufill')}
                type="select"
              >
                <option value="" key="0" />
                {orderLineItems
                  ? orderLineItems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="received-item-poLineItem"
                name="poLineItem"
                data-cy="poLineItem"
                label={translate('myApp.receivedItem.poLineItem')}
                type="select"
              >
                <option value="" key="0" />
                {purchaseOrderLineItems
                  ? purchaseOrderLineItems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/received-item" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReceivedItemUpdate;
