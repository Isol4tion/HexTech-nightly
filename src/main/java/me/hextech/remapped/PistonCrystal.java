package me.hextech.remapped;

import java.util.Iterator;
import me.hextech.HexTech;
import me.hextech.remapped.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.ThunderExplosionUtil;
import me.hextech.remapped.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PistonCrystal
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static PistonCrystal INSTANCE;
    public final EnumSetting<_nsRHxiHWZMPWnytkAhif> page = this.add(new EnumSetting<_nsRHxiHWZMPWnytkAhif>("Page", _nsRHxiHWZMPWnytkAhif.General));
    public final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Rotate));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", false, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Rotate));
    private final BooleanSetting newRotate = this.add(new BooleanSetting("NewRotate", false, v -> this.rotate.isOpen() && this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Rotate));
    private final SliderSetting yawStep = this.add(new SliderSetting("YawStep", 0.3f, 0.1f, 1.0, 0.01f, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Rotate));
    private final BooleanSetting packet = this.add(new BooleanSetting("Packet", false, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Rotate));
    private final BooleanSetting checkLook = this.add(new BooleanSetting("CheckLook", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Rotate));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.checkLook.getValue() && this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Rotate));
    private final BooleanSetting autoYaw = this.add(new BooleanSetting("AutoYaw", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Rotate));
    private final BooleanSetting preferAnchor = this.add(new BooleanSetting("PreferAnchor", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final BooleanSetting preferCrystal = this.add(new BooleanSetting("PreferCrystal", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 5.0, 1.0, 8.0, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting range = this.add(new SliderSetting("TargetRange", 4.0, 1.0, 8.0, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting updateDelay = this.add(new SliderSetting("PlaceDelay", 100, 0, 500, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting posUpdateDelay = this.add(new SliderSetting("UpdateDelay", 500, 0, 1000, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting stageSetting = this.add(new SliderSetting("Stage", 4, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting pistonStage = this.add(new SliderSetting("PistonStage", 1, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting pistonMaxStage = this.add(new SliderSetting("PistonMaxStage", 1, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting powerStage = this.add(new SliderSetting("PowerStage", 3, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting powerMaxStage = this.add(new SliderSetting("PowerMaxStage", 3, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting crystalStage = this.add(new SliderSetting("CrystalStage", 4, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting crystalMaxStage = this.add(new SliderSetting("CrystalMaxStage", 4, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting fireStage = this.add(new SliderSetting("FireStage", 2, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final SliderSetting fireMaxStage = this.add(new SliderSetting("FireMaxStage", 2, 1, 10, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final BooleanSetting endSwing = this.add(new BooleanSetting("EndSwing", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final BooleanSetting debug = this.add(new BooleanSetting("Debug", false, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final BooleanSetting fire = this.add(new BooleanSetting("Fire", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final BooleanSetting switchPos = this.add(new BooleanSetting("Switch", false, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("SelfGround", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final BooleanSetting onlyStatic = this.add(new BooleanSetting("MovingPause", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final BooleanSetting noEating = this.add(new BooleanSetting("NoEating", true, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final BooleanSetting eatingBreak = this.add(new BooleanSetting("EatingBreak", false, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.Misc));
    private final Timer timer = new Timer();
    private final Timer crystalTimer = new Timer();
    public SliderSetting speed = this.add(new SliderSetting("MaxSpeed", 8, 0, 20, v -> this.page.getValue() == _nsRHxiHWZMPWnytkAhif.General));
    public Vec3d directionVec = null;
    public BlockPos bestPos = null;
    public BlockPos bestOPos = null;
    public Direction bestFacing = null;
    public double distance = 100.0;
    public boolean getPos = false;
    public int stage = 1;
    private PlayerEntity target = null;
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;

    public PistonCrystal() {
        super("PistonCrystal", Module_JlagirAibYQgkHtbRnhw.Combat);
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

    private static boolean canFire(BlockPos pos) {
        if (BlockUtil.canReplace(pos.method_10074())) {
            return false;
        }
        if (PistonCrystal.mc.field_1687 != null && !PistonCrystal.mc.field_1687.method_22347(pos)) {
            return false;
        }
        if (!BlockUtil.canClick(pos.method_10093(Direction.field_11033))) {
            return false;
        }
        return BlockUtil.isStrictDirection(pos.method_10074(), Direction.field_11036);
    }

    @EventHandler
    public void onRotate(RotateEvent event) {
        if (this.newRotate.getValue() && this.directionVec != null) {
            float[] newAngle = this.injectStep(EntityUtil.getLegitRotations(this.directionVec), this.yawStep.getValueFloat());
            if (newAngle != null) {
                this.lastYaw = newAngle[0];
            }
            if (newAngle != null) {
                this.lastPitch = newAngle[1];
            }
            event.setYaw(this.lastYaw);
            event.setPitch(this.lastPitch);
        } else {
            this.lastYaw = HexTech.ROTATE.lastYaw;
            this.lastPitch = RotateManager.lastPitch;
        }
    }

    public void onTick() {
        if (this.pistonStage.getValue() > this.stageSetting.getValue()) {
            this.pistonStage.setValue(this.stageSetting.getValue());
        }
        if (this.fireStage.getValue() > this.stageSetting.getValue()) {
            this.fireStage.setValue(this.stageSetting.getValue());
        }
        if (this.powerStage.getValue() > this.stageSetting.getValue()) {
            this.powerStage.setValue(this.stageSetting.getValue());
        }
        if (this.crystalStage.getValue() > this.stageSetting.getValue()) {
            this.crystalStage.setValue(this.stageSetting.getValue());
        }
        if (this.pistonMaxStage.getValue() > this.stageSetting.getValue()) {
            this.pistonMaxStage.setValue(this.stageSetting.getValue());
        }
        if (this.fireMaxStage.getValue() > this.stageSetting.getValue()) {
            this.fireMaxStage.setValue(this.stageSetting.getValue());
        }
        if (this.powerMaxStage.getValue() > this.stageSetting.getValue()) {
            this.powerMaxStage.setValue(this.stageSetting.getValue());
        }
        if (this.crystalMaxStage.getValue() > this.stageSetting.getValue()) {
            this.crystalMaxStage.setValue(this.stageSetting.getValue());
        }
        if (this.crystalMaxStage.getValue() < this.crystalStage.getValue()) {
            this.crystalStage.setValue(this.crystalMaxStage.getValue());
        }
        if (this.powerMaxStage.getValue() < this.powerStage.getValue()) {
            this.powerStage.setValue(this.powerMaxStage.getValue());
        }
        if (this.pistonMaxStage.getValue() < this.pistonStage.getValue()) {
            this.pistonStage.setValue(this.pistonMaxStage.getValue());
        }
        if (this.fireMaxStage.getValue() < this.fireStage.getValue()) {
            this.fireStage.setValue(this.fireMaxStage.getValue());
        }
    }

    @Override
    public void onUpdate() {
        this.onTick();
        this.directionVec = null;
        this.target = CombatUtil.getClosestEnemy(this.range.getValue());
        if (this.target == null) {
            return;
        }
        if (this.preferAnchor.getValue() && AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos != null) {
            return;
        }
        if (this.preferCrystal.getValue() && AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null) {
            return;
        }
        if (this.noEating.getValue() && EntityUtil.isUsing()) {
            return;
        }
        if (PistonCrystal.mc.field_1724 != null && this.check(this.onlyStatic.getValue(), !PistonCrystal.mc.field_1724.method_24828(), this.onlyGround.getValue())) {
            return;
        }
        BlockPos pos = EntityUtil.getEntityPos((Entity)this.target, true);
        if (!EntityUtil.isUsing() || this.eatingBreak.getValue()) {
            if (this.checkCrystal(pos.method_10086(0))) {
                this.attackCrystal(pos.method_10086(0), this.rotate.getValue(), false);
            }
            if (this.checkCrystal(pos.method_10086(1))) {
                this.attackCrystal(pos.method_10086(1), this.rotate.getValue(), false);
            }
            if (this.checkCrystal(pos.method_10086(2))) {
                this.attackCrystal(pos.method_10086(2), this.rotate.getValue(), false);
            }
        }
        if (PistonCrystal.mc.field_1687 != null && this.bestPos != null) {
            PistonCrystal.mc.field_1687.method_8320(this.bestPos).method_26204();
        }
        if (this.crystalTimer.passedMs(this.posUpdateDelay.getValueInt())) {
            this.stage = 0;
            this.distance = 100.0;
            this.getPos = false;
            this.getBestPos(pos.method_10086(2));
            this.getBestPos(pos.method_10084());
        }
        if (!this.timer.passedMs(this.updateDelay.getValueInt())) {
            return;
        }
        if (this.getPos && this.bestPos != null) {
            this.timer.reset();
            if (this.debug.getValue()) {
                CommandManager.sendChatMessage("[Debug] PistonPos:" + String.valueOf(this.bestPos) + " Facing:" + String.valueOf(this.bestFacing) + " CrystalPos:" + String.valueOf(this.bestOPos.method_10093(this.bestFacing)));
            }
            if (this.check(this.onlyStatic.getValue(), !PistonCrystal.mc.field_1724.method_24828(), this.onlyGround.getValue())) {
                return;
            }
            this.doPistonAura(this.bestPos, this.bestFacing, this.bestOPos);
        }
    }

    public void attackCrystal(BlockPos pos, boolean rotate, boolean eatingPause) {
        block0: {
            Iterator iterator = PistonCrystal.mc.field_1687.method_18467(EndCrystalEntity.class, new Box(pos)).iterator();
            if (!iterator.hasNext()) break block0;
            EndCrystalEntity entity = (EndCrystalEntity)iterator.next();
            this.attackCrystal((Entity)entity, rotate, eatingPause);
        }
    }

    public void attackCrystal(Entity crystal, boolean rotate, boolean usingPause) {
        if (!CombatUtil.breakTimer.passedMs((long)(CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackDelay.getValue() * 1000.0))) {
            return;
        }
        if (usingPause && EntityUtil.isUsing()) {
            return;
        }
        if (crystal != null) {
            CombatUtil.breakTimer.reset();
            if (rotate && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue() && !this.faceVector(new Vec3d(crystal.method_23317(), crystal.method_23318() + 0.25, crystal.method_23321()))) {
                return;
            }
            PistonCrystal.mc.field_1724.field_3944.method_52787((Packet)PlayerInteractEntityC2SPacket.method_34206((Entity)crystal, (boolean)PistonCrystal.mc.field_1724.method_5715()));
            PistonCrystal.mc.field_1724.method_7350();
            EntityUtil.swingHand(Hand.field_5808, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        }
    }

    public boolean check(boolean onlyStatic, boolean onGround, boolean onlyGround) {
        if (MovementUtil.isMoving() && onlyStatic) {
            return true;
        }
        if (onGround && onlyGround) {
            return true;
        }
        if (this.findBlock(Blocks.field_10002) == -1) {
            return true;
        }
        if (this.findClass(PistonBlock.class) == -1) {
            return true;
        }
        return this.findItem(Items.field_8301) == -1;
    }

    private boolean checkCrystal(BlockPos pos) {
        if (PistonCrystal.mc.field_1687 != null) {
            for (Entity entity : PistonCrystal.mc.field_1687.method_18467(EndCrystalEntity.class, new Box(pos))) {
                float damage = ThunderExplosionUtil.calculateDamage(entity.method_19538(), this.target, this.target, 6.0f);
                if (!(damage > 6.0f)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean checkCrystal2(BlockPos pos) {
        if (PistonCrystal.mc.field_1687 != null) {
            for (Entity entity : PistonCrystal.mc.field_1687.method_18467(Entity.class, new Box(pos))) {
                if (!(entity instanceof EndCrystalEntity) || !EntityUtil.getEntityPos(entity).equals((Object)pos)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getInfo() {
        if (this.target != null) {
            return this.target.method_5477().getString();
        }
        return null;
    }

    private void getBestPos(BlockPos pos) {
        for (Direction i : Direction.values()) {
            if (i == Direction.field_11033 || i == Direction.field_11036) continue;
            this.getPos(pos, i);
        }
    }

    private void getPos(BlockPos pos, Direction i) {
        if (!BlockUtil.canPlaceCrystal(pos.method_10093(i)) && !this.checkCrystal2(pos.method_10093(i))) {
            return;
        }
        this.getPos(pos.method_10079(i, 3), i, pos);
        this.getPos(pos.method_10079(i, 3).method_10084(), i, pos);
        int offsetX = pos.method_10093(i).method_10263() - pos.method_10263();
        int offsetZ = pos.method_10093(i).method_10260() - pos.method_10260();
        this.getPos(pos.method_10079(i, 3).method_10069(offsetZ, 0, offsetX), i, pos);
        this.getPos(pos.method_10079(i, 3).method_10069(-offsetZ, 0, -offsetX), i, pos);
        this.getPos(pos.method_10079(i, 3).method_10069(offsetZ, 1, offsetX), i, pos);
        this.getPos(pos.method_10079(i, 3).method_10069(-offsetZ, 1, -offsetX), i, pos);
        this.getPos(pos.method_10079(i, 2), i, pos);
        this.getPos(pos.method_10079(i, 2).method_10084(), i, pos);
        this.getPos(pos.method_10079(i, 2).method_10069(offsetZ, 0, offsetX), i, pos);
        this.getPos(pos.method_10079(i, 2).method_10069(-offsetZ, 0, -offsetX), i, pos);
        this.getPos(pos.method_10079(i, 2).method_10069(offsetZ, 1, offsetX), i, pos);
        this.getPos(pos.method_10079(i, 2).method_10069(-offsetZ, 1, -offsetX), i, pos);
    }

    private void getPos(BlockPos pos, Direction facing, BlockPos oPos) {
        if (PistonCrystal.mc.field_1687 != null && this.switchPos.getValue() && this.bestPos != null && this.bestPos.equals((Object)pos) && PistonCrystal.mc.field_1687.method_22347(this.bestPos)) {
            return;
        }
        if (!BlockUtil.canPlace(pos, this.placeRange.getValue()) && !(this.getBlock(pos) instanceof PistonBlock)) {
            return;
        }
        if (this.findClass(PistonBlock.class) == -1) {
            return;
        }
        if (!(this.getBlock(pos) instanceof PistonBlock)) {
            if (PistonCrystal.mc.field_1724 != null && (PistonCrystal.mc.field_1724.method_23318() - (double)pos.method_10264() <= -2.0 || PistonCrystal.mc.field_1724.method_23318() - (double)pos.method_10264() >= 3.0) && BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6) {
                return;
            }
            if (!this.isTrueFacing(pos, facing)) {
                return;
            }
        }
        if (!PistonCrystal.mc.field_1687.method_22347(pos.method_10079(facing, -1)) || PistonCrystal.mc.field_1687.method_8320(pos.method_10079(facing, -1)).method_26204() == Blocks.field_10036 || this.getBlock(pos.method_10093(facing.method_10153())) == Blocks.field_10008 && !this.checkCrystal2(pos.method_10093(facing.method_10153()))) {
            return;
        }
        if (!BlockUtil.canPlace(pos, this.placeRange.getValue()) && !this.isPiston(pos, facing)) {
            return;
        }
        if (!((double)MathHelper.method_15355((float)((float)EntityUtil.getEyesPos().method_1025(pos.method_46558()))) < this.distance) && this.bestPos != null) {
            return;
        }
        this.bestPos = pos;
        this.bestOPos = oPos;
        this.bestFacing = facing;
        this.distance = MathHelper.method_15355((float)((float)EntityUtil.getEyesPos().method_1025(pos.method_46558())));
        this.getPos = true;
        this.crystalTimer.reset();
    }

    private void doPistonAura(BlockPos pos, Direction facing, BlockPos oPos) {
        if ((double)this.stage >= this.stageSetting.getValue()) {
            this.stage = 0;
        }
        ++this.stage;
        if (PistonCrystal.mc.field_1687 != null && PistonCrystal.mc.field_1687.method_22347(pos)) {
            if (BlockUtil.canPlace(pos)) {
                if ((double)this.stage >= this.pistonStage.getValue() && (double)this.stage <= this.pistonMaxStage.getValue()) {
                    Vec3d hitVec;
                    Direction side = BlockUtil.getPlaceSide(pos);
                    if (side == null) {
                        return;
                    }
                    int old = 0;
                    if (PistonCrystal.mc.field_1724 != null) {
                        old = PistonCrystal.mc.field_1724.method_31548().field_7545;
                    }
                    BlockPos neighbour = pos.method_10093(side);
                    Direction opposite = side.method_10153();
                    if (this.rotate.getValue() && !this.faceVector(hitVec = pos.method_46558().method_1019(new Vec3d((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5)))) {
                        return;
                    }
                    if (this.shouldYawCheck()) {
                        PistonCrystal.pistonFacing(facing);
                    }
                    int piston = this.findClass(PistonBlock.class);
                    this.doSwap(piston);
                    this.placeBlock(pos, false, this.endSwing.getValue());
                    if (this.inventory.getValue()) {
                        this.doSwap(piston);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(old);
                    }
                    if (this.rotate.getValue()) {
                        EntityUtil.facePosSide(neighbour, opposite);
                    }
                }
            } else {
                return;
            }
        }
        if ((double)this.stage >= this.powerStage.getValue() && (double)this.stage <= this.powerMaxStage.getValue()) {
            this.doRedStone(pos, facing, oPos.method_10093(facing));
        }
        if ((double)this.stage >= this.crystalStage.getValue() && (double)this.stage <= this.crystalMaxStage.getValue()) {
            this.placeCrystal(oPos, facing);
        }
        if ((double)this.stage >= this.fireStage.getValue() && (double)this.stage <= this.fireMaxStage.getValue()) {
            this.doFire(oPos, facing);
        }
    }

    private void placeCrystal(BlockPos pos, Direction facing) {
        if (!BlockUtil.canPlaceCrystal(pos.method_10093(facing))) {
            return;
        }
        int crystal = this.findItem(Items.field_8301);
        if (crystal == -1) {
            return;
        }
        int old = 0;
        if (PistonCrystal.mc.field_1724 != null) {
            old = PistonCrystal.mc.field_1724.method_31548().field_7545;
        }
        this.doSwap(crystal);
        this.placeCrystal(pos.method_10093(facing), this.rotate.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(crystal);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(old);
        }
    }

    public void placeCrystal(BlockPos pos, boolean rotate) {
        BlockPos obsPos = pos.method_10074();
        Direction facing = BlockUtil.getClickSide(obsPos);
        this.clickBlock(obsPos, facing, rotate);
    }

    private boolean isPiston(BlockPos pos, Direction facing) {
        if (PistonCrystal.mc.field_1687 != null && !(PistonCrystal.mc.field_1687.method_8320(pos).method_26204() instanceof PistonBlock)) {
            return false;
        }
        if (((Direction)PistonCrystal.mc.field_1687.method_8320(pos).method_11654((Property)FacingBlock.field_10927)).method_10153() != facing) {
            return false;
        }
        return PistonCrystal.mc.field_1687.method_22347(pos.method_10079(facing, -1)) || this.getBlock(pos.method_10079(facing, -1)) == Blocks.field_10036 || this.getBlock(pos.method_10093(facing.method_10153())) == Blocks.field_10008;
    }

    private void doFire(BlockPos pos, Direction facing) {
        if (!this.fire.getValue()) {
            return;
        }
        int fire = this.findItem(Items.field_8884);
        if (fire == -1) {
            return;
        }
        int old = 0;
        if (PistonCrystal.mc.field_1724 != null) {
            old = PistonCrystal.mc.field_1724.method_31548().field_7545;
        }
        int[] xOffset = new int[]{0, facing.method_10165(), -facing.method_10165()};
        int[] yOffset = new int[]{0, 1};
        int[] zOffset = new int[]{0, facing.method_10148(), -facing.method_10148()};
        for (int x : xOffset) {
            for (int y : yOffset) {
                for (int z : zOffset) {
                    if (this.getBlock(pos.method_10069(x, y, z)) != Blocks.field_10036) continue;
                    return;
                }
            }
        }
        for (int x : xOffset) {
            for (int y : yOffset) {
                for (int z : zOffset) {
                    if (!PistonCrystal.canFire(pos.method_10069(x, y, z))) continue;
                    this.doSwap(fire);
                    this.placeFire(pos.method_10069(x, y, z));
                    if (this.inventory.getValue()) {
                        this.doSwap(fire);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(old);
                    }
                    return;
                }
            }
        }
    }

    public void placeFire(BlockPos pos) {
        BlockPos neighbour = pos.method_10093(Direction.field_11033);
        this.clickBlock(neighbour, Direction.field_11036, this.rotate.getValue());
    }

    private void doRedStone(BlockPos pos, Direction facing, BlockPos crystalPos) {
        Direction bestNeighboring;
        if (PistonCrystal.mc.field_1687 != null && !PistonCrystal.mc.field_1687.method_22347(pos.method_10079(facing, -1)) && this.getBlock(pos.method_10079(facing, -1)) != Blocks.field_10036 && this.getBlock(pos.method_10093(facing.method_10153())) != Blocks.field_10008) {
            return;
        }
        for (Direction i : Direction.values()) {
            if (this.getBlock(pos.method_10093(i)) != Blocks.field_10002) continue;
            return;
        }
        int power = this.findBlock(Blocks.field_10002);
        if (power == -1) {
            return;
        }
        int old = 0;
        if (PistonCrystal.mc.field_1724 != null) {
            old = PistonCrystal.mc.field_1724.method_31548().field_7545;
        }
        if ((bestNeighboring = BlockUtil.getBestNeighboring(pos, facing)) != null && bestNeighboring != facing.method_10153() && BlockUtil.canPlace(pos.method_10093(bestNeighboring), this.placeRange.getValue()) && !pos.method_10093(bestNeighboring).equals((Object)crystalPos)) {
            this.doSwap(power);
            this.placeBlock(pos.method_10093(bestNeighboring), this.rotate.getValue(), this.endSwing.getValue());
            if (this.inventory.getValue()) {
                this.doSwap(power);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(old);
            }
            return;
        }
        for (Direction i : Direction.values()) {
            if (!BlockUtil.canPlace(pos.method_10093(i), this.placeRange.getValue()) || pos.method_10093(i).equals((Object)crystalPos) || i == facing.method_10153()) continue;
            this.doSwap(power);
            this.placeBlock(pos.method_10093(i), this.rotate.getValue(), this.endSwing.getValue());
            if (this.inventory.getValue()) {
                this.doSwap(power);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(old);
            }
            return;
        }
    }

    private boolean shouldYawCheck() {
        return this.yawDeceive.getValue() || this.autoYaw.getValue() && !EntityUtil.isInsideBlock();
    }

    private boolean isTrueFacing(BlockPos pos, Direction facing) {
        Vec3d hitVec;
        if (this.shouldYawCheck()) {
            return true;
        }
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            side = Direction.field_11036;
        }
        return Direction.method_10150((double)EntityUtil.getLegitRotations(hitVec = pos.method_10093((side = side.method_10153()).method_10153()).method_46558().method_1019(new Vec3d((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5)))[0]) == facing;
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            if (PistonCrystal.mc.field_1724 != null) {
                InventoryUtil.inventorySwap(slot, PistonCrystal.mc.field_1724.method_31548().field_7545);
            }
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public int findItem(Item itemIn) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findItemInventorySlot(itemIn);
        }
        return InventoryUtil.findItem(itemIn);
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

    private Block getBlock(BlockPos pos) {
        if (PistonCrystal.mc.field_1687 != null) {
            return PistonCrystal.mc.field_1687.method_8320(pos).method_26204();
        }
        return null;
    }

    public void placeBlock(BlockPos pos, boolean rotate, boolean bypass) {
        Direction side;
        if (BlockUtil.airPlace()) {
            for (Direction i : Direction.values()) {
                if (PistonCrystal.mc.field_1687 == null || !PistonCrystal.mc.field_1687.method_22347(pos.method_10093(i))) continue;
                this.clickBlock(pos, i, rotate);
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
        if (PistonCrystal.mc.field_1724 != null) {
            sprint = PistonCrystal.mc.field_1724.method_5624();
        }
        boolean sneak = false;
        if (PistonCrystal.mc.field_1687 != null) {
            boolean bl = sneak = BlockUtil.needSneak(PistonCrystal.mc.field_1687.method_8320(result.method_17777()).method_26204()) && !PistonCrystal.mc.field_1724.method_5715();
        }
        if (sprint) {
            PistonCrystal.mc.field_1724.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)PistonCrystal.mc.field_1724, ClientCommandC2SPacket.Mode.field_12985));
        }
        if (sneak) {
            PistonCrystal.mc.field_1724.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)PistonCrystal.mc.field_1724, ClientCommandC2SPacket.Mode.field_12979));
        }
        this.clickBlock(pos.method_10093(side), side.method_10153(), rotate);
        if (sneak) {
            PistonCrystal.mc.field_1724.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)PistonCrystal.mc.field_1724, ClientCommandC2SPacket.Mode.field_12984));
        }
        if (sprint) {
            PistonCrystal.mc.field_1724.field_3944.method_52787((Packet)new ClientCommandC2SPacket((Entity)PistonCrystal.mc.field_1724, ClientCommandC2SPacket.Mode.field_12981));
        }
        if (bypass) {
            EntityUtil.swingHand(Hand.field_5808, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        }
    }

    private float[] injectStep(float[] angle, float steps) {
        if (steps < 0.01f) {
            steps = 0.01f;
        }
        if (steps > 1.0f) {
            steps = 1.0f;
        }
        if (steps < 1.0f && angle != null) {
            float packetPitch;
            float packetYaw = this.lastYaw;
            float diff = MathHelper.method_15393((float)(angle[0] - packetYaw));
            if (Math.abs(diff) > 90.0f * steps) {
                angle[0] = packetYaw + diff * (90.0f * steps / Math.abs(diff));
            }
            if (Math.abs(diff = angle[1] - (packetPitch = this.lastPitch)) > 90.0f * steps) {
                angle[1] = packetPitch + diff * (90.0f * steps / Math.abs(diff));
            }
        }
        if (angle != null) {
            return new float[]{angle[0], angle[1]};
        }
        return null;
    }

    public boolean clickBlock(BlockPos pos, Direction side, boolean rotate) {
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (rotate && !this.faceVector(directionVec)) {
            return false;
        }
        EntityUtil.swingHand(Hand.field_5808, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        if (PistonCrystal.mc.field_1724 != null) {
            PistonCrystal.mc.field_1724.field_3944.method_52787((Packet)new PlayerInteractBlockC2SPacket(Hand.field_5808, result, BlockUtil.getWorldActionId(PistonCrystal.mc.field_1687)));
        }
        return true;
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!this.newRotate.getValue()) {
            EntityUtil.faceVector(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        float[] angle = EntityUtil.getLegitRotations(directionVec);
        if (Math.abs(MathHelper.method_15393((float)(angle[0] - this.lastYaw))) < this.fov.getValueFloat() && Math.abs(MathHelper.method_15393((float)(angle[1] - this.lastPitch))) < this.fov.getValueFloat()) {
            if (this.packet.getValue()) {
                EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            }
            return true;
        }
        return !this.checkLook.getValue();
    }

    public static final class _nsRHxiHWZMPWnytkAhif
    extends Enum<_nsRHxiHWZMPWnytkAhif> {
        public static final /* enum */ _nsRHxiHWZMPWnytkAhif General;
        public static final /* enum */ _nsRHxiHWZMPWnytkAhif Misc;
        public static final /* enum */ _nsRHxiHWZMPWnytkAhif Rotate;

        public static _nsRHxiHWZMPWnytkAhif[] values() {
            return null;
        }

        public static _nsRHxiHWZMPWnytkAhif valueOf(String string) {
            return null;
        }
    }
}
