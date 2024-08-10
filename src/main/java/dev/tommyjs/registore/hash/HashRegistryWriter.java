package dev.tommyjs.registore.hash;

import dev.tommyjs.registore.Registry;
import dev.tommyjs.registore.encoder.RegistryEncoder;
import dev.tommyjs.registore.io.AbstractRegistryWriter;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static dev.tommyjs.registore.hash.HashRegistries.*;

public class HashRegistryWriter<K, V> extends AbstractRegistryWriter<K, V> {

    private final @NotNull RegistryEncoder<K> keyEncoder;
    private final @NotNull RegistryEncoder<V> valueEncoder;
    private final float expandFactor;

    private final int keySize;
    private final int valueSize;

    public HashRegistryWriter(@NotNull RegistryEncoder<K> keyEncoder, @NotNull RegistryEncoder<V> valueEncoder,
                              float expandFactor) {
        if (expandFactor < 1F) {
            throw new IllegalArgumentException("Illegal expand factor");
        }

        this.keySize = keyEncoder.getSize();
        this.valueSize = valueEncoder.getSize();

        if (keySize <= 0 || keySize > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Invalid key size " + keySize);
        }

        if (valueSize <= 0 || valueSize > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Invalid value size " + valueSize);
        }

        this.keyEncoder = keyEncoder;
        this.valueEncoder = valueEncoder;
        this.expandFactor = expandFactor;
    }

    public HashRegistryWriter(@NotNull RegistryEncoder<K> keyEncoder, @NotNull RegistryEncoder<V> valueEncoder) {
        this(keyEncoder, valueEncoder, DEFAULT_EXPAND_FACTOR);
    }

    @Override
    public void write(@NotNull Registry<K, V> registry, @NotNull OutputStream stream) throws IOException {
        int tableLength = (int) Math.floor(registry.size() * expandFactor);
        int entrySize = keySize + valueSize;
        byte[] table = new byte[tableLength * entrySize];
        byte[] presentSet = new byte[Math.ceilDiv(tableLength, 8)];
        Object[] keys = new Object[tableLength];

        int realSize = 0;
        for (Map.Entry<K, V> entry : registry.entrySet()) {
            K key = entry.getKey();
            byte[] keyData = keyEncoder.encode(key);
            byte[] valueData = valueEncoder.encode(entry.getValue());

            if (keyData.length != keySize) throw new IllegalArgumentException("Illegal encoded key size");
            if (valueData.length != valueSize) throw new IllegalArgumentException("Illegal encoded key size");

            int i = probe(key, keys);
            if (i < 0) {
                throw new IllegalArgumentException("Duplicate key " + key);
            }

            System.arraycopy(keyData, 0, table, i * entrySize, keySize);
            System.arraycopy(valueData, 0, table, i * entrySize + keySize, valueSize);

            keys[i] = key;
            presentSet[i / 8] |= (1 << (7 - (i % 8)));
            realSize++;
        }

        try (DataOutputStream data = new DataOutputStream(stream)) {
            data.writeInt(MAGIC_HEADER);
            data.writeShort(VERSION);
            data.writeInt(realSize);
            data.writeInt(tableLength);
            data.writeShort(keySize);
            data.writeShort(valueSize);
            data.write(table);
            data.write(presentSet);
            data.writeInt(MAGIC_FOOTER);
        }
    }

    public int probe(@NotNull K key, @NotNull Object[] keys) {
        int idx = ((keyEncoder.hash(key) % keys.length) + keys.length) % keys.length;
        while (true) {
            if (keys[idx] == null) {
                return idx;
            } else if (key.equals(keys[idx])) {
                return -1;
            } else if (++idx >= keys.length) {
                idx = 0;
            }
        }
    }

}
