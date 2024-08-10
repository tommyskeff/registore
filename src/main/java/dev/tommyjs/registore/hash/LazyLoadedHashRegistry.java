package dev.tommyjs.registore.hash;

import dev.tommyjs.registore.encoder.RegistryEncoder;
import dev.tommyjs.registore.impl.AbstractRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

import static dev.tommyjs.registore.hash.HashRegistries.MAX_DATA_SIZE;

public class LazyLoadedHashRegistry<K, V> extends AbstractRegistry<K, V> {

    private final transient @NotNull RegistryEncoder<K> keyEncoder;
    private final transient @NotNull RegistryEncoder<V> valueEncoder;
    private final transient int tableLength;
    private final transient int entrySize;

    private final int realSize;
    private final int keySize;
    private final int valueSize;
    private final byte[] table;
    private final byte[] presentSet;

    LazyLoadedHashRegistry(@NotNull RegistryEncoder<K> keyEncoder, @NotNull RegistryEncoder<V> valueEncoder,
                           byte[] table, byte[] presentSet, int realSize) {
        this.realSize = realSize;
        this.keyEncoder = keyEncoder;
        this.valueEncoder = valueEncoder;
        this.keySize = keyEncoder.getSize();
        this.valueSize = valueEncoder.getSize();
        this.entrySize = keySize + valueSize;

        checkSizes();

        if (table.length < 1) {
            throw new IllegalArgumentException("Empty table");
        }

        if (table.length % entrySize > 0) {
            throw new IllegalArgumentException("Invalid size for table");
        }

        this.tableLength = table.length / entrySize;

        if (realSize < 0 || realSize > tableLength) {
            throw new IllegalArgumentException("Illegal real size");
        }

        if (Math.ceilDiv(tableLength, 8) != presentSet.length) {
            throw new IllegalArgumentException("Invalid size for present set");
        }

        this.table = table;
        this.presentSet = presentSet;
    }

    private void checkSizes() {
        if (keySize < 0 || keySize > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Invalid key size " + keySize);
        }

        if (valueSize < 0 || valueSize > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Invalid value size " + valueSize);
        }
    }

    private @NotNull K decodeKey(int idx) {
        return keyEncoder.decode(Arrays.copyOfRange(table, idx * entrySize, idx * entrySize + keySize));
    }

    private @NotNull V decodeValue(int idx) {
        return valueEncoder.decode(Arrays.copyOfRange(table, idx * entrySize + keySize, (idx + 1) * entrySize));
    }

    @Override
    public int size() {
        return realSize;
    }

    @Override
    public boolean has(@NotNull K key) {
        return probe(key) >= 0;
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        int idx = probe(key);
        return idx >= 0 ? decodeValue(idx) : null;
    }

    @Override
    public @NotNull Set<K> keySet() {
        return new HashRegistrySet<>(KeyIterator::new);
    }

    @Override
    public @NotNull Collection<V> values() {
        return new HashRegistrySet<>(ValueIterator::new);
    }

    @Override
    public @NotNull Set<Map.Entry<K, V>> entrySet() {
        return new HashRegistrySet<>(EntryIterator::new);
    }

    public int probe(@NotNull K key) {
        int idx = ((keyEncoder.hash(key) % tableLength) + tableLength) % tableLength;
        while (true) {
            if ((presentSet[idx / 8] & (1 << (7 - (idx % 8)))) == 0) {
                return -1;
            } else if (key.equals(decodeKey(idx))) {
                return idx;
            } else if (++idx >= tableLength) {
                idx = 0;
            }
        }
    }

    private class HashRegistrySet<T> extends AbstractSet<T> {

        private final @NotNull Supplier<Iterator<T>> supplier;

        public HashRegistrySet(@NotNull Supplier<Iterator<T>> supplier) {
            this.supplier = supplier;
        }

        @Override
        public Iterator<T> iterator() {
            return supplier.get();
        }

        @Override
        public int size() {
            return LazyLoadedHashRegistry.this.size();
        }

    }

    private abstract class HashRegistryIterator<T> implements Iterator<T> {

        private int i = -1;

        protected abstract @NotNull T get(int idx);

        @Override
        public boolean hasNext() {
            while (true) {
                if ((i + 1) >= tableLength) {
                    return false;
                } else if ((presentSet[(i + 1) / 8] & (1 << (7 - ((i + 1) % 8)))) > 0) {
                    return true;
                } else {
                    i++;
                }
            }
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return get(++i);
        }

    }

    private class KeyIterator extends HashRegistryIterator<K> {

        @Override
        protected @NotNull K get(int idx) {
            return decodeKey(idx);
        }

    }

    private class ValueIterator extends HashRegistryIterator<V> {

        @Override
        protected @NotNull V get(int idx) {
            return decodeValue(idx);
        }

    }

    private class EntryIterator extends HashRegistryIterator<Map.Entry<K, V>> {

        @Override
        protected @NotNull Map.Entry<K, V> get(int idx) {
            return new AbstractMap.SimpleEntry<>(decodeKey(idx), decodeValue(idx));
        }

    }

}
