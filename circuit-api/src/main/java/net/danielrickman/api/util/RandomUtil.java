package net.danielrickman.api.util;

import java.util.Random;

public class RandomUtil {

    public static <T> T randomFrom(T... items) {
        return items[new Random().nextInt(items.length)];
    }
}
