package io.ayte.performance.breakdown.number.maximum.inspection;

import io.ayte.performance.breakdown.number.maximum.support.Configuration;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Threads(Configuration.THREADS)
@Fork(Configuration.INSPECTION_FORKS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(
        time = Configuration.INSPECTION_DURATION,
        iterations = Configuration.INSPECTION_ITERATIONS
)
@Warmup(
        time = Configuration.INSPECTION_DURATION,
        iterations = Configuration.INSPECTION_WARMUPS
)
public class CoreMaximumInspection {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Benchmark
    public void run(Blackhole blackhole) {
        blackhole.consume(Math.max(RANDOM.nextInt(), RANDOM.nextInt()));
    }
}
