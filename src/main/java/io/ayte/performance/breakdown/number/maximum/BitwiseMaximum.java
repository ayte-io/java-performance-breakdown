package io.ayte.performance.breakdown.number.maximum;

import io.ayte.performance.breakdown.number.maximum.support.Configuration;
import io.ayte.performance.breakdown.number.maximum.support.Corpus;
import lombok.val;
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

/**
 * A simple max(int, int) implementation using bitwise-only operations,
 * thus no branching, inlining disabled.
 */
@Threads(Configuration.THREADS)
@Fork(Configuration.FORKS)
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
public class BitwiseMaximum {
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
        // 0b11... if negative, 0b0 if positive
        val difference = (left - right) >> 31;
        // 0b11... if negative, 0b0 if positive
        val alpha = left >> 31;
        // 0b11... if negative, 0b0 if positive
        val beta = right >> 31;
        // 0b11... if arguments have different signs
        // &
        // 0b11... if difference and left have different signs
        val overflow = (alpha ^ beta) & (alpha ^ difference);
        // 0b11... if difference is negative and no overflow
        // or difference is positive and overflow present
        val mask = difference ^ overflow;
        // one of arguments is zeroed, while second one is preserved
        return (left & ~mask) | (right & mask);
    }
}
