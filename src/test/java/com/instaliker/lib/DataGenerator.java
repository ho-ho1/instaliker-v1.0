package com.instaliker.lib;

import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static final Random random = new Random();

    public static <T> T getRandomElement(List<T> list) {
        int randomInt = random.nextInt(list.size());
        return list.get(randomInt);
    }

    public static int getRandomPercent() {
        return random.nextInt(101);
    }

    public static int getIntBetween(int min, int max) {
        if (max - min == 0) {
            return min;
        }
        if (max - min < 0) {
            return getIntBetween(max, min);
        }
        return random.nextInt(max - min) + min;
    }

    public static void main(String[] args) {
        System.out.printf(String.valueOf(random.nextInt(0)));
    }
}
