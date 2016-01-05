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

package com.udeyrishi.androidelasticsearchdatamanager.datamanagers;

import android.content.Context;

import com.path.android.jobqueue.JobManager;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.jobs.DeleteDataJob;
import com.udeyrishi.androidelasticsearchdatamanager.jobs.WriteDataJob;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A {@link CachedDataManager} that uses {@link JobManager} for queuing up the the write and deletion
 * operations. It uses {@link HttpDataManager} as the inner manager.
 * <p/>
 * It guarantees:
 * 1. All the write and delete operations will eventually go through, no matter what the current
 * internet state is. The requests are persisted, so that they are still sent if the app restarts.
 * 2. All the operations will be sent in the exact same order as they were queued. This guarantees
 * data integrity for operations that work on the same remote object.
 * 3. All the write and delete operations will be performed on a different thread. So these methods
 * can be called on the UI thread safely, even though network is accessed.
 * <p/>
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManager extends CachedDataManager {

    private final JobManager jobManager;
    private final String rootUrl;

    /**
     * Creates an instance of the {@link QueuedDataManager}.
     *
     * @param context                     The {@link Context} to be used for network operations.
     * @param rootUrl                     The root URL to elastic search.
     * @param jobManager                  The {@link JobManager} to be used for queueing jobs.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     */
    public QueuedDataManager(Context context, String rootUrl, JobManager jobManager, boolean useExplicitExposeAnnotation) {
        this(context, rootUrl, jobManager, new HttpDataManager(context, rootUrl, useExplicitExposeAnnotation));
    }

    /**
     * Creates an instance of the {@link QueuedDataManager}. Sets the "useExplicitExposeAnnotation"
     * value to false.
     *
     * @param context    The {@link Context} to be used for network operations.
     * @param rootUrl    The root URL to elastic search.
     * @param jobManager The {@link JobManager} to be used for queueing jobs.
     */
    public QueuedDataManager(Context context, String rootUrl, JobManager jobManager) {
        this(context, rootUrl, jobManager, false);
    }

    /**
     * Test only constructor for working with a test version of {@link HttpDataManager}.
     *
     * @param rootUrl      The root URL to elastic search.
     * @param jobManager   The {@link JobManager} to be used for queueing jobs.
     * @param innerManager The {@link HttpDataManager} to be used as the inner manager.
     */
    protected QueuedDataManager(Context context, String rootUrl, JobManager jobManager, HttpDataManager innerManager) {
        super(context, Preconditions.checkNotNull(innerManager, "innerManager"));
        this.jobManager = Preconditions.checkNotNull(jobManager, "jobManager");
        this.rootUrl = Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl");
    }

    /**
     * Creates a job for writing the data to the location pointed by the key, and queues it up.
     *
     * @param key     The {@link DataKey} for the object.
     * @param obj     The object to be stored.
     * @param typeOfT The {@link Type} of the object.
     * @param <T>     The type of the object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public <T> void writeData(final DataKey key, final T obj, final Type typeOfT) throws IOException {
        String json = serialize(obj, typeOfT);
        WriteDataJob job = new WriteDataJob(rootUrl, key, json);
        jobManager.addJobInBackground(job);
        writeToCache(key, obj, typeOfT);
    }

    /**
     * Creates a job for deleting the data at the location pointed by the key, and queues it up.
     *
     * @param key The {@link DataKey} for which the object has to be deleted.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public void deleteIfExists(final DataKey key) throws IOException {
        DeleteDataJob job = new DeleteDataJob(rootUrl, key);
        jobManager.addJobInBackground(job);
        deleteFromCache(key);
    }

    /**
     * Tells if the data manager is operational or not.
     *
     * @return Always true, as the manager is always operational, either through cache or through queuing.
     */
    @Override
    public boolean isOperational() {
        return true;
    }
}
