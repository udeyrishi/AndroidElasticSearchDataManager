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

import android.test.AndroidTestCase;

import com.google.gson.reflect.TypeToken;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.ExceptionUtils;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.DataKeyNotFoundException;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import com.udeyrishi.androidelasticsearchdatamanager.mocks.TestDto;

import junit.framework.AssertionFailedError;

import java.io.IOException;

import static com.udeyrishi.androidelasticsearchdatamanager.ExceptionAsserter.assertThrowsException;


/**
 * Created by rishi on 15-10-30.
 */
// Source: http://developer.android.com/reference/android/test/AndroidTestCase.html
// Date: 30-Oct-15
public abstract class BaseDataManagerTests<T extends DataManager> extends AndroidTestCase {
    protected T dataManager;
    protected TestDto testDto;
    protected DataKey dataKey;

    protected abstract T createNewDataManager();

    public void setUp() {
        dataManager = createNewDataManager();
        testDto = new TestDto(100, "Hundred", false, "a hidden string");
        dataKey = new DataKey("testdto", "123");
    }

    protected void keyExistsTest(int requestDelay) {
        try {
            assertFalse(dataManager.keyExists(dataKey));
            dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {
            }.getType());
            Thread.sleep(requestDelay);
            assertTrue(dataManager.keyExists(dataKey));
            assertFalse(dataManager.keyExists(new DataKey("not", "exists")));
            dataManager.deleteIfExists(dataKey);
            Thread.sleep(requestDelay);
        } catch (IOException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        } catch (InterruptedException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        } catch (ServiceNotAvailableException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    protected void keyExistsTest() {
        keyExistsTest(0);
    }

    protected void getDataWhenKeyDoesNotExistThrowsExceptionTest() {
        try {
            assertFalse(dataManager.keyExists(dataKey));
            assertThrowsException(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataManager.getData(dataKey, new TypeToken<TestDto>() {
                        }.getType());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ServiceNotAvailableException e) {
                        throw new RuntimeException("App is offline.", e);
                    }
                }
            }, DataKeyNotFoundException.class);
        } catch (IOException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        } catch (ServiceNotAvailableException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    protected void writeDataTest(int requestDelay) {
        try {
            dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {
            }.getType());
            Thread.sleep(requestDelay);
            assertTrue(dataManager.keyExists(dataKey));
            TestDto receivedData = dataManager.getData(dataKey, new TypeToken<TestDto>() {
            }.getType());
            assertEquals(testDto, receivedData);
            dataManager.deleteIfExists(dataKey);
            Thread.sleep(requestDelay);
        } catch (IOException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        } catch (InterruptedException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        } catch (ServiceNotAvailableException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    protected void writeDataTest() {
        writeDataTest(0);
    }

    protected void deleteTest(int requestDelay) {
        try {
            assertFalse(dataManager.keyExists(dataKey));
            dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {
            }.getType());
            Thread.sleep(requestDelay);
            assertTrue(dataManager.keyExists(dataKey));
            dataManager.deleteIfExists(dataKey);
            Thread.sleep(requestDelay);
            assertFalse(dataManager.keyExists(dataKey));
        } catch (IOException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        } catch (InterruptedException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        } catch (ServiceNotAvailableException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    protected void deleteTest() {
        deleteTest(0);
    }

    protected void isOperationalTest() {
        assertTrue(dataManager.isOperational());
    }
}
