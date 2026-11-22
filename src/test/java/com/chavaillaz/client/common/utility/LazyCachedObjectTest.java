package com.chavaillaz.client.common.utility;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class LazyCachedObjectTest {

    private static final String VALUE = "Test";

    @Test
    void testWithParameterAtInstantiation() {
        LazyCachedObject<String> cachedObject = new LazyCachedObject<>(() -> VALUE);

        Optional<String> firstCall = cachedObject.get();
        assertThat(firstCall).contains(VALUE);

        Optional<String> secondCall = cachedObject.get();
        assertThat(secondCall).contains(VALUE);

        // Check that the same object has been used
        assertThat(secondCall).containsSame(firstCall.orElseThrow());
    }

    @Test
    void testWithParameterAtGet() {
        LazyCachedObject<String> cachedObject = new LazyCachedObject<>();
        assertThat(cachedObject.get()).isEmpty();

        String firstCall = cachedObject.get(() -> VALUE);
        assertThat(firstCall).isEqualTo(VALUE);

        // Give on purpose a new string instance in parameter
        String secondCall = cachedObject.get(() -> "Test");
        assertThat(secondCall).isEqualTo(VALUE);

        // Check that the same object has been used
        assertThat(secondCall).isSameAs(firstCall);
    }

}
