package io.ayte.performance.breakdown.number.maximum.support;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Configuration {
    public final int FORKS = 5;
    public final int THREADS = 4;

    public final int DEFAULT_CYCLES = 10;
    public final int DEFAULT_DURATION = 30;

    public final int ITERATIONS = DEFAULT_CYCLES;
    public final int ITERATION_DURATION = DEFAULT_DURATION;

    public final int WARMUPS = DEFAULT_CYCLES;
    public final int WARMUP_DURATION = DEFAULT_DURATION;

    // 32 768 entries of two ints or 262 144 bytes (plus java headers!)
    // somewhere at this point benchmarks started showing expected 1-2%
    // deviations (as opposed to ~30%), which is probably related to
    // additional memory loads / CPU cache misses or cache line
    // overlaps / other shit i have vague idea about, but i never had
    // the time to go THAT deep.
    public final int CORPUS_SIZE = 1 << 15;

    public final int CURIOSITY_FORKS = 1;
    public final int CURIOSITY_WARMUPS = 2;
    public final int CURIOSITY_ITERATIONS = 2;

    public final int INSPECTION_FORKS = 1;
    public final int INSPECTION_WARMUPS = 0;
    public final int INSPECTION_ITERATIONS = 3;
    public final int INSPECTION_DURATION = 180;
}
