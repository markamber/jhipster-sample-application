import { IProduct } from 'app/shared/model/product.model';
import { IReceivedItem } from 'app/shared/model/received-item.model';
import { IProject } from 'app/shared/model/project.model';
import { LineItemType } from 'app/shared/model/enumerations/line-item-type.model';

export interface IOrderLineItem {
  id?: number;
  description?: string | null;
  type?: LineItemType | null;
  expectedCostUnit?: number | null;
  sellPriceUnit?: number | null;
  numberUnits?: number | null;
  room?: string | null;
  system?: string | null;
  product?: IProduct | null;
  fufilledBy?: IReceivedItem | null;
  project?: IProject | null;
}

export const defaultValue: Readonly<IOrderLineItem> = {};
