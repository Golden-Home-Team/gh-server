package kr.co.goldenhome;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class SensitiveMasker {

    private static final Set<String> SENSITIVE_KEYS = Set.of("password", "newpassword", "confirmpassword", "accesstoken", "refreshtoken");
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
