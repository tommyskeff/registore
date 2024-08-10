package dev.tommyjs.registore.io;

import dev.tommyjs.registore.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface RegistryReader<K, V> {

    @NotNull Registry<K, V> read(@NotNull InputStream stream) throws IOException;

    @NotNull Registry<K, V> read(@NotNull ByteBuffer buffer) throws IOException;

}
