package me.hextech.remapped;

import java.util.function.Predicate;

public class EnumSetting<T extends java.lang.Enum<T>> extends Setting {
   public boolean popped = false;
   private T value;

   public EnumSetting(String name, T defaultValue) {
      super(name, ModuleManager.lastLoadMod.getName() + "_" + name);
      this.value = defaultValue;
   }

   public EnumSetting(String name, T defaultValue, Predicate visibilityIn) {
      super(name, ModuleManager.lastLoadMod.getName() + "_" + name, visibilityIn);
      this.value = defaultValue;
   }

   public void increaseEnum() {
      this.value = (T)EnumConverter.increaseEnum(this.value);
   }

   public final T getValue() {
      return this.value;
   }

   public void setEnumValue(String value) {
      for (java.lang.Enum e : (java.lang.Enum[])this.value.getClass().getEnumConstants()) {
         if (e.name().equalsIgnoreCase(value)) {
            this.value = (T)e;
         }
      }
   }

   @Override
   public void loadSetting() {
      EnumConverter converter = new EnumConverter(this.value.getClass());
      String enumString = me.hextech.HexTech.CONFIG.getString(this.getLine());
      if (enumString != null) {
         java.lang.Enum value = converter.doBackward(enumString);
         if (value != null) {
            this.value = (T)value;
         }
      }
   }

   public boolean is(T mode) {
      return this.getValue() == mode;
   }
}
