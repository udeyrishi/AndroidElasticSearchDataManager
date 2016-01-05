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
import com.google.gson.JsonSerializer;
import com.udeyrishi.androidelasticsearchdatamanager.JsonFormatter;

import java.lang.reflect.Type;

/**
 * An abstract implementation of {@link DataManager} that stores the objects as JSONs to a storage
 * media.
 * Created by rishi on 15-10-29.
 */
public abstract class JsonDataManager implements DataManager {

    protected final JsonFormatter jsonFormatter;

    /**
     * Creates an instance of {@link JsonDataManager}.
     *
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public JsonDataManager(boolean useExplicitExposeAnnotation) {
        this.jsonFormatter = new JsonFormatter(useExplicitExposeAnnotation, true);
    }

    /**
     * Creates an instance of {@link JsonDataManager}. The manager will set the "useExplicitExposeAnnotation"
     * value to false.
     */
    public JsonDataManager() {
        this(false);
    }

    /**
     * Registers a custom {@link JsonSerializer} for an object of custom type.
     *
     * @param classOfObject The {@link Class} of the object to be serialized.
     * @param serializer    The custom {@link JsonSerializer} to be used for this type.
     */
    public void registerSerializer(Class<?> classOfObject, JsonSerializer<?> serializer) {
        jsonFormatter.registerSerializer(classOfObject, serializer);
    }

    /**
     * Serializes the passed object to a JSON string. A {@link JsonFormatter} with "useExplicitExposeAnnotation"
     * set to the construction time value, and "usePrettyJson" value set to true will be used.
     *
     * @param obj  The object to be serialized.
     * @param type The runtime {@link Type} of the object.
     * @return The serialized JSON.
     */
    protected String serialize(Object obj, Type type) {
        return serialize(obj, type, jsonFormatter);
    }

    /**
     * Serializes the passed object to a JSON string.
     *
     * @param obj    The object to be serialized.
     * @param type   The runtime {@link Type} of the object.
     * @param format The {@link JsonFormatter} to be used for serialization.
     * @return The serialized JSON.
     */
    protected String serialize(Object obj, Type type, JsonFormatter format) {
        Gson gson = format.getGson();
        return gson.toJson(obj, type);
    }

    /**
     * De-serializes the JSON to a Java object. A {@link JsonFormatter} with "useExplicitExposeAnnotation"
     * set to the construction time value, and "usePrettyJson" value set to true will be used.
     *
     * @param obj     The JSON string.
     * @param typeOfT The Java object's {@link Type}
     * @param <T>     The generic type param corresponding to the parameter "typeOfT"
     * @return The de-serialized object.
     */
    protected <T> T deserialize(String obj, Type typeOfT) {
        return deserialize(obj, typeOfT, jsonFormatter);
    }

    /**
     * De-serializes the JSON to a Java object.
     *
     * @param obj     The JSON string.
     * @param typeOfT The Java object's {@link Type}
     * @param format  The {@link JsonFormatter} to be used for de-serialization.
     * @param <T>     The generic type param corresponding to the parameter "typeOfT"
     * @return The de-serialized object.
     */
    protected <T> T deserialize(String obj, Type typeOfT, JsonFormatter format) {
        Gson gson = format.getGson();
        return gson.fromJson(obj, typeOfT);
    }
}
