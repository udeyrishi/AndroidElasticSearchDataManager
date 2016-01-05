package com.udeyrishi.androidelasticsearchdatamanager.elasticsearch.queries;

import java.io.IOException;

/**
 * An interface for a class that executes {@link Query}
 * Created by rishi on 15-11-28.
 */
public interface QueryExecutor {

    /**
     * Performs the query and gets the {@link AggregationQueryResult} obtained back.
     * @param suffix The suffix at which the query should be executed.
     * @param query The {@link Query} to be executed.
     * @return The obtained {@link AggregationQueryResult}.
     * @throws IOException Thrown, if there is some network failure.
     */
    AggregationQueryResult executeQuery(String suffix, Query query) throws IOException;
}
