package dev.tommyjs.registore.hash;

import dev.tommyjs.registore.MutableRegistry;
import dev.tommyjs.registore.Registry;
import dev.tommyjs.registore.encoder.RegistryEncoder;
import dev.tommyjs.registore.io.AbstractRegistryReader;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static dev.tommyjs.registore.hash.HashRegistries.*;

public class HashRegistryReader<K, V> extends AbstractRegistryReader<K, V> {

    private final @NotNull RegistryEncoder<K> keyEncoder;
    private final @NotNull RegistryEncoder<V> valueEncoder;
    private final boolean lazyLoad;
    private final boolean concurrent;
    private final int bucketSize;

    private final int keySize;
    private final int valueSize;

    public HashRegistryReader(@NotNull RegistryEncoder<K> keyEncoder, @NotNull RegistryEncoder<V> valueEncoder,
                              boolean lazyLoad, boolean concurrent, int bucketSize) {
        this.lazyLoad = lazyLoad;
        this.keySize = keyEncoder.getSize();
        this.valueSize = valueEncoder.getSize();
        this.concurrent = concurrent;
        this.bucketSize = bucketSize;

        if (keySize <= 0 || keySize > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Invalid key size " + keySize);
        }

        if (valueSize <= 0 || valueSize > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Invalid value size " + valueSize);
        }

        this.keyEncoder = keyEncoder;
        this.valueEncoder = valueEncoder;
    }

    @Override
    public @NotNull Registry<K, V> read(@NotNull InputStream stream) throws IOException {
        int entrySize = keySize + valueSize;
        int realSize;
        int tableLength;
        byte[] table;
        byte[] presentSet;

        try (DataInputStream data = new DataInputStream(stream)) {
            if (data.readInt() != MAGIC_HEADER) throw new IOException("Format does not match hash registry (HEADER)");
            if (data.readShort() != 1) throw new IOException("Unsupported format version");

            realSize = data.readInt();
            tableLength = data.readInt();

            if (data.readShort() != keySize) throw new IOException("Mismatching key size");
            if (data.readShort() != valueSize) throw new IOException("Mismatching value size");

            table = new byte[tableLength * entrySize];
            data.read(table);

            presentSet = new byte[Math.ceilDiv(tableLength, 8)];
            data.read(presentSet);

            if (data.readInt() != MAGIC_FOOTER) {
                throw new IOException("Format does not match hash registry (FOOTER)");
            }
        }

        if (lazyLoad) {
            return new LazyLoadedHashRegistry<>(keyEncoder, valueEncoder, table, presentSet, realSize);
        } else {
            MutableRegistry<K, V> registry = concurrent ? new ConcurrentHashRegistry<>() : new HashRegistry<>();
            for (int i = 0; i < tableLength; i++) {
                if ((presentSet[i / 8] & (1 << (7 - (i % 8)))) > 0) {
                    registry.set(
                        keyEncoder.decode(Arrays.copyOfRange(table, i * entrySize, i * entrySize + keySize)),
                        valueEncoder.decode(Arrays.copyOfRange(table, i * entrySize + keySize, (i + 1) * entrySize))
                    );
                }
            }

            return registry;
        }
    }

}
