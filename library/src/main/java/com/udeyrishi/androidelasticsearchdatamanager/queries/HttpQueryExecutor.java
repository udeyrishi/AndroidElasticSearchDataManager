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

import com.udeyrishi.androidelasticsearchdatamanager.JsonFormatter;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers.ElasticSearchHelper;

import java.io.IOException;

/**
 * A {@link QueryExecutor} that performs HTTP requests to Elastic Search to get the results.
 * Created by rishi on 15-11-28.
 */
public class HttpQueryExecutor implements QueryExecutor {

    private final ElasticSearchHelper elasticSearchHelper;

    public HttpQueryExecutor(ElasticSearchHelper elasticSearchHelper) {
        this.elasticSearchHelper = Preconditions.checkNotNull(elasticSearchHelper, "elasticSearchHelper");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AggregationQueryResult executeQuery(String suffix, Query query) throws IOException {
        Preconditions.checkNotNull(query, "query");
        Preconditions.checkNotNullOrWhitespace(suffix, "suffix");

        String resultJson = elasticSearchHelper.postJson(query.formQuery(), suffix);
        return new JsonFormatter(false, true).getGson().fromJson(resultJson, AggregationQueryResult.class);
    }
}
