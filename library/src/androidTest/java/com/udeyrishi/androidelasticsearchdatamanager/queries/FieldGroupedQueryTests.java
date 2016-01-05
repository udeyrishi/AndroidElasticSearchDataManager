package com.udeyrishi.androidelasticsearchdatamanager.queries;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by rishi on 15-11-27.
 */
public class FieldGroupedQueryTests extends TestCase {

    private static final String expectedQueryJson = "{\n" +
            "  \"aggs\": {\n" +
            "    \"group\": {\n" +
            "      \"terms\": {\n" +
            "        \"field\": \"owner.username\",\n" +
            "        \"size\": 5\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"query\": {\n" +
            "    \"query_string\": {\n" +
            "      \"default_field\": \"state\",\n" +
            "      \"query\": \"TradeStateOffered OR TradeStateAccepted\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public void testFormQuery() {
        FieldGroupedQuery query = new FieldGroupedQuery("state", new ArrayList<String>() {
            {
                add("TradeStateOffered");
                add("TradeStateAccepted");
            }
        }, "owner.username", 5, "test_query");

        String queryJson = query.formQuery();
        assertEquals(expectedQueryJson, queryJson);
    }
}
