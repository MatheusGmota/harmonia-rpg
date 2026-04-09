package br.com.api.harmonia_rpg.tools;

import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public interface UpdateTool {

    static Map<String, Object> flattenMap(Map<String, Object> map, String prefix) {
        Map<String, Object> flattened = new HashMap<>();

        map.forEach((key, value) -> {
            String newKey = (prefix.isEmpty()) ? key : prefix + "." + key;

            if (value instanceof Map) {
                // Se o valor for outro mapa, chama a função recursivamente
                flattened.putAll(flattenMap((Map<String, Object>) value, newKey));
            } else {
                flattened.put(newKey, value);
            }
        });

        return flattened;
    }

    static Map<String, Object> filterValidFields(
            Map<String, Object> updates,
            Class<?> rootClass
    ) {
        Map<String, Object> validUpdates = new HashMap<>();

        updates.forEach((path, value) -> {
            if (isValidFieldPath(rootClass, path)) {
                validUpdates.put(path, value);
            }
        });

        if (validUpdates.isEmpty()) {
            throw new BusinessException("Nenhum campo válido para atualização");
        }

        return validUpdates;
    }

    private static boolean isValidFieldPath(Class<?> clazz, String path) {
        String[] parts = path.split("\\.");
        Class<?> currentClass = clazz;

        for (String part : parts) {
            Field field = ReflectionUtils.findField(currentClass, part);
            if (field == null) {
                return false;
            }
            currentClass = field.getType();
        }
        return true;
    }
}
