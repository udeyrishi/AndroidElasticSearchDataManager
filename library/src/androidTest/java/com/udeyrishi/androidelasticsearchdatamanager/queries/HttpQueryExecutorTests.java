package com.udeyrishi.androidelasticsearchdatamanager.queries;

import com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers.ElasticSearchHelper;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishi on 15-11-28.
 */
public class HttpQueryExecutorTests extends TestCase {

    private final String rootUrl = "http://cmput301.softwareprocess.es:8080/cmput301f15t03/";

    public void testExecuteQuery() throws IOException {

        HttpQueryExecutor queryExecutor = new HttpQueryExecutor(new ElasticSearchHelper(rootUrl));

        Query query = new FieldGroupedQuery("state",
                new ArrayList<String>() {
                    {
                        add("TradeStateOffered");
                        add("TradeStateAccepted");
                    }
                },
                "owner.username", 5, "top_traders_query");
        AggregationQueryResult result = queryExecutor.executeQuery("Trade/_search", query);

        AggregationQueryResult.Aggregations aggregations = result.getAggregations();
        assertNotNull(aggregations);

        AggregationQueryResult.AggregationGroup group = aggregations.getGroup();
        assertNotNull(group);

        List<AggregationQueryResult.Bucket> buckets = group.getBuckets();
        assertNotNull(buckets);

        assertNotNull(buckets);
        assertTrue(buckets.size() > 0);

        for (AggregationQueryResult.Bucket bucket : buckets) {
            assertTrue(bucket.getCount() > 0);
            assertNotNull(bucket.getKey());
            assertFalse(bucket.getKey().trim().isEmpty());
        }

    }
}
