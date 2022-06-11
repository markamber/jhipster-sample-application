import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IPurchaseOrderLineItem } from 'app/shared/model/purchase-order-line-item.model';
import { getEntity, updateEntity, createEntity, reset } from './purchase-order-line-item.reducer';

export const PurchaseOrderLineItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const products = useAppSelector(state => state.product.entities);
  const purchaseOrderLineItemEntity = useAppSelector(state => state.purchaseOrderLineItem.entity);
  const loading = useAppSelector(state => state.purchaseOrderLineItem.loading);
  const updating = useAppSelector(state => state.purchaseOrderLineItem.updating);
  const updateSuccess = useAppSelector(state => state.purchaseOrderLineItem.updateSuccess);
  const handleClose = () => {
    props.history.push('/purchase-order-line-item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...purchaseOrderLineItemEntity,
      ...values,
      product: products.find(it => it.id.toString() === values.product.toString()),
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
          ...purchaseOrderLineItemEntity,
          product: purchaseOrderLineItemEntity?.product?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.purchaseOrderLineItem.home.createOrEditLabel" data-cy="PurchaseOrderLineItemCreateUpdateHeading">
            <Translate contentKey="myApp.purchaseOrderLineItem.home.createOrEditLabel">Create or edit a PurchaseOrderLineItem</Translate>
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
                  id="purchase-order-line-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myApp.purchaseOrderLineItem.note')}
                id="purchase-order-line-item-note"
                name="note"
                data-cy="note"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.purchaseOrderLineItem.estimatedShipDate')}
                id="purchase-order-line-item-estimatedShipDate"
                name="estimatedShipDate"
                data-cy="estimatedShipDate"
                type="date"
              />
              <ValidatedField
                id="purchase-order-line-item-product"
                name="product"
                data-cy="product"
                label={translate('myApp.purchaseOrderLineItem.product')}
                type="select"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/purchase-order-line-item" replace color="info">
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

export default PurchaseOrderLineItemUpdate;
