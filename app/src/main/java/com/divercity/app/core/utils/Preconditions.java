package com.divercity.app.core.utils;

import android.os.Looper;

public class Preconditions {

    private Preconditions() {
        throw new IllegalStateException("No instances.");
    }

    public static void checkUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException(
                "Must be called from the main thread. Was: " + Thread.currentThread());
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
    /**
     * Ensures that an object reference passed as userAttributes parameter to the calling
     * method is not null.
     *
     * @param reference an object reference
     * @param errorMessage the exception text to use if the check fails; will
     *     be converted to userAttributes string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }


    /**
     * @throws IllegalArgumentException if condition is false.
     */
    public static void checkArgument(boolean condition, String errorMessage, Object... args) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(errorMessage, args));
        }
    }
}
