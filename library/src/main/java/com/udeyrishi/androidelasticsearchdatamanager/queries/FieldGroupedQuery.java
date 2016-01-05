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

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.udeyrishi.androidelasticsearchdatamanager.JsonFormatter;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;

/**
 * A {@link Query} for performing filters based on a field's values and then grouping based on
 * another field in the result of this filter.
 * Created by rishi on 15-11-27.
 */
public class FieldGroupedQuery implements Query {

    @Expose
    @SerializedName("query")
    private final HashMap<String, HashMap<String, String>> filteringQuery;

    @Expose
    @SerializedName("aggs")
    private final HashMap<String, HashMap<String, Terms>> aggregationQuery;

    private final String queryIdentifier;

    /**
     * Creates a new instance of {@link FieldGroupedQuery}.
     *
     * @param filterField               The field to be used for filtering.
     * @param possibleFilterFieldValues The allowed values for the filterField. The result will be
     *                                  calculated only on the objects whose filterField has one of
     *                                  these values.
     * @param groupingField             This is the field on which grouping happens. The grouping is done after
     *                                  filtering. The result is sorted in decreasing order based on the value
     *                                  of this field
     * @param maxCount                  The max number of results after grouping.
     * @param queryIdentifier           The unique identifier for this query.
     */
    public FieldGroupedQuery(final String filterField,
                             final Collection<String> possibleFilterFieldValues,
                             final String groupingField,
                             final Integer maxCount,
                             final String queryIdentifier) {

        Preconditions.checkNotNullOrWhitespace(filterField, "filterField");
        Preconditions.checkNotNullOrWhitespace(groupingField, "groupingField");
        Preconditions.checkNotNullOrEmpty(possibleFilterFieldValues, "possibleFilterFieldValues");

        this.queryIdentifier = Preconditions.checkNotNullOrWhitespace(queryIdentifier, "queryIdentifier");
        this.filteringQuery = new HashMap<>();
        this.aggregationQuery = new HashMap<>();

        String filterFieldQuery = StringUtils.join(possibleFilterFieldValues, " OR ");
        buildFilteringQuery(filterField, filterFieldQuery);

        buildAggregationQuery(groupingField, maxCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String formQuery() {
        Gson gson = new JsonFormatter(true, true).getGson();
        return gson.toJson(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUniqueId() {
        return queryIdentifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof FieldGroupedQuery)) {
            return false;
        }

        FieldGroupedQuery rhs = (FieldGroupedQuery) o;

        return this.filteringQuery.equals(rhs.filteringQuery) &&
                this.aggregationQuery.equals(rhs.aggregationQuery) &&
                this.queryIdentifier.equals(rhs.queryIdentifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.aggregationQuery.hashCode() ^ this.filteringQuery.hashCode() ^ this.queryIdentifier.hashCode();
    }

    private void buildAggregationQuery(final String groupingField, final Integer maxCount) {
        aggregationQuery.put(AGGREGATION_KEY, new HashMap<String, Terms>() {
            {
                put("terms", new Terms(groupingField, maxCount));
            }
        });
    }

    private void buildFilteringQuery(final String filterField, final String filterFieldQuery) {
        this.filteringQuery.put("query_string", new HashMap<String, String>() {
            {
                put("default_field", filterField);
                put("query", filterFieldQuery);
            }
        });
    }

    private class Terms {
        @Expose
        private String field;

        @Expose
        private Integer size;

        public Terms(String field, Integer size) {
            this.field = field;
            this.size = size;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (!(o instanceof Terms)) {
                return false;
            }

            Terms rhs = (Terms) o;

            return rhs.field.equals(this.field) &&
                    rhs.size.equals(this.size);
        }

        @Override
        public int hashCode() {
            return field.hashCode() ^ size.hashCode();
        }
    }
}
