package com.markamber.testjhip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.markamber.testjhip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchaseOrderLineItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrderLineItem.class);
        PurchaseOrderLineItem purchaseOrderLineItem1 = new PurchaseOrderLineItem();
        purchaseOrderLineItem1.setId(1L);
        PurchaseOrderLineItem purchaseOrderLineItem2 = new PurchaseOrderLineItem();
        purchaseOrderLineItem2.setId(purchaseOrderLineItem1.getId());
        assertThat(purchaseOrderLineItem1).isEqualTo(purchaseOrderLineItem2);
        purchaseOrderLineItem2.setId(2L);
        assertThat(purchaseOrderLineItem1).isNotEqualTo(purchaseOrderLineItem2);
        purchaseOrderLineItem1.setId(null);
        assertThat(purchaseOrderLineItem1).isNotEqualTo(purchaseOrderLineItem2);
    }
}
