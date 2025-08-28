package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import me.hextech.HexTech;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AutoTrap
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoTrap INSTANCE;
    public final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    public final BooleanSetting render = this.add(new BooleanSetting("Render", true));
    public final BooleanSetting box = this.add(new BooleanSetting("Box", true, v -> this.render.getValue()));
    public final BooleanSetting outline = this.add(new BooleanSetting("Outline", false, v -> this.render.getValue()));
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100), v -> this.render.getValue()));
    public final SliderSetting fadeTime = this.add(new SliderSetting("FadeTime", 500, 0, 5000, v -> this.render.getValue()).setSuffix("ms"));
    public final BooleanSetting pre = this.add(new BooleanSetting("Pre", false, v -> this.render.getValue()));
    public final BooleanSetting sync = this.add(new BooleanSetting("Sync", true, v -> this.render.getValue()));
    final Timer timer = new Timer();
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 1.0, 8.0).setSuffix("m"));
    private final EnumSetting<_YlnJzIMwjFLWxhoVZoJp> targetMod = this.add(new EnumSetting<_YlnJzIMwjFLWxhoVZoJp>("TargetMode", _YlnJzIMwjFLWxhoVZoJp.Single));
    private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", false));
    private final BooleanSetting helper = this.add(new BooleanSetting("Helper", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting extend = this.add(new BooleanSetting("Extend", true));
    private final BooleanSetting antiStep = this.add(new BooleanSetting("AntiStep", false));
    private final BooleanSetting onlyBreak = this.add(new BooleanSetting("OnlyBreak", false, v -> this.antiStep.getValue()));
    private final BooleanSetting head = this.add(new BooleanSetting("Head", true));
    private final BooleanSetting headExtend = this.add(new BooleanSetting("HeadExtend", true));
    private final BooleanSetting headAnchor = this.add(new BooleanSetting("HeadAnchor", true));
    private final BooleanSetting chestUp = this.add(new BooleanSetting("ChestUp", true));
    private final BooleanSetting onlyBreaking = this.add(new BooleanSetting("OnlyBreaking", false, v -> this.chestUp.getValue()));
    private final BooleanSetting chest = this.add(new BooleanSetting("Chest", true));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false, v -> this.chest.getValue()));
    private final BooleanSetting legs = this.add(new BooleanSetting("Legs", false));
    private final BooleanSetting legAnchor = this.add(new BooleanSetting("LegAnchor", true));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", false));
    private final BooleanSetting onlyHole = this.add(new BooleanSetting("OnlyHole", false));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Breaks", true));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4.0, 1.0, 6.0).setSuffix("m"));
    private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100));
    private final ArrayList<BlockPos> trapList = new ArrayList();
    private final ArrayList<BlockPos> placeList = new ArrayList();
    public PlayerEntity target;
    public Vec3d directionVec = null;
    int progress = 0;

    public AutoTrap() {
        super("AutoTrap", "Automatically trap the enemy", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
        HexTech.EVENT_BUS.subscribe(new _WyicUWrboAjyTEvkkOOH());
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.directionVec != null && this.rotate.getValue() && this.yawStep.getValue()) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @Override
    public void onUpdate() {
        if (AutoTrap.nullCheck()) {
            return;
        }
        this.trapList.clear();
        this.directionVec = null;
        this.placeList.clear();
        this.progress = 0;
        if (this.selfGround.getValue() && !AutoTrap.mc.player.isOnGround()) {
            this.target = null;
            return;
        }
        if (this.usingPause.getValue() && EntityUtil.isUsing()) {
            this.target = null;
            return;
        }
        if (!this.timer.passedMs((long)this.delay.getValue())) {
            return;
        }
        if (this.targetMod.getValue() == _YlnJzIMwjFLWxhoVZoJp.Single) {
            this.target = CombatUtil.getClosestEnemy(this.range.getValue());
            if (this.target == null) {
                if (this.autoDisable.getValue()) {
                    this.disable();
                }
                return;
            }
            this.trapTarget(this.target);
        } else if (this.targetMod.getValue() == _YlnJzIMwjFLWxhoVZoJp.Multi) {
            boolean found = false;
            for (PlayerEntity player : CombatUtil.getEnemies(this.range.getValue())) {
                found = true;
                this.target = player;
                this.trapTarget(this.target);
            }
            if (!found) {
                if (this.autoDisable.getValue()) {
                    this.disable();
                }
                this.target = null;
            }
        }
    }

    private void trapTarget(PlayerEntity target) {
        if (this.onlyHole.getValue() && !BlockUtil.isHole(EntityUtil.getEntityPos(target))) {
            return;
        }
        this.doTrap(EntityUtil.getEntityPos(target, true));
    }

    private void doTrap(BlockPos pos) {
        if (this.trapList.contains(pos)) {
            return;
        }
        this.trapList.add(pos);
        if (this.legs.getValue()) {
            for (final Direction i : Direction.values()) {
                if (i != Direction.DOWN) {
                    if (i != Direction.UP) {
                        final BlockPos offsetPos = pos.offset(i);
                        this.tryPlaceBlock(offsetPos, this.legAnchor.getValue());
                        if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue()) && this.getHelper(offsetPos) != null) {
                            this.tryPlaceObsidian(this.getHelper(offsetPos));
                        }
                    }
                }
            }
        }
        if (this.headExtend.getValue()) {
            for (final int x : new int[] { 1, 0, -1 }) {
                for (final int z : new int[] { 1, 0, -1 }) {
                    final BlockPos offsetPos2 = pos.add(z, 0, x);
                    if (this.checkEntity(new BlockPos(offsetPos2))) {
                        this.tryPlaceBlock(offsetPos2.up(2), this.headAnchor.getValue());
                    }
                }
            }
        }
        if (this.head.getValue() && BlockUtil.clientCanPlace(pos.up(2), this.breakCrystal.getValue())) {
            if (BlockUtil.getPlaceSide(pos.up(2)) == null) {
                boolean trapChest = this.helper.getValue();
                if (this.getHelper(pos.up(2)) != null) {
                    this.tryPlaceObsidian(this.getHelper(pos.up(2)));
                    trapChest = false;
                }
                if (trapChest) {
                    for (final Direction j : Direction.values()) {
                        if (j != Direction.DOWN) {
                            if (j != Direction.UP) {
                                final BlockPos offsetPos3 = pos.offset(j).up();
                                if (BlockUtil.clientCanPlace(offsetPos3.up(), this.breakCrystal.getValue()) && BlockUtil.canPlace(offsetPos3, this.placeRange.getValue(), this.breakCrystal.getValue())) {
                                    this.tryPlaceObsidian(offsetPos3);
                                    trapChest = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (trapChest) {
                        for (final Direction j : Direction.values()) {
                            if (j != Direction.DOWN) {
                                if (j != Direction.UP) {
                                    final BlockPos offsetPos3 = pos.offset(j).up();
                                    if (BlockUtil.clientCanPlace(offsetPos3.up(), this.breakCrystal.getValue()) && BlockUtil.getPlaceSide(offsetPos3) == null && BlockUtil.clientCanPlace(offsetPos3, this.breakCrystal.getValue()) && this.getHelper(offsetPos3) != null) {
                                        this.tryPlaceObsidian(this.getHelper(offsetPos3));
                                        trapChest = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (trapChest) {
                            for (final Direction j : Direction.values()) {
                                if (j != Direction.DOWN) {
                                    if (j != Direction.UP) {
                                        final BlockPos offsetPos3 = pos.offset(j).up();
                                        if (BlockUtil.clientCanPlace(offsetPos3.up(), this.breakCrystal.getValue()) && BlockUtil.getPlaceSide(offsetPos3) == null && BlockUtil.clientCanPlace(offsetPos3, this.breakCrystal.getValue()) && this.getHelper(offsetPos3) != null && BlockUtil.getPlaceSide(offsetPos3.down()) == null && BlockUtil.clientCanPlace(offsetPos3.down(), this.breakCrystal.getValue()) && this.getHelper(offsetPos3.down()) != null) {
                                            this.tryPlaceObsidian(this.getHelper(offsetPos3.down()));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.tryPlaceBlock(pos.up(2), this.headAnchor.getValue());
        }
        if (this.antiStep.getValue() && (BlockUtil.isMining(pos.up(2)) || !this.onlyBreak.getValue())) {
            if (BlockUtil.getPlaceSide(pos.up(3)) == null && BlockUtil.clientCanPlace(pos.up(3), this.breakCrystal.getValue()) && this.getHelper(pos.up(3), Direction.DOWN) != null) {
                this.tryPlaceObsidian(this.getHelper(pos.up(3)));
            }
            this.tryPlaceObsidian(pos.up(3));
        }
        if (this.down.getValue()) {
            final BlockPos offsetPos4 = pos.down();
            this.tryPlaceObsidian(offsetPos4);
            if (BlockUtil.getPlaceSide(offsetPos4) == null && BlockUtil.clientCanPlace(offsetPos4, this.breakCrystal.getValue()) && this.getHelper(offsetPos4) != null) {
                this.tryPlaceObsidian(this.getHelper(offsetPos4));
            }
        }
        if (this.chestUp.getValue()) {
            for (final Direction i : Direction.values()) {
                if (i != Direction.DOWN) {
                    if (i != Direction.UP) {
                        final BlockPos offsetPos = pos.offset(i).up(2);
                        if (!this.onlyBreaking.getValue() || BlockUtil.isMining(pos.up(2))) {
                            this.tryPlaceObsidian(offsetPos);
                            if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())) {
                                if (this.getHelper(offsetPos) != null) {
                                    this.tryPlaceObsidian(this.getHelper(offsetPos));
                                }
                                else if (BlockUtil.getPlaceSide(offsetPos.down()) == null && BlockUtil.clientCanPlace(offsetPos.down(), this.breakCrystal.getValue()) && this.getHelper(offsetPos.down()) != null) {
                                    this.tryPlaceObsidian(this.getHelper(offsetPos.down()));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.chest.getValue() && (!this.onlyGround.getValue() || this.target.isOnGround())) {
            for (final Direction i : Direction.values()) {
                if (i != Direction.DOWN) {
                    if (i != Direction.UP) {
                        final BlockPos offsetPos = pos.offset(i).up();
                        this.tryPlaceObsidian(offsetPos);
                        if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())) {
                            if (this.getHelper(offsetPos) != null) {
                                this.tryPlaceObsidian(this.getHelper(offsetPos));
                            }
                            else if (BlockUtil.getPlaceSide(offsetPos.down()) == null && BlockUtil.clientCanPlace(offsetPos.down(), this.breakCrystal.getValue()) && this.getHelper(offsetPos.down()) != null) {
                                this.tryPlaceObsidian(this.getHelper(offsetPos.down()));
                            }
                        }
                    }
                }
            }
        }
        if (this.extend.getValue()) {
            for (final int x : new int[] { 1, 0, -1 }) {
                for (final int z : new int[] { 1, 0, -1 }) {
                    final BlockPos offsetPos2 = pos.add(x, 0, z);
                    if (this.checkEntity(new BlockPos(offsetPos2))) {
                        this.doTrap(offsetPos2);
                    }
                }
            }
        }
    }

    @Override
    public String getInfo() {
        if (this.target != null) {
            return this.target.getName().getString();
        }
        return null;
    }

    public BlockPos getHelper(BlockPos pos) {
        if (!this.helper.getValue()) {
            return null;
        }
        for (Direction i : Direction.values()) {
            if (this.checkMine.getValue() && BlockUtil.isMining(pos.offset(i)) || CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.placement.getValue() == Placement.Strict && !BlockUtil.isStrictDirection(pos.offset(i), i.getOpposite(), true) || !BlockUtil.canPlace(pos.offset(i), this.placeRange.getValue(), this.breakCrystal.getValue())) continue;
            return pos.offset(i);
        }
        return null;
    }

    public BlockPos getHelper(BlockPos pos, Direction ignore) {
        if (!this.helper.getValue()) {
            return null;
        }
        for (Direction i : Direction.values()) {
            if (i == ignore || this.checkMine.getValue() && BlockUtil.isMining(pos.offset(i)) || !BlockUtil.isStrictDirection(pos.offset(i), i.getOpposite(), true) || !BlockUtil.canPlace(pos.offset(i), this.placeRange.getValue(), this.breakCrystal.getValue())) continue;
            return pos.offset(i);
        }
        return null;
    }

    private boolean checkEntity(BlockPos pos) {
        if (AutoTrap.mc.player.getBoundingBox().intersects(new Box(pos))) {
            return false;
        }
        for (Entity entity : AutoTrap.mc.world.getNonSpectatingEntities(PlayerEntity.class, new Box(pos))) {
            if (!entity.isAlive()) continue;
            return true;
        }
        return false;
    }

    private void tryPlaceBlock(BlockPos pos, boolean anchor) {
        int block;
        if (this.pre.getValue()) {
            _WyicUWrboAjyTEvkkOOH.addBlock(pos);
        }
        if (this.placeList.contains(pos)) {
            return;
        }
        if (BlockUtil.isMining(pos)) {
            return;
        }
        if (!BlockUtil.canPlace(pos, 6.0, this.breakCrystal.getValue())) {
            return;
        }
        if (this.rotate.getValue() && !this.faceVector(this.directionVec)) {
            return;
        }
        if (!((double)this.progress < this.blocksPer.getValue())) {
            return;
        }
        if ((double)MathHelper.sqrt((float)EntityUtil.getEyesPos().squaredDistanceTo(pos.toCenterPos())) > this.placeRange.getValue()) {
            return;
        }
        int old = AutoTrap.mc.player.getInventory().selectedSlot;
        int n = block = anchor && this.getAnchor() != -1 ? this.getAnchor() : this.getBlock();
        if (block == -1) {
            return;
        }
        if (!this.pre.getValue()) {
            _WyicUWrboAjyTEvkkOOH.addBlock(pos);
        }
        this.placeList.add(pos);
        CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.usingPause.getValue());
        this.doSwap(block);
        BlockUtil.placeBlock(pos, this.rotate.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(old);
        }
        this.timer.reset();
        ++this.progress;
    }

    private void tryPlaceObsidian(BlockPos pos) {
        if (this.pre.getValue()) {
            _WyicUWrboAjyTEvkkOOH.addBlock(pos);
        }
        if (this.placeList.contains(pos)) {
            return;
        }
        if (BlockUtil.isMining(pos)) {
            return;
        }
        if (!BlockUtil.canPlace(pos, 6.0, this.breakCrystal.getValue())) {
            return;
        }
        if (this.rotate.getValue() && !this.faceVector(this.directionVec)) {
            return;
        }
        if (!((double)this.progress < this.blocksPer.getValue())) {
            return;
        }
        if ((double)MathHelper.sqrt((float)EntityUtil.getEyesPos().squaredDistanceTo(pos.toCenterPos())) > this.placeRange.getValue()) {
            return;
        }
        int old = AutoTrap.mc.player.getInventory().selectedSlot;
        int block = this.getBlock();
        if (block == -1) {
            return;
        }
        if (!this.pre.getValue()) {
            _WyicUWrboAjyTEvkkOOH.addBlock(pos);
        }
        this.placeList.add(pos);
        CombatUtil.attackCrystal(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue(), this.usingPause.getValue());
        this.doSwap(block);
        BlockUtil.placeBlock(pos, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injblock.getValue());
        if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(old);
        }
        this.timer.reset();
        ++this.progress;
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, AutoTrap.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!this.yawStep.getValue()) {
            RotateManager.TrueVec3d(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        if (HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
        }
        return !this.checkFov.getValue();
    }

    private int getBlock() {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN);
        }
        return InventoryUtil.findBlock(Blocks.OBSIDIAN);
    }

    private int getAnchor() {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.RESPAWN_ANCHOR);
        }
        return InventoryUtil.findBlock(Blocks.RESPAWN_ANCHOR);
    }

    public enum _YlnJzIMwjFLWxhoVZoJp {
        Single,
        Multi

    }

    public class _WyicUWrboAjyTEvkkOOH {
        public static final HashMap<BlockPos, AutoTrap_RYPZUKNZXVloqcMUfNgc> PlaceMap = new HashMap();

        public static void addBlock(BlockPos pos) {
            if (BlockUtil.clientCanPlace(pos, true) && !PlaceMap.containsKey(pos)) {
                PlaceMap.put(pos, new AutoTrap_RYPZUKNZXVloqcMUfNgc(pos));
            }
        }

        private void drawBlock(BlockPos pos, double alpha, Color color, MatrixStack matrixStack) {
            if (AutoTrap.this.sync.getValue()) {
                color = AutoTrap.INSTANCE.color.getValue();
            }
            Render3DUtil.draw3DBox(matrixStack, new Box(pos), ColorUtil.injectAlpha(color, (int)alpha), AutoTrap.this.outline.getValue(), AutoTrap.this.box.getValue());
        }

        @EventHandler
        public void onRender3D(Render3DEvent event) {
            if (!AutoTrap.this.render.getValue()) {
                return;
            }
            if (PlaceMap.isEmpty()) {
                return;
            }
            boolean shouldClear = true;
            for (AutoTrap_RYPZUKNZXVloqcMUfNgc placePosition : PlaceMap.values()) {
                if (!BlockUtil.clientCanPlace(placePosition.pos, true)) {
                    placePosition.isAir = false;
                }
                if (!placePosition.timer.passedMs((long)(AutoTrap.this.delay.getValue() + 100.0)) && placePosition.isAir) {
                    placePosition.firstFade.reset();
                }
                if (placePosition.firstFade.getQuad(FadeUtils.In2) == 1.0) continue;
                shouldClear = false;
                this.drawBlock(placePosition.pos, (double)AutoTrap.this.color.getValue().getAlpha() * (1.0 - placePosition.firstFade.getQuad(FadeUtils.In2)), placePosition.posColor, event.getMatrixStack());
            }
            if (shouldClear) {
                PlaceMap.clear();
            }
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class AutoTrap_RYPZUKNZXVloqcMUfNgc {
        public final FadeUtils_DPfHthPqEJdfXfNYhDbG firstFade;
        public final BlockPos pos;
        public final Color posColor;
        public final Timer timer;
        public boolean isAir;

        public AutoTrap_RYPZUKNZXVloqcMUfNgc(BlockPos placePos) {
            this.firstFade = new FadeUtils_DPfHthPqEJdfXfNYhDbG((long) INSTANCE.fadeTime.getValue());
            this.pos = placePos;
            this.posColor = INSTANCE.color.getValue();
            this.timer = new Timer();
            this.isAir = true;
            this.timer.reset();
        }
    }
}
