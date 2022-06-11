import dayjs from 'dayjs';
import { IOrderLineItem } from 'app/shared/model/order-line-item.model';
import { IPurchaseOrderLineItem } from 'app/shared/model/purchase-order-line-item.model';

export interface IReceivedItem {
  id?: number;
  note?: string | null;
  location?: string | null;
  assetIdSerial?: string | null;
  assetIdMAC?: string | null;
  receivedDate?: string | null;
  tracked?: boolean | null;
  forInventory?: boolean | null;
  bundleQuantity?: number | null;
  fufill?: IOrderLineItem | null;
  poLineItem?: IPurchaseOrderLineItem | null;
}

export const defaultValue: Readonly<IReceivedItem> = {
  tracked: false,
  forInventory: false,
};
