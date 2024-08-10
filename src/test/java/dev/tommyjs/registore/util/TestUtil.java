package dev.tommyjs.registore.util;

import dev.tommyjs.registore.Registry;
import dev.tommyjs.registore.encoder.RegistryEncoders;
import dev.tommyjs.registore.hash.HashRegistry;
import dev.tommyjs.registore.io.RegistryIO;
import dev.tommyjs.registore.io.RegistryReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestUtil {

    public static Map<Integer, Integer> createSampleData() {
        Map<Integer, Integer> data = new HashMap<>();

        for (int i = 0; i < 200; i += 9) {
            data.put(i, i % 3 * i % 17);
        }

        for (int i = 300; i > 50; i -= 11) {
            data.put(i, i % 3 * i % 23);
        }

        return data;
    }

    public static Registry<Integer, Integer> createSample() {
        return Registry.fromMap(createSampleData());
    }

    public static Registry<Integer, Integer> loadSample(boolean lazy) {
        RegistryReader<Integer, Integer> reader = RegistryIO.builder(int.class)
            .setLazyLoad(lazy)
            .setConcurrent(false)
            .setKeyEncoder(RegistryEncoders.INT)
            .setValueEncoder(RegistryEncoders.INT)
            .build();

        try {
            return reader.read(new FileInputStream("samples/single-sample-data-1000.bin"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
