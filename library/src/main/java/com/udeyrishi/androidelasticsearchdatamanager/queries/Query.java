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
     *
     * @return The JSON query.
     */
    String formQuery();

    /**
     * Get a unique ID for this query. Can be used to cache results for this query.
     *
     * @return The unique ID for this query.
     */
    String getUniqueId();
}
