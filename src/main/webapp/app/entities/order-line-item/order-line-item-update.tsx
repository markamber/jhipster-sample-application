import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IReceivedItem } from 'app/shared/model/received-item.model';
import { getEntities as getReceivedItems } from 'app/entities/received-item/received-item.reducer';
import { IProject } from 'app/shared/model/project.model';
import { getEntities as getProjects } from 'app/entities/project/project.reducer';
import { IOrderLineItem } from 'app/shared/model/order-line-item.model';
import { LineItemType } from 'app/shared/model/enumerations/line-item-type.model';
import { getEntity, updateEntity, createEntity, reset } from './order-line-item.reducer';

export const OrderLineItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const products = useAppSelector(state => state.product.entities);
  const receivedItems = useAppSelector(state => state.receivedItem.entities);
  const projects = useAppSelector(state => state.project.entities);
  const orderLineItemEntity = useAppSelector(state => state.orderLineItem.entity);
  const loading = useAppSelector(state => state.orderLineItem.loading);
  const updating = useAppSelector(state => state.orderLineItem.updating);
  const updateSuccess = useAppSelector(state => state.orderLineItem.updateSuccess);
  const lineItemTypeValues = Object.keys(LineItemType);
  const handleClose = () => {
    props.history.push('/order-line-item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProducts({}));
    dispatch(getReceivedItems({}));
    dispatch(getProjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...orderLineItemEntity,
      ...values,
      product: products.find(it => it.id.toString() === values.product.toString()),
      project: projects.find(it => it.id.toString() === values.project.toString()),
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
          type: 'SALE_ITEM',
          ...orderLineItemEntity,
          product: orderLineItemEntity?.product?.id,
          project: orderLineItemEntity?.project?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.orderLineItem.home.createOrEditLabel" data-cy="OrderLineItemCreateUpdateHeading">
            Create or edit a OrderLineItem
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
                <ValidatedField name="id" required readOnly id="order-line-item-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Description" id="order-line-item-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Type" id="order-line-item-type" name="type" data-cy="type" type="select">
                {lineItemTypeValues.map(lineItemType => (
                  <option value={lineItemType} key={lineItemType}>
                    {lineItemType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Expected Cost Unit"
                id="order-line-item-expectedCostUnit"
                name="expectedCostUnit"
                data-cy="expectedCostUnit"
                type="text"
              />
              <ValidatedField
                label="Sell Price Unit"
                id="order-line-item-sellPriceUnit"
                name="sellPriceUnit"
                data-cy="sellPriceUnit"
                type="text"
              />
              <ValidatedField label="Number Units" id="order-line-item-numberUnits" name="numberUnits" data-cy="numberUnits" type="text" />
              <ValidatedField label="Room" id="order-line-item-room" name="room" data-cy="room" type="text" />
              <ValidatedField label="System" id="order-line-item-system" name="system" data-cy="system" type="text" />
              <ValidatedField id="order-line-item-product" name="product" data-cy="product" label="Product" type="select">
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="order-line-item-project" name="project" data-cy="project" label="Project" type="select">
                <option value="" key="0" />
                {projects
                  ? projects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order-line-item" replace color="info">
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

export default OrderLineItemUpdate;
