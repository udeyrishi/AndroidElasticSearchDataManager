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

package com.udeyrishi.androidelasticsearchdatamanager.mocks;

import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.JsonDataManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;


/**
 * Created by rishi on 15-11-08.
 */
public class InMemoryDataManager extends JsonDataManager {

    private boolean isOperational = true;
    private HashMap<String, Object> inMemoryDataRepository = new HashMap<>();

    @Override
    public boolean keyExists(DataKey key) throws IOException {
        return inMemoryDataRepository.containsKey(key.toString());
    }

    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException {
        return (T) inMemoryDataRepository.get(key.toString());
    }

    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException {
        inMemoryDataRepository.put(key.toString(), obj);
    }

    @Override
    public void deleteIfExists(DataKey key) throws IOException {
        inMemoryDataRepository.remove(key.toString());
    }

    @Override
    public boolean isOperational() {
        return isOperational;
    }

    @Override
    public boolean requiresNetwork() {
        return false;
    }

    public void setIsOperational(boolean isOperational) {
        this.isOperational = isOperational;
    }
}
