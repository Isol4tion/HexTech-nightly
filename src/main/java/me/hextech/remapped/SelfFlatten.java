package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SelfFlatten
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static SelfFlatten INSTANCE;
    private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final SliderSetting blocksPer = this.add(new SliderSetting("MaxBlock", 1, 1, 4));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 1000));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100));
    private final Timer timer = new Timer();
    public Vec3d directionVec = null;
    int progress = 0;

    public SelfFlatten() {
        super("SelfFlatten", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.directionVec != null && this.rotate.getValue() && this.yawStep.getValue()) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @Override
    public void onUpdate() {
        this.progress = 0;
        if (this.usingPause.getValue() && SelfFlatten.mc.player.isUsingItem()) {
            return;
        }
        if (this.selfGround.getValue() && !SelfFlatten.mc.player.isOnGround()) {
            return;
        }
        if (!SelfFlatten.mc.player.isOnGround()) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValueInt())) {
            return;
        }
        int oldSlot = SelfFlatten.mc.player.getInventory().selectedSlot;
        int block = this.getBlock();
        if (block == -1) {
            return;
        }
        if (!EntityUtil.isInsideBlock()) {
            return;
        }
        BlockPos pos1 = new BlockPosX(SelfFlatten.mc.player.getX() + 0.6, SelfFlatten.mc.player.getY() + 0.5, SelfFlatten.mc.player.getZ() + 0.6).down();
        BlockPos pos2 = new BlockPosX(SelfFlatten.mc.player.getX() - 0.6, SelfFlatten.mc.player.getY() + 0.5, SelfFlatten.mc.player.getZ() + 0.6).down();
        BlockPos pos3 = new BlockPosX(SelfFlatten.mc.player.getX() + 0.6, SelfFlatten.mc.player.getY() + 0.5, SelfFlatten.mc.player.getZ() - 0.6).down();
        BlockPos pos4 = new BlockPosX(SelfFlatten.mc.player.getX() - 0.6, SelfFlatten.mc.player.getY() + 0.5, SelfFlatten.mc.player.getZ() - 0.6).down();
        if (!(this.canPlace(pos1) || this.canPlace(pos2) || this.canPlace(pos3) || this.canPlace(pos4))) {
            return;
        }
        this.doSwap(block);
        this.tryPlaceObsidian(pos1, this.rotate.getValue());
        this.tryPlaceObsidian(pos2, this.rotate.getValue());
        this.tryPlaceObsidian(pos3, this.rotate.getValue());
        this.tryPlaceObsidian(pos4, this.rotate.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(oldSlot);
        }
    }

    private boolean tryPlaceObsidian(BlockPos pos, boolean rotate) {
        if (this.canPlace(pos)) {
            if (this.checkMine.getValue() && BlockUtil.isMining(pos)) {
                return false;
            }
            if (!((double)this.progress < this.blocksPer.getValue())) {
                return false;
            }
            Direction side = BlockUtil.getPlaceSide(pos);
            if (side == null) {
                if (BlockUtil.airPlace()) {
                    BlockUtil.placedPos.add(pos);
                    BlockUtil.clickBlock(pos, Direction.UP, rotate);
                    this.timer.reset();
                    ++this.progress;
                    return true;
                }
                return false;
            }
            ++this.progress;
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos.offset(side), side.getOpposite(), rotate);
            this.timer.reset();
            return true;
        }
        return false;
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!this.yawStep.getValue()) {
            RotateManager.TrueVec3d(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        if (HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
        }
        return !this.checkFov.getValue();
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, SelfFlatten.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private boolean canPlace(BlockPos pos) {
        if (BlockUtil.getPlaceSide(pos) == null) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        return !this.hasEntity(pos);
    }

    private boolean hasEntity(BlockPos pos) {
        for (Entity entity : SelfFlatten.mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
            if (entity == SelfFlatten.mc.player || !entity.isAlive() || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ArrowEntity || entity instanceof EndCrystalEntity || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    private int getBlock() {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN);
        }
        return InventoryUtil.findBlock(Blocks.OBSIDIAN);
    }
}
