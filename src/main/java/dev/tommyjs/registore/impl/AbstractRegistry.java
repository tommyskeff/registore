package dev.tommyjs.registore.impl;

import dev.tommyjs.registore.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractRegistry<K, V> implements Registry<K, V> {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Registry<?,?> registry)) {
            return false;
        }

        return Objects.equals(new HashSet<>(entrySet()), new HashSet<>(registry.entrySet()));
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }

}
