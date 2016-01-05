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

package com.udeyrishi.androidelasticsearchdatamanager;

import com.google.gson.reflect.TypeToken;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
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
        return new QueuedDataManager(jobManager, getContext(), rootUrl);
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

        Type type = new TypeToken<TestDto>() {}.getType();

        MockNetworkUtil mockNetworkUtil = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtil)
                        .build()),
                new HttpDataManager(getContext(), rootUrl, mockNetworkUtil), getContext(), rootUrl);

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
        Type type = new TypeToken<TestDto>() {}.getType();
        DataKey key2 = new DataKey(dataKey.getType(), dataKey.getId() + "2");
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(getContext(), rootUrl, mockNetworkUtilForHttp), getContext(), rootUrl);

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
        Type type = new TypeToken<TestDto>() {}.getType();
        DataKey key2 = new DataKey(dataKey.getType(), dataKey.getId() + "2");
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(getContext(), rootUrl, mockNetworkUtilForHttp), getContext(), rootUrl);

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
        Type type = new TypeToken<TestDto>() {}.getType();
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(getContext(), rootUrl, mockNetworkUtilForHttp), getContext(), rootUrl);

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
