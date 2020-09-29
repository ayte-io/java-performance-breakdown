/**
 * So, if you look into {@link java.lang.Math#max(int, int)} source
 * code, you'll see the most expected expression:
 * {@code return left >= right ? left : right}.
 *
 * <p>
 * This code <em>may be</em> subject to branch prediction. While this
 * doesn't matter at all - i can't imagine workload that spends
 * significant time in {@code Math.max} - it's just interesting to test
 * whether bitwise implementation would be faster than this tiny
 * expression and what happens with same user-defined implementation,
 * since it should not be subject for intrinsic replacement.
 * </p>
 *
 * <p>
 * All benchmarks are implemented in same way. To test branch
 * prediction, particular implementation should be fed with a) input
 * that doesn't change often or doesn't change at all and b) random
 * input. The second one should be precalculated, otherwise random
 * generation would be included in benchmark, and it can weight more
 * than implementation itself. As a workaround, every benchmark iterates
 * precomputed corpus which has size of 32 768 pair of ints; on one side
 * i don't know how many calls are required for branch prediction to
 * start working, on the other one this should fit in my CPU cache.
 * Since both constant and random benchmarks are iterating over arrays,
 * there shouldn't be any skew in memory operations.
 * </p>
 *
 * <p>
 * Benchmark can be launched via following ways (paths are relative to
 * project root):
 *
 * <pre>
 * # just benchmark. compiler.options is required to work with inlining.
 * # remove print directives to get rid of disassembly, which may emit
 * # tens of megabytes.
 * java \
 *    -XX:+UnlockDiagnosticVMOptions \
 *    -XX:CompileCommandFile=src/main/resources/number/maximum/compiler.options \
 *    -jar target/io.ayte.performance.breakdown.jar \
 *    io.ayte.performance.breakdown.number.maximum
 *
 * # benchmark + hardware counters
 * java \
 *    -XX:+UnlockDiagnosticVMOptions \
 *    -XX:CompileCommandFile=src/main/resources/number/maximum/compiler.options \
 *    -jar target/io.ayte.performance.breakdown.jar \
 *    -prof perfnorm \
 *    io.ayte.performance.breakdown.number.maximum
 *
 * # benchmark + log compilation
 * java \
 *    -XX:+UnlockDiagnosticVMOptions \
 *    -XX:+LogCompilation -XX:LogFile=target/number.maximum.compilation.log \
 *    -XX:CompileCommandFile=src/main/resources/number/maximum/compiler.options \
 *    -jar target/io.ayte.performance.breakdown.jar \
 *    -prof perfnorm \
 *    io.ayte.performance.breakdown.number.maximum
 *
 * # disable C1 completely and get disassembly for C2 only
 * # this should be safe, since there's not much sense in (int, int)
 * # profiling
 * java \
 *    -XX:+UnlockDiagnosticVMOptions \
 *    -XX:-TieredCompilation \
 *    -XX:CompileCommandFile=src/main/resources/number/maximum/compiler.options \
 *    -jar target/io.ayte.performance.breakdown.jar \
 *    -prof perfnorm \
 *    io.ayte.performance.breakdown.number.maximum
 * </pre>
 * </p>
 *
 * <p>
 * Obtained results:
 *
 * <pre>
 * Benchmark                         Mode       Score    Error  Units
 * ------------------------------------------------------------------
 * LoopBaseline                      -          65.674 ± 0.023  us/op
 * CoreMaximum                       constant   88.596 ± 0.205  us/op
 * CoreMaximum                       random     88.568 ± 0.096  us/op
 * MaximumBaseline                   constant  104.173 ± 0.087  us/op
 * MaximumBaseline                   random    104.414 ± 0.350  us/op
 * BitwiseMaximum                    constant  120.357 ± 0.079  us/op
 * BitwiseMaximum                    random    120.667 ± 0.389  us/op
 * InlinedBitwiseMaximum             constant  104.652 ± 0.087  us/op
 * InlinedBitwiseMaximum             random    104.952 ± 0.332  us/op
 * ExtractedMaximum                  constant  111.125 ± 0.149  us/op
 * ExtractedMaximum                  random    110.896 ± 0.018  us/op
 * InlinedExtractedMaximum           constant   88.456 ± 0.091  us/op
 * InlinedExtractedMaximum           random     88.817 ± 0.189  us/op
 * NaiveMaximum                      constant   88.466 ± 0.112  us/op
 * NaiveMaximum                      random     88.366 ± 0.042  us/op
 * PrematurelyOptimizedNaiveMaximum  constant   88.372          us/op
 * PrematurelyOptimizedNaiveMaximum  random     88.394          us/op
 * EntangledExtractedMaximum         constant  140.545          us/op
 * EntangledExtractedMaximum         random    140.398          us/op
 * </pre>
 *
 * Full research can be found on
 * <a href="https://medium.com/p/f378c007e419">Medium</a>.
 * </p>
 */
package io.ayte.performance.breakdown.number.maximum;
