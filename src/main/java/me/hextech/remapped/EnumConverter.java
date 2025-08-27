package me.hextech.remapped;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public class EnumConverter extends Converter<java.lang.Enum, JsonElement> {
   private final Class<? extends java.lang.Enum> clazz;

   public EnumConverter(Class<? extends java.lang.Enum> clazz) {
      this.clazz = clazz;
   }

   public static int currentEnum(java.lang.Enum clazz) {
      for (int i = 0; i < ((java.lang.Enum[])clazz.getClass().getEnumConstants()).length; i++) {
         java.lang.Enum e = ((java.lang.Enum[])clazz.getClass().getEnumConstants())[i];
         if (e.name().equalsIgnoreCase(clazz.name())) {
            return i;
         }
      }

      return -1;
   }

   public static java.lang.Enum increaseEnum(java.lang.Enum clazz) {
      int index = currentEnum(clazz);

      for (int i = 0; i < ((java.lang.Enum[])clazz.getClass().getEnumConstants()).length; i++) {
         java.lang.Enum e = ((java.lang.Enum[])clazz.getClass().getEnumConstants())[i];
         if (i == index + 1) {
            return e;
         }
      }

      return ((java.lang.Enum[])clazz.getClass().getEnumConstants())[0];
   }

   public static String getProperName(java.lang.Enum clazz) {
      return Character.toUpperCase(clazz.name().charAt(0)) + clazz.name().toLowerCase().substring(1);
   }

   @NotNull
   public JsonElement doForward(java.lang.Enum anEnum) {
      return new JsonPrimitive(anEnum.toString());
   }

   public java.lang.Enum doBackward(String string) {
      try {
         return java.lang.Enum.valueOf(this.clazz, string);
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   @NotNull
   public java.lang.Enum doBackward(JsonElement jsonElement) {
      try {
         return java.lang.Enum.valueOf(this.clazz, jsonElement.getAsString());
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }
}
