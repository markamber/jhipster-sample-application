import { IManufacture } from 'app/shared/model/manufacture.model';

export interface IVendor {
  id?: number;
  legalEntity?: string;
  nickname?: string | null;
  billingAddress?: string | null;
  manufactures?: IManufacture[] | null;
}

export const defaultValue: Readonly<IVendor> = {};
