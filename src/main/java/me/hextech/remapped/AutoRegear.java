package me.hextech.remapped;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import me.hextech.HexTech;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class AutoRegear
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true));
    final int[] stealCountList = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final SliderSetting disableTime = this.add(new SliderSetting("DisableTime", 500, 0, 1000));
    private final BooleanSetting place = this.add(new BooleanSetting("Place", true));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting preferOpen = this.add(new BooleanSetting("PerferOpen", true));
    private final BooleanSetting open = this.add(new BooleanSetting("Open", true));
    private final BooleanSetting close = this.add(new BooleanSetting("Close", true));
    private final SliderSetting range = this.add(new SliderSetting("Range", 4.0, 0.0, 6.0));
    private final SliderSetting minRange = this.add(new SliderSetting("MinRange", 1.0, 0.0, 3.0));
    private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true));
    private final BooleanSetting take = this.add(new BooleanSetting("Take", true));
    private final SliderSetting empty = this.add(new SliderSetting("Empty", 1, 0, 36, v -> this.take.getValue()));
    private final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, v -> this.take.getValue()).setParent());
    private final SliderSetting helmet = this.add(new SliderSetting("Helmet", 1, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting chestplate = this.add(new SliderSetting("ChestPlate", 1, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting leggings = this.add(new SliderSetting("Leggings", 1, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting boots = this.add(new SliderSetting("Boots", 1, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting elytra = this.add(new SliderSetting("Elytra", 1, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting sword = this.add(new SliderSetting("Sword", 1, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting pickaxe = this.add(new SliderSetting("Pickaxe", 1, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting crystal = this.add(new SliderSetting("Crystal", 256, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting obs = this.add(new SliderSetting("Obsidian", 256, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting exp = this.add(new SliderSetting("Exp", 256, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting totem = this.add(new SliderSetting("Totem", 6, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting turtleMaster = this.add(new SliderSetting("Turtle_Master", 6, 0, 36, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting gapple = this.add(new SliderSetting("Gapple", 128, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting cfruit = this.add(new SliderSetting("Cfruit", 64, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting endChest = this.add(new SliderSetting("EndChest", 64, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting web = this.add(new SliderSetting("Web", 64, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting glowstone = this.add(new SliderSetting("Glowstone", 256, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting anchor = this.add(new SliderSetting("Anchor", 256, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting piston = this.add(new SliderSetting("Piston", 64, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting redstone = this.add(new SliderSetting("RedStone", 64, 0, 512, v -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting pearl = this.add(new SliderSetting("Pearl", 16, 0, 64, v -> this.take.getValue() && this.smart.isOpen()));
    private final Timer timer = new Timer();
    private final Timer disableTimer = new Timer();
    BlockPos placePos = null;
    BlockPos openPos;
    boolean opend = false;

    public AutoRegear() {
        super("AutoRegear", "Auto place shulker and replenish", Module_JlagirAibYQgkHtbRnhw.Combat);
    }

    public int findShulker() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        if (this.findClass(ShulkerBoxBlock.class) != -1) {
            atomicInteger.set(this.findClass(ShulkerBoxBlock.class));
        }
        return atomicInteger.get();
    }

    public int findClass(Class clazz) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findClassInventorySlot(clazz);
        }
        return InventoryUtil.findClass(clazz);
    }

    @Override
    public void onEnable() {
        this.openPos = null;
        this.disableTimer.reset();
        this.placePos = null;
        if (AutoRegear.nullCheck()) {
            return;
        }
        int oldSlot = AutoRegear.mc.player.method_31548().selectedSlot;
        if (!this.place.getValue()) {
            return;
        }
        double distance = 100.0;
        BlockPos bestPos = null;
        for (BlockPos pos : BlockUtil.getSphere((float)this.range.getValue())) {
            if (!BlockUtil.isAir(pos.up())) continue;
            if (this.preferOpen.getValue() && AutoRegear.mc.world.method_8320(pos).method_26204() instanceof ShulkerBoxBlock) {
                return;
            }
            if ((double)MathHelper.sqrt((float)((float)AutoRegear.mc.player.method_5707(pos.toCenterPos()))) < this.minRange.getValue() || !BlockUtil.clientCanPlace(pos, false) || !BlockUtil.isStrictDirection(pos.offset(Direction.DOWN), Direction.UP) || !BlockUtil.canClick(pos.offset(Direction.DOWN)) || this.detectMining.getValue() && (HexTech.BREAK.isMining(pos) || pos.equals((Object)SpeedMine.breakPos)) || bestPos != null && !((double)MathHelper.sqrt((float)((float)AutoRegear.mc.player.method_5707(pos.toCenterPos()))) < distance)) continue;
            distance = MathHelper.sqrt((float)((float)AutoRegear.mc.player.method_5707(pos.toCenterPos())));
            bestPos = pos;
        }
        if (bestPos != null) {
            int slot = this.findShulker();
            if (slot == -1) {
                CommandManager.sendChatMessage("\u00a7c[!] No shulkerbox found");
                return;
            }
            this.doSwap(slot);
            this.placeBlock(bestPos);
            this.placePos = bestPos;
            if (this.inventory.getValue()) {
                this.doSwap(slot);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(oldSlot);
            }
            this.timer.reset();
        } else {
            CommandManager.sendChatMessage("\u00a7c[!] No place pos found");
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, AutoRegear.mc.player.method_31548().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private void update() {
        this.stealCountList[0] = (int)(this.crystal.getValue() - (double)InventoryUtil.getItemCount(Items.END_CRYSTAL));
        this.stealCountList[1] = (int)(this.exp.getValue() - (double)InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE));
        this.stealCountList[2] = (int)(this.totem.getValue() - (double)InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING));
        this.stealCountList[3] = (int)(this.gapple.getValue() - (double)InventoryUtil.getItemCount(Items.ENCHANTED_GOLDEN_APPLE));
        this.stealCountList[4] = (int)(this.endChest.getValue() - (double)InventoryUtil.getItemCount(Item.fromBlock((Block)Blocks.ENDER_CHEST)));
        this.stealCountList[5] = (int)(this.web.getValue() - (double)InventoryUtil.getItemCount(Item.fromBlock((Block)Blocks.COBWEB)));
        this.stealCountList[6] = (int)(this.glowstone.getValue() - (double)InventoryUtil.getItemCount(Item.fromBlock((Block)Blocks.GLOWSTONE)));
        this.stealCountList[7] = (int)(this.anchor.getValue() - (double)InventoryUtil.getItemCount(Item.fromBlock((Block)Blocks.RESPAWN_ANCHOR)));
        this.stealCountList[8] = (int)(this.pearl.getValue() - (double)InventoryUtil.getItemCount(Items.ENDER_PEARL));
        this.stealCountList[9] = (int)(this.turtleMaster.getValue() - (double)InventoryUtil.getPotCount(StatusEffects.field_5907));
        this.stealCountList[10] = (int)(this.helmet.getValue() - (double)InventoryUtil.getArmorCount(ArmorItem.class_8051.field_41934));
        this.stealCountList[11] = (int)(this.chestplate.getValue() - (double)InventoryUtil.getArmorCount(ArmorItem.class_8051.field_41935));
        this.stealCountList[12] = (int)(this.leggings.getValue() - (double)InventoryUtil.getArmorCount(ArmorItem.class_8051.field_41936));
        this.stealCountList[13] = (int)(this.boots.getValue() - (double)InventoryUtil.getArmorCount(ArmorItem.class_8051.field_41937));
        this.stealCountList[14] = (int)(this.elytra.getValue() - (double)InventoryUtil.getItemCount(Items.ELYTRA));
        this.stealCountList[15] = (int)(this.sword.getValue() - (double)InventoryUtil.getClassCount(SwordItem.class));
        this.stealCountList[16] = (int)(this.pickaxe.getValue() - (double)InventoryUtil.getClassCount(PickaxeItem.class));
        this.stealCountList[17] = (int)(this.piston.getValue() - (double)InventoryUtil.getClassCount(PistonBlock.class));
        this.stealCountList[18] = (int)(this.redstone.getValue() - (double)InventoryUtil.getItemCount(Item.fromBlock((Block)Blocks.REDSTONE_BLOCK)));
        this.stealCountList[19] = (int)(this.cfruit.getValue() - (double)InventoryUtil.getItemCount(Items.CHORUS_FRUIT));
        this.stealCountList[20] = (int)(this.obs.getValue() - (double)InventoryUtil.getItemCount(Item.fromBlock((Block)Blocks.OBSIDIAN)));
    }

    @Override
    public void onDisable() {
        this.opend = false;
        if (this.mine.getValue() && this.placePos != null) {
            SpeedMine.INSTANCE.mine(this.placePos);
        }
    }

    @Override
    public void onUpdate() {
        if (this.smart.getValue()) {
            this.update();
        }
        if (!(AutoRegear.mc.currentScreen instanceof ShulkerBoxScreen)) {
            if (this.opend) {
                this.opend = false;
                if (this.autoDisable.getValue()) {
                    this.disable2();
                }
                if (this.mine.getValue() && this.openPos != null) {
                    if (AutoRegear.mc.world.method_8320(this.openPos).method_26204() instanceof ShulkerBoxBlock) {
                        SpeedMine.INSTANCE.mine(this.openPos);
                    } else {
                        this.openPos = null;
                    }
                }
                return;
            }
            if (this.open.getValue()) {
                if (this.placePos != null && (double)MathHelper.sqrt((float)((float)AutoRegear.mc.player.method_5707(this.placePos.toCenterPos()))) <= this.range.getValue() && AutoRegear.mc.world.method_22347(this.placePos.up()) && (!this.timer.passedMs(500L) || AutoRegear.mc.world.method_8320(this.placePos).method_26204() instanceof ShulkerBoxBlock)) {
                    if (AutoRegear.mc.world.method_8320(this.placePos).method_26204() instanceof ShulkerBoxBlock) {
                        this.openPos = this.placePos;
                        BlockUtil.clickBlock(this.placePos, BlockUtil.getClickSide(this.placePos), this.rotate.getValue());
                    }
                } else {
                    boolean found = false;
                    for (BlockPos pos : BlockUtil.getSphere((float)this.range.getValue())) {
                        if (!BlockUtil.isAir(pos.up()) || !(AutoRegear.mc.world.method_8320(pos).method_26204() instanceof ShulkerBoxBlock)) continue;
                        this.openPos = pos;
                        BlockUtil.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue());
                        found = true;
                        break;
                    }
                    if (!found && this.autoDisable.getValue()) {
                        this.disable2();
                    }
                }
            } else if (!this.take.getValue() && this.autoDisable.getValue()) {
                this.disable2();
            }
            return;
        }
        this.opend = true;
        if (!this.take.getValue()) {
            if (this.autoDisable.getValue()) {
                this.disable2();
            }
            return;
        }
        boolean take = false;
        ScreenHandler screenHandler = AutoRegear.mc.player.field_7512;
        if (screenHandler instanceof ShulkerBoxScreenHandler) {
            ShulkerBoxScreenHandler shulker = (ShulkerBoxScreenHandler)screenHandler;
            for (Slot slot : shulker.field_7761) {
                if (slot.id >= 27 || slot.getStack().isEmpty() || this.smart.getValue() && !this.needSteal(slot.getStack()) || !((double)InventoryUtil.getEmptySlotCount() > this.empty.getValue())) continue;
                AutoRegear.mc.interactionManager.clickSlot(shulker.field_7763, slot.id, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)AutoRegear.mc.player);
                take = true;
            }
        }
        if (this.autoDisable.getValue() && !take) {
            this.disable2();
        }
    }

    private void disable2() {
        if (this.disableTimer.passedMs(this.disableTime.getValueInt())) {
            if (this.close.getValue()) {
                AutoRegear.mc.player.networkHandler.method_52787((Packet)new CloseHandledScreenC2SPacket(AutoRegear.mc.player.field_7512.syncId));
                AutoRegear.mc.player.method_7346();
            }
            this.disable();
        }
    }

    private boolean needSteal(ItemStack i) {
        if (i.getItem().equals(Items.END_CRYSTAL) && this.stealCountList[0] > 0) {
            this.stealCountList[0] = this.stealCountList[0] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Items.EXPERIENCE_BOTTLE) && this.stealCountList[1] > 0) {
            this.stealCountList[1] = this.stealCountList[1] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Items.TOTEM_OF_UNDYING) && this.stealCountList[2] > 0) {
            this.stealCountList[2] = this.stealCountList[2] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Items.ENCHANTED_GOLDEN_APPLE) && this.stealCountList[3] > 0) {
            this.stealCountList[3] = this.stealCountList[3] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Item.fromBlock((Block)Blocks.ENDER_CHEST)) && this.stealCountList[4] > 0) {
            this.stealCountList[4] = this.stealCountList[4] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Item.fromBlock((Block)Blocks.COBWEB)) && this.stealCountList[5] > 0) {
            this.stealCountList[5] = this.stealCountList[5] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Item.fromBlock((Block)Blocks.GLOWSTONE)) && this.stealCountList[6] > 0) {
            this.stealCountList[6] = this.stealCountList[6] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Item.fromBlock((Block)Blocks.RESPAWN_ANCHOR)) && this.stealCountList[7] > 0) {
            this.stealCountList[7] = this.stealCountList[7] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Items.ENDER_PEARL) && this.stealCountList[8] > 0) {
            this.stealCountList[8] = this.stealCountList[8] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Items.SPLASH_POTION) && this.stealCountList[9] > 0) {
            ArrayList effects = new ArrayList(PotionContentsComponent.method_8067((ItemStack)i));
            for (StatusEffectInstance potionEffect : effects) {
                if (potionEffect.method_5579() != StatusEffects.field_5907) continue;
                this.stealCountList[9] = this.stealCountList[9] - i.getCount();
                return true;
            }
        }
        if (InventoryUtil.CheckArmorType(i.getItem(), ArmorItem.class_8051.field_41934) && this.stealCountList[10] > 0) {
            this.stealCountList[10] = this.stealCountList[10] - i.getCount();
            return true;
        }
        if (InventoryUtil.CheckArmorType(i.getItem(), ArmorItem.class_8051.field_41935) && this.stealCountList[11] > 0) {
            this.stealCountList[11] = this.stealCountList[11] - i.getCount();
            return true;
        }
        if (InventoryUtil.CheckArmorType(i.getItem(), ArmorItem.class_8051.field_41936) && this.stealCountList[12] > 0) {
            this.stealCountList[12] = this.stealCountList[12] - i.getCount();
            return true;
        }
        if (InventoryUtil.CheckArmorType(i.getItem(), ArmorItem.class_8051.field_41937) && this.stealCountList[13] > 0) {
            this.stealCountList[13] = this.stealCountList[13] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Items.ELYTRA) && this.stealCountList[14] > 0) {
            this.stealCountList[14] = this.stealCountList[14] - i.getCount();
            return true;
        }
        if (i.getItem() instanceof SwordItem && this.stealCountList[15] > 0) {
            this.stealCountList[15] = this.stealCountList[15] - i.getCount();
            return true;
        }
        if (i.getItem() instanceof PickaxeItem && this.stealCountList[16] > 0) {
            this.stealCountList[16] = this.stealCountList[16] - i.getCount();
            return true;
        }
        if (i.getItem() instanceof BlockItem && ((BlockItem)i.getItem()).getBlock() instanceof PistonBlock && this.stealCountList[17] > 0) {
            this.stealCountList[17] = this.stealCountList[17] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Item.fromBlock((Block)Blocks.REDSTONE_BLOCK)) && this.stealCountList[18] > 0) {
            this.stealCountList[18] = this.stealCountList[18] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Items.CHORUS_FRUIT) && this.stealCountList[19] > 0) {
            this.stealCountList[19] = this.stealCountList[19] - i.getCount();
            return true;
        }
        if (i.getItem().equals(Item.fromBlock((Block)Blocks.OBSIDIAN)) && this.stealCountList[20] > 0) {
            this.stealCountList[20] = this.stealCountList[20] - i.getCount();
            return true;
        }
        return false;
    }

    private void placeBlock(BlockPos pos) {
        BlockUtil.clickBlock(pos.offset(Direction.DOWN), Direction.UP, this.rotate.getValue());
    }
}
