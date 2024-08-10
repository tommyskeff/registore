package dev.tommyjs.registore.encoder;

import org.jetbrains.annotations.NotNull;

public abstract class RegistryEncoder<T> {

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 1024;

    private final int size;

    public RegistryEncoder(int size) {
        if (size < MIN_SIZE || size > MAX_SIZE) {
            throw new IllegalArgumentException("Illegal data size");
        }

        this.size = size;
    }

    public abstract @NotNull T decode(byte[] data);

    public abstract byte[] encode(@NotNull T data);

    public abstract int hash(@NotNull T data);

    public int getSize() {
        return size;
    }

}
