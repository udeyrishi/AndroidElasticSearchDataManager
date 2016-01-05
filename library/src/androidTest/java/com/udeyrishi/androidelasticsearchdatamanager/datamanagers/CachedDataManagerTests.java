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
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import com.udeyrishi.androidelasticsearchdatamanager.mocks.InMemoryDataManager;
import com.udeyrishi.androidelasticsearchdatamanager.mocks.TestDto;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.udeyrishi.androidelasticsearchdatamanager.ExceptionAsserter.assertThrowsException;

/**
 * Created by rishi on 15-10-31.
 */
public class CachedDataManagerTests extends BaseDataManagerTests<CachedDataManager> implements DataManagerApiTests {

    private final String rootUrl = "http://cmput301.softwareprocess.es:8080/cmput301f15t03/";

    @Override
    protected CachedDataManager createNewDataManager() {
        return new CachedDataManager(getContext(), new HttpDataManager(getContext(), rootUrl));
    }

    @Override
    public void testKeyExists() {
        super.keyExistsTest();
    }

    @Override
    public void testGetDataWhenKeyDoesNotExistThrowsException() {
        super.getDataWhenKeyDoesNotExistThrowsExceptionTest();
    }

    @Override
    public void testWriteData() {
        super.writeDataTest();
    }

    @Override
    public void testDelete() {
        super.deleteTest();
    }

    @Override
    public void testIsOperational() {
        super.isOperationalTest();
    }

    public void testWriteThenGetDataWhenInnerDataManagerNotAvailable() throws IOException, ServiceNotAvailableException {

        InMemoryDataManager mockDataManager = new InMemoryDataManager();
        CachedDataManager cachedDataManager = new CachedDataManager(getContext(), mockDataManager);

        Type type = new TypeToken<TestDto>() {
        }.getType();

        assertFalse(cachedDataManager.keyExists(dataKey));
        cachedDataManager.writeData(dataKey, testDto, type);
        assertTrue(cachedDataManager.innerManager.keyExists(dataKey));
        assertTrue(cachedDataManager.keyExists(dataKey));

        mockDataManager.setIsOperational(false);
        assertFalse(cachedDataManager.isOperational());
        assertTrue(cachedDataManager.keyExists(dataKey));
        TestDto retrieved = cachedDataManager.getData(dataKey, type);
        assertEquals(testDto, retrieved);
        mockDataManager.setIsOperational(true);
        cachedDataManager.deleteIfExists(dataKey);
        assertFalse(cachedDataManager.keyExists(dataKey));
    }

    public void testWriteWhenInnerManagerIsNotAvailableThrowsException() throws IOException, ServiceNotAvailableException {
        InMemoryDataManager mockDataManager = new InMemoryDataManager();
        final CachedDataManager cachedDataManager = new CachedDataManager(getContext(), mockDataManager);

        final Type type = new TypeToken<TestDto>() {
        }.getType();

        assertFalse(cachedDataManager.keyExists(dataKey));
        mockDataManager.setIsOperational(false);
        assertFalse(cachedDataManager.isOperational());
        assertThrowsException(new Runnable() {
            @Override
            public void run() {
                try {
                    cachedDataManager.writeData(dataKey, testDto, type); // throws
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException(e);
                }
            }
        }, ServiceNotAvailableException.class, true);
        assertFalse(cachedDataManager.keyExists(dataKey));
        mockDataManager.setIsOperational(true);
        assertTrue(cachedDataManager.isOperational());
        assertFalse(cachedDataManager.keyExists(dataKey));
    }
}
