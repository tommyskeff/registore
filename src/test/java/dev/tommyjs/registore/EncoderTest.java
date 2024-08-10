package dev.tommyjs.registore;

import dev.tommyjs.registore.encoder.RegistryEncoder;
import dev.tommyjs.registore.encoder.RegistryEncoders;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

public class EncoderTest {

    @Test
    public void ByteEncoder() {
        test(RegistryEncoders.BYTE, 1, (byte) 95, new byte[]{95}, 95);
    }

    @Test
    public void ShortEncoder() {
        test(RegistryEncoders.SHORT, 2, (short) 9463, new byte[]{36, -9}, 9463);
    }

    @Test
    public void IntEncoder() {
        test(RegistryEncoders.INT, 4, 48965389, new byte[]{2, -21, 39, 13}, 48965389);
    }

    @Test
    public void LongEncoder() {
        test(RegistryEncoders.LONG, 8, 409764089764087634L, new byte[]{5, -81, -58, 68, -60, 25, -121, 82}, -1045020394);
    }

    @Test
    public void String64Encoder() {
        test(RegistryEncoders.STRING_64, 66, "Hello World", new byte[]{
            0, 11, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        }, -862545276);
    }

    private static <T> void test(@NotNull RegistryEncoder<T> encoder, int size, @NotNull T input, byte[] output, int hash) {
        assert Arrays.equals(encoder.encode(input), output);
        assert Objects.equals(encoder.decode(output), input);
        assert encoder.getSize() == size;
        assert encoder.hash(input) == hash;
    }

}
