package com.hulkhiretech.payments.util;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility for JSON (de)serialization using Jackson.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JsonUtil {

    private final ObjectMapper objectMapper;

    /**
     * Convert JSON string to specified Java type. Returns null if conversion fails.
     *
     * @param jsonString JSON input
     * @param clazz      target class
     * @param <T>        type
     * @return deserialized object or null on error
     */
    public <T> T convertJsonToObject(String jsonString, Class<T> clazz) {
        if (jsonString == null) {
            log.warn("convertJsonToObject called with null jsonString");
            return null;
        }
        if (clazz == null) {
            log.warn("convertJsonToObject called with null clazz");
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            log.error("Failed to convert JSON to object for class {}: {}", clazz.getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * Convert object to JSON string. Returns null if conversion fails.
     *
     * @param obj input object
     * @return JSON string or null on error
     */
    public String convertObjectToJson(Object obj) {
        if (obj == null) {
            log.warn("convertObjectToJson called with null object");
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to JSON: {}", e.getMessage(), e);
            return null;
        }
    }
    
    public String prepareFormattedJsonString(String body) {
	    
	    if (body == null || body.isBlank()) {
	        return "";
	    }

	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();

		    // Parse JSON while preserving order
		    LinkedHashMap<String, Object> map =
		        objectMapper.readValue(body, LinkedHashMap.class);

		    // Serialize back to JSON
		    return objectMapper.writeValueAsString(map);
		} catch (Exception e) {
			log.error("Error while preparing formatted JSON string: ", e);
			return null;
		}
	}
    
}