package io.ayte.performance.breakdown.collection;

import lombok.val;
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
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * This benchmark tests nearly nothing - how much Arrays.asList() (and
 * additional calls) costs to populating a set rather than using for
 * constructs.
 *
 * Normally there shouldn't be any significant penalty, this suite is
 * added only to start the work.
 */
@Fork(warmups = 3, value = 1)
@Warmup(iterations = 3)
@Measurement(time = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class ArraysAsListPenaltyBenchmark {
    @Param({"256", "262144", "4194304"})
    private int size;

    private Integer[] elements;

    @Setup
    public void setUp() {
        elements = new Integer[size];
        for (int i = 0; i < size; i++) {
            elements[i] = i;
        }
    }

    @SuppressWarnings({"ManualArrayToCollectionCopy", "UseBulkOperation"})
    @Benchmark
    public void foreach(Blackhole blackhole) {
        val set = new HashSet<Integer>((int) (size / 0.75));
        for (val element : elements) {
            set.add(element);
        }
        blackhole.consume(set);
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    public void forVar(Blackhole blackhole) {
        val set = new HashSet<Integer>((int) (size / 0.75));
        // let compiler know we don't expect updates
        val limit = size;
        for (int i = 0; i < limit; i++) {
            set.add(elements[i]);
        }
        blackhole.consume(set);
    }

    @Benchmark
    public void wrapped(Blackhole blackhole) {
        blackhole.consume(new HashSet<>(Arrays.asList(elements)));
    }
}
