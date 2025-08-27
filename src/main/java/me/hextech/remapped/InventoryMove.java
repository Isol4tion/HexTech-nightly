package me.hextech.remapped;

import me.hextech.remapped.AutoWalk;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Sprint;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;

public class InventoryMove
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final BooleanSetting sneak = this.add(new BooleanSetting("Sneak", false));

    public InventoryMove() {
        super("InventoryMove", Module_JlagirAibYQgkHtbRnhw.Movement);
        this.setDescription("Walk in inventory.");
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        this.update();
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        this.update();
    }

    @Override
    public void onUpdate() {
        this.update();
    }

    private void update() {
        if (!(InventoryMove.mc.currentScreen instanceof ChatScreen)) {
            for (KeyBinding k : new KeyBinding[]{InventoryMove.mc.options.backKey, InventoryMove.mc.options.leftKey, InventoryMove.mc.options.rightKey, InventoryMove.mc.options.jumpKey}) {
                k.setPressed(InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)k.getBoundKeyTranslationKey()).getCode()));
            }
            InventoryMove.mc.options.forwardKey.setPressed(AutoWalk.INSTANCE.isOn() || InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)InventoryMove.mc.options.forwardKey.getBoundKeyTranslationKey()).getCode()));
            InventoryMove.mc.options.sprintKey.setPressed(Sprint.INSTANCE.isOn() || InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)InventoryMove.mc.options.sprintKey.getBoundKeyTranslationKey()).getCode()));
            if (this.sneak.getValue()) {
                InventoryMove.mc.options.sneakKey.setPressed(InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)InventoryMove.mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
            }
        }
    }
}
