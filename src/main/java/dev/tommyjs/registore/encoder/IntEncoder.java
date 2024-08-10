package dev.tommyjs.registore.encoder;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class IntEncoder extends RegistryEncoder<Integer> {

    IntEncoder() {
        super(4);
    }

    @Override
    public @NotNull Integer decode(byte[] data) {
        return ByteBuffer.wrap(data).getInt();
    }

    @Override
    public byte[] encode(@NotNull Integer data) {
        return ByteBuffer.allocate(4).putInt(data).array();
    }

    @Override
    public int hash(@NotNull Integer data) {
        return data;
    }

}
