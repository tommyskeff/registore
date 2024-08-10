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
import java.util.ArrayList;
import java.util.List;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@Fork(1)
@Warmup(time = 1, iterations = 3)
@Measurement(time = 3)
public class ReadBenchmark {

    @Param({"1", "10", "100", "1000", "10000", "100000", "1000000"})
    private int size;

    @Param({"true", "false"})
    private boolean lazyLoad;

    private Registry<Integer, Integer> data;
    private int[] keys;
    private long i;

    @Setup
    public void setup() throws IOException {
        RegistryReader<Integer, Integer> reader = new HashRegistryReader<>(RegistryEncoders.INT, RegistryEncoders.INT, lazyLoad, false, 10);
        try (InputStream stream = new FileInputStream("samples/single-sample-data-" + size + ".bin")) {
            data = reader.read(stream);
        }

        List<Integer> pool = new ArrayList<>(data.keySet());
        keys = new int[pool.size() / 10];

        for (int j = 0; j < keys.length; j += 2) {
            keys[j] = pool.get(j / 3);
        }

        for (int j = 1; j < keys.length; j += 2) {
            keys[j] = Integer.MAX_VALUE - j;
        }
    }

    @Benchmark
    public void get(Blackhole blackhole) {
        blackhole.consume(data.get(keys[(int) (i++ % keys.length)]));
    }

}
