package me.hextech.remapped.mod.modules.impl.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.mod.modules.impl.setting.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.api.utils.combat.CombatUtil;
import me.hextech.remapped.api.utils.entity.EntityUtil;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class AntiCev
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AntiCev INSTANCE;
    final Timer timer = new Timer();
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 50, 0, 500));
    private final SliderSetting multiPlace = this.add(new SliderSetting("MultiPlace", 1, 1, 8));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("BreakCrystal", true));
    private final BooleanSetting checkMine = this.add(new BooleanSetting("CheckMine", true));
    private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final List<BlockPos> crystalPos = new ArrayList<BlockPos>();
    int progress = 0;
    private BlockPos pos;

    public AntiCev() {
        super("AntiCev", "Anti cev", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (!this.timer.passedMs(this.delay.getValue())) {
            return;
        }
        if (this.eatingPause.getValue() && EntityUtil.isUsing()) {
            return;
        }
        this.progress = 0;
        if (this.pos != null && !this.pos.equals(EntityUtil.getPlayerPos(true))) {
            this.crystalPos.clear();
        }
        this.pos = EntityUtil.getPlayerPos(true);
        for (Direction i : Direction.values()) {
            BlockPos offsetPos;
            if (i == Direction.DOWN || this.isGod(this.pos.offset(i).up()) || !this.crystalHere(offsetPos = this.pos.offset(i).up(2)) || this.crystalPos.contains(offsetPos)) continue;
            this.crystalPos.add(offsetPos);
        }
        if (this.getBlock() == -1) {
            return;
        }
        if (this.onlyGround.getValue() && !Objects.requireNonNull(AntiCev.mc.player).isOnGround()) {
            return;
        }
        this.crystalPos.removeIf(pos -> !BlockUtil.clientCanPlace(pos, true));
        for (BlockPos defensePos : this.crystalPos) {
            if (this.crystalHere(defensePos) && this.breakCrystal.getValue()) {
                CombatUtil.attackCrystal(defensePos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), false);
            }
            if (!BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) continue;
            this.placeBlock(defensePos);
        }
    }

    private boolean crystalHere(BlockPos pos) {
        for (Entity entity : Objects.requireNonNull(AntiCev.mc.world).getNonSpectatingEntities(EndCrystalEntity.class, new Box(pos))) {
            if (!EntityUtil.getEntityPos(entity).equals(pos)) continue;
            return true;
        }
        return false;
    }

    private boolean isGod(BlockPos pos) {
        return Objects.requireNonNull(AntiCev.mc.world).getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    private void placeBlock(BlockPos pos) {
        if (!((double)this.progress < this.multiPlace.getValue())) {
            return;
        }
        if (this.checkMine.getValue() && BlockUtil.isMining(pos)) {
            return;
        }
        int block = this.getBlock();
        if (block == -1) {
            return;
        }
        this.doSwap(block);
        BlockUtil.placeBlock(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            int old = 0;
            this.doSwap(old);
        }
        ++this.progress;
        this.timer.reset();
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, AntiCev.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getBlock() {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN);
        }
        return InventoryUtil.findBlock(Blocks.OBSIDIAN);
    }
}
