package dev.tommyjs.registore.encoder;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringEncoder extends RegistryEncoder<String> {

    private final int maxLength;

    public StringEncoder(int maxLength) {
        super(maxLength + 2);
        this.maxLength = maxLength;
    }

    @Override
    public @NotNull String decode(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        short length = buffer.getShort();
        if (length > maxLength) {
            throw new IllegalArgumentException("String too long");
        } else {
            return new String(data, 2, length, StandardCharsets.UTF_8);
        }
    }

    @Override
    public byte[] encode(@NotNull String data) {
        byte[] encoded = data.getBytes(StandardCharsets.UTF_8);
        if (encoded.length > maxLength) {
            throw new IllegalArgumentException("String too long");
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(maxLength + 2);
            buffer.put(2, encoded);
            buffer.putShort((short) encoded.length);
            return buffer.array();
        }
    }

    @Override
    public int hash(@NotNull String data) {
        return data.hashCode();
    }

}
