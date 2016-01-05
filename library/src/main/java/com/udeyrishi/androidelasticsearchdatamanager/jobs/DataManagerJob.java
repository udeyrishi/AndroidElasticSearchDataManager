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

package com.udeyrishi.androidelasticsearchdatamanager.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.DataManager;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;

/**
 * A {@link Job} for queuing {@link DataManager} requests.
 * Created by rishi on 15-11-11.
 */
public abstract class DataManagerJob extends Job {
    private static final int PRIORITY = 1000;
    private static final String GROUP = "datamanagerjob";

    private final String type;
    private final String id;
    protected final String rootUrl;

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
    public void onAdded() { }

    /**
     * Called if the {@link DataManagerJob} fails after all the {@link RetryConstraint} constraints.
     * Not implemented yet.
     */
    @Override
    protected void onCancel() {
        throw new RuntimeException();
    }

    /**
     * Gets the request suffix for thsi job. This approach is needed vs. storing a {@link DataKey},
     * because only simple types can be serialized
     * @return
     */
    protected String getRequestSuffix() {
        return new DataKey(type, id).toString();
    }

    /**
     * Called if the job throws an exception. It cancels the job.
     * @return {@link RetryConstraint#CANCEL}
     */
    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }
}
