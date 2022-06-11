import { IOrderLineItem } from 'app/shared/model/order-line-item.model';

export interface IProject {
  id?: number;
  name?: string | null;
  orderLineItems?: IOrderLineItem[] | null;
}

export const defaultValue: Readonly<IProject> = {};
