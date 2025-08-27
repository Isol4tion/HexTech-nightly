package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.BindSetting;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class HolePush
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HolePush INSTANCE;
    private final EnumSetting<_yzHmKDmPzgLjnTXAWdrq> page = this.add(new EnumSetting<_yzHmKDmPzgLjnTXAWdrq>("Page", _yzHmKDmPzgLjnTXAWdrq.General));
    private final BooleanSetting torch = this.add(new BooleanSetting("Torch", false, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final BooleanSetting rotate = this.add(new BooleanSetting("ROTATE", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Rotate));
    private final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Rotate));
    private final BooleanSetting allowWeb = this.add(new BooleanSetting("AllowWeb", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    public final BindSetting allowKey = this.add(new BindSetting("AllowKey", -1, v -> !this.allowWeb.getValue() && this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    private final BooleanSetting noEating = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    private final BooleanSetting onlyNoInside = this.add(new BooleanSetting("OnlyNoInside", false, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    private final BooleanSetting onlyInside = this.add(new BooleanSetting("OnlyInside", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    private final SliderSetting surroundCheck = this.add(new SliderSetting("SurroundCheck", 0, 0, 4, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    private final BooleanSetting syncCrystal = this.add(new BooleanSetting("SyncCrystal", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.Check));
    private final BooleanSetting pistonPacket = this.add(new BooleanSetting("PistonPacket", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final BooleanSetting powerPacket = this.add(new BooleanSetting("PowerPacket", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final BooleanSetting pauseCity = this.add(new BooleanSetting("PauseCity", true, v -> this.mine.isOpen() && this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final SliderSetting updateDelay = this.add(new SliderSetting("Delay", 200, 0, 1000, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final SliderSetting range = this.add(new SliderSetting("Range", 4.7, 0.0, 6.0, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4.7, 0.0, 6.0, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == _yzHmKDmPzgLjnTXAWdrq.General));
    private final Timer timer = new Timer();
    Vec3d directionVec = null;

    public HolePush() {
        super("HolePush", Module_JlagirAibYQgkHtbRnhw.Combat);
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

    @Override
    public void onEnable() {
        AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastBreakTimer.reset();
    }

    private boolean faceVector(Vec3d directionVec) {
        EntityUtil.faceVector(directionVec);
        return true;
    }

    boolean isTargetHere(BlockPos pos, Entity target) {
        return new Box(pos).intersects(target.getBoundingBox());
    }

    @Override
    public void onUpdate() {
        if (!this.timer.passedMs(this.updateDelay.getValue())) {
            return;
        }
        if (this.selfGround.getValue() && !HolePush.mc.player.isOnGround()) {
            return;
        }
        if (this.findBlock(this.getBlockType()) == -1 || this.findClass(PistonBlock.class) == -1) {
            if (this.autoDisable.getValue()) {
                this.disable();
            }
            return;
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null && this.syncCrystal.getValue()) {
            return;
        }
        if (this.noEating.getValue() && HolePush.mc.player.isUsingItem()) {
            return;
        }
        if (Blink.INSTANCE.isOn()) {
            return;
        }
        for (PlayerEntity player : CombatUtil.getEnemies(this.range.getValue())) {
            BlockPos pos;
            BlockPosX playerPos;
            float[] offset;
            if (!this.canPush(player).booleanValue()) continue;
            for (Direction i : Direction.values()) {
                BlockPos pos2;
                if (i == Direction.UP || i == Direction.DOWN || !this.isTargetHere(pos2 = EntityUtil.getEntityPos((Entity)player).offset(i), (Entity)player) || !HolePush.mc.world.canCollide((Entity)player, new Box(pos2))) continue;
                if (this.tryPush(EntityUtil.getEntityPos((Entity)player).offset(i.getOpposite()), i)) {
                    this.timer.reset();
                    return;
                }
                if (!this.tryPush(EntityUtil.getEntityPos((Entity)player).offset(i.getOpposite()).up(), i)) continue;
                this.timer.reset();
                return;
            }
            for (float x : offset = new float[]{-0.25f, 0.0f, 0.25f}) {
                for (float z : offset) {
                    playerPos = new BlockPosX(player.getX() + (double)x, player.getY() + 0.5, player.getZ() + (double)z);
                    for (Direction i : Direction.values()) {
                        if (i == Direction.UP || i == Direction.DOWN || !this.isTargetHere(pos = playerPos.offset(i), (Entity)player) || !HolePush.mc.world.canCollide((Entity)player, new Box(pos))) continue;
                        if (this.tryPush(playerPos.offset(i.getOpposite()), i)) {
                            this.timer.reset();
                        }
                        if (!this.tryPush(playerPos.offset(i.getOpposite()).up(), i)) continue;
                        this.timer.reset();
                        return;
                    }
                }
            }
            if (!HolePush.mc.world.canCollide((Entity)player, new Box((BlockPos)new BlockPosX(player.getX(), player.getY() + 2.5, player.getZ())))) {
                for (Direction i : Direction.values()) {
                    if (i == Direction.UP || i == Direction.DOWN) continue;
                    BlockPos pos3 = EntityUtil.getEntityPos((Entity)player).offset(i);
                    Box box = player.getBoundingBox().offset(new Vec3d((double)i.getOffsetX(), (double)i.getOffsetY(), (double)i.getOffsetZ()));
                    if (this.getBlock(pos3.up()) == Blocks.PISTON_HEAD || HolePush.mc.world.canCollide((Entity)player, box.offset(0.0, 1.0, 0.0)) || this.isTargetHere(pos3, (Entity)player)) continue;
                    if (this.tryPush(EntityUtil.getEntityPos((Entity)player).offset(i.getOpposite()).up(), i)) {
                        this.timer.reset();
                        return;
                    }
                    if (!this.tryPush(EntityUtil.getEntityPos((Entity)player).offset(i.getOpposite()), i)) continue;
                    this.timer.reset();
                    return;
                }
            }
            for (float x : offset) {
                for (float z : offset) {
                    playerPos = new BlockPosX(player.getX() + (double)x, player.getY() + 0.5, player.getZ() + (double)z);
                    for (Direction i : Direction.values()) {
                        if (i == Direction.UP || i == Direction.DOWN || !this.isTargetHere(pos = playerPos.offset(i), (Entity)player)) continue;
                        if (this.tryPush(playerPos.offset(i.getOpposite()).up(), i)) {
                            this.timer.reset();
                            return;
                        }
                        if (!this.tryPush(playerPos.offset(i.getOpposite()), i)) continue;
                        this.timer.reset();
                        return;
                    }
                }
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    private boolean tryPush(BlockPos piston, Direction direction) {
        BlockState state;
        if (!HolePush.mc.world.isAir(piston.offset(direction))) {
            return false;
        }
        if (this.isTrueFacing(piston, direction) && this.facingCheck(piston) && BlockUtil.clientCanPlace(piston)) {
            boolean canPower = false;
            if (BlockUtil.getPlaceSide(piston, this.placeRange.getValue()) != null) {
                CombatUtil.modifyPos = piston;
                CombatUtil.modifyBlockState = Blocks.PISTON.getDefaultState();
                for (Direction direction2 : Direction.values()) {
                    if (this.getBlock(piston.offset(direction2)) != this.getBlockType()) continue;
                    canPower = true;
                    break;
                }
                Direction[] directionArray = Direction.values();
                int n = directionArray.length;
                for (int i = 0; i < n; ++i) {
                    Direction direction3 = directionArray[i];
                    if (canPower) break;
                    if (!BlockUtil.canPlace(piston.offset(direction3), this.placeRange.getValue())) continue;
                    canPower = true;
                }
                CombatUtil.modifyPos = null;
                if (canPower) {
                    int pistonSlot = this.findClass(PistonBlock.class);
                    Direction side = BlockUtil.getPlaceSide(piston);
                    if (side != null) {
                        Vec3d directionVec;
                        if (this.rotate.getValue() && !this.faceVector(directionVec = new Vec3d((double)piston.getX() + 0.5 + (double)side.getVector().getX() * 0.5, (double)piston.getY() + 0.5 + (double)side.getVector().getY() * 0.5, (double)piston.getZ() + 0.5 + (double)side.getVector().getZ() * 0.5))) {
                            return true;
                        }
                        if (this.yawDeceive.getValue()) {
                            HolePush.pistonFacing(direction.getOpposite());
                        }
                        int old = HolePush.mc.player.getInventory().selectedSlot;
                        this.doSwap(pistonSlot);
                        BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                        if (this.inventory.getValue()) {
                            this.doSwap(pistonSlot);
                            EntityUtil.syncInventory();
                        } else {
                            this.doSwap(old);
                        }
                        if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                            EntityUtil.facePosSide(piston.offset(side), side.getOpposite());
                        }
                        for (Direction i : Direction.values()) {
                            if (this.getBlock(piston.offset(i)) != this.getBlockType()) continue;
                            if (this.mine.getValue() && !this.pauseCity.getValue() || this.mine.getValue() && SpeedMine.breakPos == SpeedMine.secondPos) {
                                SpeedMine.INSTANCE.mine(piston.offset(i));
                            }
                            if (this.autoDisable.getValue()) {
                                this.disable();
                            }
                            return true;
                        }
                        for (Direction i : Direction.values()) {
                            if (i == Direction.UP && this.torch.getValue() || !BlockUtil.canPlace(piston.offset(i), this.placeRange.getValue())) continue;
                            int oldSlot = HolePush.mc.player.getInventory().selectedSlot;
                            int powerSlot = this.findBlock(this.getBlockType());
                            this.doSwap(powerSlot);
                            BlockUtil.placeBlock(piston.offset(i), this.rotate.getValue(), this.powerPacket.getValue());
                            if (this.inventory.getValue()) {
                                this.doSwap(powerSlot);
                                EntityUtil.syncInventory();
                            } else {
                                this.doSwap(oldSlot);
                            }
                            if (this.mine.getValue() && !this.pauseCity.getValue() || this.mine.getValue() && SpeedMine.breakPos == SpeedMine.secondPos) {
                                SpeedMine.INSTANCE.mine(piston.offset(i));
                            }
                            return true;
                        }
                        return true;
                    }
                }
            } else {
                void var7_23;
                Direction powerFacing = null;
                Direction[] side = Direction.values();
                int old = side.length;
                boolean bl = false;
                while (var7_23 < old) {
                    Direction i = side[var7_23];
                    if (i != Direction.UP || !this.torch.getValue()) {
                        if (powerFacing != null) break;
                        CombatUtil.modifyPos = piston.offset(i);
                        CombatUtil.modifyBlockState = this.getBlockType().getDefaultState();
                        if (BlockUtil.getPlaceSide(piston) != null) {
                            powerFacing = i;
                        }
                        CombatUtil.modifyPos = null;
                        if (powerFacing != null && !BlockUtil.canPlace(piston.offset(powerFacing))) {
                            powerFacing = null;
                        }
                    }
                    ++var7_23;
                }
                if (powerFacing != null) {
                    int oldSlot = HolePush.mc.player.getInventory().selectedSlot;
                    int powerSlot = this.findBlock(this.getBlockType());
                    this.doSwap(powerSlot);
                    BlockUtil.placeBlock(piston.offset(powerFacing), this.rotate.getValue(), this.powerPacket.getValue());
                    if (this.inventory.getValue()) {
                        this.doSwap(powerSlot);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(oldSlot);
                    }
                    CombatUtil.modifyPos = piston.offset(powerFacing);
                    CombatUtil.modifyBlockState = this.getBlockType().getDefaultState();
                    int n = this.findClass(PistonBlock.class);
                    Direction side2 = BlockUtil.getPlaceSide(piston);
                    if (side2 != null) {
                        Vec3d directionVec;
                        if (this.rotate.getValue() && !this.faceVector(directionVec = new Vec3d((double)piston.getX() + 0.5 + (double)side2.getVector().getX() * 0.5, (double)piston.getY() + 0.5 + (double)side2.getVector().getY() * 0.5, (double)piston.getZ() + 0.5 + (double)side2.getVector().getZ() * 0.5))) {
                            return true;
                        }
                        if (this.yawDeceive.getValue()) {
                            HolePush.pistonFacing(direction.getOpposite());
                        }
                        int old2 = HolePush.mc.player.getInventory().selectedSlot;
                        this.doSwap(n);
                        BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                        if (this.inventory.getValue()) {
                            this.doSwap(n);
                            EntityUtil.syncInventory();
                        } else {
                            this.doSwap(old2);
                        }
                        if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                            EntityUtil.facePosSide(piston.offset(side2), side2.getOpposite());
                        }
                    }
                    CombatUtil.modifyPos = null;
                    return true;
                }
            }
        }
        if ((state = HolePush.mc.world.getBlockState(piston)).getBlock() instanceof PistonBlock && this.getBlockState(piston).get((Property)FacingBlock.FACING) == direction) {
            for (Direction direction4 : Direction.values()) {
                if (this.getBlock(piston.offset(direction4)) != this.getBlockType()) continue;
                if (this.autoDisable.getValue()) {
                    this.disable();
                    return true;
                }
                return false;
            }
            for (Direction direction5 : Direction.values()) {
                if (direction5 == Direction.UP && this.torch.getValue() || !BlockUtil.canPlace(piston.offset(direction5), this.placeRange.getValue())) continue;
                int oldSlot = HolePush.mc.player.getInventory().selectedSlot;
                int powerSlot = this.findBlock(this.getBlockType());
                this.doSwap(powerSlot);
                BlockUtil.placeBlock(piston.offset(direction5), this.rotate.getValue(), this.powerPacket.getValue());
                if (this.inventory.getValue()) {
                    this.doSwap(powerSlot);
                    EntityUtil.syncInventory();
                } else {
                    this.doSwap(oldSlot);
                }
                return true;
            }
        }
        this.directionVec = null;
        return false;
    }

    @Override
    public String getInfo() {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null || !this.syncCrystal.getValue()) {
            if (this.allowWeb.getValue()) {
                return "CrazyDog";
            }
            if (this.allowKey.isPressed()) {
                return "CrazyDog";
            }
            return "SmartPush";
        }
        return "WaitSync";
    }

    private boolean facingCheck(BlockPos pos) {
        return true;
    }

    private boolean isTrueFacing(BlockPos pos, Direction facing) {
        if (this.yawDeceive.getValue()) {
            return true;
        }
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            return false;
        }
        Vec3d directionVec = new Vec3d((double)pos.getX() + 0.5 + (double)side.getVector().getX() * 0.5, (double)pos.getY() + 0.5 + (double)side.getVector().getY() * 0.5, (double)pos.getZ() + 0.5 + (double)side.getVector().getZ() * 0.5);
        float[] ROTATE = HexTech.ROTATE.getRotation(directionVec);
        return MathUtil.getFacingOrder(ROTATE[0], ROTATE[1]).getOpposite() == facing;
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, HolePush.mc.player.getInventory().selectedSlot);
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

    private boolean burrowUpdate(PlayerEntity player) {
        for (float x : new float[]{0.0f, 0.3f, -0.3f}) {
            for (float z : new float[]{0.0f, 0.3f, -0.3f}) {
                BlockPosX pos = new BlockPosX(player.getX() + (double)x, player.getY() + 1.5, player.getZ() + (double)z);
                if (!new Box((BlockPos)pos).intersects(player.getBoundingBox()) || HolePush.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.OBSIDIAN && HolePush.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.RESPAWN_ANCHOR && HolePush.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.BEDROCK && HolePush.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.ENDER_CHEST) continue;
                return true;
            }
        }
        return false;
    }

    private Boolean canPush(PlayerEntity player) {
        if (this.onlyGround.getValue() && !player.isOnGround()) {
            return false;
        }
        if (!this.allowWeb.getValue() && HexTech.PLAYER.isInWeb(player) && !this.allowKey.isPressed()) {
            return false;
        }
        if (this.onlyNoInside.getValue() && this.burrowUpdate(player)) {
            return false;
        }
        if (this.onlyInside.getValue() && !this.burrowUpdate(player)) {
            return false;
        }
        float[] offset = new float[]{-0.25f, 0.0f, 0.25f};
        int progress = 0;
        if (HolePush.mc.world.canCollide((Entity)player, new Box((BlockPos)new BlockPosX(player.getX() + 1.0, player.getY() + 0.5, player.getZ())))) {
            ++progress;
        }
        if (HolePush.mc.world.canCollide((Entity)player, new Box((BlockPos)new BlockPosX(player.getX() - 1.0, player.getY() + 0.5, player.getZ())))) {
            ++progress;
        }
        if (HolePush.mc.world.canCollide((Entity)player, new Box((BlockPos)new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ() + 1.0)))) {
            ++progress;
        }
        if (HolePush.mc.world.canCollide((Entity)player, new Box((BlockPos)new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ() - 1.0)))) {
            ++progress;
        }
        for (float x : offset) {
            for (float z : offset) {
                BlockPosX playerPos = new BlockPosX(player.getX() + (double)x, player.getY() + 0.5, player.getZ() + (double)z);
                for (Direction i : Direction.values()) {
                    BlockPos pos;
                    if (i == Direction.UP || i == Direction.DOWN || !this.isTargetHere(pos = playerPos.offset(i), (Entity)player)) continue;
                    if (HolePush.mc.world.canCollide((Entity)player, new Box(pos))) {
                        return true;
                    }
                    if (!((double)progress > this.surroundCheck.getValue() - 1.0)) continue;
                    return true;
                }
            }
        }
        if (!HolePush.mc.world.canCollide((Entity)player, new Box((BlockPos)new BlockPosX(player.getX(), player.getY() + 2.5, player.getZ())))) {
            for (Direction i : Direction.values()) {
                if (i == Direction.UP || i == Direction.DOWN) continue;
                BlockPos pos = EntityUtil.getEntityPos((Entity)player).offset(i);
                Box box = player.getBoundingBox().offset(new Vec3d((double)i.getOffsetX(), (double)i.getOffsetY(), (double)i.getOffsetZ()));
                if (this.getBlock(pos.up()) == Blocks.PISTON_HEAD || HolePush.mc.world.canCollide((Entity)player, box.offset(0.0, 1.0, 0.0)) || this.isTargetHere(pos, (Entity)player) || !HolePush.mc.world.canCollide((Entity)player, new Box((BlockPos)new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ())))) continue;
                return true;
            }
        }
        return (double)progress > this.surroundCheck.getValue() - 1.0 || CombatUtil.isHard(new BlockPosX(player.getX(), player.getY() + 0.5, player.getZ()));
    }

    private Block getBlock(BlockPos pos) {
        return HolePush.mc.world.getBlockState(pos).getBlock();
    }

    private Block getBlockType() {
        if (this.torch.getValue()) {
            return Blocks.REDSTONE_TORCH;
        }
        return Blocks.REDSTONE_BLOCK;
    }

    private BlockState getBlockState(BlockPos pos) {
        return HolePush.mc.world.getBlockState(pos);
    }

    private static enum _yzHmKDmPzgLjnTXAWdrq {
        General,
        Rotate,
        Check;

    }
}
