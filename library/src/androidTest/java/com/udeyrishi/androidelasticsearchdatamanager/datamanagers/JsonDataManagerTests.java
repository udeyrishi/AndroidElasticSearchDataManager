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
