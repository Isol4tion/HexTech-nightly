package me.hextech.remapped;

import net.minecraft.world.biome.Biome.Precipitation;

public enum Weather {
   None,
   Rain,
   Snow,
   Both;

   public Precipitation toMC() {
      return switch (this) {
         case None -> Precipitation.field_9384;
         case Rain -> Precipitation.field_9382;
         case Snow -> Precipitation.field_9383;
         case Both -> Precipitation.field_9383;
      };
   }
}
