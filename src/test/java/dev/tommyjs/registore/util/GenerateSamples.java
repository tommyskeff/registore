package dev.tommyjs.registore.util;

import dev.tommyjs.registore.MutableRegistry;
import dev.tommyjs.registore.encoder.RegistryEncoders;
import dev.tommyjs.registore.hash.HashRegistry;
import dev.tommyjs.registore.hash.HashRegistryWriter;
import dev.tommyjs.registore.io.RegistryWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class GenerateSamples {

    private static final int[] SIZES = {1, 10, 100, 1000, 10000, 100000, 1000000};

    public static void main(String[] args) {
        generateSingleSamples();
    }

    public static void generateSingleSamples() {
        Random random = new Random();
        MutableRegistry<Integer, Integer> registry = new HashRegistry<>();

        for (int size : SIZES) {
            for (int i = 0; i < size; i++) {
                registry.set(random.nextInt(), random.nextInt());
            }

            RegistryWriter<Integer, Integer> writer = new HashRegistryWriter<>(RegistryEncoders.INT, RegistryEncoders.INT);
            try (OutputStream stream = new FileOutputStream("samples/single-sample-data-" + size + ".bin")) {
                writer.write(registry, stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
