package me.hextech.remapped;

import java.lang.reflect.Field;
import java.util.function.Predicate;
import org.lwjgl.glfw.GLFW;

public class BindSetting extends Setting {
   public boolean hold = false;
   private boolean isListening = false;
   private int key;
   private boolean pressed = false;
   private boolean holdEnable = false;

   public BindSetting(String name, int key) {
      super(name, ModuleManager.lastLoadMod.getName() + "_" + name);
      this.key = key;
   }

   public BindSetting(String name, int key, Predicate visibilityIn) {
      super(name, ModuleManager.lastLoadMod.getName() + "_" + name, visibilityIn);
      this.key = key;
   }

   @Override
   public void loadSetting() {
      this.setKey(me.hextech.HexTech.CONFIG.getInt(this.getLine(), this.key));
      this.setHoldEnable(me.hextech.HexTech.CONFIG.getBoolean(this.getLine() + "_hold"));
   }

   public int getKey() {
      return this.key;
   }

   public void setKey(int key) {
      this.key = key;
   }

   public String getBind() {
      if (this.key == -1) {
         return "None";
      } else {
         String kn;
         if (this.key >= 3 && this.key <= 4) {
            switch (this.key) {
               case 3:
                  kn = "Mouse_4";
                  break;
               case 4:
                  kn = "Mouse_5";
                  break;
               default:
                  kn = "None";
            }
         } else {
            kn = this.key > 0 ? GLFW.glfwGetKeyName(this.key, GLFW.glfwGetKeyScancode(this.key)) : "None";
         }

         if (kn == null) {
            try {
               for (Field declaredField : GLFW.class.getDeclaredFields()) {
                  if (declaredField.getName().startsWith("GLFW_KEY_")) {
                     int a = (Integer)declaredField.get(null);
                     if (a == this.key) {
                        String nb = declaredField.getName().substring("GLFW_KEY_".length());
                        kn = nb.substring(0, 1).toUpperCase() + nb.substring(1).toLowerCase();
                     }
                  }
               }
            } catch (Exception var8) {
               kn = "None";
            }
         }

         return kn.toUpperCase();
      }
   }

   public boolean isListening() {
      return this.isListening;
   }

   public void setListening(boolean set) {
      this.isListening = set;
   }

   public boolean isPressed() {
      return this.pressed;
   }

   public void setPressed(boolean pressed) {
      this.pressed = pressed;
   }

   public boolean isHoldEnable() {
      return this.holdEnable;
   }

   public void setHoldEnable(boolean holdEnable) {
      this.holdEnable = holdEnable;
   }
}
