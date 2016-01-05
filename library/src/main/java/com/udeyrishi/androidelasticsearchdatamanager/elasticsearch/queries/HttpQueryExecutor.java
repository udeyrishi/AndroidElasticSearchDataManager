package com.udeyrishi.androidelasticsearchdatamanager.elasticsearch.queries;

import com.udeyrishi.androidelasticsearchdatamanager.JsonFormatter;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.elasticsearch.ElasticSearchHelper;

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

        String resultJson =  elasticSearchHelper.postJson(query.formQuery(), suffix);
        return new JsonFormatter(false, true).getGson().fromJson(resultJson, AggregationQueryResult.class);
    }
}
