package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;

public class ItemTag
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final BooleanSetting customName = this.add(new BooleanSetting("CustomName", false));
    public final BooleanSetting count = this.add(new BooleanSetting("Count", true));

    public ItemTag() {
        super("ItemTag", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    @Override
    public void onUpdate() {
        for (Entity entity : ItemTag.mc.world.getEntities()) {
            if (!(entity instanceof ItemEntity)) continue;
            ItemEntity itemEntity = (ItemEntity)entity;
            String s = this.count.getValue() ? " x" + itemEntity.getStack().getCount() : "";
            itemEntity.method_5665(Text.of((String)((this.customName.getValue() ? itemEntity.getStack().getName() : itemEntity.getStack().getItem().method_7848()).getString() + s)));
            itemEntity.method_5880(true);
        }
    }

    @Override
    public void onDisable() {
        if (ItemTag.mc.world == null) {
            return;
        }
        for (Entity entity : ItemTag.mc.world.getEntities()) {
            if (!(entity instanceof ItemEntity)) continue;
            ItemEntity itemEntity = (ItemEntity)entity;
            itemEntity.method_5880(false);
        }
    }
}
