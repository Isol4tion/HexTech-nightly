package me.hextech.remapped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow;
import me.hextech.remapped.BurrowAssist;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Enum_HAljcKfxHffzpuFTAOaQ;
import me.hextech.remapped.Enum_MOJxjPVaItysgPVNqBDX;
import me.hextech.remapped.Enum_YBWtbEXllPkRSdEiULQW;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.HoleKickTest;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.MineManager;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.SelfTrap;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Burrow_eOaBGEoOSTDRbYIUAbXC
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Burrow_eOaBGEoOSTDRbYIUAbXC INSTANCE;
    public final EnumSetting<Enum_MOJxjPVaItysgPVNqBDX> page = this.add(new EnumSetting<Enum_MOJxjPVaItysgPVNqBDX>("Page", Enum_MOJxjPVaItysgPVNqBDX.General));
    public final SliderSetting placeDelay = this.add(new SliderSetting("Delay", 50, 0, 500, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final BooleanSetting webcheck = this.add(new BooleanSetting("WebCheck", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final BooleanSetting antiLag = this.add(new BooleanSetting("AntiLag", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final SliderSetting multiPlace = this.add(new SliderSetting("BlocksPer", 4.0, 1.0, 4.0, 1.0, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final SliderSetting offest = this.add(new SliderSetting("BoxOffset", 0.3, 0.0, 1.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General).setSuffix("/int x2"));
    public final BooleanSetting syncCrystal = this.add(new BooleanSetting("SyncCrsytal", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final BooleanSetting helper = this.add(new BooleanSetting("Helper", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final BooleanSetting ground = this.add(new BooleanSetting("Ground", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final BooleanSetting disable = this.add(new BooleanSetting("Disable", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final BooleanSetting SmartActive = this.add(new BooleanSetting("SmartActive", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public final BooleanSetting onlyStatic = this.add(new BooleanSetting("OnlyStatic", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    private final BooleanSetting autocenter = this.add(new BooleanSetting("AutoCenter", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    private final BooleanSetting cancelblink = this.add(new BooleanSetting("CancelBlink", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    private final EnumSetting<Enum_HAljcKfxHffzpuFTAOaQ> rotate = this.add(new EnumSetting<Enum_HAljcKfxHffzpuFTAOaQ>("RotateMode", Enum_HAljcKfxHffzpuFTAOaQ.Bypass, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.Rotate));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Breaks", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    private final BooleanSetting fakeMove = this.add(new BooleanSetting("FakeMove", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.Above));
    private final BooleanSetting center = this.add(new BooleanSetting("AllowCenter", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.Above));
    private final BooleanSetting Air = this.add(new BooleanSetting("SejiaLag/Air", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.intersic));
    private final BooleanSetting Wait = this.add(new BooleanSetting("Wait/SeJiaAutoLag", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.intersic));
    private final BooleanSetting nomove = this.add(new BooleanSetting("MovingPause", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.intersic));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", false, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    private final BooleanSetting noeat = this.add(new BooleanSetting("NoUsingPlace", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    private final EnumSetting<Enum_YBWtbEXllPkRSdEiULQW> lagMode = this.add(new EnumSetting<Enum_YBWtbEXllPkRSdEiULQW>("LagMode", Enum_YBWtbEXllPkRSdEiULQW.Auto, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode));
    private final EnumSetting<Enum_YBWtbEXllPkRSdEiULQW> aboveLagMode = this.add(new EnumSetting<Enum_YBWtbEXllPkRSdEiULQW>("Above", Enum_YBWtbEXllPkRSdEiULQW.Auto, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode));
    private final SliderSetting AutoXZ = this.add(new SliderSetting("AutoXZ", 3.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode));
    private final SliderSetting AutoUp = this.add(new SliderSetting("AutoUp", 3.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode));
    private final SliderSetting AutoDown = this.add(new SliderSetting("AutoDown", 3.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode));
    private final SliderSetting Distance = this.add(new SliderSetting("Distance", 2.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode));
    private final SliderSetting cuicanHeight = this.add(new SliderSetting("cuicanHeightLag", -7.0, -10.0, 10.0, 0.1, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.LagMode));
    private final Timer inWebTimer = new Timer();
    private final Timer timer = new Timer();
    public BooleanSetting checkselfpos = this.add(new BooleanSetting("CheckSelfPos", true, v -> this.page.getValue() == Enum_MOJxjPVaItysgPVNqBDX.General));
    public List<BlockPos> placePos = new ArrayList<BlockPos>();
    int progress = 0;
    BlockPos movedPos = null;
    private boolean shouldCenter = true;

    public Burrow_eOaBGEoOSTDRbYIUAbXC() {
        super("Burrow", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
        Vec3d vec3d = posTo.subtract(posFrom);
        return SelfTrap.getRotationFromVec(vec3d);
    }

    @Override
    @EventHandler
    public void onUpdate() {
        if (this.cancelblink.getValue() && Blink.INSTANCE.isOn()) {
            return;
        }
        if (this.syncCrystal.getValue() && AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null) {
            return;
        }
        if (!this.timer.passedMs((long)this.placeDelay.getValue())) {
            return;
        }
        if (Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player != null && this.check(this.nomove.getValue(), this.ground.getValue())) {
            return;
        }
        this.movedPos = null;
        if (this.ground.getValue() && !Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.isOnGround()) {
            return;
        }
        if (this.Air.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.isAutoJumpEnabled()) {
            this.toggle();
        }
        if (this.noeat.getValue() && EntityUtil.isUsing()) {
            return;
        }
        if (this.webcheck.getValue() && HoleKickTest.isInWeb((PlayerEntity)Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player)) {
            this.inWebTimer.reset();
            return;
        }
        if (this.antiLag.getValue()) {
            BlockUtil.getState(EntityUtil.getPlayerPos(true).down()).method_51366();
        }
        this.timer.reset();
        int oldSlot = Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getInventory().selectedSlot;
        int block = this.getBlock();
        if (block == -1) {
            CommandManager.sendChatMessage("\u00a7e[?] \u00a7c\u00a7oObsidian" + (this.enderChest.getValue() ? "/EnderChest" : "") + "?");
            this.disable();
            return;
        }
        BlockPosX pos1 = new BlockPosX(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX() + this.offest.getValue(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.5, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ() + this.offest.getValue());
        BlockPosX pos2 = new BlockPosX(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX() - this.offest.getValue(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.5, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ() + this.offest.getValue());
        BlockPosX pos3 = new BlockPosX(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX() + this.offest.getValue(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.5, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ() - this.offest.getValue());
        BlockPosX pos4 = new BlockPosX(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX() - this.offest.getValue(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.5, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ() - this.offest.getValue());
        BlockPos playerPos = EntityUtil.getPlayerPos(true);
        if (!(this.canPlace(pos1) || this.canPlace(pos2) || this.canPlace(pos3) || this.canPlace(pos4))) {
            if (!this.Wait.getValue()) {
                this.disable();
            }
            return;
        }
        boolean above = false;
        BlockPos headPos = EntityUtil.getPlayerPos().up(2);
        boolean rotate = this.rotate.getValue() == Enum_HAljcKfxHffzpuFTAOaQ.Normal;
        CombatUtil.attackCrystal(pos1, rotate, false);
        CombatUtil.attackCrystal(pos2, rotate, false);
        CombatUtil.attackCrystal(pos3, rotate, false);
        CombatUtil.attackCrystal(pos4, rotate, false);
        if (Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_18276() || this.Trapped(headPos) || this.Trapped(headPos.add(1, 0, 0)) || this.Trapped(headPos.add(-1, 0, 0)) || this.Trapped(headPos.add(0, 0, 1)) || this.Trapped(headPos.add(0, 0, -1)) || this.Trapped(headPos.add(1, 0, -1)) || this.Trapped(headPos.add(-1, 0, -1)) || this.Trapped(headPos.add(1, 0, 1)) || this.Trapped(headPos.add(-1, 0, 1))) {
            above = true;
            if (!this.fakeMove.getValue()) {
                if (!this.Wait.getValue()) {
                    this.disable();
                }
                return;
            }
            boolean moved = false;
            BlockPos offPos = playerPos;
            if (this.checkSelf(offPos) && !BlockUtil.canReplace(offPos)) {
                this.gotoPos(offPos);
            } else {
                for (Direction direction : Direction.values()) {
                    if (direction == Direction.UP || direction == Direction.DOWN || !this.checkSelf(offPos = playerPos.offset(direction)) || BlockUtil.canReplace(offPos)) continue;
                    this.gotoPos(offPos);
                    moved = true;
                    break;
                }
                if (!moved) {
                    for (Direction direction : Direction.values()) {
                        if (direction == Direction.UP || direction == Direction.DOWN || !this.checkSelf(offPos = playerPos.offset(direction))) continue;
                        this.gotoPos(offPos);
                        moved = true;
                        break;
                    }
                    if (!moved) {
                        if (!this.center.getValue()) {
                            return;
                        }
                        for (Direction direction : Direction.values()) {
                            if (direction == Direction.UP || direction == Direction.DOWN || !this.canAbove(offPos = playerPos.offset(direction))) continue;
                            this.gotoPos(offPos);
                            moved = true;
                            break;
                        }
                        if (!moved) {
                            if (!this.Wait.getValue()) {
                                this.disable();
                            }
                            return;
                        }
                    }
                }
            }
        } else {
            Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.4199999868869781, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), false));
            Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.7531999805212017, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), false));
            Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.9999957640154541, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), false));
            Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 1.1661092609382138, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), false));
        }
        this.doSwap(block);
        this.progress = 0;
        this.placePos.clear();
        if (this.rotate.getValue() == Enum_HAljcKfxHffzpuFTAOaQ.Bypass) {
            EntityUtil.sendYawAndPitch(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_36454(), 90.0f);
        }
        this.placeBlock(playerPos, rotate);
        this.placeBlock(pos1, rotate);
        this.placeBlock(pos2, rotate);
        this.placeBlock(pos3, rotate);
        this.placeBlock(pos4, rotate);
        if (this.helper.getValue()) {
            this.placeBlock(playerPos.down(), rotate);
        }
        this.placeBlock(playerPos, rotate);
        if (this.helper.getValue()) {
            this.placeBlock(pos1.down(), rotate);
        }
        this.placeBlock(pos1, rotate);
        if (this.helper.getValue()) {
            this.placeBlock(pos2.down(), rotate);
        }
        this.placeBlock(pos2, rotate);
        if (this.helper.getValue()) {
            this.placeBlock(pos3.down(), rotate);
        }
        this.placeBlock(pos3, rotate);
        if (this.helper.getValue()) {
            this.placeBlock(pos4.down(), rotate);
        }
        this.placeBlock(pos4, rotate);
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(oldSlot);
        }
        switch (Burrow.$SwitchMap$me$hextech$mod$modules$impl$combat$burrow$Enum$LagBackMode[(above ? this.aboveLagMode.getValue() : this.lagMode.getValue()).ordinal()]) {
            case 1: {
                double distance = 0.0;
                BlockPos bestPos = null;
                for (int i = 0; i < 10; ++i) {
                    BlockPos pos = EntityUtil.getPlayerPos().up(i);
                    if (!this.canGoto(pos) || MathHelper.sqrt((float)((float)Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.squaredDistanceTo(pos.toCenterPos()))) < 2.0f || bestPos != null && !(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.squaredDistanceTo(pos.toCenterPos()) < distance)) continue;
                    bestPos = pos;
                    distance = Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.squaredDistanceTo(pos.toCenterPos());
                }
                if (bestPos == null) break;
                Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround((double)bestPos.method_10263() + 0.5, (double)bestPos.method_10264(), (double)bestPos.method_10260() + 0.5, false));
                break;
            }
            case 2: {
                int i;
                for (i = 0; i < 20; ++i) {
                    Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 1337.0, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), false));
                }
                break;
            }
            case 3: {
                Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.setPosition(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 3.0, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ());
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), true));
                break;
            }
            case 4: {
                Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 2.3400880035762786, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), false));
                break;
            }
            case 5: {
                Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + this.cuicanHeight.getValue(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), false));
                break;
            }
            case 6: {
                ArrayList<BlockPosX> list = new ArrayList<BlockPosX>();
                for (double x = Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538().method_10216() - this.AutoXZ.getValue(); x < Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538().method_10216() + this.AutoXZ.getValue(); x += 1.0) {
                    for (double z = Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538().method_10215() - this.AutoXZ.getValue(); z < Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538().method_10215() + this.AutoXZ.getValue(); z += 1.0) {
                        for (double y = Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538().method_10214() - this.AutoDown.getValue(); y < Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538().method_10214() + this.AutoUp.getValue(); y += 1.0) {
                            list.add(new BlockPosX(x, y, z));
                        }
                    }
                }
                double distance = 0.0;
                BlockPos bestPos = null;
                for (BlockPos blockPos : list) {
                    if (!this.canAbove(blockPos) || !this.canGoto(blockPos) || (double)MathHelper.sqrt((float)((float)Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.squaredDistanceTo(blockPos.toCenterPos().add(0.0, -0.5, 0.0)))) < this.Distance.getValue() || bestPos != null && !(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.squaredDistanceTo(blockPos.toCenterPos()) < distance)) continue;
                    bestPos = blockPos;
                    distance = Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.squaredDistanceTo(blockPos.toCenterPos());
                }
                if (bestPos == null) break;
                Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround((double)bestPos.method_10263() + 0.5, (double)bestPos.method_10264(), (double)bestPos.method_10260() + 0.5, false));
                break;
            }
            case 7: {
                int i;
                for (i = -10; i < 10; ++i) {
                    if (i == -1) {
                        i = 4;
                    }
                    if (!Burrow_eOaBGEoOSTDRbYIUAbXC.mc.world.getBlockState(BlockPos.ofFloored((Position)Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538()).add(0, i, 0)).getBlock().equals(Blocks.AIR) || !Burrow_eOaBGEoOSTDRbYIUAbXC.mc.world.getBlockState(BlockPos.ofFloored((Position)Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538()).add(0, i + 1, 0)).getBlock().equals(Blocks.AIR)) continue;
                    BlockPos pos = BlockPos.ofFloored((Position)Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538()).add(0, i, 0);
                    Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround((double)pos.method_10263() + 0.3, (double)pos.method_10264(), (double)pos.method_10260() + 0.3, false));
                    return;
                }
                break;
            }
        }
        if (this.disable.getValue()) {
            this.disable();
        }
    }

    @EventHandler(priority=-1)
    public void onMove(MoveEvent event) {
        if (Burrow_eOaBGEoOSTDRbYIUAbXC.nullCheck() || !this.autocenter.getValue() || Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_6128()) {
            return;
        }
        BlockPos blockPos = EntityUtil.getPlayerPos(true);
        if (Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX() - (double)blockPos.method_10263() - 0.5 <= 0.2 && Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX() - (double)blockPos.method_10263() - 0.5 >= -0.2 && Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ() - (double)blockPos.method_10260() - 0.5 <= 0.2 && Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ() - 0.5 - (double)blockPos.method_10260() >= -0.2) {
            if (this.shouldCenter && (Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.isOnGround() || MovementUtil.isMoving())) {
                event.setX(0.0);
                event.setZ(0.0);
                this.shouldCenter = false;
            }
        } else if (this.shouldCenter) {
            Vec3d centerPos = EntityUtil.getPlayerPos(true).toCenterPos();
            float rotation = Burrow_eOaBGEoOSTDRbYIUAbXC.getRotationTo((Vec3d)Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538(), (Vec3d)centerPos).x;
            float yawRad = rotation / 180.0f * (float)Math.PI;
            double dist = Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.method_19538().distanceTo(new Vec3d(centerPos.x, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY(), centerPos.z));
            double cappedSpeed = Math.min(0.2873, dist);
            double x = (double)(-((float)Math.sin(yawRad))) * cappedSpeed;
            double z = (double)((float)Math.cos(yawRad)) * cappedSpeed;
            event.setX(x);
            event.setZ(z);
        }
    }

    public boolean check(boolean onlyStatic, boolean value) {
        return MovementUtil.isMoving() && onlyStatic;
    }

    private void placeBlock(BlockPos pos, boolean rotate) {
        if (this.canPlace(pos) && !this.placePos.contains(pos) && this.progress < this.multiPlace.getValueInt()) {
            Direction side;
            this.placePos.add(pos);
            if (BlockUtil.airPlace()) {
                ++this.progress;
                BlockUtil.placedPos.add(pos);
                BlockUtil.clickBlock(pos, Direction.UP, rotate, this.packetPlace.getValue());
            }
            if ((side = BlockUtil.getPlaceSide(pos)) == null) {
                return;
            }
            ++this.progress;
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos.offset(side), side.getOpposite(), rotate, this.packetPlace.getValue());
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private void gotoPos(BlockPos offPos) {
        this.movedPos = offPos;
        if (Math.abs((double)offPos.method_10263() + 0.5 - Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX()) < Math.abs((double)offPos.method_10260() + 0.5 - Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ())) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX(), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.2, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ() + ((double)offPos.method_10260() + 0.5 - Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ()), true));
        } else {
            Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX() + ((double)offPos.method_10263() + 0.2 - Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getX()), Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getY() + 0.2, Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getZ(), true));
        }
    }

    private boolean canGoto(BlockPos pos) {
        return !BlockUtil.getState(pos).method_51366() && !BlockUtil.getState(pos.up()).method_51366();
    }

    private boolean canAbove(BlockPos pos) {
        return Burrow_eOaBGEoOSTDRbYIUAbXC.mc.world.isAir(pos) && Burrow_eOaBGEoOSTDRbYIUAbXC.mc.world.isAir(pos.up());
    }

    public boolean canPlace(BlockPos pos) {
        if (BlockUtil.getPlaceSide(pos) == null) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        if (this.checkselfpos.getValue() && pos.equals((Object)SpeedMine.breakPos)) {
            SpeedMine.breakPos = null;
        }
        if (BurrowAssist.INSTANCE.isOn() && BurrowAssist.INSTANCE.checkPos.getValue()) {
            for (MineManager breakData : new HashMap<Integer, MineManager>(HexTech.BREAK.breakMap).values()) {
                if (breakData == null || breakData.getEntity() == null || !pos.equals((Object)breakData.pos) || breakData.getEntity() == Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player) continue;
                return false;
            }
        }
        return !this.hasEntity(pos);
    }

    private boolean hasEntity(BlockPos pos) {
        for (Entity entity : Burrow_eOaBGEoOSTDRbYIUAbXC.mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
            if (entity == Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player || !entity.isAlive() || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ArrowEntity || entity instanceof EndCrystalEntity && this.breakCrystal.getValue() || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    private boolean checkSelf(BlockPos pos) {
        return Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player.getBoundingBox().intersects(new Box(pos));
    }

    private boolean Trapped(BlockPos pos) {
        return (Burrow_eOaBGEoOSTDRbYIUAbXC.mc.world.method_39454((Entity)Burrow_eOaBGEoOSTDRbYIUAbXC.mc.player, new Box(pos)) || BlockUtil.getBlock(pos) == Blocks.COBWEB) && this.checkSelf(pos.down(2));
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
}
