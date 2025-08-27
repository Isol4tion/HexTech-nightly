package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Surround;
import me.hextech.remapped.Timer;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Surround_BjIoVRziuWIfEWTJHPVz
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Surround_BjIoVRziuWIfEWTJHPVz INSTANCE;
    public final EnumSetting<Surround> page = this.add(new EnumSetting<Surround>("Page", Surround.General));
    public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, v -> this.page.is(Surround.General)));
    public final BooleanSetting extend = this.add(new BooleanSetting("Extend", true, v -> this.page.is(Surround.General))).setParent();
    public final BooleanSetting onlySelf = this.add(new BooleanSetting("OnlySelf", false, v -> this.page.is(Surround.General) && this.extend.isOpen()));
    public final BooleanSetting inAir = this.add(new BooleanSetting("InAir", true, v -> this.page.is(Surround.Check)));
    private final Timer timer = new Timer();
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, v -> this.page.is(Surround.General)));
    private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true, v -> this.page.is(Surround.General)));
    private final BooleanSetting onlyTick = this.add(new BooleanSetting("OnlyTick", true, v -> this.page.is(Surround.General)));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true, v -> this.page.is(Surround.General)).setParent());
    private final BooleanSetting eatPause = this.add(new BooleanSetting("EatingPause", true, v -> this.breakCrystal.isOpen()));
    private final BooleanSetting center = this.add(new BooleanSetting("Center", true, v -> this.page.is(Surround.General)));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.is(Surround.General)));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true, v -> this.page.is(Surround.General)));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, v -> this.page.getValue() == Surround.Rotate));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, v -> this.page.getValue() == Surround.Rotate));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, v -> this.page.getValue() == Surround.Rotate));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, v -> this.page.getValue() == Surround.Rotate));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0, v -> this.checkFov.getValue() && this.page.getValue() == Surround.Rotate));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, v -> this.page.getValue() == Surround.Rotate));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", false, v -> this.page.is(Surround.Check)));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, v -> this.page.is(Surround.Check)));
    private final BooleanSetting moveDisable = this.add(new BooleanSetting("MoveDisable", true, v -> this.page.is(Surround.Check)));
    private final BooleanSetting jumpDisable = this.add(new BooleanSetting("JumpDisable", true, v -> this.page.is(Surround.Check)));
    public Vec3d directionVec = null;
    double startX = 0.0;
    double startY = 0.0;
    double startZ = 0.0;
    int progress = 0;
    private boolean shouldCenter = true;

    public Surround_BjIoVRziuWIfEWTJHPVz() {
        super("Surround", "Surrounds you with Obsidian", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static boolean selfIntersectPos(BlockPos pos) {
        return Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_5829().method_994(new Box(pos));
    }

    public static boolean otherIntersectPos(BlockPos pos) {
        for (PlayerEntity player : Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1687.method_18456()) {
            if (!player.method_5829().method_994(new Box(pos))) continue;
            return true;
        }
        return false;
    }

    public static Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
        Vec3d vec3d = posTo.method_1020(posFrom);
        return Surround_BjIoVRziuWIfEWTJHPVz.getRotationFromVec(vec3d);
    }

    private static Vec2f getRotationFromVec(Vec3d vec) {
        double d = vec.field_1352;
        double d2 = vec.field_1350;
        double xz = Math.hypot(d, d2);
        d2 = vec.field_1350;
        double d3 = vec.field_1352;
        double yaw = Surround_BjIoVRziuWIfEWTJHPVz.normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = Surround_BjIoVRziuWIfEWTJHPVz.normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_1351, xz)));
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

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.directionVec != null && this.rotate.getValue() && this.yawStep.getValue()) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        if (!this.onlyTick.getValue()) {
            this.onUpdate();
        }
    }

    @Override
    public void onEnable() {
        if (Surround_BjIoVRziuWIfEWTJHPVz.nullCheck()) {
            if (this.moveDisable.getValue() || this.jumpDisable.getValue()) {
                this.disable();
            }
            return;
        }
        this.startX = Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23317();
        this.startY = Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23318();
        this.startZ = Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23321();
        this.shouldCenter = true;
    }

    @EventHandler(priority=-1)
    public void onMove(MoveEvent event) {
        if (Surround_BjIoVRziuWIfEWTJHPVz.nullCheck() || !this.center.getValue() || Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_6128()) {
            return;
        }
        BlockPos blockPos = EntityUtil.getPlayerPos(true);
        if (Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23317() - (double)blockPos.method_10263() - 0.5 <= 0.2 && Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23317() - (double)blockPos.method_10263() - 0.5 >= -0.2 && Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23321() - (double)blockPos.method_10260() - 0.5 <= 0.2 && Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23321() - 0.5 - (double)blockPos.method_10260() >= -0.2) {
            if (this.shouldCenter && (Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_24828() || MovementUtil.isMoving())) {
                event.setX(0.0);
                event.setZ(0.0);
                this.shouldCenter = false;
            }
        } else if (this.shouldCenter) {
            Vec3d centerPos = EntityUtil.getPlayerPos(true).method_46558();
            float rotation = Surround_BjIoVRziuWIfEWTJHPVz.getRotationTo((Vec3d)Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_19538(), (Vec3d)centerPos).field_1343;
            float yawRad = rotation / 180.0f * (float)Math.PI;
            double dist = Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_19538().method_1022(new Vec3d(centerPos.field_1352, Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23318(), centerPos.field_1350));
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
        this.directionVec = null;
        this.progress = 0;
        if (!MovementUtil.isMoving() && !Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1690.field_1903.method_1434()) {
            this.startX = Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23317();
            this.startY = Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23318();
            this.startZ = Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23321();
        }
        BlockPos pos = EntityUtil.getPlayerPos(true);
        double distanceToStart = MathHelper.method_15355((float)((float)Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_5649(this.startX, this.startY, this.startZ)));
        if (this.getBlock() == -1) {
            CommandManager.sendChatMessageWidthId("\u00a7c\u00a7oObsidian" + (this.enderChest.getValue() ? "/EnderChest" : "") + "?", this.hashCode());
            this.disable();
            return;
        }
        if (this.moveDisable.getValue() && distanceToStart > 1.0 || this.jumpDisable.getValue() && Math.abs(this.startY - Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_23318()) > 0.5) {
            this.disable();
            return;
        }
        if (this.usingPause.getValue() && Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_6115()) {
            return;
        }
        if (!this.inAir.getValue() && !Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_24828()) {
            return;
        }
        for (Direction i : Direction.values()) {
            if (i == Direction.field_11036) continue;
            BlockPos offsetPos = pos.method_10093(i);
            if (BlockUtil.getPlaceSide(offsetPos) != null) {
                this.tryPlaceBlock(offsetPos);
            } else if (BlockUtil.canReplace(offsetPos)) {
                this.tryPlaceBlock(this.getHelperPos(offsetPos));
            }
            if (!Surround_BjIoVRziuWIfEWTJHPVz.selfIntersectPos(offsetPos) && (this.onlySelf.getValue() || !Surround_BjIoVRziuWIfEWTJHPVz.otherIntersectPos(offsetPos)) || !this.extend.getValue()) continue;
            for (Direction i2 : Direction.values()) {
                if (i2 == Direction.field_11036) continue;
                BlockPos offsetPos2 = offsetPos.method_10093(i2);
                if (Surround_BjIoVRziuWIfEWTJHPVz.selfIntersectPos(offsetPos2) || !this.onlySelf.getValue() && Surround_BjIoVRziuWIfEWTJHPVz.otherIntersectPos(offsetPos2)) {
                    for (Direction i3 : Direction.values()) {
                        if (i3 == Direction.field_11036) continue;
                        this.tryPlaceBlock(offsetPos2);
                        BlockPos offsetPos3 = offsetPos2.method_10093(i3);
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
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            return;
        }
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (!BlockUtil.canPlace(pos, 6.0, true)) {
            return;
        }
        if (this.rotate.getValue() && !this.faceVector(directionVec)) {
            return;
        }
        if (this.breakCrystal.getValue()) {
            CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.eatPause.getValue());
        } else if (BlockUtil.hasEntity(pos, false)) {
            return;
        }
        int old = Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_31548().field_7545;
        this.doSwap(block);
        if (BlockUtil.airPlace()) {
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos, Direction.field_11033, false, Hand.field_5808, this.packetPlace.getValue());
        } else {
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), false, Hand.field_5808, this.packetPlace.getValue());
        }
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(old);
        }
        ++this.progress;
        this.timer.reset();
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
            InventoryUtil.inventorySwap(slot, Surround_BjIoVRziuWIfEWTJHPVz.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getBlock() {
        if (this.inventory.getValue()) {
            if (InventoryUtil.findBlockInventorySlot(Blocks.field_10540) != -1 || !this.enderChest.getValue()) {
                return InventoryUtil.findBlockInventorySlot(Blocks.field_10540);
            }
            return InventoryUtil.findBlockInventorySlot(Blocks.field_10443);
        }
        if (InventoryUtil.findBlock(Blocks.field_10540) != -1 || !this.enderChest.getValue()) {
            return InventoryUtil.findBlock(Blocks.field_10540);
        }
        return InventoryUtil.findBlock(Blocks.field_10443);
    }

    public BlockPos getHelperPos(BlockPos pos) {
        for (Direction i : Direction.values()) {
            if (this.detectMining.getValue() && HexTech.BREAK.isMining(pos.method_10093(i)) || !BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) || !BlockUtil.canPlace(pos.method_10093(i))) continue;
            return pos.method_10093(i);
        }
        return null;
    }
}
