package dev.tommyjs.registore;

import dev.tommyjs.registore.encoder.RegistryEncoders;
import dev.tommyjs.registore.io.RegistryFormat;
import dev.tommyjs.registore.io.RegistryIO;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static dev.tommyjs.registore.util.TestUtil.createSample;

public class RegistryIOTest {

    @ParameterizedTest
    @MethodSource("arguments")
    public void IOTest(RegistryFormat format, boolean lazyLoad, boolean concurrent) throws IOException {
        RegistryIO<Integer, Integer> io = RegistryIO.builder(int.class, int.class)
            .setKeyEncoder(RegistryEncoders.INT)
            .setValueEncoder(RegistryEncoders.INT)
            .setFormat(format)
            .setLazyLoad(lazyLoad)
            .setConcurrent(concurrent)
            .build();

        File file = new File("tmp-" + ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE) + ".bin");

        try (FileOutputStream out = new FileOutputStream(file)) {
            io.write(createSample(), out);
        }

        Registry<Integer, Integer> registry;
        try (FileInputStream out = new FileInputStream(file)) {
            registry = io.read(out);
        }

        assert file.delete();

        assert Objects.equals(registry, createSample());
    }

    private static Stream<Arguments> arguments() {
        List<Arguments> arguments = new ArrayList<>();
        for (RegistryFormat format : RegistryFormat.values()) {
            for (boolean lazyLoad : new boolean[]{true, false}) {
                for (boolean concurrent : new boolean[]{true, false}) {
                    arguments.add(Arguments.of(format, lazyLoad, concurrent));
                }
            }
        }

        return arguments.stream();
    }

}
