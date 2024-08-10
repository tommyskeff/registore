package dev.tommyjs.registore.hash;

import dev.tommyjs.registore.io.RegistryFormat;

class HashRegistries {

    static final int MAGIC_HEADER = RegistryFormat.HASH_REGISTRY.getHeader();
    static final int MAGIC_FOOTER = RegistryFormat.HASH_REGISTRY.getFooter();
    static final int VERSION = 1;
    static final float DEFAULT_EXPAND_FACTOR = 2F;
    static final int MAX_DATA_SIZE = 1024;

}
