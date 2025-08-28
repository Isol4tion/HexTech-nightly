package me.hextech.mod.modules.impl.combat;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.Render3DEvent;
import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.render.ColorUtil;
import me.hextech.api.utils.render.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.api.utils.render.Render3DUtil;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.setting.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Blocker_mEBqWazfEhCLEwVSYEFP
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Blocker_mEBqWazfEhCLEwVSYEFP INSTANCE;
    final Timer timer = new Timer();
    private final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    public final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == Page.Render));
    public final SliderSetting fadeTime = this.add(new SliderSetting("FadeTime", 500, 0, 5000, v -> this.render.getValue()));
    final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255), v -> this.page.getValue() == Page.Render).injectBoolean(true));
    final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100), v -> this.page.getValue() == Page.Render).injectBoolean(true));
    private final SliderSetting delay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, v -> this.page.getValue() == Page.General));
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, v -> this.page.getValue() == Page.General));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Breaks", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting bevelCev = this.add(new BooleanSetting("BevelCev", true, v -> this.page.getValue() == Page.Target));
    private final BooleanSetting feet = this.add(new BooleanSetting("Feet", true, v -> this.page.getValue() == Page.Target).setParent());
    private final BooleanSetting onlySurround = this.add(new BooleanSetting("OnlySurround", true, v -> this.page.getValue() == Page.Target && this.feet.isOpen()));
    private final BooleanSetting inAirPause = this.add(new BooleanSetting("InAirPause", false, v -> this.page.getValue() == Page.Check));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", true, v -> this.page.getValue() == Page.Check));
    private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Page.Check));
    private final List<BlockPos> placePos = new ArrayList<BlockPos>();
    private int placeProgress = 0;
    private BlockPos playerBP;

    public Blocker_mEBqWazfEhCLEwVSYEFP() {
        super("Blocker", Category.Combat);
        INSTANCE = this;
        HexTech.EVENT_BUS.subscribe(new Blocker());
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
        if (this.playerBP != null && !this.playerBP.equals(EntityUtil.getPlayerPos(true))) {
            this.placePos.clear();
        }
        this.playerBP = EntityUtil.getPlayerPos(true);
        if (this.bevelCev.getValue()) {
            for (Direction i : Direction.values()) {
                BlockPos blockerPos;
                if (i == Direction.DOWN || this.isBedrock(this.playerBP.offset(i).up()) || !this.crystalHere(blockerPos = this.playerBP.offset(i).up(2)) || this.placePos.contains(blockerPos)) continue;
                this.placePos.add(blockerPos);
            }
        }
        if (this.getObsidian() == -1) {
            return;
        }
        if (this.inAirPause.getValue() && !Blocker_mEBqWazfEhCLEwVSYEFP.mc.player.isOnGround()) {
            return;
        }
        this.placePos.removeIf(pos -> !BlockUtil.clientCanPlace(pos, true));
        if (this.feet.getValue() && (!this.onlySurround.getValue() || Surround_BjIoVRziuWIfEWTJHPVz.INSTANCE.isOn())) {
            for (Direction i : Direction.values()) {
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
        return Blocker_mEBqWazfEhCLEwVSYEFP.mc.world.getNonSpectatingEntities(EndCrystalEntity.class, new Box(pos)).stream().anyMatch(entity -> entity.getBlockPos().equals(pos));
    }

    private boolean isBedrock(BlockPos pos) {
        return Blocker_mEBqWazfEhCLEwVSYEFP.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    private void tryPlaceObsidian(BlockPos pos) {
        if (!((double)this.placeProgress < this.blocksPer.getValue())) {
            return;
        }
        if (this.detectMining.getValue() && BlockUtil.isMining(pos)) {
            return;
        }
        int oldSlot = Blocker_mEBqWazfEhCLEwVSYEFP.mc.player.getInventory().selectedSlot;
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
            InventoryUtil.inventorySwap(slot, Blocker_mEBqWazfEhCLEwVSYEFP.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getObsidian() {
        if (this.inventorySwap.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN);
        }
        return InventoryUtil.findBlock(Blocks.OBSIDIAN);
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public class Blocker {
        public static final HashMap<BlockPos, _FijMAnLaeintSRYqwSXS> renderMap = new HashMap();

        public Blocker() {
        }

        public static void addBlock(BlockPos pos) {
            renderMap.put(pos, new _FijMAnLaeintSRYqwSXS(pos));
        }

        @EventHandler
        public void onRender3D(Render3DEvent event) {
            if (!INSTANCE.render.getValue()) {
                return;
            }
            if (renderMap.isEmpty()) {
                return;
            }
            boolean shouldClear = true;
            for (_FijMAnLaeintSRYqwSXS placePosition : renderMap.values()) {
                if (!BlockUtil.clientCanPlace(placePosition.pos, true)) {
                    placePosition.isAir = false;
                }
                if (!placePosition.timer.passedMs((long)(delay.getValue() + 100.0)) && placePosition.isAir) {
                    placePosition.firstFade.reset();
                }
                if (placePosition.firstFade.getQuad(FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad.In2) == 1.0) continue;
                shouldClear = false;
                MatrixStack matrixStack = event.getMatrixStack();
                if (INSTANCE.fill.booleanValue) {
                    Render3DUtil.drawFill(matrixStack, new Box(placePosition.pos), ColorUtil.injectAlpha(INSTANCE.fill.getValue(), (int)((double)fill.getValue().getAlpha() * (1.0 - placePosition.firstFade.getQuad(FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad.In2)))));
                }
                if (!INSTANCE.box.booleanValue) continue;
                Render3DUtil.drawBox(matrixStack, new Box(placePosition.pos), ColorUtil.injectAlpha(INSTANCE.box.getValue(), (int)((double)box.getValue().getAlpha() * (1.0 - placePosition.firstFade.getQuad(FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad.In2)))));
            }
            if (shouldClear) {
                renderMap.clear();
            }
        }

        /*
         * Exception performing whole class analysis ignored.
         */
        public static class _FijMAnLaeintSRYqwSXS {
            public final FadeUtils_DPfHthPqEJdfXfNYhDbG firstFade;
            public final BlockPos pos;
            public final Timer timer;
            public boolean isAir;

            public _FijMAnLaeintSRYqwSXS(BlockPos placePos) {
                this.firstFade = new FadeUtils_DPfHthPqEJdfXfNYhDbG((long) INSTANCE.fadeTime.getValue());
                this.pos = placePos;
                this.timer = new Timer();
                this.isAir = true;
            }
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Page {
        General,
        Target,
        Check,
        Render

    }
}
