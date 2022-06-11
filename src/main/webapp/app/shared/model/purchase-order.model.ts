import dayjs from 'dayjs';
import { IPurchaseOrderLineItem } from 'app/shared/model/purchase-order-line-item.model';

export interface IPurchaseOrder {
  id?: number;
  shipTo?: string | null;
  notes?: string | null;
  date?: string | null;
  poLineItems?: IPurchaseOrderLineItem | null;
}

export const defaultValue: Readonly<IPurchaseOrder> = {};
