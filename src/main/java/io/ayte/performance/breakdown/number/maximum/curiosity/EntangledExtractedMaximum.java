package io.ayte.performance.breakdown.number.maximum.curiosity;

import io.ayte.performance.breakdown.number.maximum.support.Configuration;
import io.ayte.performance.breakdown.number.maximum.support.Corpus;
import lombok.var;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Threads(Configuration.THREADS)
@Fork(Configuration.CURIOSITY_FORKS)
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(
        time = Configuration.ITERATION_DURATION,
        iterations = Configuration.CURIOSITY_ITERATIONS
)
@Warmup(
        time = Configuration.WARMUP_DURATION,
        iterations = Configuration.CURIOSITY_WARMUPS
)
public class EntangledExtractedMaximum {
    @Param({"true", "false"})
    public boolean constant;

    private int[][] corpus;

    @Setup
    public void setUp() {
        corpus = constant ? Corpus.constant() : Corpus.random();
    }

    @Benchmark
    public void run(Blackhole blackhole) {
        for (var i = 0; i < Corpus.SIZE; i++) {
            blackhole.consume(implementation(corpus[i][0], corpus[i][1]));
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private static int implementation(int left, int right) {
        return condition(left, right) ? left : right;
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private static boolean condition(int left, int right) {
        return left >= right;
    }
}
