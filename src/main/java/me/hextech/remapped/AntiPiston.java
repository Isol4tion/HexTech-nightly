package me.hextech.remapped;

import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class AntiPiston
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AntiPiston INSTANCE;
    public final BooleanSetting moveUp = this.add(new BooleanSetting("MoveUp", true));
    public final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true));
    public final BooleanSetting packet = this.add(new BooleanSetting("Packet", true));
    public final BooleanSetting helper = this.add(new BooleanSetting("Helper", true));
    public final BooleanSetting trap = this.add(new BooleanSetting("Trap", true).setParent());
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting onlyBurrow = this.add(new BooleanSetting("OnlyBurrow", true, v -> this.trap.isOpen()).setParent());
    private final BooleanSetting whenDouble = this.add(new BooleanSetting("WhenDouble", true, v -> this.onlyBurrow.isOpen()));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));

    public AntiPiston() {
        super("AntiPiston", "Trap self when piston kick", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static boolean canPlace(BlockPos pos) {
        if (!BlockUtil.canBlockFacing(pos)) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        return !BlockUtil.hasEntity(pos, false);
    }

    @Override
    public void onUpdate() {
        if (!AntiPiston.mc.player.method_24828()) {
            return;
        }
        if (this.usingPause.getValue() && AntiPiston.mc.player.method_6115()) {
            return;
        }
        this.block();
    }

    private void block() {
        BlockPos pos = EntityUtil.getPlayerPos();
        if (this.moveUp.getValue()) {
            boolean canMove = false;
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP || !(this.getBlock(pos.offset(i).up()) instanceof PistonBlock) || ((Direction)AntiPiston.mc.world.getBlockState(pos.offset(i).up()).method_11654((Property)FacingBlock.field_10927)).method_10153() != i) {
                    if (this.webUpdate((PlayerEntity)AntiPiston.mc.player)) continue;
                    canMove = true;
                    continue;
                }
                if (!canMove) continue;
                mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(AntiPiston.mc.player.getX(), AntiPiston.mc.player.getY() + 0.4199999868869781, AntiPiston.mc.player.getZ(), false));
                mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(AntiPiston.mc.player.getX(), AntiPiston.mc.player.getY() + 0.7531999805212017, AntiPiston.mc.player.getZ(), false));
                AntiPiston.mc.player.method_5814(AntiPiston.mc.player.getX(), AntiPiston.mc.player.getY() + 1.0, AntiPiston.mc.player.getZ());
                mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(AntiPiston.mc.player.getX(), AntiPiston.mc.player.getY(), AntiPiston.mc.player.getZ(), true));
                canMove = false;
            }
        }
        if (this.getBlock(pos.method_10086(2)) == Blocks.field_10540 || this.getBlock(pos.method_10086(2)) == Blocks.field_9987) {
            return;
        }
        int progress = 0;
        if (this.whenDouble.getValue()) {
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN || i == Direction.UP || !(this.getBlock(pos.offset(i).up()) instanceof PistonBlock) || ((Direction)AntiPiston.mc.world.getBlockState(pos.offset(i).up()).method_11654((Property)FacingBlock.field_10927)).method_10153() != i) continue;
                ++progress;
            }
        }
        if (!this.webUpdate((PlayerEntity)AntiPiston.mc.player)) {
            AntiPiston.mc.player.method_5814(AntiPiston.mc.player.getX(), AntiPiston.mc.player.getY() + 3.0, AntiPiston.mc.player.getZ());
            mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(AntiPiston.mc.player.getX(), AntiPiston.mc.player.getY(), AntiPiston.mc.player.getZ(), true));
        }
        for (Direction i : Direction.values()) {
            if (i == Direction.DOWN || i == Direction.UP || !(this.getBlock(pos.offset(i).up()) instanceof PistonBlock) || ((Direction)AntiPiston.mc.world.getBlockState(pos.offset(i).up()).method_11654((Property)FacingBlock.field_10927)).method_10153() != i) continue;
            this.placeBlock(pos.up().method_10079(i, -1));
            if (this.trap.getValue() && (this.getBlock(pos) != Blocks.AIR || !this.onlyBurrow.getValue() || progress >= 2)) {
                this.placeBlock(pos.method_10086(2));
                if (!BlockUtil.canPlace(pos.method_10086(2))) {
                    for (Direction i2 : Direction.values()) {
                        if (!AntiPiston.canPlace(pos.offset(i2).method_10086(2))) continue;
                        this.placeBlock(pos.offset(i2).method_10086(2));
                        break;
                    }
                }
            }
            if (BlockUtil.canPlace(pos.up().method_10079(i, -1)) || !this.helper.getValue()) continue;
            if (BlockUtil.canPlace(pos.method_10079(i, -1))) {
                this.placeBlock(pos.method_10079(i, -1));
                continue;
            }
            this.placeBlock(pos.method_10079(i, -1).method_10074());
        }
    }

    private Block getBlock(BlockPos block) {
        return AntiPiston.mc.world.getBlockState(block).getBlock();
    }

    private void placeBlock(BlockPos pos) {
        if (!AntiPiston.canPlace(pos)) {
            return;
        }
        int old = AntiPiston.mc.player.method_31548().field_7545;
        int block = this.findBlock(Blocks.field_10540);
        if (block == -1) {
            return;
        }
        this.doSwap(block);
        BlockUtil.placeBlock(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), this.packet.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(old);
        }
    }

    public int findBlock(Block blockIn) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(blockIn);
        }
        return InventoryUtil.findBlock(blockIn);
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, AntiPiston.mc.player.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private boolean webUpdate(PlayerEntity player) {
        for (float x : new float[]{0.0f, 0.3f, -0.3f}) {
            for (float z : new float[]{0.0f, 0.3f, -0.3f}) {
                for (int y : new int[]{-1, 0, 1, 2}) {
                    BlockPos pos = new BlockPosX(player.getX() + (double)x, player.getY(), player.getZ() + (double)z).method_10086(y);
                    if (!new Box(pos).method_994(player.method_5829()) || AntiPiston.mc.world.getBlockState(pos).getBlock() != Blocks.field_10343) continue;
                    return true;
                }
            }
        }
        return false;
    }
}
