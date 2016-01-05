package com.udeyrishi.androidelasticsearchdatamanager.queries;

import android.test.AndroidTestCase;

import com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers.ElasticSearchHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by rishi on 15-11-28.
 */
public class CachedQueryExecutorTests extends AndroidTestCase {

    private final String rootUrl = "http://cmput301.softwareprocess.es:8080/cmput301f15t03/";
    private Query query;

    public void setUp() {
        query = new FieldGroupedQuery("state",
                new ArrayList<String>() {
                    {
                        add("TradeStateOffered");
                        add("TradeStateAccepted");
                    }
                },
                "owner.username", 5, "top_traders_query");
    }

    public void testExecuteQuery() throws IOException {
        HttpQueryExecutor queryExecutor = new HttpQueryExecutor(new ElasticSearchHelper(rootUrl));

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
            assertNotSame("", bucket.getKey());
        }

    }

    public void testCachingWorks() throws IOException {
        // For real request. This should cache.
        CachedQueryExecutor queryExecutor = new CachedQueryExecutor(getContext(), new HttpQueryExecutor(new ElasticSearchHelper(rootUrl)));
        AggregationQueryResult resultFromServer = queryExecutor.executeQuery("Trade/_search", query);

        queryExecutor = new CachedQueryExecutor(getContext(), new MockQueryExecutor());
        AggregationQueryResult resultFromCache = queryExecutor.executeQuery("Trade/_search", query);

        List<AggregationQueryResult.Bucket> bucketsFromServer = resultFromServer.getAggregations().getGroup().getBuckets();
        List<AggregationQueryResult.Bucket> bucketsFromCache = resultFromCache.getAggregations().getGroup().getBuckets();

        assertEquals(bucketsFromServer.size(), bucketsFromCache.size());

        for (int i = 0; i < bucketsFromServer.size(); ++i) {
            assertEquals(bucketsFromServer.get(i), bucketsFromCache.get(i));
        }
    }


    public class MockQueryExecutor implements QueryExecutor {

        @Override
        public AggregationQueryResult executeQuery(String suffix, Query query) throws IOException {
            throw new IOException("Mocking internet failure.");
        }
    }
}
