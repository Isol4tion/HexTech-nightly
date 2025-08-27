package me.hextech.remapped;

import net.minecraft.world.biome.Biome;

/*
 * Exception performing whole class analysis ignored.
 */
public static enum Weather {
    None,
    Rain,
    Snow,
    Both;


    public Biome.Precipitation toMC() {
        return switch (this.ordinal()) {
            default -> throw new IncompatibleClassChangeError();
            case 0 -> Biome.Precipitation.field_9384;
            case 1 -> Biome.Precipitation.field_9382;
            case 2 -> Biome.Precipitation.field_9383;
            case 3 -> Biome.Precipitation.field_9383;
        };
    }
}
