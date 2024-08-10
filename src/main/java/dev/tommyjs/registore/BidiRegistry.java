package dev.tommyjs.registore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BidiRegistry<K, V> extends Registry<K, V> {

    @NotNull Registry<K, V> forwards();

    @NotNull Registry<V, K> backwards();

    @Nullable K getKey(@NotNull V value);

    @Nullable V getValue(@NotNull K key);

}
