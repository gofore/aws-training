package com.gofore.aws.workshop.common.functional;

import java.util.HashMap;
import java.util.Map;

public class Maps {
    
    public static <K, V> Map<K, V> of() {
        return new HashMap<>();
    }
    
    public static <K, V> Map<K, V> of(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    
    private Maps() {
        
    }
}
