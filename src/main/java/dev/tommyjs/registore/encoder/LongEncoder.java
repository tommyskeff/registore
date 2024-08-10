package dev.tommyjs.registore.encoder;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class LongEncoder extends RegistryEncoder<Long> {

    LongEncoder() {
        super(8);
    }

    @Override
    public @NotNull Long decode(byte[] data) {
        return ByteBuffer.wrap(data).getLong();
    }

    @Override
    public byte[] encode(@NotNull Long data) {
        return ByteBuffer.allocate(8).putLong(data).array();
    }

    @Override
    public int hash(@NotNull Long data) {
        return (int) (data ^ (data >>> 32));
    }

}
