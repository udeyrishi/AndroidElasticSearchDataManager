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

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.DataManager;

/**
 * A {@link Job} for queuing {@link DataManager} requests.
 * Created by rishi on 15-11-11.
 */
public abstract class DataManagerJob extends Job {
    private static final int PRIORITY = 1000;
    private static final String GROUP = "datamanagerjob";
    protected final String rootUrl;
    private final String type;
    private final String id;

    /**
     * Creates an instance of {@link DataManagerJob}.
     *
     * @param rootUrl The root URL to elastic search.
     * @param dataKey The {@link DataKey} to be used for this operation.
     */
    public DataManagerJob(String rootUrl, DataKey dataKey) {
        super(new Params(PRIORITY).persist().requireNetwork().groupBy(GROUP));
        Preconditions.checkNotNull(dataKey, "dataKey");

        this.rootUrl = Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl");
        this.type = dataKey.getType();
        this.id = dataKey.getId();
    }

    /**
     * Called when the {@link DataManagerJob} is queued. Does nothing. Should be overridden for
     * specific implementations.
     */
    @Override
    public void onAdded() {
    }

    /**
     * Called if the {@link DataManagerJob} fails after all the {@link RetryConstraint} constraints.
     * Not implemented yet.
     */
    @Override
    protected void onCancel() {
        throw new RuntimeException("DataManagerJob failed.");
    }

    /**
     * Gets the request suffix for this job. This approach is needed vs. storing a {@link DataKey},
     * because only simple types can be serialized
     *
     * @return The request suffix
     */
    protected String getRequestSuffix() {
        return new DataKey(type, id).toString();
    }

    /**
     * Called if the job throws an exception. It cancels the job.
     *
     * @return {@link RetryConstraint#CANCEL}
     */
    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }
}
