package com.markamber.testjhip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.markamber.testjhip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceivedItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceivedItem.class);
        ReceivedItem receivedItem1 = new ReceivedItem();
        receivedItem1.setId(1L);
        ReceivedItem receivedItem2 = new ReceivedItem();
        receivedItem2.setId(receivedItem1.getId());
        assertThat(receivedItem1).isEqualTo(receivedItem2);
        receivedItem2.setId(2L);
        assertThat(receivedItem1).isNotEqualTo(receivedItem2);
        receivedItem1.setId(null);
        assertThat(receivedItem1).isNotEqualTo(receivedItem2);
    }
}
