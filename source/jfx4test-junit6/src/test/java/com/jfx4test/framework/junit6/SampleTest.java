package com.jfx4test.framework.junit6;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SampleTest {

    @Test
    public void testX() {
        Assertions.assertThat(new LabelX()).hasToString("test");
    }
}
