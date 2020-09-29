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
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark that in fact does the same as {@link Math#max(int, int)},
 * but preserves branching, thus allowing to get an overview of it's
 * performance and branch misprediction penalties.
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
public class IntentionalBranching {
    @Param({
            "constant",
            "random",
            "1:0",
            "2:1",
            "4:1", "4:2",
            "8:1", "8:2", "8:4",
            "32:1", "32:2", "32:4", "32:8",
            "128:1", "128:4", "128:16", "128:64",
            "512:1", "512:16", "512:32", "512:128"
    })
    public String configuration;

    private int[][] corpus;

    @Setup
    public void setUp() {
        if (configuration.equals("constant")) {
            corpus = Corpus.constant();
            return;
        }

        if (configuration.equals("random")) {
            corpus = Corpus.random();
            return;
        }

        String[] split = configuration.split(":");
        corpus = Corpus.tripping(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    @SuppressWarnings({"squid:S1121", "ManualMinMaxCalculation"})
    @Benchmark
    public void run(Blackhole blackhole) {
        for (var i = 0; i < Corpus.SIZE; i++) {
            if (corpus[i][0] > corpus[i][1]) {
                blackhole.consume(corpus[i][0]);
            } else {
                blackhole.consume(corpus[i][1]);
            }
        }
    }
}
