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

package com.udeyrishi.androidelasticsearchdatamanager.jobs;

import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers.ElasticSearchHelper;

import java.io.IOException;

/**
 * A {@link DataManagerJob} for performing the write data request. Creates a new object, or replaces
 * the existing one.
 * Created by rishi on 15-11-11.
 */
public class WriteDataJob extends DataManagerJob {

    private final String json;

    /**
     * Creates an instance of {@link WriteDataJob}.
     *
     * @param rootUrl The root URL to elastic search.
     * @param dataKey The {@link DataKey} for the object. The new object will be created at the location
     *                pointed by this key. Existing object will be replaced, if applicable.
     * @param json    The JSON string to be written.
     */
    public WriteDataJob(String rootUrl, DataKey dataKey, String json) {
        super(rootUrl, dataKey);
        this.json = Preconditions.checkNotNullOrWhitespace(json, "json");
    }

    /**
     * Calls the {@link ElasticSearchHelper#postJson(String, String)} operation using the JSON
     * and {@link DataKey} passed during construction.
     *
     * @throws IOException Thrown if the network request fails.
     */
    @Override
    public void onRun() throws IOException {
        new ElasticSearchHelper(rootUrl).postJson(json, getRequestSuffix());
    }
}
