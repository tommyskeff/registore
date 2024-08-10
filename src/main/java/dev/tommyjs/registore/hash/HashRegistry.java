package dev.tommyjs.registore.hash;

import dev.tommyjs.registore.impl.AbstractMapRegistry;

import java.util.HashMap;

public class HashRegistry<K, V> extends AbstractMapRegistry<K, V> {

    public HashRegistry() {
        super(new HashMap<>());
    }

}
