/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.udeyrishi.androidelasticsearchdatamanager.elasticsearch.queries;

import android.content.Context;

import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.LocalDataManager;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.elasticsearch.queries.QueryExecutor;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A {@link QueryExecutor} that uses an inner {@link QueryExecutor} to obtain results, and caches the
 * results. For subsequent requests, if an {@link IOException} is thrown, the cache is used to obtain
 * the results.
 * Created by rishi on 15-11-28.
 */
public class CachedQueryExecutor implements QueryExecutor {

    private final QueryExecutor innerExecutor;
    private final LocalDataManager cachingDataManager;

    /**
     * Creates an instance of {@link CachedQueryExecutor}.
     *
     * @param context The {@link Context} to be used for local file IO.
     * @param innerExecutor The inner {@link QueryExecutor} to be used for executing the first chance
     *                      queries.
     */
    public CachedQueryExecutor(Context context, QueryExecutor innerExecutor) {
        this.innerExecutor = Preconditions.checkNotNull(innerExecutor, "innerExecutor");
        this.cachingDataManager = new LocalDataManager(context);
    }

    /**
     * Performs the query and gets the {@link AggregationQueryResult} obtained back. For subsequent
     * requests, if an {@link IOException} is thrown, the cache is used to obtain the results.
     * @param suffix The suffix at which the query should be executed.
     * @param query The {@link Query} to be executed.
     * @return The obtained {@link AggregationQueryResult}.
     * @throws IOException Thrown, if there is some network failure.
     */
    @Override
    public AggregationQueryResult executeQuery(String suffix, Query query) throws IOException {
        AggregationQueryResult aggregationQueryResult;

        try {
            aggregationQueryResult = innerExecutor.executeQuery(suffix, query);
            writeToCache(suffix, query, aggregationQueryResult);
        }
        catch (IOException e) {
            if (isQueryResultInCache(suffix, query)) {
                aggregationQueryResult = getResultFromCache(suffix, query);
            }
            else {
                throw e;
            }
        }

        return aggregationQueryResult;
    }

    private void writeToCache(String suffix, Query query, AggregationQueryResult aggregationQueryResult) {
        cachingDataManager.writeData(getQueryDataKey(suffix, query), aggregationQueryResult, AggregationQueryResult.class);
    }

    private AggregationQueryResult getResultFromCache(String suffix, Query query) throws IOException {
        return cachingDataManager.getData(getQueryDataKey(suffix, query), AggregationQueryResult.class);
    }

    private boolean isQueryResultInCache(String suffix, Query query) {
        return cachingDataManager.keyExists(getQueryDataKey(suffix, query));
    }

    private DataKey getQueryDataKey(String suffix, Query query) {
        return new DataKey(suffix, query.getUniqueId());
    }
}
