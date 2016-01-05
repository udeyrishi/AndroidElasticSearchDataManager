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

import com.google.gson.reflect.TypeToken;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import com.udeyrishi.androidelasticsearchdatamanager.mocks.MockNetworkUtil;
import com.udeyrishi.androidelasticsearchdatamanager.mocks.TestDto;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManagerTests extends BaseDataManagerTests<QueuedDataManager> implements DataManagerApiTests {

    private static final int DELAY_MS = 500;
    private final String rootUrl = "http://cmput301.softwareprocess.es:8080/cmput301f15t03/";

    @Override
    protected QueuedDataManager createNewDataManager() {
        Configuration configuration = new Configuration.Builder(getContext())
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .loadFactor(3)
                .consumerKeepAlive(120)
                .build();

        JobManager jobManager = new JobManager(getContext(), configuration);
        return new QueuedDataManager(getContext(), rootUrl, jobManager);
    }

    @Override
    public void testKeyExists() {
        super.keyExistsTest(DELAY_MS);
    }

    @Override
    public void testGetDataWhenKeyDoesNotExistThrowsException() {
        super.getDataWhenKeyDoesNotExistThrowsExceptionTest();
    }

    @Override
    public void testWriteData() {
        super.writeDataTest(DELAY_MS);
    }

    @Override
    public void testDelete() {
        super.deleteTest(DELAY_MS);
    }

    @Override
    public void testIsOperational() {
        super.isOperationalTest();
    }


    public void testWriteThenGetDataWhenInnerDataManagerNotAvailable() throws IOException, InterruptedException, ServiceNotAvailableException {

        // QDM doesn't change the getData and keyExists implementations of CachedDataManager
        // Test to check that the write (when device is online) is not messing with these methods

        Type type = new TypeToken<TestDto>() {
        }.getType();

        MockNetworkUtil mockNetworkUtil = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(getContext(), rootUrl, new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtil)
                        .build()),
                new HttpDataManager(getContext(), rootUrl, mockNetworkUtil));

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        Thread.sleep(DELAY_MS);
        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        mockNetworkUtil.setNetworkState(false);
        assertTrue(testDataManagerWithMockNetworkUtil.isOperational());
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.isOperational());
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        TestDto retrieved = testDataManagerWithMockNetworkUtil.getData(dataKey, type);
        assertEquals(testDto, retrieved);
        mockNetworkUtil.setNetworkState(true);
        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);
        Thread.sleep(DELAY_MS);
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
    }

    public void testWriteWhenDeviceOfflineQueuesUpRequests() throws IOException, InterruptedException, ServiceNotAvailableException {
        Type type = new TypeToken<TestDto>() {
        }.getType();
        DataKey key2 = new DataKey(dataKey.getType(), dataKey.getId() + "2");
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(getContext(), rootUrl, new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(getContext(), rootUrl, mockNetworkUtilForHttp));

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));

        mockNetworkUtilForQueue.setNetworkState(false);
        mockNetworkUtilForHttp.setNetworkState(false);

        testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        Thread.sleep(DELAY_MS);

        testDataManagerWithMockNetworkUtil.writeData(key2, testDto, type);
        Thread.sleep(DELAY_MS);

        mockNetworkUtilForHttp.setNetworkState(true);
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));
        mockNetworkUtilForHttp.setNetworkState(false);
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(key2));

        // Open Http first so that the worker threads don't start until HttpDataManager is ready
        mockNetworkUtilForHttp.setNetworkState(true);
        mockNetworkUtilForQueue.setNetworkState(true);
        Thread.sleep(DELAY_MS);

        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(key2));

        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);
        testDataManagerWithMockNetworkUtil.deleteIfExists(key2);
        Thread.sleep(DELAY_MS);
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));
    }

    public void testDeleteWhenDeviceOfflineQueuesUpRequests() throws IOException, InterruptedException, ServiceNotAvailableException {
        Type type = new TypeToken<TestDto>() {
        }.getType();
        DataKey key2 = new DataKey(dataKey.getType(), dataKey.getId() + "2");
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(getContext(), rootUrl, new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(getContext(), rootUrl, mockNetworkUtilForHttp));

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));

        testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        testDataManagerWithMockNetworkUtil.writeData(key2, testDto, type);
        Thread.sleep(DELAY_MS);

        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(key2));

        mockNetworkUtilForQueue.setNetworkState(false);
        mockNetworkUtilForHttp.setNetworkState(false);
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(key2));


        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);
        testDataManagerWithMockNetworkUtil.deleteIfExists(key2);
        Thread.sleep(DELAY_MS);

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));
        mockNetworkUtilForHttp.setNetworkState(true);
        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));

        mockNetworkUtilForQueue.setNetworkState(true);
        Thread.sleep(DELAY_MS);
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));
    }

    public void testQueueAndRequestDispatchOrdersAreSame() throws IOException, InterruptedException, ServiceNotAvailableException {
        Type type = new TypeToken<TestDto>() {
        }.getType();
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(getContext(), rootUrl, new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(getContext(), rootUrl, mockNetworkUtilForHttp));

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));

        mockNetworkUtilForQueue.setNetworkState(false);
        mockNetworkUtilForHttp.setNetworkState(false);

        testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);

        mockNetworkUtilForHttp.setNetworkState(true);
        mockNetworkUtilForQueue.setNetworkState(true);
        Thread.sleep(DELAY_MS);

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));

        mockNetworkUtilForQueue.setNetworkState(false);
        mockNetworkUtilForHttp.setNetworkState(false);

        // Queue up 10 requests with different DTOs
        for (int i = 0; i < 10; ++i) {
            testDto.setaNumber(testDto.getaNumber() + i);
            testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        }

        mockNetworkUtilForHttp.setNetworkState(true);
        mockNetworkUtilForQueue.setNetworkState(true);
        Thread.sleep(DELAY_MS * 10);

        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        TestDto retrieved = testDataManagerWithMockNetworkUtil.getData(dataKey, type);
        // Retrieved == last state of DTO
        assertEquals(testDto, retrieved);

        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);
        Thread.sleep(DELAY_MS);

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
    }
}
