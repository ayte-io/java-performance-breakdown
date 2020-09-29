package io.ayte.performance.breakdown.number.maximum.support;

import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;

import java.security.SecureRandom;
import java.util.Random;

@UtilityClass
public class Corpus {
    private final Random RANDOM = new SecureRandom();

    public final String SIZE_INITIALIZER = Integer.toString(Configuration.CORPUS_SIZE);
    public final int SIZE = Integer.parseInt(System.getProperty("pls no constant folding", SIZE_INITIALIZER));

    public int[][] random() {
        val values = new int[SIZE][2];
        for (var i = 0; i < SIZE; i++) {
            values[i][0] = RANDOM.nextInt();
            values[i][1] = RANDOM.nextInt();
        }
        return values;
    }

    public int[][] constant() {
        val values = new int[SIZE][2];
        val left = RANDOM.nextInt();
        val right = RANDOM.nextInt();
        for (var i = 0; i < SIZE; i++) {
            values[i][0] = left;
            values[i][1] = right;
        }
        return values;
    }

    public int[][] left() {
        val values = new int[SIZE][2];
        for (var i = 0; i < SIZE; i++) {
            val number = RANDOM.nextInt(Integer.MAX_VALUE - 1);
            values[i][0] = number + 1;
            values[i][1] = number;
        }
        return values;
    }

    public int[][] right() {
        val values = new int[SIZE][2];
        for (var i = 0; i < SIZE; i++) {
            val number = RANDOM.nextInt(Integer.MAX_VALUE - 1);
            values[i][0] = number;
            values[i][1] = number + 1;
        }
        return values;
    }

    /**
     * Returns a two-dimensional array of {@link #SIZE} size, which is
     * initialized with repeating pattern of {@code interval} two
     * element arrays, first {@code length} of which contain greater
     * number as zero element, and element with index one otherwise.
     */
    public int[][] tripping(int interval, int length) {
        val values = new int[SIZE][2];
        for (var i = 0; i < SIZE; i++) {
            val number = RANDOM.nextInt(Integer.MAX_VALUE - 1);
            val inverted = (i % interval) < length;

            values[i][0] = number;
            values[i][1] = inverted ? number - 1 : number + 1;
        }

        return values;
    }
}
