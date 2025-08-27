package me.hextech.remapped;

import me.hextech.remapped.ModuleList_ZBgBxeJhVhAvRjXaLZeK;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;

/*
 * Exception performing whole class analysis ignored.
 */
public static class ModuleList_JTSxbGphPlVCUvdsPgfV {
    public final Module_eSdgMXWuzcxgQVaJFmKZ module;
    public boolean isEnabled = false;
    public double x = 0.0;
    public double y = 0.0;
    public double fade = 0.0;
    public boolean hide = true;
    public double fold = 0.0;
    public String lastName = "";
    public String name = "";
    public boolean nameUpdated = false;
    final /* synthetic */ ModuleList_ZBgBxeJhVhAvRjXaLZeK this$0;

    public ModuleList_JTSxbGphPlVCUvdsPgfV(ModuleList_ZBgBxeJhVhAvRjXaLZeK this$0, Module_eSdgMXWuzcxgQVaJFmKZ module) {
        this.this$0 = this$0;
        this.module = module;
    }

    public void enable() {
        if (this.isEnabled) {
            return;
        }
        this.isEnabled = true;
    }

    public void disable() {
        if (!this.isEnabled) {
            return;
        }
        this.isEnabled = false;
    }

    public void updateName() {
        Object name = this.module.getArrayName();
        this.lastName = name;
        if (this.this$0.space.getValue()) {
            name = this.module.getName().replaceAll("([a-z])([A-Z])", "$1 $2");
            if (((String)name).startsWith(" ")) {
                name = ((String)name).replaceFirst(" ", "");
            }
            name = (String)name + this.module.getArrayInfo();
        }
        this.name = name;
        this.this$0.update = true;
    }

    public void update() {
        Object name = this.module.getArrayName();
        if (!this.lastName.equals(name)) {
            this.lastName = name;
            if (this.this$0.space.getValue()) {
                name = this.module.getName().replaceAll("([a-z])([A-Z])", "$1 $2");
                if (((String)name).startsWith(" ")) {
                    name = ((String)name).replaceFirst(" ", "");
                }
                name = (String)name + this.module.getArrayInfo();
            }
            this.name = name;
            this.this$0.update = true;
            this.nameUpdated = true;
        }
    }
}
