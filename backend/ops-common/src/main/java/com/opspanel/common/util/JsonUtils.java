package com.opspanel.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

/**
 * JSON utility class for OpsPanel.
 *
 * <p>Wrapper around Jackson's ObjectMapper providing thread-safe
 * serialization and deserialization.</p>
 *
 * <p>Supports LocalDateTime, null-safe conversions, and pretty printing.</p>
 *
 * @author OpsPanel
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // for Java 8 time types
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private JsonUtils() {
        // Prevent instantiation
    }

    /** Serialize an object to JSON string. */
    public static String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /** Serialize with pretty printing. */
    public static String toPrettyJson(Object obj) {
        if (obj == null) return null;
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON (pretty)", e);
        }
    }

    /** Deserialize JSON string to an object of given type. */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON: " + json, e);
        }
    }

    /** Deserialize JSON string to a generic type (List, Map, etc.). */
    public static <T> T fromJson(String json, JavaType type) {
        if (json == null || json.isEmpty()) return null;
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to generic type", e);
        }
    }

    /** Get the shared ObjectMapper for advanced use. */
    public static ObjectMapper mapper() {
        return MAPPER;
    }
}
