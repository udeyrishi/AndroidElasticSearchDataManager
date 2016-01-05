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
