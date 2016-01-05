package com.udeyrishi.androidelasticsearchdatamanager.elasticsearch.queries;

/**
 * An interface for an Elastic Search query.
 * Created by rishi on 15-11-27.
 */
public interface Query {

    /**
     * The key for the aggregations results in the query response.
     */
    String AGGREGATION_KEY = "group";

    /**
     * Form a string query that can be sent as the content of the query HTTP request.
     * @return The JSON query.
     */
    String formQuery();

    /**
     * Get a unique ID for this query. Can be used to cache results for this query.
     * @return The unique ID for this query.
     */
    String getUniqueId();
}
