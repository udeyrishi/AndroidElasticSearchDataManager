package com.udeyrishi.androidelasticsearchdatamanager.exceptions;

/**
 * {@link IllegalArgumentException} thrown when an argument to a method is null.
 * Created by rishi on 15-10-30.
 */
public class ArgumentNullException extends IllegalArgumentException {

    /**
     * Creates an instance of {@link ArgumentNullException}.
     */
    public ArgumentNullException() {
        super();
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     *
     * @param message The detail message for this exception.
     */
    public ArgumentNullException(String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     *
     * @param cause The cause of this exception, may be {@code null}.
     */
    public ArgumentNullException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     *
     * @param message The detail message for this exception.
     * @param cause   The cause of this exception, may be {@code null}.
     */
    public ArgumentNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
