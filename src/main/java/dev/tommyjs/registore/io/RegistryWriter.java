package dev.tommyjs.registore.io;

import dev.tommyjs.registore.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

public interface RegistryWriter<K, V> {

    void write(@NotNull Registry<K, V> registry, @NotNull OutputStream stream) throws IOException;

    void write(@NotNull Registry<K, V> registry, @NotNull ByteBuffer buffer) throws IOException;

    void write(@NotNull Map<K, V> map, @NotNull OutputStream stream) throws IOException;

    void write(@NotNull Map<K, V> map, @NotNull ByteBuffer buffer) throws IOException;

}
