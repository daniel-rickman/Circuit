package net.danielrickman.api.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Random;

@UtilityClass
public class RandomUtil {

    public <T> T randomFrom(T... items) {
        return items[new Random().nextInt(items.length)];
    }

    public <T> T randomFrom(List<T> items) {
        return items.get(new Random().nextInt(items.size()));
    }
}
