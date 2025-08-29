package me.hextech.mod.modules.impl.combat;

import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.util.math.MathHelper;

public class AntiRegear
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final SliderSetting safeRange = this.add(new SliderSetting("SafeRange", 2, 0, 8));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5, 0, 8));

    public AntiRegear() {
        super("AntiRegear", "Shulker nuker", Category.Combat);
    }

    @Override
    public void onUpdate() {
        if (SpeedMine.breakPos != null && AntiRegear.mc.world.getBlockState(SpeedMine.breakPos).getBlock() instanceof ShulkerBoxBlock) {
            return;
        }
        if (this.getBlock() != null) {
            SpeedMine.INSTANCE.mine(this.getBlock().getPos());
        }
    }

    private ShulkerBoxBlockEntity getBlock() {
        for (BlockEntity entity : BlockUtil.getTileEntities()) {
            ShulkerBoxBlockEntity shulker;
            if (!(entity instanceof ShulkerBoxBlockEntity) || (double) MathHelper.sqrt((float) AntiRegear.mc.player.squaredDistanceTo((shulker = (ShulkerBoxBlockEntity) entity).getPos().toCenterPos())) <= this.safeRange.getValue() || !((double) MathHelper.sqrt((float) AntiRegear.mc.player.squaredDistanceTo(shulker.getPos().toCenterPos())) <= this.range.getValue()))
                continue;
            return shulker;
        }
        return null;
    }
}
