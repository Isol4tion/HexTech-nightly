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
        for (Entity entity : ItemTag.mc.world.method_18112()) {
            if (!(entity instanceof ItemEntity)) continue;
            ItemEntity itemEntity = (ItemEntity)entity;
            String s = this.count.getValue() ? " x" + itemEntity.method_6983().method_7947() : "";
            itemEntity.method_5665(Text.method_30163((String)((this.customName.getValue() ? itemEntity.method_6983().method_7964() : itemEntity.method_6983().method_7909().method_7848()).getString() + s)));
            itemEntity.method_5880(true);
        }
    }

    @Override
    public void onDisable() {
        if (ItemTag.mc.world == null) {
            return;
        }
        for (Entity entity : ItemTag.mc.world.method_18112()) {
            if (!(entity instanceof ItemEntity)) continue;
            ItemEntity itemEntity = (ItemEntity)entity;
            itemEntity.method_5880(false);
        }
    }
}
