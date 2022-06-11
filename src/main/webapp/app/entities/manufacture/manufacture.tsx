import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IManufacture } from 'app/shared/model/manufacture.model';
import { getEntities } from './manufacture.reducer';

export const Manufacture = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const manufactureList = useAppSelector(state => state.manufacture.entities);
  const loading = useAppSelector(state => state.manufacture.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="manufacture-heading" data-cy="ManufactureHeading">
        <Translate contentKey="myApp.manufacture.home.title">Manufactures</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.manufacture.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/manufacture/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.manufacture.home.createLabel">Create new Manufacture</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {manufactureList && manufactureList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.manufacture.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.manufacture.name">Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {manufactureList.map((manufacture, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/manufacture/${manufacture.id}`} color="link" size="sm">
                      {manufacture.id}
                    </Button>
                  </td>
                  <td>{manufacture.name}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/manufacture/${manufacture.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/manufacture/${manufacture.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/manufacture/${manufacture.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="myApp.manufacture.home.notFound">No Manufactures found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Manufacture;
