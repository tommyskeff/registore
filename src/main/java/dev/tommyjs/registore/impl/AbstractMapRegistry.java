package dev.tommyjs.registore.impl;

import dev.tommyjs.registore.MutableRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMapRegistry<K, V> extends AbstractRegistry<K, V> implements MutableRegistry<K, V> {

    private final @NotNull Map<K, V> map;

    protected AbstractMapRegistry(@NotNull Map<K, V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean has(@NotNull K key) {
        return map.containsKey(key);
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        return map.get(key);
    }

    @Override
    public void set(@NotNull K key, @NotNull V value) {
        map.put(key, value);
    }

    @Override
    public void remove(@NotNull K key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public @NotNull Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public @NotNull Collection<V> values() {
        return map.values();
    }

    @Override
    public @NotNull Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public @NotNull Map<K, V> toMap() {
        return Collections.unmodifiableMap(map);
    }

}
