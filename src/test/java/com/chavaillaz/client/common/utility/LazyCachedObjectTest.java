package com.chavaillaz.client.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class LazyCachedObjectTest {

    private static final String VALUE = "Test";

    @Test
    void testWithParameterAtInstantiation() {
        LazyCachedObject<String> cachedObject = new LazyCachedObject<>(() -> VALUE);

        Optional<String> firstCall = cachedObject.get();
        assertTrue(firstCall.isPresent());
        assertEquals(VALUE, firstCall.get());

        Optional<String> secondCall = cachedObject.get();
        assertTrue(secondCall.isPresent());
        assertEquals(VALUE, secondCall.get());

        // Check that the same object has been used
        assertSame(firstCall.get(), secondCall.get());
    }

    @Test
    void testWithParameterAtGet() {
        LazyCachedObject<String> cachedObject = new LazyCachedObject<>();
        assertFalse(cachedObject.get().isPresent());

        String firstCall = cachedObject.get(() -> VALUE);
        assertEquals(VALUE, firstCall);

        // Give on purpose a new string instance in parameter
        String secondCall = cachedObject.get(() -> "Test");
        assertEquals(VALUE, secondCall);

        // Check that the same object has been used
        assertSame(firstCall, secondCall);
    }

}
