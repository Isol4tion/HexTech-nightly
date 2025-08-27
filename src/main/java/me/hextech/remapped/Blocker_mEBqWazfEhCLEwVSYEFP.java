package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.Blocker;
import me.hextech.remapped.Blocker_BybKYKuAntfATLqEYmcO;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Surround_BjIoVRziuWIfEWTJHPVz;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class Blocker_mEBqWazfEhCLEwVSYEFP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Blocker_mEBqWazfEhCLEwVSYEFP INSTANCE;
    final Timer timer = new Timer();
    private final EnumSetting<Blocker_BybKYKuAntfATLqEYmcO> page = this.add(new EnumSetting<Blocker_BybKYKuAntfATLqEYmcO>("Page", Blocker_BybKYKuAntfATLqEYmcO.General));
    public final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Render));
    public final SliderSetting fadeTime = this.add(new SliderSetting("FadeTime", 500, 0, 5000, v -> this.render.getValue()));
    final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255), v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Render).injectBoolean(true));
    final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100), v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Render).injectBoolean(true));
    private final SliderSetting delay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.General));
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.General));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Breaks", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.General));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.General));
    private final BooleanSetting bevelCev = this.add(new BooleanSetting("BevelCev", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Target));
    private final BooleanSetting feet = this.add(new BooleanSetting("Feet", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Target).setParent());
    private final BooleanSetting onlySurround = this.add(new BooleanSetting("OnlySurround", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Target && this.feet.isOpen()));
    private final BooleanSetting inAirPause = this.add(new BooleanSetting("InAirPause", false, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Check));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Check));
    private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Blocker_BybKYKuAntfATLqEYmcO.Check));
    private final List<BlockPos> placePos = new ArrayList<BlockPos>();
    private int placeProgress = 0;
    private BlockPos playerBP;

    public Blocker_mEBqWazfEhCLEwVSYEFP() {
        super("Blocker", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
        HexTech.EVENT_BUS.subscribe(new Blocker(this));
    }

    @Override
    public void onUpdate() {
        if (!this.timer.passedMs(this.delay.getValue())) {
            return;
        }
        if (this.eatingPause.getValue() && EntityUtil.isUsing()) {
            return;
        }
        this.placeProgress = 0;
        if (this.playerBP != null && !this.playerBP.equals((Object)EntityUtil.getPlayerPos(true))) {
            this.placePos.clear();
        }
        this.playerBP = EntityUtil.getPlayerPos(true);
        if (this.bevelCev.getValue()) {
            for (Object i : Direction.values()) {
                BlockPos blockerPos;
                if (i == Direction.DOWN || this.isBedrock(this.playerBP.offset(i).up()) || !this.crystalHere(blockerPos = this.playerBP.offset(i).method_10086(2)) || this.placePos.contains(blockerPos)) continue;
                this.placePos.add(blockerPos);
            }
        }
        if (this.getObsidian() == -1) {
            return;
        }
        if (this.inAirPause.getValue() && !Blocker_mEBqWazfEhCLEwVSYEFP.mc.player.method_24828()) {
            return;
        }
        this.placePos.removeIf(pos -> !BlockUtil.clientCanPlace(pos, true));
        if (this.feet.getValue() && (!this.onlySurround.getValue() || Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.isOn())) {
            for (Object i : Direction.values()) {
                BlockPos surroundPos;
                if (i == Direction.DOWN || i == Direction.UP || this.isBedrock(surroundPos = this.playerBP.offset(i)) || !BlockUtil.isMining(surroundPos)) continue;
                for (Direction direction : Direction.values()) {
                    if (direction == Direction.DOWN || direction == Direction.UP) continue;
                    BlockPos defensePos = this.playerBP.offset(i).offset(direction);
                    if (this.breakCrystal.getValue()) {
                        CombatUtil.attackCrystal(defensePos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), false);
                    }
                    if (!BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) continue;
                    this.tryPlaceObsidian(defensePos);
                }
                BlockPos defensePos = this.playerBP.offset(i).up();
                if (this.breakCrystal.getValue()) {
                    CombatUtil.attackCrystal(defensePos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), false);
                }
                if (!BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) continue;
                this.tryPlaceObsidian(defensePos);
            }
        }
        for (BlockPos defensePos : this.placePos) {
            if (this.breakCrystal.getValue() && this.crystalHere(defensePos)) {
                CombatUtil.attackCrystal(defensePos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), false);
            }
            if (!BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) continue;
            this.tryPlaceObsidian(defensePos);
        }
    }

    private boolean crystalHere(BlockPos pos) {
        return Blocker_mEBqWazfEhCLEwVSYEFP.mc.world.method_18467(EndCrystalEntity.class, new Box(pos)).stream().anyMatch(entity -> entity.method_24515().equals((Object)pos));
    }

    private boolean isBedrock(BlockPos pos) {
        return Blocker_mEBqWazfEhCLEwVSYEFP.mc.world.getBlockState(pos).getBlock() == Blocks.field_9987;
    }

    private void tryPlaceObsidian(BlockPos pos) {
        if (!((double)this.placeProgress < this.blocksPer.getValue())) {
            return;
        }
        if (this.detectMining.getValue() && BlockUtil.isMining(pos)) {
            return;
        }
        int oldSlot = Blocker_mEBqWazfEhCLEwVSYEFP.mc.player.method_31548().field_7545;
        int block = this.getObsidian();
        if (block == -1) {
            return;
        }
        this.doSwap(block);
        BlockUtil.placeBlock(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue());
        if (this.inventorySwap.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(oldSlot);
        }
        ++this.placeProgress;
        Blocker.addBlock(pos);
        this.timer.reset();
    }

    private void doSwap(int slot) {
        if (this.inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, Blocker_mEBqWazfEhCLEwVSYEFP.mc.player.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getObsidian() {
        if (this.inventorySwap.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.field_10540);
        }
        return InventoryUtil.findBlock(Blocks.field_10540);
    }
}
