package com.udeyrishi.androidelasticsearchdatamanager;

import com.google.gson.Gson;

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
