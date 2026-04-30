package com.liveclass.notification.domain.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReferenceData {

    private final Map<String, String> values;

    public ReferenceData(Map<String, String> values) {
        this.values = values != null ? new HashMap<>(values) : new HashMap<>();
    }

    public String get(String key) {
        return values.get(key);
    }

    public boolean hasKey(String key) {
        return values.containsKey(key);
    }

    public Map<String, String> toMap() {
        return Collections.unmodifiableMap(values);
    }
}
