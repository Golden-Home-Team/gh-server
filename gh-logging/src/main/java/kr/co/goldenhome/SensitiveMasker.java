package kr.co.goldenhome;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.*;

public final class SensitiveMasker {

    private static final Set<String> SENSITIVE_KEYS = Set.of("password", "newpassword", "confirmpassword", "accesstoken", "refreshtoken", "authorization");
    private static final String MASK_VALUE = "***************";

    public static String mask(String body, ObjectMapper objectMapper) {
        if (body == null || body.isBlank()) {
            return body;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(body);

            if (rootNode.isObject() || rootNode.isArray()) {
                maskJsonNodeRecursively(rootNode);
            }

            return objectMapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            return "";
        }
    }

    public static Map<String, String> maskMap(Map<String, String> data) {
        if (data == null || data.isEmpty()) {
            return data;
        }

        Map<String, String> maskedMap = new HashMap<>();
        data.forEach((key, value) -> {
            if (SENSITIVE_KEYS.contains(key.toLowerCase())) {
                maskedMap.put(key, MASK_VALUE);
            } else {
                maskedMap.put(key, value);
            }
        });
        return maskedMap;
    }

    private static void maskJsonNodeRecursively(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            List<String> fieldNames = new ArrayList<>();

            Iterator<String> it = objectNode.fieldNames();
            while (it.hasNext()) {
                fieldNames.add(it.next());
            }

            for (String fieldName : fieldNames) {
                JsonNode fieldValue = objectNode.get(fieldName);

                if (SENSITIVE_KEYS.contains(fieldName.toLowerCase())) {
                    objectNode.set(fieldName, new TextNode(MASK_VALUE));
                } else {
                    maskJsonNodeRecursively(fieldValue);
                }
            }
        } else if (node.isArray()) {
            for (JsonNode arrayElement : node) {
                maskJsonNodeRecursively(arrayElement);
            }
        }
    }

}
