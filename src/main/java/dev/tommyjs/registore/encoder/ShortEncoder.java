package dev.tommyjs.registore.encoder;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class ShortEncoder extends RegistryEncoder<Short> {

    ShortEncoder() {
        super(2);
    }

    @Override
    public @NotNull Short decode(byte[] data) {
        return ByteBuffer.wrap(data).getShort();
    }

    @Override
    public byte[] encode(@NotNull Short data) {
        return ByteBuffer.allocate(2).putShort(data).array();
    }

    @Override
    public int hash(@NotNull Short data) {
        return (int) data;
    }

}
