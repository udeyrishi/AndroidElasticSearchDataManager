package com.udeyrishi.androidelasticsearchdatamanager.exceptions;

/**
 * A {@link RuntimeException} thrown when a service is not available.
 * Created by rishi on 15-11-01.
 */
public class ServiceNotAvailableException extends Exception {

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     */
    public ServiceNotAvailableException() {
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param detailMessage The detailed message describing the exception.
     */
    public ServiceNotAvailableException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param detailMessage The detailed message describing the exception.
     * @param cause         The cause of this exception.
     */
    public ServiceNotAvailableException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param cause The cause of this exception.
     */
    public ServiceNotAvailableException(Throwable cause) {
        super(cause);
    }
}
