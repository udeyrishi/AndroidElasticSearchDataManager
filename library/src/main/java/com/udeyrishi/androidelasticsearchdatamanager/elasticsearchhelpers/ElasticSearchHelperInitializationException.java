package com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers;

/**
 * An {@link RuntimeException} thrown when the {@link ElasticSearchHelper} construction fails.
 * Created by ross on 15-11-03.
 */
public class ElasticSearchHelperInitializationException extends RuntimeException {

    /**
     * Creates an instance of {@link ElasticSearchHelperInitializationException}.
     */
    public ElasticSearchHelperInitializationException() {
        super();
    }

    /**
     * Create an instance of {@link ElasticSearchHelperInitializationException}.
     *
     * @param message The detailed message describing the exception.
     */
    public ElasticSearchHelperInitializationException(String message) {
        super(message);
    }
}
