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

package com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers;

/**
 * An {@link RuntimeException} thrown when the {@link ElasticSearchHelper} construction fails.
 * Created by ross on 15-11-03.
 */
public class ElasticSearchHelperInitializationException extends RuntimeException {

    /**
     * Creates an instance of {@link ElasticSearchHelperInitializationException}.
     */
    public ElasticSearchHelperInitializationException() {
        super();
    }

    /**
     * Create an instance of {@link ElasticSearchHelperInitializationException}.
     *
     * @param message The detailed message describing the exception.
     */
    public ElasticSearchHelperInitializationException(String message) {
        super(message);
    }
}
