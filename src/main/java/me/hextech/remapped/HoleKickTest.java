package me.hextech.remapped;

import me.hextech.remapped.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Enum_EeQOXZQmWkBIGBYWBifQ;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Timer;
import me.hextech.remapped.WebAuraTick_gaIdrzDzsbegzNTtPQoV;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HoleKickTest
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HoleKickTest INSTANCE;
    public static boolean dopush;
    public final EnumSetting<Enum_EeQOXZQmWkBIGBYWBifQ> page = this.add(new EnumSetting<Enum_EeQOXZQmWkBIGBYWBifQ>("Page", Enum_EeQOXZQmWkBIGBYWBifQ.General));
    public final BooleanSetting syncCrystal = this.add(new BooleanSetting("SyncCrystal", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.Sync));
    public final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    public final BooleanSetting allowWeb = this.add(new BooleanSetting("AllowWeb", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    public final SliderSetting updateDelay = this.add(new SliderSetting("UpdateDelay", 100, 0, 500, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    public final SliderSetting surroundCheck = this.add(new SliderSetting("SurroundCheck", 2, 0, 4, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final Timer timer = new Timer();
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting preferAnchor = this.add(new BooleanSetting("PreferAnchor", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting waitBurrow = this.add(new BooleanSetting("WaitBurrow", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting cancelBurrow = this.add(new BooleanSetting("CancelBurrow", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting cancelBlink = this.add(new BooleanSetting("CancelBlink", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting nomove = this.add(new BooleanSetting("MovePause", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.Sync));
    private final BooleanSetting syncweb = this.add(new BooleanSetting("SyncWeb[!Test]", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.Sync));
    private final BooleanSetting pistonPacket = this.add(new BooleanSetting("PistonPacket", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting redStonePacket = this.add(new BooleanSetting("RedStonePacket", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting noEating = this.add(new BooleanSetting("NoEating", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting attackCrystal = this.add(new BooleanSetting("BreakCrystal", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting mine = this.add(new BooleanSetting("Mine", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting checkPiston = this.add(new BooleanSetting("CheckPiston", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting pullBack = this.add(new BooleanSetting("PullBack", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General).setParent());
    private final BooleanSetting onlyBurrow = this.add(new BooleanSetting("OnlyBurrow", true, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", false, v -> this.page.getValue() == Enum_EeQOXZQmWkBIGBYWBifQ.General));
    public PlayerEntity displayTarget = null;
    public Vec3d directionVec = null;
    private Enum_EeQOXZQmWkBIGBYWBifQ Page;

    public HoleKickTest() {
        super("HoleKickTest", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static void pistonFacing(Direction i) {
        if (i == Direction.field_11034) {
            EntityUtil.sendYawAndPitch(-90.0f, 5.0f);
        } else if (i == Direction.field_11039) {
            EntityUtil.sendYawAndPitch(90.0f, 5.0f);
        } else if (i == Direction.field_11043) {
            EntityUtil.sendYawAndPitch(180.0f, 5.0f);
        } else if (i == Direction.field_11035) {
            EntityUtil.sendYawAndPitch(0.0f, 5.0f);
        }
    }

    public static boolean isTargetHere(BlockPos pos, Entity target) {
        return new Box(pos).method_994(target.method_5829());
    }

    public static boolean isInWeb(PlayerEntity player) {
        Vec3d playerPos = player.method_19538();
        for (float x : new float[]{0.0f, 0.3f, -0.3f}) {
            for (float z : new float[]{0.0f, 0.3f, -0.3f}) {
                for (float y : new float[]{0.0f, 1.0f, -1.0f}) {
                    BlockPosX pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214() + (double)y, playerPos.method_10215() + (double)z);
                    if (!HoleKickTest.isTargetHere(pos, (Entity)player) || HoleKickTest.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.field_10343) continue;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastBreakTimer.reset();
    }

    public boolean check(boolean onlyStatic) {
        return MovementUtil.isMoving() && onlyStatic;
    }

    @Override
    public void onUpdate() {
        if (!this.timer.passedMs(this.updateDelay.getValue())) {
            return;
        }
        if (HoleKickTest.mc.player != null && this.selfGround.getValue() && !HoleKickTest.mc.player.method_24828()) {
            if (this.autoDisable.getValue()) {
                this.disable();
            }
            return;
        }
        if (HoleKickTest.mc.player != null && this.check(this.nomove.getValue())) {
            return;
        }
        if (this.preferAnchor.getValue() && AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos != null) {
            return;
        }
        if (this.waitBurrow.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.placePos == null) {
            return;
        }
        if (this.cancelBurrow.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            return;
        }
        if (this.cancelBlink.getValue() && Blink.INSTANCE.isOn()) {
            return;
        }
        if (this.syncCrystal.getValue() && AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null) {
            return;
        }
        if (this.syncweb.getValue() && WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.pos == null) {
            return;
        }
        if (this.findBlock(Blocks.field_10002) == -1 || this.findClass(PistonBlock.class) == -1) {
            if (this.autoDisable.getValue()) {
                this.disable();
            }
            return;
        }
        if (this.noEating.getValue() && HoleKickTest.mc.player.method_6115()) {
            return;
        }
        this.timer.reset();
        for (PlayerEntity target : CombatUtil.getEnemies(this.range.getValue())) {
            if (!this.canPush(target).booleanValue() || !target.method_24828() && this.onlyGround.getValue()) continue;
            this.displayTarget = target;
            if (!this.doPush(EntityUtil.getEntityPos((Entity)target), target)) continue;
            return;
        }
        if (this.autoDisable.getValue()) {
            this.disable();
        }
        this.displayTarget = null;
    }

    private boolean checkPiston(BlockPos targetPos) {
        for (Direction i : Direction.values()) {
            BlockPos pos;
            if (i == Direction.DOWN || i == Direction.UP || !(this.getBlock((pos = targetPos.up()).offset(i)) instanceof PistonBlock) || ((Direction)this.getBlockState(pos.offset(i)).method_11654((Property)FacingBlock.field_10927)).method_10153() != i) continue;
            for (Direction i2 : Direction.values()) {
                if (this.getBlock(pos.offset(i).offset(i2)) != Blocks.field_10002 || !this.mine.getValue()) continue;
                this.mine(pos.offset(i).offset(i2));
                if (this.autoDisable.getValue()) {
                    this.disable();
                }
                return true;
            }
        }
        return false;
    }

    public boolean doPush(BlockPos targetPos, PlayerEntity target) {
        if (this.checkPiston.getValue() && this.checkPiston(targetPos)) {
            return true;
        }
        if (!HoleKickTest.mc.world.getBlockState(targetPos.method_10086(2)).method_51366()) {
            BlockPos pos;
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP || !(this.getBlock(pos = targetPos.offset(i).up()) instanceof PistonBlock) || this.getBlockState(pos.method_10079(i, -2)).method_51366() || this.getBlock(pos.method_10079(i, -2).up()) != Blocks.AIR && this.getBlock(pos.method_10079(i, -2).up()) != Blocks.field_10002 || ((Direction)this.getBlockState(pos).method_11654((Property)FacingBlock.field_10927)).method_10153() != i) continue;
                for (Direction i2 : Direction.values()) {
                    if (this.getBlock(pos.offset(i2)) != Blocks.field_10002) continue;
                    if (this.mine.getValue()) {
                        this.mine(pos.offset(i2));
                    }
                    if (this.autoDisable.getValue()) {
                        this.disable();
                    }
                    return true;
                }
            }
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP || !(this.getBlock(pos = targetPos.offset(i).up()) instanceof PistonBlock) || this.getBlockState(pos.method_10079(i, -2)).method_51366() || this.getBlock(pos.method_10079(i, -2).up()) != Blocks.AIR && this.getBlock(pos.method_10079(i, -2).up()) != Blocks.field_10002 || ((Direction)this.getBlockState(pos).method_11654((Property)FacingBlock.field_10927)).method_10153() != i || !this.doPower(pos)) continue;
                return true;
            }
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP) continue;
                pos = targetPos.offset(i).up();
                if ((HoleKickTest.mc.player.getY() - target.getY() <= -1.0 || HoleKickTest.mc.player.getY() - target.getY() >= 2.0) && BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6) continue;
                this.attackCrystal(pos);
                if (!this.isTrueFacing(pos, i) || !BlockUtil.clientCanPlace(pos, false) || this.getBlockState(pos.method_10079(i, -2)).method_51366() || this.getBlockState(pos.method_10079(i, -2).up()).method_51366()) continue;
                if (BlockUtil.getPlaceSide(pos) == null && this.downPower(pos)) break;
                this.doPiston(i, pos, this.mine.getValue());
                return true;
            }
            if (this.getBlock(targetPos) == Blocks.AIR && this.onlyBurrow.getValue() || !this.pullBack.getValue()) {
                if (this.autoDisable.getValue()) {
                    this.disable();
                }
                return true;
            }
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP) continue;
                pos = targetPos.offset(i).up();
                for (Direction i2 : Direction.values()) {
                    if (!(this.getBlock(pos) instanceof PistonBlock) || this.getBlock(pos.offset(i2)) != Blocks.field_10002 || ((Direction)this.getBlockState(pos).method_11654((Property)FacingBlock.field_10927)).method_10153() != i) continue;
                    this.mine(pos.offset(i2));
                    if (this.autoDisable.getValue()) {
                        this.disable();
                    }
                    return true;
                }
            }
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP) continue;
                pos = targetPos.offset(i).up();
                for (Direction i2 : Direction.values()) {
                    if (!(this.getBlock(pos) instanceof PistonBlock) || this.getBlock(pos.offset(i2)) != Blocks.AIR || ((Direction)this.getBlockState(pos).method_11654((Property)FacingBlock.field_10927)).method_10153() != i) continue;
                    this.attackCrystal(pos.offset(i2));
                    if (this.doPower(pos, i2)) continue;
                    this.mine(pos.offset(i2));
                    return true;
                }
            }
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP) continue;
                pos = targetPos.offset(i).up();
                if (HoleKickTest.mc.player != null && (HoleKickTest.mc.player.getY() - target.getY() <= -1.0 || HoleKickTest.mc.player.getY() - target.getY() >= 2.0) && BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6) continue;
                this.attackCrystal(pos);
                if (!this.isTrueFacing(pos, i) || !BlockUtil.clientCanPlace(pos, false) || this.downPower(pos)) continue;
                this.doPiston(i, pos, true);
                return true;
            }
        } else {
            BlockPos pos;
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP || !(this.getBlock(pos = targetPos.offset(i).up()) instanceof PistonBlock) || (!HoleKickTest.mc.world.isAir(pos.method_10079(i, -2)) || !HoleKickTest.mc.world.isAir(pos.method_10079(i, -2).method_10074())) && !HoleKickTest.isTargetHere(pos.method_10079(i, 2), (Entity)target) || ((Direction)this.getBlockState(pos).method_11654((Property)FacingBlock.field_10927)).method_10153() != i) continue;
                for (Direction i2 : Direction.values()) {
                    if (this.getBlock(pos.offset(i2)) != Blocks.field_10002) continue;
                    if (this.mine.getValue()) {
                        this.mine(pos.offset(i2));
                    }
                    if (this.autoDisable.getValue()) {
                        this.disable();
                    }
                    return true;
                }
            }
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP || !(this.getBlock(pos = targetPos.offset(i).up()) instanceof PistonBlock) || (!HoleKickTest.mc.world.isAir(pos.method_10079(i, -2)) || !HoleKickTest.mc.world.isAir(pos.method_10079(i, -2).method_10074())) && !HoleKickTest.isTargetHere(pos.method_10079(i, 2), (Entity)target) || ((Direction)this.getBlockState(pos).method_11654((Property)FacingBlock.field_10927)).method_10153() != i || !this.doPower(pos)) continue;
                return true;
            }
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP) continue;
                pos = targetPos.offset(i).up();
                if (HoleKickTest.mc.player != null && (HoleKickTest.mc.player.getY() - target.getY() <= -1.0 || HoleKickTest.mc.player.getY() - target.getY() >= 2.0) && BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6) continue;
                this.attackCrystal(pos);
                if (!this.isTrueFacing(pos, i) || !BlockUtil.clientCanPlace(pos, false) || (!HoleKickTest.mc.world.isAir(pos.method_10079(i, -2)) || !HoleKickTest.mc.world.isAir(pos.method_10079(i, -2).method_10074())) && !HoleKickTest.isTargetHere(pos.method_10079(i, 2), (Entity)target) || this.getBlockState(pos.method_10079(i, -2).up()).method_51366()) continue;
                if (BlockUtil.getPlaceSide(pos) != null || !this.downPower(pos)) {
                    dopush = true;
                    this.doPiston(i, pos, this.mine.getValue());
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean isTrueFacing(BlockPos pos, Direction facing) {
        Vec3d hitVec;
        if (this.yawDeceive.getValue()) {
            return true;
        }
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            side = Direction.UP;
        }
        return Direction.method_10150((double)EntityUtil.getLegitRotations(hitVec = pos.offset((side = side.method_10153()).method_10153()).toCenterPos().method_1019(new Vec3d((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5)))[0]) == facing;
    }

    private boolean doPower(BlockPos pos, Direction i2) {
        if (!BlockUtil.canPlace(pos.offset(i2), this.placeRange.getValue())) {
            return true;
        }
        int old = 0;
        if (HoleKickTest.mc.player != null) {
            old = HoleKickTest.mc.player.method_31548().field_7545;
        }
        int power = this.findBlock(Blocks.field_10002);
        this.doSwap(power);
        BlockUtil.placeBlock(pos.offset(i2), this.rotate.getValue(), this.redStonePacket.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(power);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(old);
        }
        return false;
    }

    private boolean doPower(BlockPos pos) {
        Direction facing = BlockUtil.getBestNeighboring(pos, null);
        if (facing != null) {
            this.attackCrystal(pos.offset(facing));
            if (!this.doPower(pos, facing)) {
                return true;
            }
        }
        for (Direction i2 : Direction.values()) {
            this.attackCrystal(pos.offset(i2));
            if (this.doPower(pos, i2)) continue;
            return true;
        }
        return false;
    }

    private boolean downPower(BlockPos pos) {
        if (BlockUtil.getPlaceSide(pos) == null) {
            boolean noPower = true;
            for (Direction i2 : Direction.values()) {
                if (this.getBlock(pos.offset(i2)) != Blocks.field_10002) continue;
                noPower = false;
                break;
            }
            if (noPower) {
                if (!BlockUtil.canPlace(pos.method_10069(0, -1, 0), this.placeRange.getValue())) {
                    return true;
                }
                int old = 0;
                if (HoleKickTest.mc.player != null) {
                    old = HoleKickTest.mc.player.method_31548().field_7545;
                }
                int power = this.findBlock(Blocks.field_10002);
                this.doSwap(power);
                BlockUtil.placeBlock(pos.method_10069(0, -1, 0), this.rotate.getValue(), this.redStonePacket.getValue());
                if (this.inventory.getValue()) {
                    this.doSwap(power);
                    EntityUtil.syncInventory();
                } else {
                    this.doSwap(old);
                }
            }
        }
        return false;
    }

    private void doPiston(Direction i, BlockPos pos, boolean mine) {
        if (BlockUtil.canPlace(pos, this.placeRange.getValue())) {
            int piston = this.findClass(PistonBlock.class);
            Direction side = BlockUtil.getPlaceSide(pos);
            if (this.rotate.getValue()) {
                EntityUtil.facePosSide(pos.offset(side), side.method_10153());
            }
            if (this.yawDeceive.getValue()) {
                HoleKickTest.pistonFacing(i);
            }
            int old = 0;
            if (HoleKickTest.mc.player != null) {
                old = HoleKickTest.mc.player.method_31548().field_7545;
            }
            this.doSwap(piston);
            BlockUtil.placeBlock(pos, false, this.pistonPacket.getValue());
            if (this.inventory.getValue()) {
                this.doSwap(piston);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(old);
            }
            if (this.rotate.getValue()) {
                EntityUtil.facePosSide(pos.offset(side), side.method_10153());
            }
            for (Direction i2 : Direction.values()) {
                if (this.getBlock(pos.offset(i2)) != Blocks.field_10002) continue;
                if (mine) {
                    this.mine(pos.offset(i2));
                }
                if (this.autoDisable.getValue()) {
                    this.disable();
                }
                return;
            }
            this.doPower(pos);
        }
    }

    @Override
    public String getInfo() {
        if (this.displayTarget != null) {
            return this.displayTarget.method_5477().getString();
        }
        return null;
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            if (HoleKickTest.mc.player != null) {
                InventoryUtil.inventorySwap(slot, HoleKickTest.mc.player.method_31548().field_7545);
            }
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public int findBlock(Block blockIn) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(blockIn);
        }
        return InventoryUtil.findBlock(blockIn);
    }

    public int findClass(Class clazz) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findClassInventorySlot(clazz);
        }
        return InventoryUtil.findClass(clazz);
    }

    private void attackCrystal(BlockPos pos) {
        if (!this.attackCrystal.getValue()) {
            return;
        }
        if (HoleKickTest.mc.world != null) {
            for (Entity crystal : HoleKickTest.mc.world.method_18112()) {
                if (!(crystal instanceof EndCrystalEntity) || (double)MathHelper.method_15355((float)((float)crystal.method_5649((double)pos.method_10263() + 0.5, (double)pos.method_10264(), (double)pos.method_10260() + 0.5))) > 2.0) continue;
                CombatUtil.attackCrystal(crystal, this.rotate.getValue(), false);
                return;
            }
        }
    }

    private void mine(BlockPos pos) {
        SpeedMine.INSTANCE.mine(pos);
    }

    private Block getBlock(BlockPos pos) {
        return HoleKickTest.mc.world.getBlockState(pos).getBlock();
    }

    private BlockState getBlockState(BlockPos pos) {
        if (HoleKickTest.mc.world != null) {
            return HoleKickTest.mc.world.getBlockState(pos);
        }
        return null;
    }

    private Boolean canPush(PlayerEntity player) {
        if (this.onlyGround.getValue() && !player.method_24828()) {
            return false;
        }
        if (!this.allowWeb.getValue() && HoleKickTest.isInWeb(player)) {
            return false;
        }
        int progress = 0;
        if (!HoleKickTest.mc.world.isAir((BlockPos)new BlockPosX(player.getX() + 1.0, player.getY() + 0.5, player.getZ()))) {
            ++progress;
        }
        if (!HoleKickTest.mc.world.isAir((BlockPos)new BlockPosX(player.getX() - 1.0, player.getY() + 0.5, player.getZ()))) {
            ++progress;
        }
        if (!HoleKickTest.mc.world.isAir((BlockPos)new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ() + 1.0))) {
            ++progress;
        }
        if (!HoleKickTest.mc.world.isAir((BlockPos)new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ() - 1.0))) {
            ++progress;
        }
        if (!HoleKickTest.mc.world.isAir((BlockPos)new BlockPosX(player.getX(), player.getY() + 2.5, player.getZ()))) {
            for (Direction i : Direction.values()) {
                BlockPos pos;
                if (i == Direction.UP || i == Direction.DOWN || (!HoleKickTest.mc.world.isAir(pos = EntityUtil.getEntityPos((Entity)player).offset(i)) || !HoleKickTest.mc.world.isAir(pos.up())) && !HoleKickTest.isTargetHere(pos, (Entity)player)) continue;
                if (!HoleKickTest.mc.world.isAir((BlockPos)new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ()))) {
                    return true;
                }
                return (double)progress > this.surroundCheck.getValue() - 1.0;
            }
            return false;
        }
        if (!HoleKickTest.mc.world.method_39454((Entity)player, new Box((BlockPos)new BlockPosX(player.getX(), player.getY() + 2.5, player.getZ())))) {
            for (Direction i : Direction.values()) {
                if (i == Direction.UP || i == Direction.DOWN) continue;
                BlockPos pos = EntityUtil.getEntityPos((Entity)player).offset(i);
                Box box = player.method_5829().method_997(new Vec3d((double)i.method_10148(), (double)i.method_10164(), (double)i.method_10165()));
                if (this.getBlock(pos.up()) == Blocks.field_10379 || HoleKickTest.mc.world.method_39454((Entity)player, box.method_989(0.0, 1.0, 0.0)) || HoleKickTest.isTargetHere(pos, (Entity)player) || !HoleKickTest.mc.world.method_39454((Entity)player, new Box((BlockPos)new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ())))) continue;
                return true;
            }
        }
        return (double)progress > this.surroundCheck.getValue() - 1.0 || CombatUtil.isHard(new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ()));
    }

    public void placeBlock(BlockPos pos, boolean rotate, boolean bypass) {
        Direction side;
        if (BlockUtil.airPlace()) {
            for (Direction i : Direction.values()) {
                if (HoleKickTest.mc.world == null || !HoleKickTest.mc.world.isAir(pos.offset(i))) continue;
                BlockUtil.clickBlock(pos, i, rotate);
                return;
            }
        }
        if ((side = BlockUtil.getPlaceSide(pos)) == null) {
            return;
        }
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        EntityUtil.swingHand(Hand.field_5808, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        BlockUtil.placedPos.add(pos);
        boolean sprint = false;
        if (HoleKickTest.mc.player != null) {
            sprint = HoleKickTest.mc.player.method_5624();
        }
        boolean sneak = false;
        if (sprint) {
            HoleKickTest.mc.player.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)HoleKickTest.mc.player, ClientCommandC2SPacket.Mode.field_12985));
        }
        if (sneak) {
            HoleKickTest.mc.player.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)HoleKickTest.mc.player, ClientCommandC2SPacket.Mode.field_12979));
        }
        BlockUtil.clickBlock(pos.offset(side), side.method_10153(), rotate);
        if (sneak) {
            HoleKickTest.mc.player.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)HoleKickTest.mc.player, ClientCommandC2SPacket.Mode.field_12984));
        }
        if (sprint) {
            HoleKickTest.mc.player.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)HoleKickTest.mc.player, ClientCommandC2SPacket.Mode.field_12981));
        }
        if (bypass) {
            EntityUtil.swingHand(Hand.field_5808, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        }
    }
}
