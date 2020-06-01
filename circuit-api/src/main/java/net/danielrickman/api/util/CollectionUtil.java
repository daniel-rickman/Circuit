package net.danielrickman.api.util;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class CollectionUtil {

    /*
    Returns a sorted list of a hashmap's values
     */
    public <K> List<Map.Entry<K, Integer>> orderByValue(Map<K, Integer> map, boolean putLowestFirst) {
        Stream<Map.Entry<K, Integer>> stream = map.entrySet().stream();
        if (putLowestFirst) {
            stream = stream.sorted(Map.Entry.comparingByValue());
        } else {
            stream = stream.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        }
        return stream.collect(Collectors.toList());
    }

    /*
    Returns the entries with the highest value
     */
    public <K> List<Map.Entry<K, Integer>> getTopEntriesByValue(Map<K, Integer> map) {
        List<Map.Entry<K, Integer>> topEntries = new ArrayList<>();
        for (Map.Entry<K, Integer> entry : map.entrySet()) {
            if (topEntries.isEmpty()) {
                topEntries.add(entry);
            } else {
                var newValue = entry.getValue();
                var existingValue = topEntries.get(0).getValue();
                if (newValue >= existingValue) {
                    if (newValue > existingValue) {
                        topEntries.clear();
                    }
                    topEntries.add(entry);
                }
            }
        }
        return topEntries;
    }

    /*
    Returns the entries with the highest number of elements
     */
    public <K, V> List<Map.Entry<K, Integer>> getTopEntriesByElements(Map<K, List<V>> map) {
        Map<K, Integer> sizeMap = new HashMap<>();
        map.forEach((k, list) -> sizeMap.put(k, list.size()));
        return getTopEntriesByValue(sizeMap);
    }
}