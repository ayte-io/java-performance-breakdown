# Ayte :: Performance :: Breakdown

This repository is basically a huge [JMH][] project devoted to find 
answers for questions of zero importance like "are streams significantly 
slower than manual unrolling?" and "what's the penalty of using 
optionals?"

## Launching

```
bin/run <suite name> <list of benchmarks...>
```

Where suite name is a package relative to `io.ayte.performance.breakdown`,
and benchmarks are class names relative to that package, e.g.:

```
bin/run number.maximum LoopBaseline CoreMaximum InlinedBitwiseMaximum
```

Some benchmarks are already configured and exposed as executable bash
scripts in `bin/benchmark/`

  [JMH]: http://openjdk.java.net/projects/code-tools/jmh/
