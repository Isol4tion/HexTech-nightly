package me.hextech.remapped;

public class ModuleList_JTSxbGphPlVCUvdsPgfV {
   public final Module_eSdgMXWuzcxgQVaJFmKZ module;
   public boolean isEnabled;
   public double x;
   public double y;
   public double fade;
   public boolean hide;
   public double fold;
   public String lastName;
   public String name;
   public boolean nameUpdated;

   public ModuleList_JTSxbGphPlVCUvdsPgfV(final ModuleList_ZBgBxeJhVhAvRjXaLZeK this$0, Module_eSdgMXWuzcxgQVaJFmKZ module) {
      this.this$0 = this$0;
      this.isEnabled = false;
      this.x = 0.0;
      this.y = 0.0;
      this.fade = 0.0;
      this.hide = true;
      this.fold = 0.0;
      this.lastName = "";
      this.name = "";
      this.nameUpdated = false;
      this.module = module;
   }

   public void enable() {
      if (!this.isEnabled) {
         this.isEnabled = true;
      }
   }

   public void disable() {
      if (this.isEnabled) {
         this.isEnabled = false;
      }
   }

   public void updateName() {
      String name = this.module.getArrayName();
      this.lastName = name;
      if (this.this$0.space.getValue()) {
         name = this.module.getName().replaceAll("([a-z])([A-Z])", "$1 $2");
         if (name.startsWith(" ")) {
            name = name.replaceFirst(" ", "");
         }

         name = name + this.module.getArrayInfo();
      }

      this.name = name;
      this.this$0.update = true;
   }

   public void update() {
      String name = this.module.getArrayName();
      if (!this.lastName.equals(name)) {
         this.lastName = name;
         if (this.this$0.space.getValue()) {
            name = this.module.getName().replaceAll("([a-z])([A-Z])", "$1 $2");
            if (name.startsWith(" ")) {
               name = name.replaceFirst(" ", "");
            }

            name = name + this.module.getArrayInfo();
         }

         this.name = name;
         this.this$0.update = true;
         this.nameUpdated = true;
      }
   }
}
