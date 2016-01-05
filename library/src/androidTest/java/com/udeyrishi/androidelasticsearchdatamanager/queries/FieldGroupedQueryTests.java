/**
 Copyright 2016 Udey Rishi
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

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
