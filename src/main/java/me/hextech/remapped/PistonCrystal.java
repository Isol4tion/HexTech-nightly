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
        if (i == Direction.EAST) {
            EntityUtil.sendYawAndPitch(-90.0f, 5.0f);
        } else if (i == Direction.WEST) {
            EntityUtil.sendYawAndPitch(90.0f, 5.0f);
        } else if (i == Direction.NORTH) {
            EntityUtil.sendYawAndPitch(180.0f, 5.0f);
        } else if (i == Direction.SOUTH) {
            EntityUtil.sendYawAndPitch(0.0f, 5.0f);
        }
    }

    private static boolean canFire(BlockPos pos) {
        if (BlockUtil.canReplace(pos.down())) {
            return false;
        }
        if (PistonCrystal.mc.world != null && !PistonCrystal.mc.world.isAir(pos)) {
            return false;
        }
        if (!BlockUtil.canClick(pos.offset(Direction.DOWN))) {
            return false;
        }
        return BlockUtil.isStrictDirection(pos.down(), Direction.UP);
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
        if (PistonCrystal.mc.player != null && this.check(this.onlyStatic.getValue(), !PistonCrystal.mc.player.isOnGround(), this.onlyGround.getValue())) {
            return;
        }
        BlockPos pos = EntityUtil.getEntityPos(this.target, true);
        if (!EntityUtil.isUsing() || this.eatingBreak.getValue()) {
            if (this.checkCrystal(pos.up(0))) {
                this.attackCrystal(pos.up(0), this.rotate.getValue(), false);
            }
            if (this.checkCrystal(pos.up(1))) {
                this.attackCrystal(pos.up(1), this.rotate.getValue(), false);
            }
            if (this.checkCrystal(pos.up(2))) {
                this.attackCrystal(pos.up(2), this.rotate.getValue(), false);
            }
        }
        if (PistonCrystal.mc.world != null && this.bestPos != null) {
            PistonCrystal.mc.world.getBlockState(this.bestPos).getBlock();
        }
        if (this.crystalTimer.passedMs(this.posUpdateDelay.getValueInt())) {
            this.stage = 0;
            this.distance = 100.0;
            this.getPos = false;
            this.getBestPos(pos.up(2));
            this.getBestPos(pos.up());
        }
        if (!this.timer.passedMs(this.updateDelay.getValueInt())) {
            return;
        }
        if (this.getPos && this.bestPos != null) {
            this.timer.reset();
            if (this.debug.getValue()) {
                CommandManager.sendChatMessage("[Debug] PistonPos:" + this.bestPos + " Facing:" + this.bestFacing + " CrystalPos:" + this.bestOPos.offset(this.bestFacing));
            }
            if (this.check(this.onlyStatic.getValue(), !PistonCrystal.mc.player.isOnGround(), this.onlyGround.getValue())) {
                return;
            }
            this.doPistonAura(this.bestPos, this.bestFacing, this.bestOPos);
        }
    }

    public void attackCrystal(BlockPos pos, boolean rotate, boolean eatingPause) {
        block0: {
            Iterator iterator = PistonCrystal.mc.world.getNonSpectatingEntities(EndCrystalEntity.class, new Box(pos)).iterator();
            if (!iterator.hasNext()) break block0;
            EndCrystalEntity entity = (EndCrystalEntity)iterator.next();
            this.attackCrystal(entity, rotate, eatingPause);
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
            if (rotate && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue() && !this.faceVector(new Vec3d(crystal.getX(), crystal.getY() + 0.25, crystal.getZ()))) {
                return;
            }
            PistonCrystal.mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(crystal, PistonCrystal.mc.player.isSneaking()));
            PistonCrystal.mc.player.resetLastAttackedTicks();
            EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        }
    }

    public boolean check(boolean onlyStatic, boolean onGround, boolean onlyGround) {
        if (MovementUtil.isMoving() && onlyStatic) {
            return true;
        }
        if (onGround && onlyGround) {
            return true;
        }
        if (this.findBlock(Blocks.REDSTONE_BLOCK) == -1) {
            return true;
        }
        if (this.findClass(PistonBlock.class) == -1) {
            return true;
        }
        return this.findItem(Items.END_CRYSTAL) == -1;
    }

    private boolean checkCrystal(BlockPos pos) {
        if (PistonCrystal.mc.world != null) {
            for (Entity entity : PistonCrystal.mc.world.getNonSpectatingEntities(EndCrystalEntity.class, new Box(pos))) {
                float damage = ThunderExplosionUtil.calculateDamage(entity.getPos(), this.target, this.target, 6.0f);
                if (!(damage > 6.0f)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean checkCrystal2(BlockPos pos) {
        if (PistonCrystal.mc.world != null) {
            for (Entity entity : PistonCrystal.mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
                if (!(entity instanceof EndCrystalEntity) || !EntityUtil.getEntityPos(entity).equals(pos)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getInfo() {
        if (this.target != null) {
            return this.target.getName().getString();
        }
        return null;
    }

    private void getBestPos(BlockPos pos) {
        for (Direction i : Direction.values()) {
            if (i == Direction.DOWN || i == Direction.UP) continue;
            this.getPos(pos, i);
        }
    }

    private void getPos(BlockPos pos, Direction i) {
        if (!BlockUtil.canPlaceCrystal(pos.offset(i)) && !this.checkCrystal2(pos.offset(i))) {
            return;
        }
        this.getPos(pos.offset(i, 3), i, pos);
        this.getPos(pos.offset(i, 3).up(), i, pos);
        int offsetX = pos.offset(i).getX() - pos.getX();
        int offsetZ = pos.offset(i).getZ() - pos.getZ();
        this.getPos(pos.offset(i, 3).add(offsetZ, 0, offsetX), i, pos);
        this.getPos(pos.offset(i, 3).add(-offsetZ, 0, -offsetX), i, pos);
        this.getPos(pos.offset(i, 3).add(offsetZ, 1, offsetX), i, pos);
        this.getPos(pos.offset(i, 3).add(-offsetZ, 1, -offsetX), i, pos);
        this.getPos(pos.offset(i, 2), i, pos);
        this.getPos(pos.offset(i, 2).up(), i, pos);
        this.getPos(pos.offset(i, 2).add(offsetZ, 0, offsetX), i, pos);
        this.getPos(pos.offset(i, 2).add(-offsetZ, 0, -offsetX), i, pos);
        this.getPos(pos.offset(i, 2).add(offsetZ, 1, offsetX), i, pos);
        this.getPos(pos.offset(i, 2).add(-offsetZ, 1, -offsetX), i, pos);
    }

    private void getPos(BlockPos pos, Direction facing, BlockPos oPos) {
        if (PistonCrystal.mc.world != null && this.switchPos.getValue() && this.bestPos != null && this.bestPos.equals(pos) && PistonCrystal.mc.world.isAir(this.bestPos)) {
            return;
        }
        if (!BlockUtil.canPlace(pos, this.placeRange.getValue()) && !(this.getBlock(pos) instanceof PistonBlock)) {
            return;
        }
        if (this.findClass(PistonBlock.class) == -1) {
            return;
        }
        if (!(this.getBlock(pos) instanceof PistonBlock)) {
            if (PistonCrystal.mc.player != null && (PistonCrystal.mc.player.getY() - (double)pos.getY() <= -2.0 || PistonCrystal.mc.player.getY() - (double)pos.getY() >= 3.0) && BlockUtil.distanceToXZ((double)pos.getX() + 0.5, (double)pos.getZ() + 0.5) < 2.6) {
                return;
            }
            if (!this.isTrueFacing(pos, facing)) {
                return;
            }
        }
        if (!PistonCrystal.mc.world.isAir(pos.offset(facing, -1)) || PistonCrystal.mc.world.getBlockState(pos.offset(facing, -1)).getBlock() == Blocks.FIRE || this.getBlock(pos.offset(facing.getOpposite())) == Blocks.MOVING_PISTON && !this.checkCrystal2(pos.offset(facing.getOpposite()))) {
            return;
        }
        if (!BlockUtil.canPlace(pos, this.placeRange.getValue()) && !this.isPiston(pos, facing)) {
            return;
        }
        if (!((double)MathHelper.sqrt((float)EntityUtil.getEyesPos().squaredDistanceTo(pos.toCenterPos())) < this.distance) && this.bestPos != null) {
            return;
        }
        this.bestPos = pos;
        this.bestOPos = oPos;
        this.bestFacing = facing;
        this.distance = MathHelper.sqrt((float)EntityUtil.getEyesPos().squaredDistanceTo(pos.toCenterPos()));
        this.getPos = true;
        this.crystalTimer.reset();
    }

    private void doPistonAura(BlockPos pos, Direction facing, BlockPos oPos) {
        if ((double)this.stage >= this.stageSetting.getValue()) {
            this.stage = 0;
        }
        ++this.stage;
        if (PistonCrystal.mc.world != null && PistonCrystal.mc.world.isAir(pos)) {
            if (BlockUtil.canPlace(pos)) {
                if ((double)this.stage >= this.pistonStage.getValue() && (double)this.stage <= this.pistonMaxStage.getValue()) {
                    Vec3d hitVec;
                    Direction side = BlockUtil.getPlaceSide(pos);
                    if (side == null) {
                        return;
                    }
                    int old = 0;
                    if (PistonCrystal.mc.player != null) {
                        old = PistonCrystal.mc.player.getInventory().selectedSlot;
                    }
                    BlockPos neighbour = pos.offset(side);
                    Direction opposite = side.getOpposite();
                    if (this.rotate.getValue() && !this.faceVector(hitVec = pos.toCenterPos().add(new Vec3d((double)side.getVector().getX() * 0.5, (double)side.getVector().getY() * 0.5, (double)side.getVector().getZ() * 0.5)))) {
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
            this.doRedStone(pos, facing, oPos.offset(facing));
        }
        if ((double)this.stage >= this.crystalStage.getValue() && (double)this.stage <= this.crystalMaxStage.getValue()) {
            this.placeCrystal(oPos, facing);
        }
        if ((double)this.stage >= this.fireStage.getValue() && (double)this.stage <= this.fireMaxStage.getValue()) {
            this.doFire(oPos, facing);
        }
    }

    private void placeCrystal(BlockPos pos, Direction facing) {
        if (!BlockUtil.canPlaceCrystal(pos.offset(facing))) {
            return;
        }
        int crystal = this.findItem(Items.END_CRYSTAL);
        if (crystal == -1) {
            return;
        }
        int old = 0;
        if (PistonCrystal.mc.player != null) {
            old = PistonCrystal.mc.player.getInventory().selectedSlot;
        }
        this.doSwap(crystal);
        this.placeCrystal(pos.offset(facing), this.rotate.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(crystal);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(old);
        }
    }

    public void placeCrystal(BlockPos pos, boolean rotate) {
        BlockPos obsPos = pos.down();
        Direction facing = BlockUtil.getClickSide(obsPos);
        this.clickBlock(obsPos, facing, rotate);
    }

    private boolean isPiston(BlockPos pos, Direction facing) {
        if (PistonCrystal.mc.world != null && !(PistonCrystal.mc.world.getBlockState(pos).getBlock() instanceof PistonBlock)) {
            return false;
        }
        if (((Direction)PistonCrystal.mc.world.getBlockState(pos).get((Property)FacingBlock.FACING)).getOpposite() != facing) {
            return false;
        }
        return PistonCrystal.mc.world.isAir(pos.offset(facing, -1)) || this.getBlock(pos.offset(facing, -1)) == Blocks.FIRE || this.getBlock(pos.offset(facing.getOpposite())) == Blocks.MOVING_PISTON;
    }

    private void doFire(BlockPos pos, Direction facing) {
        if (!this.fire.getValue()) {
            return;
        }
        int fire = this.findItem(Items.FLINT_AND_STEEL);
        if (fire == -1) {
            return;
        }
        int old = 0;
        if (PistonCrystal.mc.player != null) {
            old = PistonCrystal.mc.player.getInventory().selectedSlot;
        }
        int[] xOffset = new int[]{0, facing.getOffsetZ(), -facing.getOffsetZ()};
        int[] yOffset = new int[]{0, 1};
        int[] zOffset = new int[]{0, facing.getOffsetX(), -facing.getOffsetX()};
        for (int x : xOffset) {
            for (int y : yOffset) {
                for (int z : zOffset) {
                    if (this.getBlock(pos.add(x, y, z)) != Blocks.FIRE) continue;
                    return;
                }
            }
        }
        for (int x : xOffset) {
            for (int y : yOffset) {
                for (int z : zOffset) {
                    if (!PistonCrystal.canFire(pos.add(x, y, z))) continue;
                    this.doSwap(fire);
                    this.placeFire(pos.add(x, y, z));
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
        BlockPos neighbour = pos.offset(Direction.DOWN);
        this.clickBlock(neighbour, Direction.UP, this.rotate.getValue());
    }

    private void doRedStone(BlockPos pos, Direction facing, BlockPos crystalPos) {
        Direction bestNeighboring;
        if (PistonCrystal.mc.world != null && !PistonCrystal.mc.world.isAir(pos.offset(facing, -1)) && this.getBlock(pos.offset(facing, -1)) != Blocks.FIRE && this.getBlock(pos.offset(facing.getOpposite())) != Blocks.MOVING_PISTON) {
            return;
        }
        for (Direction i : Direction.values()) {
            if (this.getBlock(pos.offset(i)) != Blocks.REDSTONE_BLOCK) continue;
            return;
        }
        int power = this.findBlock(Blocks.REDSTONE_BLOCK);
        if (power == -1) {
            return;
        }
        int old = 0;
        if (PistonCrystal.mc.player != null) {
            old = PistonCrystal.mc.player.getInventory().selectedSlot;
        }
        if ((bestNeighboring = BlockUtil.getBestNeighboring(pos, facing)) != null && bestNeighboring != facing.getOpposite() && BlockUtil.canPlace(pos.offset(bestNeighboring), this.placeRange.getValue()) && !pos.offset(bestNeighboring).equals(crystalPos)) {
            this.doSwap(power);
            this.placeBlock(pos.offset(bestNeighboring), this.rotate.getValue(), this.endSwing.getValue());
            if (this.inventory.getValue()) {
                this.doSwap(power);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(old);
            }
            return;
        }
        for (Direction i : Direction.values()) {
            if (!BlockUtil.canPlace(pos.offset(i), this.placeRange.getValue()) || pos.offset(i).equals(crystalPos) || i == facing.getOpposite()) continue;
            this.doSwap(power);
            this.placeBlock(pos.offset(i), this.rotate.getValue(), this.endSwing.getValue());
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
            side = Direction.UP;
        }
        return Direction.fromRotation(EntityUtil.getLegitRotations(hitVec = pos.offset((side = side.getOpposite()).getOpposite()).toCenterPos().add(new Vec3d((double)side.getVector().getX() * 0.5, (double)side.getVector().getY() * 0.5, (double)side.getVector().getZ() * 0.5)))[0]) == facing;
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            if (PistonCrystal.mc.player != null) {
                InventoryUtil.inventorySwap(slot, PistonCrystal.mc.player.getInventory().selectedSlot);
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
        if (PistonCrystal.mc.world != null) {
            return PistonCrystal.mc.world.getBlockState(pos).getBlock();
        }
        return null;
    }

    public void placeBlock(BlockPos pos, boolean rotate, boolean bypass) {
        Direction side;
        if (BlockUtil.airPlace()) {
            for (Direction i : Direction.values()) {
                if (PistonCrystal.mc.world == null || !PistonCrystal.mc.world.isAir(pos.offset(i))) continue;
                this.clickBlock(pos, i, rotate);
                return;
            }
        }
        if ((side = BlockUtil.getPlaceSide(pos)) == null) {
            return;
        }
        Vec3d directionVec = new Vec3d((double)pos.getX() + 0.5 + (double)side.getVector().getX() * 0.5, (double)pos.getY() + 0.5 + (double)side.getVector().getY() * 0.5, (double)pos.getZ() + 0.5 + (double)side.getVector().getZ() * 0.5);
        EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        BlockUtil.placedPos.add(pos);
        boolean sprint = false;
        if (PistonCrystal.mc.player != null) {
            sprint = PistonCrystal.mc.player.isSprinting();
        }
        boolean sneak = false;
        if (PistonCrystal.mc.world != null) {
            boolean bl = sneak = BlockUtil.needSneak(PistonCrystal.mc.world.getBlockState(result.getBlockPos()).getBlock()) && !PistonCrystal.mc.player.isSneaking();
        }
        if (sprint) {
            PistonCrystal.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(PistonCrystal.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }
        if (sneak) {
            PistonCrystal.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(PistonCrystal.mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        }
        this.clickBlock(pos.offset(side), side.getOpposite(), rotate);
        if (sneak) {
            PistonCrystal.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(PistonCrystal.mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        }
        if (sprint) {
            PistonCrystal.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(PistonCrystal.mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
        if (bypass) {
            EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
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
            float diff = MathHelper.wrapDegrees(angle[0] - packetYaw);
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
        Vec3d directionVec = new Vec3d((double)pos.getX() + 0.5 + (double)side.getVector().getX() * 0.5, (double)pos.getY() + 0.5 + (double)side.getVector().getY() * 0.5, (double)pos.getZ() + 0.5 + (double)side.getVector().getZ() * 0.5);
        if (rotate && !this.faceVector(directionVec)) {
            return false;
        }
        EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        if (PistonCrystal.mc.player != null) {
            PistonCrystal.mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, BlockUtil.getWorldActionId(PistonCrystal.mc.world)));
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
        if (Math.abs(MathHelper.wrapDegrees(angle[0] - this.lastYaw)) < this.fov.getValueFloat() && Math.abs(MathHelper.wrapDegrees(angle[1] - this.lastPitch)) < this.fov.getValueFloat()) {
            if (this.packet.getValue()) {
                EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            }
            return true;
        }
        return !this.checkLook.getValue();
    }

    public enum _nsRHxiHWZMPWnytkAhif {
        General,
        Misc,
        Rotate

    }
}
