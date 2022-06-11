package com.markamber.testjhip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.markamber.testjhip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManufactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manufacture.class);
        Manufacture manufacture1 = new Manufacture();
        manufacture1.setId(1L);
        Manufacture manufacture2 = new Manufacture();
        manufacture2.setId(manufacture1.getId());
        assertThat(manufacture1).isEqualTo(manufacture2);
        manufacture2.setId(2L);
        assertThat(manufacture1).isNotEqualTo(manufacture2);
        manufacture1.setId(null);
        assertThat(manufacture1).isNotEqualTo(manufacture2);
    }
}
