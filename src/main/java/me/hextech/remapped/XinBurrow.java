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
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
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

public class XinBurrow
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static XinBurrow INSTANCE;
    private final Timer timer = new Timer();
    private final Timer webTimer = new Timer();
    private final BooleanSetting disable = this.add(new BooleanSetting("Disable", true));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 500, 0, 1000, v -> !this.disable.getValue()));
    private final SliderSetting webTime = this.add(new SliderSetting("WebTime", 0, 0, 500));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true));
    private final BooleanSetting antiLag = this.add(new BooleanSetting("AntiLag", false));
    private final BooleanSetting detectMine = this.add(new BooleanSetting("DetectMining", false));
    private final BooleanSetting headFill = this.add(new BooleanSetting("HeadFill", false));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", true));
    private final BooleanSetting noSelfPos = this.add(new BooleanSetting("NoSelfPos", false));
    private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true));
    private final BooleanSetting sound = this.add(new BooleanSetting("Sound", true));
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 4.0, 1.0, 4.0, 1.0));
    private final EnumSetting<_uPHLOgEUPRaLZqLkrbQU> rotate = this.add(new EnumSetting<_uPHLOgEUPRaLZqLkrbQU>("RotateMode", _uPHLOgEUPRaLZqLkrbQU.Bypass));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true));
    private final BooleanSetting wait = this.add(new BooleanSetting("Wait", true, v -> !this.disable.getValue()));
    private final BooleanSetting fakeMove = this.add(new BooleanSetting("FakeMove", true).setParent());
    private final BooleanSetting center = this.add(new BooleanSetting("AllowCenter", false, v -> this.fakeMove.isOpen()));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final EnumSetting<_nDnthhFJjiqtqyXrSjjG> lagMode = this.add(new EnumSetting<_nDnthhFJjiqtqyXrSjjG>("LagMode", _nDnthhFJjiqtqyXrSjjG.TrollHack));
    private final EnumSetting<_nDnthhFJjiqtqyXrSjjG> aboveLagMode = this.add(new EnumSetting<_nDnthhFJjiqtqyXrSjjG>("MoveLagMode", _nDnthhFJjiqtqyXrSjjG.Smart));
    private final SliderSetting smartX = this.add(new SliderSetting("SmartXZ", 3.0, 0.0, 10.0, 0.1, v -> this.lagMode.getValue() == _nDnthhFJjiqtqyXrSjjG.Smart || this.aboveLagMode.getValue() == _nDnthhFJjiqtqyXrSjjG.Smart));
    private final SliderSetting smartUp = this.add(new SliderSetting("SmartUp", 3.0, 0.0, 10.0, 0.1, v -> this.lagMode.getValue() == _nDnthhFJjiqtqyXrSjjG.Smart || this.aboveLagMode.getValue() == _nDnthhFJjiqtqyXrSjjG.Smart));
    private final SliderSetting smartDown = this.add(new SliderSetting("SmartDown", 3.0, 0.0, 10.0, 0.1, v -> this.lagMode.getValue() == _nDnthhFJjiqtqyXrSjjG.Smart || this.aboveLagMode.getValue() == _nDnthhFJjiqtqyXrSjjG.Smart));
    private final SliderSetting smartDistance = this.add(new SliderSetting("SmartDistance", 2.0, 0.0, 10.0, 0.1, v -> this.lagMode.getValue() == _nDnthhFJjiqtqyXrSjjG.Smart || this.aboveLagMode.getValue() == _nDnthhFJjiqtqyXrSjjG.Smart));
    private final List<BlockPos> placePos = new ArrayList<BlockPos>();
    private int progress = 0;

    public XinBurrow() {
        super("XinBurrow", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (HexTech.PLAYER.isInWeb((PlayerEntity)XinBurrow.mc.player)) {
            this.webTimer.reset();
            return;
        }
        if (this.usingPause.getValue() && XinBurrow.mc.player.isUsingItem()) {
            return;
        }
        if (!this.webTimer.passed(this.webTime.getValue())) {
            return;
        }
        if (!this.disable.getValue() && !this.timer.passed(this.delay.getValue())) {
            return;
        }
        if (!XinBurrow.mc.player.isOnGround()) {
            return;
        }
        if (this.antiLag.getValue() && !XinBurrow.mc.world.getBlockState(EntityUtil.getPlayerPos(true).down()).method_51366()) {
            return;
        }
        if (Blink.INSTANCE.isOn()) {
            return;
        }
        int oldSlot = XinBurrow.mc.player.getInventory().selectedSlot;
        int block = this.getBlock();
        if (block == -1) {
            this.disable();
            return;
        }
        this.progress = 0;
        this.placePos.clear();
        double offset = CombatSetting_kxXrLvbWbduSuFoeBUsC.getOffset();
        BlockPosX pos1 = new BlockPosX(XinBurrow.mc.player.getX() + offset, XinBurrow.mc.player.getY() + 0.5, XinBurrow.mc.player.getZ() + offset);
        BlockPosX pos2 = new BlockPosX(XinBurrow.mc.player.getX() - offset, XinBurrow.mc.player.getY() + 0.5, XinBurrow.mc.player.getZ() + offset);
        BlockPosX pos3 = new BlockPosX(XinBurrow.mc.player.getX() + offset, XinBurrow.mc.player.getY() + 0.5, XinBurrow.mc.player.getZ() - offset);
        BlockPosX pos4 = new BlockPosX(XinBurrow.mc.player.getX() - offset, XinBurrow.mc.player.getY() + 0.5, XinBurrow.mc.player.getZ() - offset);
        BlockPosX pos5 = new BlockPosX(XinBurrow.mc.player.getX() + offset, XinBurrow.mc.player.getY() + 1.5, XinBurrow.mc.player.getZ() + offset);
        BlockPosX pos6 = new BlockPosX(XinBurrow.mc.player.getX() - offset, XinBurrow.mc.player.getY() + 1.5, XinBurrow.mc.player.getZ() + offset);
        BlockPosX pos7 = new BlockPosX(XinBurrow.mc.player.getX() + offset, XinBurrow.mc.player.getY() + 1.5, XinBurrow.mc.player.getZ() - offset);
        BlockPosX pos8 = new BlockPosX(XinBurrow.mc.player.getX() - offset, XinBurrow.mc.player.getY() + 1.5, XinBurrow.mc.player.getZ() - offset);
        BlockPosX pos9 = new BlockPosX(XinBurrow.mc.player.getX() + offset, XinBurrow.mc.player.getY() - 1.0, XinBurrow.mc.player.getZ() + offset);
        BlockPosX pos10 = new BlockPosX(XinBurrow.mc.player.getX() - offset, XinBurrow.mc.player.getY() - 1.0, XinBurrow.mc.player.getZ() + offset);
        BlockPosX pos11 = new BlockPosX(XinBurrow.mc.player.getX() + offset, XinBurrow.mc.player.getY() - 1.0, XinBurrow.mc.player.getZ() - offset);
        BlockPosX pos12 = new BlockPosX(XinBurrow.mc.player.getX() - offset, XinBurrow.mc.player.getY() - 1.0, XinBurrow.mc.player.getZ() - offset);
        BlockPos playerPos = EntityUtil.getPlayerPos(true);
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
        boolean above = false;
        BlockPos headPos = EntityUtil.getPlayerPos(true).up(2);
        boolean rotate = this.rotate.getValue() == _uPHLOgEUPRaLZqLkrbQU.Normal;
        CombatUtil.attackCrystal(pos1, rotate, false);
        CombatUtil.attackCrystal(pos2, rotate, false);
        CombatUtil.attackCrystal(pos3, rotate, false);
        CombatUtil.attackCrystal(pos4, rotate, false);
        if (headFill || XinBurrow.mc.player.isCrawling() || this.trapped(headPos) || this.trapped(headPos.add(1, 0, 0)) || this.trapped(headPos.add(-1, 0, 0)) || this.trapped(headPos.add(0, 0, 1)) || this.trapped(headPos.add(0, 0, -1)) || this.trapped(headPos.add(1, 0, -1)) || this.trapped(headPos.add(-1, 0, -1)) || this.trapped(headPos.add(1, 0, 1)) || this.trapped(headPos.add(-1, 0, 1))) {
            above = true;
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
            mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 0.4199999868869781, XinBurrow.mc.player.getZ(), false));
            mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 0.7531999805212017, XinBurrow.mc.player.getZ(), false));
            mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 0.9999957640154541, XinBurrow.mc.player.getZ(), false));
            mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.1661092609382138, XinBurrow.mc.player.getZ(), false));
        }
        this.timer.reset();
        this.doSwap(block);
        if (this.rotate.getValue() == _uPHLOgEUPRaLZqLkrbQU.Bypass) {
            EntityUtil.sendYawAndPitch(XinBurrow.mc.player.method_36454(), 90.0f);
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
        if (this.headFill.getValue() && above) {
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
        switch ((above ? this.aboveLagMode.getValue() : this.lagMode.getValue()).ordinal()) {
            case 0: {
                ArrayList<BlockPosX> list = new ArrayList<BlockPosX>();
                for (double x = XinBurrow.mc.player.method_19538().method_10216() - this.smartX.getValue(); x < XinBurrow.mc.player.method_19538().method_10216() + this.smartX.getValue(); x += 1.0) {
                    for (double z = XinBurrow.mc.player.method_19538().method_10215() - this.smartX.getValue(); z < XinBurrow.mc.player.method_19538().method_10215() + this.smartX.getValue(); z += 1.0) {
                        for (double y = XinBurrow.mc.player.method_19538().method_10214() - this.smartDown.getValue(); y < XinBurrow.mc.player.method_19538().method_10214() + this.smartUp.getValue(); y += 1.0) {
                            list.add(new BlockPosX(x, y, z));
                        }
                    }
                }
                double distance = 0.0;
                BlockPos bestPos = null;
                for (BlockPos blockPos : list) {
                    if (!this.canMove(blockPos) || (double)MathHelper.sqrt((float)((float)XinBurrow.mc.player.squaredDistanceTo(blockPos.toCenterPos().add(0.0, -0.5, 0.0)))) < this.smartDistance.getValue() || bestPos != null && !(XinBurrow.mc.player.squaredDistanceTo(blockPos.toCenterPos()) < distance)) continue;
                    bestPos = blockPos;
                    distance = XinBurrow.mc.player.squaredDistanceTo(blockPos.toCenterPos());
                }
                if (bestPos == null) break;
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround((double)bestPos.method_10263() + 0.5, (double)bestPos.method_10264(), (double)bestPos.method_10260() + 0.5, false));
                break;
            }
            case 1: {
                for (int i = 0; i < 20; ++i) {
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1337.0, XinBurrow.mc.player.getZ(), false));
                }
                break;
            }
            case 7: {
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.16610926093821, XinBurrow.mc.player.getZ(), false));
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.170005801788139, XinBurrow.mc.player.getZ(), false));
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.2426308013947485, XinBurrow.mc.player.getZ(), false));
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 2.3400880035762786, XinBurrow.mc.player.getZ(), false));
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 2.640088003576279, XinBurrow.mc.player.getZ(), false));
                break;
            }
            case 8: {
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.0001, XinBurrow.mc.player.getZ(), false));
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.0405, XinBurrow.mc.player.getZ(), false));
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.0802, XinBurrow.mc.player.getZ(), false));
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.1027, XinBurrow.mc.player.getZ(), false));
                break;
            }
            case 2: {
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 2.3400880035762786, XinBurrow.mc.player.getZ(), false));
                break;
            }
            case 5: {
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), XinBurrow.mc.player.getY() + 1.9, XinBurrow.mc.player.getZ(), false));
                break;
            }
            case 3: {
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), -70.0, XinBurrow.mc.player.getZ(), false));
                break;
            }
            case 4: {
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(XinBurrow.mc.player.getX(), -7.0, XinBurrow.mc.player.getZ(), false));
                break;
            }
            case 6: {
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.LookAndOnGround(-180.0f, -90.0f, false));
                mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.LookAndOnGround(180.0f, 90.0f, false));
            }
        }
        if (this.disable.getValue()) {
            this.disable();
        }
    }

    private void placeBlock(BlockPos pos, boolean rotate) {
        if (this.canPlace(pos) && !this.placePos.contains(pos) && this.progress < this.blocksPer.getValueInt()) {
            Direction side;
            this.placePos.add(pos);
            if (BlockUtil.airPlace()) {
                ++this.progress;
                BlockUtil.placedPos.add(pos);
                if (this.sound.getValue()) {
                    XinBurrow.mc.world.method_8396((PlayerEntity)XinBurrow.mc.player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0f, 0.8f);
                }
                BlockUtil.clickBlock(pos, Direction.DOWN, rotate, this.packetPlace.getValue());
            }
            if ((side = BlockUtil.getPlaceSide(pos)) == null) {
                return;
            }
            ++this.progress;
            BlockUtil.placedPos.add(pos);
            if (this.sound.getValue()) {
                XinBurrow.mc.world.method_8396((PlayerEntity)XinBurrow.mc.player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0f, 0.8f);
            }
            BlockUtil.clickBlock(pos.offset(side), side.getOpposite(), rotate, this.packetPlace.getValue());
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, XinBurrow.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private void gotoPos(BlockPos offPos) {
        if (this.rotate.getValue() == _uPHLOgEUPRaLZqLkrbQU.None) {
            mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround((double)offPos.method_10263() + 0.5, XinBurrow.mc.player.getY() + 0.1, (double)offPos.method_10260() + 0.5, false));
        } else {
            mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.Full((double)offPos.method_10263() + 0.5, XinBurrow.mc.player.getY() + 0.1, (double)offPos.method_10260() + 0.5, HexTech.ROTATE.rotateYaw, 90.0f, false));
        }
    }

    private boolean canMove(BlockPos pos) {
        return XinBurrow.mc.world.isAir(pos) && XinBurrow.mc.world.isAir(pos.up());
    }

    private boolean canPlace(BlockPos pos) {
        if (this.noSelfPos.getValue() && pos.equals((Object)EntityUtil.getPlayerPos(true))) {
            return false;
        }
        if (!BlockUtil.airPlace() && BlockUtil.getPlaceSide(pos) == null) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        if (this.detectMine.getValue() && HexTech.BREAK.isMining(pos)) {
            return false;
        }
        return !this.hasEntity(pos);
    }

    private boolean hasEntity(BlockPos pos) {
        for (Entity entity : BlockUtil.getEntities(new Box(pos))) {
            if (entity == XinBurrow.mc.player || !entity.isAlive() || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ArrowEntity || entity instanceof EndCrystalEntity && this.breakCrystal.getValue()) continue;
            return true;
        }
        return false;
    }

    private boolean checkSelf(BlockPos pos) {
        return XinBurrow.mc.player.getBoundingBox().intersects(new Box(pos));
    }

    private boolean trapped(BlockPos pos) {
        return (XinBurrow.mc.world.method_39454((Entity)XinBurrow.mc.player, new Box(pos)) || BlockUtil.getBlock(pos) == Blocks.COBWEB) && this.checkSelf(pos.down(2));
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

    private static enum _uPHLOgEUPRaLZqLkrbQU {
        Bypass,
        Normal,
        None;

    }

    private static enum _nDnthhFJjiqtqyXrSjjG {
        Smart,
        Invalid,
        TrollHack,
        ToVoid,
        ToVoid2,
        Normal,
        Rotation,
        Fly,
        Glide;

    }
}
