import { IManufacture } from 'app/shared/model/manufacture.model';
import { IPurchaseOrderLineItem } from 'app/shared/model/purchase-order-line-item.model';
import { ItemType } from 'app/shared/model/enumerations/item-type.model';

export interface IProduct {
  id?: number;
  modelName?: string | null;
  type?: ItemType | null;
  manufacture?: IManufacture | null;
  purchaseOrderLineItems?: IPurchaseOrderLineItem[] | null;
}

export const defaultValue: Readonly<IProduct> = {};
