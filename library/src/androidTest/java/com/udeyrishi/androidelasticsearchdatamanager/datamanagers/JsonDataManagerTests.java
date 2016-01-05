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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.JsonFormatter;
import com.udeyrishi.androidelasticsearchdatamanager.mocks.TestDto;

import junit.framework.TestCase;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by rishi on 15-10-29.
 */
public class JsonDataManagerTests extends TestCase {

    private JsonDataManager testDataManager;

    public void setUp() {
        testDataManager = new JsonDataManager(false) {
            @Override
            public boolean keyExists(DataKey key) {
                throw new RuntimeException("Not applicable for abstract classes.");
            }

            @Override
            public <T> T getData(DataKey key, Type typeOfT) {
                throw new RuntimeException("Not applicable for abstract classes.");
            }

            @Override
            public <T> void writeData(DataKey key, T obj, Type typeOfT) {
                throw new RuntimeException("Not applicable for abstract classes.");
            }

            @Override
            public void deleteIfExists(DataKey key) {
                throw new RuntimeException("Not applicable for abstract classes.");
            }

            @Override
            public boolean isOperational() {
                throw new RuntimeException("Not applicable for abstract classes.");
            }

            @Override
            public boolean requiresNetwork() {
                throw new RuntimeException("Not applicable for abstract classes.");
            }
        };
    }

    public void testSerialize() {
        HashMap<String, Integer> testObj = new HashMap<String, Integer>() {
            {
                put("One", 1);
                put("Two", 2);
            }
        };

        Type hashMapType = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        String json = testDataManager.serialize(testObj, hashMapType);
        Gson gson = new Gson();
        HashMap<String, Integer> deserialized = gson.fromJson(json, hashMapType);
        assertEquals(new Integer(1), deserialized.get("One"));
        assertEquals(new Integer(2), deserialized.get("Two"));
    }

    public void testDeserialize() {
        String testJson = "{'One' : 1, 'Two' : 2}";
        HashMap<String, Integer> deserialized = testDataManager.deserialize(testJson,
                new TypeToken<HashMap<String, Integer>>() {
                }.getType());
        assertEquals(new Integer(1), deserialized.get("One"));
        assertEquals(new Integer(2), deserialized.get("Two"));
    }

    public void testExplitExposeSerializationTrue() {
        TestDto obj = new TestDto(1, "One", true, "hidden");
        Type testDtoType = new TypeToken<TestDto>() {
        }.getType();

        String json = testDataManager.serialize(obj, testDtoType,
                new JsonFormatter(true, true));
        Gson gson = new Gson();
        TestDto deserialized = gson.fromJson(json, testDtoType);
        assertEquals(1, deserialized.getaNumber());
        assertEquals("One", deserialized.getaString());
        assertEquals(true, deserialized.getaBoolean());
        assertNull(deserialized.getaHiddenString());
    }

    public void testExplitExposeSerializationFalse() {
        TestDto obj = new TestDto(1, "One", true, "hidden");
        Type testDtoType = new TypeToken<TestDto>() {
        }.getType();

        String json = testDataManager.serialize(obj, testDtoType,
                new JsonFormatter(false, true));
        Gson gson = new Gson();
        TestDto deserialized = gson.fromJson(json, testDtoType);
        assertEquals(1, deserialized.getaNumber());
        assertEquals("One", deserialized.getaString());
        assertEquals(true, deserialized.getaBoolean());
        assertEquals("hidden", deserialized.getaHiddenString());
    }

    public void testExplitExposeDeserializationTrue() {
        String json = "{\"aNumber\" : 1, \"aString\" : \"One\", \"aBoolean\" : True, \"aHiddenString\" : \"hidden\"}";
        Type testDtoType = new TypeToken<TestDto>() {
        }.getType();
        TestDto deserialized = testDataManager.deserialize(json, testDtoType, new JsonFormatter(true, true));
        assertEquals(1, deserialized.getaNumber());
        assertEquals("One", deserialized.getaString());
        assertEquals(true, deserialized.getaBoolean());
        assertNull(deserialized.getaHiddenString());
    }

    public void testExplitExposeDeserializationFalse() {
        String json = "{\"aNumber\" : 1, \"aString\" : \"One\", \"aBoolean\" : True, \"aHiddenString\" : \"hidden\"}";
        Type testDtoType = new TypeToken<TestDto>() {
        }.getType();
        TestDto deserialized = testDataManager.deserialize(json, testDtoType, new JsonFormatter(false, true));
        assertEquals(1, deserialized.getaNumber());
        assertEquals("One", deserialized.getaString());
        assertEquals(true, deserialized.getaBoolean());
        assertEquals("hidden", deserialized.getaHiddenString());
    }
}
