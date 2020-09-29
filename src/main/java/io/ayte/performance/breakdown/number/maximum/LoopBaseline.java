package io.ayte.performance.breakdown.number.maximum;

import io.ayte.performance.breakdown.number.maximum.support.Configuration;
import io.ayte.performance.breakdown.number.maximum.support.Corpus;
import lombok.var;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * This benchmarks is created to measure time taken by loop surrounding
 * {@link Math#max(int, int)} equivalent calls.
 */
@Threads(Configuration.THREADS)
@Fork(Configuration.CURIOSITY_FORKS)
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(
        time = Configuration.ITERATION_DURATION,
        iterations = Configuration.ITERATIONS
)
@Warmup(
        time = Configuration.WARMUP_DURATION,
        iterations = Configuration.WARMUPS
)
public class LoopBaseline {
    @Benchmark
    public void run(Blackhole blackhole) {
        for (var i = 0; i < Corpus.SIZE; i++) {
            blackhole.consume(0);
        }
    }
}
