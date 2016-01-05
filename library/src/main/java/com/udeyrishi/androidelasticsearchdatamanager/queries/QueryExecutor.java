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

import java.io.IOException;

/**
 * An interface for a class that executes {@link Query}
 * Created by rishi on 15-11-28.
 */
public interface QueryExecutor {

    /**
     * Performs the query and gets the {@link AggregationQueryResult} obtained back.
     *
     * @param suffix The suffix at which the query should be executed.
     * @param query  The {@link Query} to be executed.
     * @return The obtained {@link AggregationQueryResult}.
     * @throws IOException Thrown, if there is some network failure.
     */
    AggregationQueryResult executeQuery(String suffix, Query query) throws IOException;
}
