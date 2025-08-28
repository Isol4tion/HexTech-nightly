package me.hextech.mod.modules.impl.render;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;

public class ItemTag
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final BooleanSetting customName = this.add(new BooleanSetting("CustomName", false));
    public final BooleanSetting count = this.add(new BooleanSetting("Count", true));

    public ItemTag() {
        super("ItemTag", Category.Render);
    }

    @Override
    public void onUpdate() {
        for (Entity entity : ItemTag.mc.world.getEntities()) {
            if (!(entity instanceof ItemEntity itemEntity)) continue;
            String s = this.count.getValue() ? " x" + itemEntity.getStack().getCount() : "";
            itemEntity.setCustomName(Text.of((this.customName.getValue() ? itemEntity.getStack().getName() : itemEntity.getStack().getItem().getName()).getString() + s));
            itemEntity.setCustomNameVisible(true);
        }
    }

    @Override
    public void onDisable() {
        if (ItemTag.mc.world == null) {
            return;
        }
        for (Entity entity : ItemTag.mc.world.getEntities()) {
            if (!(entity instanceof ItemEntity itemEntity)) continue;
            itemEntity.setCustomNameVisible(false);
        }
    }
}
