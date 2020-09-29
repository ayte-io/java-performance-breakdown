package io.ayte.performance.breakdown.exception;

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

@Threads(4)
@State(Scope.Thread)
@BenchmarkMode({Mode.AverageTime, Mode.SampleTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(time = 10, iterations = 2)
@Warmup(iterations = 2)
@Fork(2)
public class ExceptionPenalty {
    private static final Exception STACKTRACED = new RuntimeException();
    private static final Exception EMPTY = new NoStackTrace();

    @Benchmark
    public void tryCatch(Blackhole blackhole) {
        try {
            throw STACKTRACED;
        } catch (Exception e) {
            blackhole.consume(e);
        }
    }

    @Benchmark
    public void tryCatchEmpty(Blackhole blackhole) {
        try {
            throw EMPTY;
        } catch (Exception e) {
            blackhole.consume(e);
        }
    }

    @Benchmark
    public void consume(Blackhole blackhole) {
        blackhole.consume(STACKTRACED);
    }

    @Benchmark
    public void create(Blackhole blackhole) {
        blackhole.consume(new RuntimeException());
    }

    @Benchmark
    public void createEmpty(Blackhole blackhole) {
        blackhole.consume(new NoStackTrace());
    }

    public static class NoStackTrace extends Exception {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
