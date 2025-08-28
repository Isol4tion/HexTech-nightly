package me.hextech.mod.modules.impl.movement;

import me.hextech.api.utils.entity.MovementUtil;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Blocks;

public class AntiVoid
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final SliderSetting voidHeight = this.add(new SliderSetting("VoidHeight", -64.0, -64.0, 319.0, 1.0));
    private final SliderSetting height = this.add(new SliderSetting("Height", 100.0, -40.0, 256.0, 1.0));

    public AntiVoid() {
        super("AntiVoid", "Allows you to fly over void blocks", Category.Movement);
    }

    @Override
    public void onUpdate() {
        boolean isVoid = true;
        for (int i = (int)AntiVoid.mc.player.getY(); i > this.voidHeight.getValueInt() - 1; --i) {
            if (AntiVoid.mc.world.getBlockState(new BlockPosX(AntiVoid.mc.player.getX(), i, AntiVoid.mc.player.getZ())).getBlock() == Blocks.AIR) continue;
            isVoid = false;
            break;
        }
        if (AntiVoid.mc.player.getY() < this.height.getValue() + this.voidHeight.getValue() && isVoid) {
            MovementUtil.setMotionY(0.0);
        }
    }
}
