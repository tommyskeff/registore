package dev.tommyjs.registore.io;

import dev.tommyjs.registore.Registry;
import dev.tommyjs.registore.encoder.RegistryEncoder;
import dev.tommyjs.registore.hash.HashRegistryReader;
import dev.tommyjs.registore.hash.HashRegistryWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class RegistryIOBuilder<K, V> {

    private final @NotNull Class<K> keyType;
    private final @NotNull Class<V> valueType;

    private RegistryEncoder<K> keyEncoder;
    private RegistryEncoder<V> valueEncoder;
    private RegistryFormat format = null;
    private boolean lazyLoad = false;
    private boolean concurrent = false;
    private int bucketSize = 10;

    RegistryIOBuilder(@NotNull Class<K> keyType, @NotNull Class<V> valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public @NotNull RegistryIOBuilder<K, V> setFormat(@NotNull RegistryFormat format) {
        this.format = format;
        return this;
    }

    public @NotNull RegistryIOBuilder<K, V> setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
        return this;
    }

    public @NotNull RegistryIOBuilder<K, V> setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
        return this;
    }

    public @NotNull RegistryIOBuilder<K, V> setKeyEncoder(@NotNull RegistryEncoder<K> keyEncoder) {
        this.keyEncoder = keyEncoder;
        return this;
    }

    public @NotNull RegistryIOBuilder<K, V> setValueEncoder(@NotNull RegistryEncoder<V> valueEncoder) {
        this.valueEncoder = valueEncoder;
        return this;
    }

    public @NotNull RegistryIOBuilder<K, V> setBucketSize(int bucketSize) {
        this.bucketSize = bucketSize;
        return this;
    }

    public @NotNull RegistryIO<K, V> build() {
        if (keyEncoder == null || valueEncoder == null) {
            throw new IllegalStateException("Key and value encoders must be set");
        }

        RegistryReader<K, V> reader;
        RegistryWriter<K, V> writer;

        switch (Objects.requireNonNullElse(format, RegistryFormat.HASH_REGISTRY)) {
            case HASH_REGISTRY -> {
                reader = new HashRegistryReader<>(keyEncoder, valueEncoder, lazyLoad, concurrent, bucketSize);
                writer = new HashRegistryWriter<>(keyEncoder, valueEncoder);
            }
            default -> throw new IllegalStateException();
        }

        RegistryReader<K, V> finalReader = reader;
        RegistryWriter<K, V> finalWriter = writer;

        return new RegistryIO<>() {
            @Override
            public @NotNull Registry<K, V> read(@NotNull InputStream stream) throws IOException {
                return finalReader.read(stream);
            }

            @Override
            public @NotNull Registry<K, V> read(@NotNull ByteBuffer buffer) throws IOException {
                return finalReader.read(buffer);
            }

            @Override
            public void write(@NotNull Registry<K, V> registry, @NotNull OutputStream stream) throws IOException {
                finalWriter.write(registry, stream);
            }

            @Override
            public void write(@NotNull Registry<K, V> registry, @NotNull ByteBuffer buffer) throws IOException {
                finalWriter.write(registry, buffer);
            }

            @Override
            public void write(@NotNull Map<K, V> map, @NotNull OutputStream stream) throws IOException {
                finalWriter.write(map, stream);
            }

            @Override
            public void write(@NotNull Map<K, V> map, @NotNull ByteBuffer buffer) throws IOException {
                finalWriter.write(map, buffer);
            }
        };
    }

}
