package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockPosX;
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
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.NewBurrow;
import me.hextech.remapped.NewBurrow_CcRGSMXmJEPCxAQiPgnz;
import me.hextech.remapped.NewBurrow_SzzqAKQBmpOchBNTHTUz;
import me.hextech.remapped.NewBurrow_qqQhaOJZqeVGcFBKpQgd;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Step_EShajbhvQeYkCdreEeNY;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class NewBurrow_bHmPnkYIKEocVyqCgEHa
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static NewBurrow_bHmPnkYIKEocVyqCgEHa INSTANCE;
    public final EnumSetting<NewBurrow_CcRGSMXmJEPCxAQiPgnz> page = this.add(new EnumSetting<NewBurrow_CcRGSMXmJEPCxAQiPgnz>("Page", NewBurrow_CcRGSMXmJEPCxAQiPgnz.Check));
    public final BooleanSetting pauseStep = this.add(new BooleanSetting("PauseStep", true, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Check)));
    public final BooleanSetting antiLag = this.add(new BooleanSetting("AntiLag", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Check)));
    public final BooleanSetting antiWebLag = this.add(new BooleanSetting("AntiWebLag", true, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)).setParent());
    private final SliderSetting move = this.add(new SliderSetting("Move", 0.05, 0.0, 0.2, 0.01, v -> this.antiWebLag.isOpen() && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Check)));
    private final Timer timer = new Timer();
    private final Timer webTimer = new Timer();
    private final Timer rotater = new Timer();
    private final BooleanSetting disable = this.add(new BooleanSetting("Disable", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Check)));
    private final BooleanSetting wait = this.add(new BooleanSetting("Wait", false, v -> this.disable.getValue() && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Check)));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 250, 0, 1000, v -> !this.disable.getValue() && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting checkClimb = this.add(new BooleanSetting("CheckClimb", true, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Check)));
    private final SliderSetting webTime = this.add(new SliderSetting("WebTime", 0, 0, 500, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final SliderSetting unNewBurrowWebTime = this.add(new SliderSetting("UnNewBurrowWebTime", 100, 0, 500, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting detectMine = this.add(new BooleanSetting("DetectMining", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting headFill = this.add(new BooleanSetting("HeadFill", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting noMine = this.add(new BooleanSetting("NoMine", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting noSelfPos = this.add(new BooleanSetting("NoSelfPos", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting sound = this.add(new BooleanSetting("Sound", true, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 5.0, 1.0, 8.0, 1.0, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final EnumSetting<NewBurrow_qqQhaOJZqeVGcFBKpQgd> rotate = this.add(new EnumSetting<NewBurrow_qqQhaOJZqeVGcFBKpQgd>("RotateMode", NewBurrow_qqQhaOJZqeVGcFBKpQgd.Bypass, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", false, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Place)));
    private final EnumSetting<NewBurrow> gotoMode = this.add(new EnumSetting<NewBurrow>("GotoMode", NewBurrow.Alien, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final SliderSetting moveUp = this.add(new SliderSetting("MoveUp", 0.1, 0.0, 0.2, 0.01, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final BooleanSetting fakeMove = this.add(new BooleanSetting("FakeMove", true, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)).setParent());
    private final BooleanSetting center = this.add(new BooleanSetting("AllowCenter", false, v -> this.fakeMove.isOpen() && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final EnumSetting<NewBurrow_SzzqAKQBmpOchBNTHTUz> lagMode = this.add(new EnumSetting<NewBurrow_SzzqAKQBmpOchBNTHTUz>("LagMode", NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final EnumSetting<NewBurrow_SzzqAKQBmpOchBNTHTUz> aboveLagMode = this.add(new EnumSetting<NewBurrow_SzzqAKQBmpOchBNTHTUz>("MoveLagMode", NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart, v -> this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final SliderSetting smartDistance = this.add(new SliderSetting("SmartDistance", 3.0, 0.0, 10.0, 0.1, v -> (this.lagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart || this.aboveLagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart) && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final SliderSetting smartX = this.add(new SliderSetting("SmartXZ", 1.3, 0.0, 10.0, 0.1, v -> (this.lagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart || this.aboveLagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart) && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final SliderSetting smartUp = this.add(new SliderSetting("SmartUp", 6.0, 0.0, 10.0, 0.1, v -> (this.lagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart || this.aboveLagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart) && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final SliderSetting smartDown = this.add(new SliderSetting("SmartDown", 6.0, 0.0, 10.0, 0.1, v -> (this.lagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart || this.aboveLagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Smart) && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final SliderSetting invalids = this.add(new SliderSetting("Invalids", 20.0, 0.0, 100.0, 1.0, v -> (this.lagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Invalid || this.aboveLagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.Invalid) && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final SliderSetting height = this.add(new SliderSetting("Height", -7.0, -10.0, 10.0, 0.1, v -> (this.lagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.HexTech || this.aboveLagMode.getValue() == NewBurrow_SzzqAKQBmpOchBNTHTUz.HexTech) && this.page.is(NewBurrow_CcRGSMXmJEPCxAQiPgnz.Lag)));
    private final List<BlockPos> placePos = new ArrayList<BlockPos>();
    public boolean Force = false;
    public boolean moveAntiLag = false;
    public boolean needAntiLag = false;
    public boolean needStep = false;
    private int progress = 0;
    private boolean above = false;

    public NewBurrow_bHmPnkYIKEocVyqCgEHa() {
        super("NewBurrow", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        if (this.above) {
            return "Above";
        }
        return null;
    }

    @EventHandler
    public void onMove(MoveEvent event) {
        if (this.moveAntiLag) {
            this.moveAntiLag = false;
            if (MovementUtil.isMoving()) {
                return;
            }
            event.setX(this.move.getValue());
            event.setZ(this.move.getValue());
            event.setX(-this.move.getValue());
            event.setZ(-this.move.getValue());
        }
    }

    @Override
    @EventHandler
    public void onUpdate() {
        if (NewBurrow_bHmPnkYIKEocVyqCgEHa.nullCheck()) {
            return;
        }
        if (this.needStep && this.rotater.passed(100L)) {
            this.needStep = false;
        }
        if (HexTech.PLAYER.isInWeb((PlayerEntity)NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player)) {
            this.webTimer.reset();
            this.needAntiLag = true;
            return;
        }
        if (this.usingPause.getValue() && NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.isUsingItem()) {
            return;
        }
        if (this.checkClimb.getValue() && NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.isCrawling()) {
            return;
        }
        if (this.pauseStep.getValue() && Step_EShajbhvQeYkCdreEeNY.INSTANCE.isOn()) {
            return;
        }
        if (HexTech.PLAYER.insideBlock || !MovementUtil.isMoving() ? !this.webTimer.passed(this.webTime.getValue()) : !this.webTimer.passed(this.unNewBurrowWebTime.getValue())) {
            return;
        }
        if (!(this.disable.getValue() || this.timer.passed(this.delay.getValue()) || this.Force)) {
            return;
        }
        this.Force = false;
        if (!NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.isOnGround()) {
            return;
        }
        if (this.antiLag.getValue() && !NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.world.getBlockState(EntityUtil.getPlayerPos().method_10074()).method_51366()) {
            return;
        }
        if (Blink.INSTANCE.isOn()) {
            return;
        }
        int oldSlot = NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getInventory().field_7545;
        int block = this.getBlock();
        if (block == -1) {
            CommandManager.sendChatMessageWidthId("\u00a7c\u00a7oObsidian" + (this.enderChest.getValue() ? "/EnderChest" : "") + "?", this.hashCode());
            this.disable();
            return;
        }
        this.progress = 0;
        this.placePos.clear();
        double offset = CombatSetting_kxXrLvbWbduSuFoeBUsC.getOffset();
        BlockPosX pos1 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() + offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 0.15, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() + offset);
        BlockPosX pos2 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() - offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 0.15, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() + offset);
        BlockPosX pos3 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() + offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 0.15, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() - offset);
        BlockPosX pos4 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() - offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 0.15, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() - offset);
        BlockPosX pos5 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() + offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.5, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() + offset);
        BlockPosX pos6 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() - offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.5, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() + offset);
        BlockPosX pos7 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() + offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.5, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() - offset);
        BlockPosX pos8 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() - offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.5, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() - offset);
        BlockPosX pos9 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() + offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() - 1.0, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() + offset);
        BlockPosX pos10 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() - offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() - 1.0, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() + offset);
        BlockPosX pos11 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() + offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() - 1.0, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() - offset);
        BlockPosX pos12 = new BlockPosX(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() - offset, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() - 1.0, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() - offset);
        BlockPos playerPos = EntityUtil.getPlayerPos();
        boolean headFill = false;
        if (!(this.canPlace(pos1) || this.canPlace(pos2) || this.canPlace(pos3) || this.canPlace(pos4))) {
            boolean cantDown;
            boolean cantHeadFill = !this.headFill.getValue() || !this.canPlace(pos5) && !this.canPlace(pos6) && !this.canPlace(pos7) && !this.canPlace(pos8);
            boolean bl = cantDown = !this.down.getValue() || !this.canPlace(pos9) && !this.canPlace(pos10) && !this.canPlace(pos11) && !this.canPlace(pos12);
            if (cantHeadFill) {
                if (cantDown) {
                    if (!this.wait.getValue() && this.disable.getValue()) {
                        this.disable();
                    }
                    return;
                }
            } else {
                headFill = true;
            }
        }
        this.above = false;
        BlockPos headPos = EntityUtil.getPlayerPos().up(2);
        boolean rotate = this.rotate.getValue() == NewBurrow_qqQhaOJZqeVGcFBKpQgd.Normal;
        CombatUtil.attackCrystal(pos1, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue(), false);
        CombatUtil.attackCrystal(pos2, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue(), false);
        CombatUtil.attackCrystal(pos3, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue(), false);
        CombatUtil.attackCrystal(pos4, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.attackRotate.getValue(), false);
        if (this.antiWebLag.getValue() && this.needAntiLag) {
            this.needAntiLag = false;
            this.moveAntiLag = true;
        }
        if (headFill || NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.isCrawling() || this.trapped(headPos) || this.trapped(headPos.method_10069(1, 0, 0)) || this.trapped(headPos.method_10069(-1, 0, 0)) || this.trapped(headPos.method_10069(0, 0, 1)) || this.trapped(headPos.method_10069(0, 0, -1)) || this.trapped(headPos.method_10069(1, 0, -1)) || this.trapped(headPos.method_10069(-1, 0, -1)) || this.trapped(headPos.method_10069(1, 0, 1)) || this.trapped(headPos.method_10069(-1, 0, 1))) {
            this.above = true;
            if (!this.fakeMove.getValue()) {
                if (!this.wait.getValue() && this.disable.getValue()) {
                    this.disable();
                }
                return;
            }
            boolean moved = false;
            BlockPos offPos = playerPos;
            if (!(!this.checkSelf(offPos) || BlockUtil.canReplace(offPos) || this.headFill.getValue() && BlockUtil.canReplace(offPos.up()))) {
                this.gotoPos(offPos);
            } else {
                for (Direction direction : Direction.values()) {
                    if (direction == Direction.UP || direction == Direction.DOWN || !this.checkSelf(offPos = playerPos.offset(direction)) || BlockUtil.canReplace(offPos) || this.headFill.getValue() && BlockUtil.canReplace(offPos.up())) continue;
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
                            if (direction == Direction.UP || direction == Direction.DOWN || !this.canMove(offPos = playerPos.offset(direction))) continue;
                            this.gotoPos(offPos);
                            moved = true;
                            break;
                        }
                        if (!moved) {
                            if (!this.wait.getValue() && this.disable.getValue()) {
                                this.disable();
                            }
                            return;
                        }
                    }
                }
            }
        } else {
            mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 0.4199999868869781, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
            mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 0.7531999805212017, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
            mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 0.9999957640154541, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
            mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.1661092609382138, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
        }
        this.doSwap(block);
        if (this.rotate.getValue() == NewBurrow_qqQhaOJZqeVGcFBKpQgd.Bypass) {
            EntityUtil.sendYawAndPitch(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.method_36454(), 90.0f);
        }
        this.placeBlock(playerPos, rotate);
        this.placeBlock(pos1, rotate);
        this.placeBlock(pos2, rotate);
        this.placeBlock(pos3, rotate);
        this.placeBlock(pos4, rotate);
        if (this.down.getValue()) {
            this.placeBlock(pos9, rotate);
            this.placeBlock(pos10, rotate);
            this.placeBlock(pos11, rotate);
            this.placeBlock(pos12, rotate);
        }
        if (this.headFill.getValue() && this.above) {
            this.placeBlock(pos5, rotate);
            this.placeBlock(pos6, rotate);
            this.placeBlock(pos7, rotate);
            this.placeBlock(pos8, rotate);
        }
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(oldSlot);
        }
        switch ((this.above ? this.aboveLagMode.getValue() : this.lagMode.getValue()).ordinal()) {
            case 0: {
                ArrayList<BlockPosX> list = new ArrayList<BlockPosX>();
                for (double x = NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.method_19538().method_10216() - this.smartX.getValue(); x < NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.method_19538().method_10216() + this.smartX.getValue(); x += 1.0) {
                    for (double z = NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.method_19538().method_10215() - this.smartX.getValue(); z < NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.method_19538().method_10215() + this.smartX.getValue(); z += 1.0) {
                        for (double y = NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.method_19538().method_10214() - this.smartDown.getValue(); y < NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.method_19538().method_10214() + this.smartUp.getValue(); y += 1.0) {
                            list.add(new BlockPosX(x, y, z));
                        }
                    }
                }
                double distance = 0.0;
                BlockPos bestPos = null;
                for (BlockPos blockPos : list) {
                    if (!this.canMove(blockPos) || (double)MathHelper.method_15355((float)((float)NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.squaredDistanceTo(blockPos.toCenterPos().method_1031(0.0, -0.5, 0.0)))) < this.smartDistance.getValue() || bestPos != null && !(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.squaredDistanceTo(blockPos.toCenterPos()) < distance)) continue;
                    bestPos = blockPos;
                    distance = NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.squaredDistanceTo(blockPos.toCenterPos());
                }
                if (bestPos == null) break;
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround((double)bestPos.method_10263() + 0.5, (double)bestPos.method_10264(), (double)bestPos.method_10260() + 0.5, false));
                break;
            }
            case 1: {
                int i = 0;
                while ((double)i < this.invalids.getValue()) {
                    mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1337.0, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                    ++i;
                }
                break;
            }
            case 7: {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.16610926093821, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.170005801788139, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.2426308013947485, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 2.3400880035762786, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 2.640088003576279, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                break;
            }
            case 8: {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.0001, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.0405, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.0802, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.1027, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                break;
            }
            case 2: {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 2.3400880035762786, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                break;
            }
            case 5: {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + 1.9, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                break;
            }
            case 3: {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), -70.0, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                break;
            }
            case 4: {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), -7.0, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                break;
            }
            case 9: {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + this.height.getValue(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), false));
                break;
            }
            case 6: {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.LookAndOnGround(-180.0f, -90.0f, false));
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.LookAndOnGround(180.0f, 90.0f, false));
            }
        }
        if (this.disable.getValue()) {
            this.disable();
        }
    }

    private void placeBlock(BlockPos pos, boolean rotate) {
        if (this.canPlace(pos) && !this.placePos.contains(pos) && this.progress < this.blocksPer.getValueInt()) {
            Direction side;
            if (this.noMine.getValue() && pos.equals((Object)SpeedMine.breakPos) && SpeedMine.INSTANCE.isOn()) {
                SpeedMine.breakPos = null;
            }
            this.placePos.add(pos);
            if (BlockUtil.airPlace()) {
                ++this.progress;
                BlockUtil.placedPos.add(pos);
                if (this.sound.getValue()) {
                    NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.world.method_8396((PlayerEntity)NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player, pos, SoundEvents.field_14574, SoundCategory.field_15245, 1.0f, 0.8f);
                }
                BlockUtil.clickBlock(pos, Direction.DOWN, rotate, this.packetPlace.getValue());
            }
            if ((side = BlockUtil.getPlaceSide(pos)) == null) {
                return;
            }
            ++this.progress;
            BlockUtil.placedPos.add(pos);
            if (this.sound.getValue()) {
                NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.world.method_8396((PlayerEntity)NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player, pos, SoundEvents.field_14574, SoundCategory.field_15245, 1.0f, 0.8f);
            }
            BlockUtil.clickBlock(pos.offset(side), side.method_10153(), rotate, this.packetPlace.getValue());
            this.timer.reset();
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getInventory().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private void gotoPos(BlockPos offPos) {
        if (this.gotoMode.is(NewBurrow.Alien)) {
            if (this.rotate.getValue() == NewBurrow_qqQhaOJZqeVGcFBKpQgd.None) {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround((double)offPos.method_10263() + 0.5, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + this.moveUp.getValue(), (double)offPos.method_10260() + 0.5, false));
            } else {
                mc.method_1562().sendPacket((Packet)new PlayerMoveC2SPacket.Full((double)offPos.method_10263() + 0.5, NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + this.moveUp.getValue(), (double)offPos.method_10260() + 0.5, HexTech.ROTATE.rotateYaw, 90.0f, false));
            }
        } else if (Math.abs((double)offPos.method_10263() + 0.5 - NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX()) < Math.abs((double)offPos.method_10260() + 0.5 - NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ())) {
            NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.field_3944.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + this.moveUp.getValue(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ() + ((double)offPos.method_10260() + 0.5 - NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ()), true));
        } else {
            NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.field_3944.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX() + ((double)offPos.method_10263() + 0.2 - NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getX()), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getY() + this.moveUp.getValue(), NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getZ(), true));
        }
    }

    private boolean canMove(BlockPos pos) {
        return NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.world.isAir(pos) && NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.world.isAir(pos.up());
    }

    private boolean canPlace(BlockPos pos) {
        if (this.noSelfPos.getValue() && pos.equals((Object)EntityUtil.getPlayerPos())) {
            return false;
        }
        if (!BlockUtil.airPlace() && BlockUtil.getPlaceSide(pos) == null) {
            return false;
        }
        if (this.detectMine.getValue() && HexTech.BREAK.isMining(pos)) {
            return false;
        }
        return !this.hasEntity(pos);
    }

    private boolean hasEntity(BlockPos pos) {
        for (Entity entity : BlockUtil.getEntities(new Box(pos))) {
            if (entity == NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player || !entity.method_5805() || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ArrowEntity || entity instanceof EndCrystalEntity && this.breakCrystal.getValue() || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return true;
        }
        return false;
    }

    private boolean checkSelf(BlockPos pos) {
        return NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player.getBoundingBox().method_994(new Box(pos));
    }

    private boolean trapped(BlockPos pos) {
        return (NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.world.method_39454((Entity)NewBurrow_bHmPnkYIKEocVyqCgEHa.mc.player, new Box(pos)) || BlockUtil.getBlock(pos) == Blocks.field_10343) && this.checkSelf(pos.down(2));
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
}
