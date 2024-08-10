package dev.tommyjs.registore;

import dev.tommyjs.registore.compat.MapCompat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Registry<K, V> {

    int size();

    boolean has(@NotNull K key);

    @Nullable V get(@NotNull K key);

    @NotNull Set<K> keySet();

    @NotNull Collection<V> values();

    @NotNull Set<Map.Entry<K, V>> entrySet();

    default @NotNull Map<K, V> toMap() {
        return MapCompat.convert(this);
    }

    static <K, V> @NotNull Registry<K, V> fromMap(@NotNull Map<K, V> map) {
        return MapCompat.convert(map);
    }

}
