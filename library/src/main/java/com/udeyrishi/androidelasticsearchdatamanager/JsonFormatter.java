package com.udeyrishi.androidelasticsearchdatamanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for specifying the JSON format the {@link JsonDataManager} should use.
 * Created by rishi on 15-10-30.
 */
public class JsonFormatter {
    private boolean useExplicitExposeAnnotation;
    private boolean usePrettyJson;
    private HashMap<Class<?>, JsonSerializer<?>> serializers = new HashMap<>();
    /**
     * Creates an instance of the {@link JsonFormatter}.
     *
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     * @param usePrettyJson               True, if the serialized JSON needs to be in "pretty" format (with
     *                                    appropriate newlines and indentations). False, if the JSON needs to be
     *                                    in a single line.
     */
    public JsonFormatter(boolean useExplicitExposeAnnotation, boolean usePrettyJson) {
        setUseExplicitExposeAnnotation(useExplicitExposeAnnotation);
        setUsePrettyJson(usePrettyJson);
    }

    /**
     * Gets the {@link JsonFormatter}'s current "use explicit expose annotation" setting. If this is
     * set to true, only the fields with the annotation @expose will be serialized/de-serialized.
     *
     * @return The current "use explicit expose annotation" setting.
     */
    public boolean getUseExplicitExposeAnnotation() {
        return useExplicitExposeAnnotation;
    }

    /**
     * Sets the {@link JsonFormatter}'s current "use explicit expose annotation" setting. If this is
     * set to true, only the fields with the annotation @expose will be serialized/de-serialized.
     *
     * @param useExplicitExposeAnnotation The new "use explicit expose annotation" setting.
     */
    public void setUseExplicitExposeAnnotation(boolean useExplicitExposeAnnotation) {
        this.useExplicitExposeAnnotation = useExplicitExposeAnnotation;
    }

    /**
     * Gets the {@link JsonFormatter}'s current "use pretty JSON" setting.
     *
     * @return True, if the serialized JSON needs to be in "pretty" format (with appropriate
     * newlines and indentations). False, if the JSON needs to be in a single line.
     */
    public boolean getUsePrettyJson() {
        return usePrettyJson;
    }

    /**
     * Sets the {@link JsonFormatter}'s current "use pretty JSON" setting.
     *
     * @param usePrettyJson True, if the serialized JSON needs to be in "pretty" format (with appropriate
     *                      newlines and indentations). False, if the JSON needs to be in a single line.
     */
    public void setUsePrettyJson(boolean usePrettyJson) {
        this.usePrettyJson = usePrettyJson;
    }

    /**
     * Gets the {@link Gson} object configured with the settings of this {@link JsonFormatter}.
     *
     * @return The properly configured {@link Gson}.
     */
    public Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        if (getUsePrettyJson()) {
            gsonBuilder.setPrettyPrinting();
        }

        if (getUseExplicitExposeAnnotation()) {
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        }

        // Register custom serializers
        for (Map.Entry<Class<?>, JsonSerializer<?>> entry : serializers.entrySet()) {
            gsonBuilder.registerTypeHierarchyAdapter(entry.getKey(), entry.getValue());
        }

        return gsonBuilder.create();
    }

    /**
     * Registers a type hierarchy adapter to be used when constructing the {@link Gson} object via
     * {@link JsonFormatter#getGson()} method.
     * @param classOfObject The {@link Class} of the object to be serialized.
     * @param serializer The custom {@link JsonSerializer} to be used for this type.
     */
    public void registerSerializer(Class<?> classOfObject, JsonSerializer<?> serializer) {
        serializers.put(classOfObject, serializer);
    }
}
