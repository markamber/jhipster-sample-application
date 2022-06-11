import { IProduct } from 'app/shared/model/product.model';
import { IVendor } from 'app/shared/model/vendor.model';

export interface IManufacture {
  id?: number;
  name?: string | null;
  products?: IProduct[] | null;
  vendors?: IVendor[] | null;
}

export const defaultValue: Readonly<IManufacture> = {};
