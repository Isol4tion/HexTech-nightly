package me.hextech.api.utils.combat;

import me.hextech.HexTech;
import me.hextech.api.managers.RotateManager;
import me.hextech.api.utils.Wrapper;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.mod.modules.impl.setting.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ListenerHelper
        implements Wrapper {
    public static Block getBlock(BlockPos pos) {
        return BlockUtil.getState(pos).getBlock();
    }

    private static int getBlock() {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
            return InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN);
        }
        return InventoryUtil.findBlock(Blocks.OBSIDIAN);
    }

    public static void tryBase() {
        for (BlockPos pos : BlockUtil.getSphere((float) AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue() + 1.0f)) {
            if (ListenerHelperUtil.behindWall(pos) || !ListenerHelperUtil.canBasePlaceCrystal(pos.up(), false, false) || !ListenerHelperUtil.canTouch(pos) || (double) ListenerHelperUtil.calculateObsidian(pos, pos.up().toCenterPos(), AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.displayTarget) < AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.minDamage.getValue())
                continue;
            ListenerHelper.doBase(pos);
        }
    }

    public static void doBase(BlockPos pos) {
        if (!AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.obsidian.getValue()) {
            return;
        }
        if (!BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.placeBaseTimer.passedMs((long) AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.placeObsDelay.getValue())) {
            return;
        }
        if ((double) BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.tempDamage < ListenerDamage.getDamage(BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.displayTarget)) {
            return;
        }
        int block = ListenerHelper.getBlock();
        if (block == -1) {
            return;
        }
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            return;
        }
        Vec3d directionVec = new Vec3d((double) pos.getX() + 0.5 + (double) side.getVector().getX() * 0.5, (double) pos.getY() + 0.5 + (double) side.getVector().getY() * 0.5, (double) pos.getZ() + 0.5 + (double) side.getVector().getZ() * 0.5);
        if (!BlockUtil.canPlace(pos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue())) {
            return;
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.rotate.getValue() && !ListenerHelper.faceVector(directionVec)) {
            return;
        }
        int old = 0;
        if (ListenerHelper.mc.player != null) {
            old = ListenerHelper.mc.player.getInventory().selectedSlot;
        }
        ListenerHelper.doSwap(block);
        if (BlockUtil.airPlace()) {
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos, Direction.DOWN, false, Hand.MAIN_HAND);
        } else {
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos.offset(side), side.getOpposite(), false, Hand.MAIN_HAND);
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
            ListenerHelper.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            ListenerHelper.doSwap(old);
        }
        BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.placeTimer.reset();
    }

    private static void doSwap(int slot) {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
            if (ListenerHelper.mc.player != null) {
                InventoryUtil.inventorySwap(slot, ListenerHelper.mc.player.getInventory().selectedSlot);
            }
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private static boolean faceVector(Vec3d directionVec) {
        if (!AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.faceVector.getValue()) {
            RotateManager.TrueVec3d(directionVec);
            return true;
        }
        if (HexTech.ROTATE.inFov(directionVec, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.fov.getValueFloat())) {
            return true;
        }
        return !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.checkLook.getValue();
    }
}
