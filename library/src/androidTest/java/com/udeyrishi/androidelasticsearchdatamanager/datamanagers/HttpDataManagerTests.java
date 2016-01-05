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

import com.udeyrishi.androidelasticsearchdatamanager.mocks.MockNetworkUtil;

/**
 * Created by rishi on 15-10-30.
 */
public class HttpDataManagerTests extends BaseDataManagerTests<HttpDataManager> implements DataManagerApiTests {

    private final String rootUrl = "http://cmput301.softwareprocess.es:8080/cmput301f15t03/";

    @Override
    protected HttpDataManager createNewDataManager() {
        return new HttpDataManager(getContext(), rootUrl);
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

    public void testIsOperationalWhenNetworkOff() {
        MockNetworkUtil mockNetworkUtil = new MockNetworkUtil();
        HttpDataManager testDataManager = new HttpDataManager(getContext(), rootUrl, mockNetworkUtil);
        assertTrue(testDataManager.isOperational());

        mockNetworkUtil.setNetworkState(false);
        assertFalse(testDataManager.isOperational());
    }
}
