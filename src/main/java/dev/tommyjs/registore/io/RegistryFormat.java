package dev.tommyjs.registore.io;

public enum RegistryFormat {

    HASH_REGISTRY(0xb689179c, 0xd26c8bfb);

    private final int header;
    private final int footer;

    RegistryFormat(int header, int footer) {
        this.header = header;
        this.footer = footer;
    }

    public int getHeader() {
        return header;
    }

    public int getFooter() {
        return footer;
    }

}
