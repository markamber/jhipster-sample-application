import project from 'app/entities/project/project.reducer';
import orderLineItem from 'app/entities/order-line-item/order-line-item.reducer';
import manufacture from 'app/entities/manufacture/manufacture.reducer';
import product from 'app/entities/product/product.reducer';
import vendor from 'app/entities/vendor/vendor.reducer';
import purchaseOrder from 'app/entities/purchase-order/purchase-order.reducer';
import purchaseOrderLineItem from 'app/entities/purchase-order-line-item/purchase-order-line-item.reducer';
import receivedItem from 'app/entities/received-item/received-item.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  project,
  orderLineItem,
  manufacture,
  product,
  vendor,
  purchaseOrder,
  purchaseOrderLineItem,
  receivedItem,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
