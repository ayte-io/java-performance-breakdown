# Ayte :: Performance :: Breakdown

This repository is basically a huge [JMH][] project devoted to find 
answers for questions like "are streams significantly slower than 
manual unrolling?" and "what's the penalty of using optionals?"

## Building

It's pretty much Maven

```
./mvnw package
```

or just

```
bin/compile
```

## Launching

Harness (checking performance itself)

```
bin/harness <jmh parameters>
```

Viewing disassembly (requires hsdis installed)

```
bin/harness <FQCN>
```

  [JMH]: http://openjdk.java.net/projects/code-tools/jmh/
  