package dev.tommyjs.registore.encoder;

public class RegistryEncoders {

    public static final RegistryEncoder<Byte> BYTE = new ByteEncoder();
    public static final RegistryEncoder<Short> SHORT = new ShortEncoder();
    public static final RegistryEncoder<Integer> INT = new IntEncoder();
    public static final RegistryEncoder<Long> LONG = new LongEncoder();
    public static final RegistryEncoder<String> STRING_64 = new StringEncoder(64);
    public static final RegistryEncoder<String> STRING_256 = new StringEncoder(256);

}
