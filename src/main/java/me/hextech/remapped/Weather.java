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
            case 0 -> Biome.Precipitation.NONE;
            case 1 -> Biome.Precipitation.RAIN;
            case 2 -> Biome.Precipitation.SNOW;
            case 3 -> Biome.Precipitation.SNOW;
        };
    }
}
