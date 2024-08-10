package dev.tommyjs.registore.hash;

import dev.tommyjs.registore.impl.AbstractMapRegistry;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashRegistry<K, V> extends AbstractMapRegistry<K, V> {

    public ConcurrentHashRegistry() {
        super(new ConcurrentHashMap<>());
    }

}
