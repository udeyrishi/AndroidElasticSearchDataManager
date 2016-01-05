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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * A DTO for the result of a {@link Query} execution by a {@link QueryExecutor}.
 * Created by rishi on 15-11-28.
 */
public class AggregationQueryResult {

    private Aggregations aggregations;

    public Aggregations getAggregations() {
        return aggregations;
    }

    public void setAggregations(Aggregations aggregations) {
        this.aggregations = aggregations;
    }

    /**
     * A DTO modelling the aggregations field inside the {@link AggregationQueryResult}.
     */
    public static class Aggregations {

        @SerializedName(Query.AGGREGATION_KEY)
        private AggregationGroup group;

        public AggregationGroup getGroup() {
            return group;
        }

        public void setGroup(AggregationGroup group) {
            this.group = group;
        }

    }

    /**
     * A DTO modelling the contents inside the
     * {@link AggregationQueryResult.Aggregations}.
     * It contains a collection of {@link AggregationQueryResult.Bucket}
     * objects, containing the aggregation result.
     */
    public static class AggregationGroup {
        private ArrayList<Bucket> buckets;

        public ArrayList<Bucket> getBuckets() {
            return buckets;
        }

        public void setBuckets(ArrayList<Bucket> buckets) {
            this.buckets = buckets;
        }

    }

    /**
     * A class modelling a bucket inside an {@link AggregationQueryResult.AggregationGroup}.
     * A Bucket contains a key and a count mapping to the grouping field and the grouping count.
     */
    public static class Bucket {
        private String key;

        @SerializedName("doc_count")
        private int count;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (!(o instanceof Bucket)) {
                return false;
            }

            Bucket rhs = (Bucket) o;

            return this.count == rhs.count &&
                    this.key.equals(rhs.key);
        }

        @Override
        public int hashCode() {
            return new Integer(count).hashCode() ^ key.hashCode();
        }
    }
}
