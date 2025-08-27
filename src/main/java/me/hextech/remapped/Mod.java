package me.hextech.remapped;

public class Mod implements Wrapper {
   private final String name;

   public Mod(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }
}
