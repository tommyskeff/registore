package dev.tommyjs.registore.io;

import dev.tommyjs.registore.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

public abstract class AbstractRegistryWriter<K, V> implements RegistryWriter<K, V> {

    protected AbstractRegistryWriter() {
    }

    public abstract void write(@NotNull Registry<K, V> registry, @NotNull OutputStream stream) throws IOException;

    @Override
    public void write(@NotNull Registry<K, V> registry, @NotNull ByteBuffer buffer) throws IOException {
        try (BufferOutputStream stream = new BufferOutputStream(buffer)) {
            write(registry, stream);
        }
    }

    @Override
    public void write(@NotNull Map<K, V> map, @NotNull OutputStream stream) throws IOException {
        write(Registry.fromMap(map), stream);
    }

    @Override
    public void write(@NotNull Map<K, V> map, @NotNull ByteBuffer buffer) throws IOException {
        write(Registry.fromMap(map), buffer);
    }

    private static class BufferOutputStream extends OutputStream {

        private final @NotNull ByteBuffer buffer;

        public BufferOutputStream(@NotNull ByteBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void write(int b) {
            buffer.put((byte) (b & 0xFF));
        }

        @Override
        public void write(byte @NotNull [] b, int off, int len) {
            buffer.put(b, off, len);
        }

    }

}
