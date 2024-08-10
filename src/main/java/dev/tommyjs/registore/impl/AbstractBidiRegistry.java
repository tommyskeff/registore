package dev.tommyjs.registore.impl;

import dev.tommyjs.registore.BidiRegistry;
import dev.tommyjs.registore.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractBidiRegistry<K, V> implements BidiRegistry<K, V> {

    private final @NotNull Registry<K, V> forwards;
    private final @NotNull Registry<V, K> backwards;

    protected AbstractBidiRegistry(@NotNull Registry<K, V> forwards, @NotNull Registry<V, K> backwards) {
        this.forwards = forwards;
        this.backwards = backwards;
    }

    @Override
    public @NotNull Registry<K, V> forwards() {
        return forwards;
    }

    @Override
    public @NotNull Registry<V, K> backwards() {
        return backwards;
    }

    @Override
    public @Nullable K getKey(@NotNull V value) {
        return backwards.get(value);
    }

    @Override
    public @Nullable V getValue(@NotNull K key) {
        return forwards.get(key);
    }

    @Override
    public int size() {
        return forwards.size();
    }

    @Override
    public boolean has(@NotNull K key) {
        return forwards.has(key);
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        return forwards.get(key);
    }

    @Override
    public @NotNull Set<K> keySet() {
        return forwards.keySet();
    }

    @Override
    public @NotNull Collection<V> values() {
        return forwards.values();
    }

    @Override
    public @NotNull Set<Map.Entry<K, V>> entrySet() {
        return forwards.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBidiRegistry<?, ?> that = (AbstractBidiRegistry<?, ?>) o;
        return Objects.equals(forwards, that.forwards) && Objects.equals(backwards, that.backwards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forwards, backwards);
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }

}
