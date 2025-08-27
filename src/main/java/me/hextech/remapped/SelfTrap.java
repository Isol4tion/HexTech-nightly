package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class SelfTrap
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static SelfTrap INSTANCE;
    public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500));
    public final BooleanSetting extend = this.add(new BooleanSetting("Extend", true));
    public final BooleanSetting inAir = this.add(new BooleanSetting("InAir", true));
    private final Timer timer = new Timer();
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", false));
    private final BooleanSetting onlyTick = this.add(new BooleanSetting("OnlyTick", true));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true).setParent());
    private final BooleanSetting eatPause = this.add(new BooleanSetting("EatingPause", true));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting center = this.add(new BooleanSetting("Center", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting moveDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final BooleanSetting jumpDisable = this.add(new BooleanSetting("JumpDisable", true));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true));
    private final BooleanSetting head = this.add(new BooleanSetting("Head", true));
    private final BooleanSetting feet = this.add(new BooleanSetting("Feet", true));
    private final BooleanSetting chest = this.add(new BooleanSetting("Chest", true));
    double startX = 0.0;
    double startY = 0.0;
    double startZ = 0.0;
    int progress = 0;
    private boolean shouldCenter = true;

    public SelfTrap() {
        super("SelfTrap", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static boolean selfIntersectPos(BlockPos pos) {
        return SelfTrap.mc.player.getBoundingBox().intersects(new Box(pos));
    }

    public static Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
        Vec3d vec3d = posTo.subtract(posFrom);
        return SelfTrap.getRotationFromVec(vec3d);
    }

    public static Vec2f getRotationFromVec(Vec3d vec) {
        double d = vec.x;
        double d2 = vec.z;
        double xz = Math.hypot(d, d2);
        d2 = vec.z;
        double d3 = vec.x;
        double yaw = SelfTrap.normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = SelfTrap.normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f((float)yaw, (float)pitch);
    }

    private static double normalizeAngle(double angleIn) {
        double d;
        double angle = angleIn;
        angle %= 360.0;
        if (d >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    @Override
    public void onEnable() {
        if (SelfTrap.nullCheck()) {
            if (this.moveDisable.getValue() || this.jumpDisable.getValue()) {
                this.disable();
            }
            return;
        }
        this.startX = SelfTrap.mc.player.getX();
        this.startY = SelfTrap.mc.player.getY();
        this.startZ = SelfTrap.mc.player.getZ();
        this.shouldCenter = true;
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        if (!this.onlyTick.getValue()) {
            this.onUpdate();
        }
    }

    @EventHandler(priority=-1)
    public void onMove(MoveEvent event) {
        if (SelfTrap.nullCheck() || !this.center.getValue() || SelfTrap.mc.player.method_6128()) {
            return;
        }
        BlockPos blockPos = EntityUtil.getPlayerPos(true);
        if (SelfTrap.mc.player.getX() - (double)blockPos.getX() - 0.5 <= 0.2 && SelfTrap.mc.player.getX() - (double)blockPos.getX() - 0.5 >= -0.2 && SelfTrap.mc.player.getZ() - (double)blockPos.getZ() - 0.5 <= 0.2 && SelfTrap.mc.player.getZ() - 0.5 - (double)blockPos.getZ() >= -0.2) {
            if (this.shouldCenter && (SelfTrap.mc.player.isOnGround() || MovementUtil.isMoving())) {
                event.setX(0.0);
                event.setZ(0.0);
                this.shouldCenter = false;
            }
        } else if (this.shouldCenter) {
            Vec3d centerPos = EntityUtil.getPlayerPos(true).toCenterPos();
            float rotation = SelfTrap.getRotationTo((Vec3d)SelfTrap.mc.player.getPos(), (Vec3d)centerPos).x;
            float yawRad = rotation / 180.0f * (float)Math.PI;
            double dist = SelfTrap.mc.player.getPos().distanceTo(new Vec3d(centerPos.x, SelfTrap.mc.player.getY(), centerPos.z));
            double cappedSpeed = Math.min(0.2873, dist);
            double x = (double)(-((float)Math.sin(yawRad))) * cappedSpeed;
            double z = (double)((float)Math.cos(yawRad)) * cappedSpeed;
            event.setX(x);
            event.setZ(z);
        }
    }

    @Override
    public void onUpdate() {
        if (!this.timer.passedMs((long)this.placeDelay.getValue())) {
            return;
        }
        this.progress = 0;
        if (!MovementUtil.isMoving() && !SelfTrap.mc.options.jumpKey.isPressed()) {
            this.startX = SelfTrap.mc.player.getX();
            this.startY = SelfTrap.mc.player.getY();
            this.startZ = SelfTrap.mc.player.getZ();
        }
        BlockPos pos = EntityUtil.getPlayerPos(true);
        double distanceToStart = MathHelper.sqrt((float)((float)SelfTrap.mc.player.method_5649(this.startX, this.startY, this.startZ)));
        if (this.getBlock() == -1) {
            CommandManager.sendChatMessageWidthId("\u00a7c\u00a7oObsidian" + (this.enderChest.getValue() ? "/EnderChest" : "") + "?", this.hashCode());
            this.disable();
            return;
        }
        if (this.moveDisable.getValue() && distanceToStart > 1.0 || this.jumpDisable.getValue() && Math.abs(this.startY - SelfTrap.mc.player.getY()) > 0.5) {
            this.disable();
            return;
        }
        if (this.usingPause.getValue() && SelfTrap.mc.player.isUsingItem()) {
            return;
        }
        if (!this.inAir.getValue() && !SelfTrap.mc.player.isOnGround()) {
            return;
        }
        if (this.head.getValue()) {
            this.tryPlaceBlock(pos.up(2));
        }
        if (this.feet.getValue()) {
            this.doSurround(pos);
        }
        if (this.chest.getValue()) {
            this.doSurround(pos.up());
        }
    }

    private void doSurround(BlockPos pos) {
        for (Direction i : Direction.values()) {
            if (i == Direction.UP) continue;
            BlockPos offsetPos = pos.offset(i);
            if (BlockUtil.getPlaceSide(offsetPos) != null) {
                this.tryPlaceBlock(offsetPos);
            } else if (BlockUtil.canReplace(offsetPos)) {
                this.tryPlaceBlock(this.getHelperPos(offsetPos));
            }
            if (!SelfTrap.selfIntersectPos(offsetPos) || !this.extend.getValue()) continue;
            for (Direction i2 : Direction.values()) {
                if (i2 == Direction.UP) continue;
                BlockPos offsetPos2 = offsetPos.offset(i2);
                if (SelfTrap.selfIntersectPos(offsetPos2)) {
                    for (Direction i3 : Direction.values()) {
                        if (i3 == Direction.UP) continue;
                        this.tryPlaceBlock(offsetPos2);
                        BlockPos offsetPos3 = offsetPos2.offset(i3);
                        this.tryPlaceBlock(BlockUtil.getPlaceSide(offsetPos3) != null || !BlockUtil.canReplace(offsetPos3) ? offsetPos3 : this.getHelperPos(offsetPos3));
                    }
                }
                this.tryPlaceBlock(BlockUtil.getPlaceSide(offsetPos2) != null || !BlockUtil.canReplace(offsetPos2) ? offsetPos2 : this.getHelperPos(offsetPos2));
            }
        }
    }

    private void tryPlaceBlock(BlockPos pos) {
        if (pos == null) {
            return;
        }
        if (this.detectMining.getValue() && HexTech.BREAK.isMining(pos)) {
            return;
        }
        if (!((double)this.progress < this.blocksPer.getValue())) {
            return;
        }
        int block = this.getBlock();
        if (block == -1) {
            return;
        }
        if (!BlockUtil.canPlace(pos, 6.0, true)) {
            return;
        }
        if (this.breakCrystal.getValue()) {
            CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.eatPause.getValue());
        } else if (BlockUtil.hasEntity(pos, false)) {
            return;
        }
        int old = SelfTrap.mc.player.getInventory().selectedSlot;
        this.doSwap(block);
        BlockUtil.placeBlock(pos, this.rotate.getValue(), this.packetPlace.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(old);
        }
        ++this.progress;
        this.timer.reset();
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, SelfTrap.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getBlock() {
        if (this.inventory.getValue()) {
            if (InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN) != -1 || !this.enderChest.getValue()) {
                return InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN);
            }
            return InventoryUtil.findBlockInventorySlot(Blocks.ENDER_CHEST);
        }
        if (InventoryUtil.findBlock(Blocks.OBSIDIAN) != -1 || !this.enderChest.getValue()) {
            return InventoryUtil.findBlock(Blocks.OBSIDIAN);
        }
        return InventoryUtil.findBlock(Blocks.ENDER_CHEST);
    }

    public BlockPos getHelperPos(BlockPos pos) {
        for (Direction i : Direction.values()) {
            if (this.detectMining.getValue() && HexTech.BREAK.isMining(pos.offset(i)) || !BlockUtil.isStrictDirection(pos.offset(i), i.getOpposite()) || !BlockUtil.canPlace(pos.offset(i))) continue;
            return pos.offset(i);
        }
        return null;
    }
}
