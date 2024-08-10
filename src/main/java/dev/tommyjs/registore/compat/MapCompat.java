package dev.tommyjs.registore.compat;

import dev.tommyjs.registore.Registry;
import dev.tommyjs.registore.impl.AbstractRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MapCompat {

    public static <K, V> Map<K, V> convert(@NotNull Registry<K, V> registry) {
        return new RegistryMap<>(registry);
    }

    public static <K, V> Registry<K, V> convert(@NotNull Map<K, V> map) {
        return new MapRegistry<>(map);
    }

    private static final class MapRegistry<K, V> extends AbstractRegistry<K, V> {

        private final @NotNull Map<K, V> map;

        public MapRegistry(@NotNull Map<K, V> map) {
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

    }

    @SuppressWarnings("unchecked")
    private static final class RegistryMap<K, V> extends AbstractMap<K, V> {

        private final @NotNull Registry<K, V> registry;

        public RegistryMap(@NotNull Registry<K, V> registry) {
            this.registry = registry;
        }

        @Override
        public int size() {
            return registry.size();
        }

        @Override
        public boolean containsKey(Object key) {
            return registry.has((K) key);
        }

        @Override
        public V get(Object key) {
            return registry.get((K) key);
        }

        @Override
        public @Nullable V put(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(@NotNull Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull Set<K> keySet() {
            return registry.keySet();
        }

        @Override
        public @NotNull Collection<V> values() {
            return registry.values();
        }

        @Override
        public @NotNull Set<Map.Entry<K, V>> entrySet() {
            return registry.entrySet();
        }

    }

}
