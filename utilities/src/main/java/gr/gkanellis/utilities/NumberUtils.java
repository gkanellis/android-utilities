package gr.gkanellis.utilities;

import java.util.Random;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static int getRandomInteger(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

}
