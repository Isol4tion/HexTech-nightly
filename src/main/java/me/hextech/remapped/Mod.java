package me.hextech.remapped;

import me.hextech.remapped.api.utils.Wrapper;

public class Mod
implements Wrapper {
    private final String name;

    public Mod(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
