package dev.tommyjs.registore;

import org.jetbrains.annotations.NotNull;

public interface MutableRegistry<K, V> extends Registry<K, V> {

    void set(@NotNull K key, @NotNull V value);

    void remove(@NotNull K key);

    void clear();

}
