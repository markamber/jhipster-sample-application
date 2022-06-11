import dayjs from 'dayjs';
import { IReceivedItem } from 'app/shared/model/received-item.model';
import { IProduct } from 'app/shared/model/product.model';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';

export interface IPurchaseOrderLineItem {
  id?: number;
  note?: string | null;
  estimatedShipDate?: string | null;
  receivedItems?: IReceivedItem[] | null;
  product?: IProduct | null;
  pos?: IPurchaseOrder[] | null;
}

export const defaultValue: Readonly<IPurchaseOrderLineItem> = {};
