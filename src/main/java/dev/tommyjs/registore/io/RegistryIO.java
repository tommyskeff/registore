package dev.tommyjs.registore.io;

import org.jetbrains.annotations.NotNull;

public interface RegistryIO<K, V> extends RegistryReader<K, V>, RegistryWriter<K, V> {

    static <K, V> @NotNull RegistryIOBuilder<K, V> builder(@NotNull Class<K> keyType, @NotNull Class<V> valueType) {
        return new RegistryIOBuilder<>(keyType, valueType);
    }

    static <K> @NotNull RegistryIOBuilder<K, K> builder(@NotNull Class<K> keyType) {
        return new RegistryIOBuilder<>(keyType, keyType);
    }


}
