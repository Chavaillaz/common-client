package com.chavaillaz.client.utility;

import java.util.Optional;
import java.util.function.Supplier;

import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cached object that is lazily initialised (on demand).
 *
 * @param <T> The type of the object to store
 */
@NoArgsConstructor
public class LazyCachedObject<T> {

    private T object;

    @Setter
    private Supplier<T> supplier;

    /**
     * Creates a new lazy cached object.
     *
     * @param supplier The supplier to be called when getting the object for the first time
     */
    public LazyCachedObject(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets the object and, if it's not present, tries to initialize it (if the supplier has already been set).
     * Returns {@link Optional#empty()} in case the object has not been initialized yet and the supplier is not set.
     *
     * @return The object wrapped into an {@link Optional}
     * @see #get(Supplier)
     */
    public Optional<T> get() {
        if (object == null && supplier != null) {
            return Optional.of(get(supplier));
        } else {
            return Optional.ofNullable(object);
        }
    }

    /**
     * Gets the object and tries to initialize it, if it's not present, using the given supplier.
     *
     * @param supplier The supplier to be used to create the object instance
     * @return The object
     */
    public synchronized T get(Supplier<T> supplier) {
        if (object == null) {
            object = supplier.get();
        }
        return object;
    }

}
