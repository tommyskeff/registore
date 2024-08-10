package dev.tommyjs.registore.benchmark;

import dev.tommyjs.registore.Registry;
import dev.tommyjs.registore.encoder.RegistryEncoders;
import dev.tommyjs.registore.hash.HashRegistryReader;
import dev.tommyjs.registore.io.RegistryReader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(time = 1, iterations = 3)
@Measurement(time = 3)
public class LoadBenchmark {

    @Param({"1", "10", "100", "1000", "10000", "100000", "1000000"})
    private int size;

    @Param({"true", "false"})
    private boolean lazyLoad;

    @Benchmark
    public void load(Blackhole blackhole) throws IOException {
        RegistryReader<Integer, Integer> reader = new HashRegistryReader<>(RegistryEncoders.INT, RegistryEncoders.INT, lazyLoad, false, 10);
        try (InputStream stream = new FileInputStream("samples/single-sample-data-" + size + ".bin")) {
            Registry<Integer, Integer> registry = reader.read(stream);
            blackhole.consume(registry);
        }
    }

}
