package dev.tommyjs.registore.io;

import dev.tommyjs.registore.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class AbstractRegistryReader<K, V> implements RegistryReader<K, V> {

    protected AbstractRegistryReader() {
    }

    public abstract @NotNull Registry<K, V> read(@NotNull InputStream stream) throws IOException;

    @Override
    public @NotNull Registry<K, V> read(@NotNull ByteBuffer buffer) throws IOException {
        try (BufferInputStream stream = new BufferInputStream(buffer)) {
            return read(stream);
        }
    }

    private static class BufferInputStream extends InputStream {

        private final @NotNull ByteBuffer buffer;

        public BufferInputStream(@NotNull ByteBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public int read() {
            return buffer.get();
        }

        @Override
        public int read(byte @NotNull [] b, int off, int len) {
            buffer.get(b, off, len);
            return len;
        }

    }

}
