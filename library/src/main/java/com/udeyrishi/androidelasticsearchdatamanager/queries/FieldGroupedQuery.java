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
