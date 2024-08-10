package dev.tommyjs.registore.encoder;

import org.jetbrains.annotations.NotNull;

public final class ByteEncoder extends RegistryEncoder<Byte> {

    ByteEncoder() {
        super(1);
    }

    @Override
    public @NotNull Byte decode(byte[] data) {
        return data[0];
    }

    @Override
    public byte[] encode(@NotNull Byte data) {
        return new byte[]{data};
    }

    @Override
    public int hash(@NotNull Byte data) {
        return (int) data;
    }

}
