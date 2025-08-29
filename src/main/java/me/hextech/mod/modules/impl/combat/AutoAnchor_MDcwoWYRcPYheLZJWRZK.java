package me.hextech.mod.modules.impl.combat;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.OffTrackEvent;
import me.hextech.api.events.impl.Render3DEvent;
import me.hextech.api.managers.RotateManager;
import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.render.AnimateUtil;
import me.hextech.api.utils.render.ColorUtil;
import me.hextech.api.utils.render.Render3DUtil;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.api.utils.world.ExplosionUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.player.Blink;
import me.hextech.mod.modules.impl.setting.PredictionSetting;
import me.hextech.mod.modules.settings.SwingSide;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AutoAnchor_MDcwoWYRcPYheLZJWRZK
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoAnchor_MDcwoWYRcPYheLZJWRZK INSTANCE;
    public static BlockPos currentPos;
    static Vec3d placeVec3d;
    static Vec3d curVec3d;
    public final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    public final SliderSetting range = this.add(new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, 0.1, v -> this.page.getValue() == Page.General));
    public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 8.0, 0.1, 12.0, 0.1, v -> this.page.getValue() == Page.General));
    public final SliderSetting minDamage = this.add(new SliderSetting("Min", 4.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Page.Calc).setSuffix("dmg"));
    public final SliderSetting breakMin = this.add(new SliderSetting("ExplosionMin", 4.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Page.Calc).setSuffix("dmg"));
    public final SliderSetting headDamage = this.add(new SliderSetting("ForceHead", 7.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Page.Calc).setSuffix("dmg"));
    public final SliderSetting predictTicks = this.add(new SliderSetting("Predict", 2.0, 0.0, 50.0, 1.0, v -> this.page.getValue() == Page.Calc).setSuffix("ticks"));
    final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == Page.Render));
    final BooleanSetting shrink = this.add(new BooleanSetting("Shrink", true, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255), v -> this.page.getValue() == Page.Render && this.render.getValue()).injectBoolean(true));
    final SliderSetting lineWidth = this.add(new SliderSetting("LineWidth", 1.5, 0.01, 3.0, 0.01, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100), v -> this.page.getValue() == Page.Render && this.render.getValue()).injectBoolean(true));
    final SliderSetting sliderSpeed = this.add(new SliderSetting("SliderSpeed", 0.2, 0.0, 1.0, 0.01, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    final SliderSetting startFadeTime = this.add(new SliderSetting("StartFade", 0.3, 0.0, 2.0, 0.01, v -> this.page.getValue() == Page.Render && this.render.getValue()).setSuffix("s"));
    final SliderSetting fadeSpeed = this.add(new SliderSetting("FadeSpeed", 0.2, 0.01, 1.0, 0.01, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    final Timer noPosTimer = new Timer();
    private final BooleanSetting assist = this.add(new BooleanSetting("Assist", true, v -> this.page.getValue() == Page.Assist));
    private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", false, v -> this.page.getValue() == Page.Assist));
    private final SliderSetting assistRange = this.add(new SliderSetting("AssistRange", 5.0, 0.0, 6.0, 0.1, v -> this.page.getValue() == Page.Assist));
    private final SliderSetting assistDamage = this.add(new SliderSetting("AssistDamage", 6.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Page.Assist));
    private final SliderSetting delay = this.add(new SliderSetting("AssistDelay", 0.1, 0.0, 1.0, 0.01, v -> this.page.getValue() == Page.Assist));
    private final BooleanSetting thread = this.add(new BooleanSetting("Thread", false, v -> this.page.getValue() == Page.General));
    private final BooleanSetting light = this.add(new BooleanSetting("Light", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("BreakCrystal", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting spam = this.add(new BooleanSetting("Spam", true, v -> this.page.getValue() == Page.General).setParent());
    private final BooleanSetting mineSpam = this.add(new BooleanSetting("OnlyMining", true, v -> this.page.getValue() == Page.General && this.spam.isOpen()));
    private final BooleanSetting spamPlace = this.add(new BooleanSetting("Fast", true, v -> this.page.getValue() == Page.General).setParent());
    private final BooleanSetting inSpam = this.add(new BooleanSetting("WhenSpamming", true, v -> this.page.getValue() == Page.General && this.spamPlace.isOpen()));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting cancelblink = this.add(new BooleanSetting("CancelBlink", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting syncCyrstal = this.add(new BooleanSetting("SyncCrystal", false, v -> this.page.getValue() == Page.General));
    private final BooleanSetting cancelBurrow = this.add(new BooleanSetting("CancelBurrow", false, v -> this.page.getValue() == Page.General));
    private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.All, v -> this.page.getValue() == Page.General));
    private final SliderSetting placeDelay = this.add(new SliderSetting("Delay", 100.0, 0.0, 500.0, 1.0, v -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final SliderSetting spamDelay = this.add(new SliderSetting("SpamDelay", 200.0, 0.0, 1000.0, 1.0, v -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final SliderSetting updateDelay = this.add(new SliderSetting("UpdateDelay", 200.0, 0.0, 1000.0, 1.0, v -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, v -> this.page.getValue() == Page.Rotate).setParent());
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", true, v -> this.rotate.isOpen() && this.page.getValue() == Page.Rotate));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Page.Rotate));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Page.Rotate));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 30, 0, 50, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.checkFov.getValue() && this.page.getValue() == Page.Rotate));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Page.Rotate));
    private final BooleanSetting noSuicide = this.add(new BooleanSetting("NoSuicide", true, v -> this.page.getValue() == Page.Calc));
    private final BooleanSetting terrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true, v -> this.page.getValue() == Page.Calc));
    private final SliderSetting minPrefer = this.add(new SliderSetting("Prefer", 7.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Page.Calc).setSuffix("dmg"));
    private final SliderSetting maxSelfDamage = this.add(new SliderSetting("MaxSelf", 8.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Page.Calc).setSuffix("dmg"));
    private final EnumSetting<Aura.Aura_nurTqHTNjexQmuWdDgIn> mode = this.add(new EnumSetting<Aura.Aura_nurTqHTNjexQmuWdDgIn>("TargetESP", Aura.Aura_nurTqHTNjexQmuWdDgIn.Jello, v -> this.page.getValue() == Page.Render));
    private final ColorSetting color = this.add(new ColorSetting("TargetColor", new Color(255, 255, 255, 250), v -> this.page.getValue() == Page.Render));
    private final BooleanSetting bold = this.add(new BooleanSetting("Bold", false, v -> this.page.getValue() == Page.Render));
    private final Timer delayTimer = new Timer();
    private final Timer calcTimer = new Timer();
    private final ArrayList<BlockPos> chargeList = new ArrayList();
    private final Timer assistTimer = new Timer();
    public Vec3d directionVec = null;
    public PlayerEntity displayTarget;
    public BlockPos tempPos;
    public double lastDamage;
    double fade = 0.0;
    BlockPos assistPos;

    public AutoAnchor_MDcwoWYRcPYheLZJWRZK() {
        super("AutoAnchor", Category.Combat);
        INSTANCE = this;
        HexTech.EVENT_BUS.subscribe(new AutoAnchor_fWvHjchZKtWCdDnpHPYc());
    }

    public static boolean canSee(Vec3d from, Vec3d to) {
        BlockHitResult result = null;
        if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.world != null) {
            result = AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player));
        }
        return result == null || result.getType() == HitResult.Type.MISS;
    }

    @Override
    public String getInfo() {
        if (this.displayTarget != null && currentPos != null) {
            return this.displayTarget.getName().getString();
        }
        return null;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {
        if (this.displayTarget != null && currentPos != null) {
            Aura.doRender(matrixStack, mc.getTickDelta(), this.displayTarget, this.color.getValue(), this.mode.getValue());
        }
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (currentPos != null && this.rotate.getValue() && this.yawStep.getValue() && this.directionVec != null) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @Override
    public void onDisable() {
        currentPos = null;
    }

    @Override
    public void onThread() {
        if (this.thread.getValue()) {
            this.calc();
        }
    }

    @Override
    public void onUpdate() {
        if (this.assist.getValue()) {
            this.onAssist();
        }
        int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.RESPAWN_ANCHOR) : InventoryUtil.findBlock(Blocks.RESPAWN_ANCHOR);
        int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.GLOWSTONE) : InventoryUtil.findBlock(Blocks.GLOWSTONE);
        int unBlock = this.inventorySwap.getValue() ? anchor : InventoryUtil.findUnBlock();
        int old = 0;
        if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player != null) {
            old = AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getInventory().selectedSlot;
        }
        if (!this.thread.getValue()) {
            this.calc();
        }
        if (anchor == -1) {
            currentPos = null;
            this.tempPos = null;
            return;
        }
        if (glowstone == -1) {
            currentPos = null;
            this.tempPos = null;
            return;
        }
        if (unBlock == -1) {
            currentPos = null;
            this.tempPos = null;
            return;
        }
        if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.isSneaking()) {
            currentPos = null;
            this.tempPos = null;
            return;
        }
        if (this.usingPause.getValue() && AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.isUsingItem()) {
            currentPos = null;
            this.tempPos = null;
            return;
        }
        if (this.syncCyrstal.getValue() && AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null) {
            return;
        }
        if (this.cancelBurrow.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            return;
        }
        if (Blink.INSTANCE.isOn() && this.cancelblink.getValue()) {
            return;
        }
        if (currentPos != null) {
            boolean shouldSpam;
            if (this.breakCrystal.getValue()) {
                CombatUtil.attackCrystal(new BlockPos(currentPos), this.rotate.getValue(), false);
            }
            boolean bl = shouldSpam = this.spam.getValue() && (!this.mineSpam.getValue() || HexTech.BREAK.isMining(currentPos));
            if (shouldSpam) {
                if (!this.delayTimer.passed((long) this.spamDelay.getValueFloat())) {
                    return;
                }
                this.delayTimer.reset();
                if (BlockUtil.canPlace(currentPos, this.range.getValue(), this.breakCrystal.getValue())) {
                    this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                }
                if (!this.chargeList.contains(currentPos)) {
                    this.delayTimer.reset();
                    this.clickBlock(currentPos, BlockUtil.getClickSide(currentPos), this.rotate.getValue(), glowstone);
                    this.chargeList.add(currentPos);
                }
                this.chargeList.remove(currentPos);
                this.clickBlock(currentPos, BlockUtil.getClickSide(currentPos), this.rotate.getValue(), unBlock);
                if (this.spamPlace.getValue() && this.inSpam.getValue()) {
                    if (this.yawStep.getValue() && this.checkFov.getValue()) {
                        Direction side = BlockUtil.getClickSide(currentPos);
                        Vec3d directionVec = new Vec3d((double) currentPos.getX() + 0.5 + (double) side.getVector().getX() * 0.5, (double) currentPos.getY() + 0.5 + (double) side.getVector().getY() * 0.5, (double) currentPos.getZ() + 0.5 + (double) side.getVector().getZ() * 0.5);
                        if (HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
                            CombatUtil.modifyPos = currentPos;
                            CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
                            this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                            CombatUtil.modifyPos = null;
                        }
                    } else {
                        CombatUtil.modifyPos = currentPos;
                        CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
                        this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                        CombatUtil.modifyPos = null;
                    }
                }
            } else if (BlockUtil.canPlace(currentPos, this.range.getValue(), this.breakCrystal.getValue())) {
                if (!this.delayTimer.passed((long) this.placeDelay.getValueFloat())) {
                    return;
                }
                this.delayTimer.reset();
                this.placeBlock(currentPos, this.rotate.getValue(), anchor);
            } else if (BlockUtil.getBlock(currentPos) == Blocks.RESPAWN_ANCHOR) {
                if (!this.chargeList.contains(currentPos)) {
                    if (!this.delayTimer.passed((long) this.placeDelay.getValueFloat())) {
                        return;
                    }
                    this.delayTimer.reset();
                    this.clickBlock(currentPos, BlockUtil.getClickSide(currentPos), this.rotate.getValue(), glowstone);
                    this.chargeList.add(currentPos);
                } else {
                    if (!this.delayTimer.passed((long) this.placeDelay.getValueFloat())) {
                        return;
                    }
                    this.delayTimer.reset();
                    this.chargeList.remove(currentPos);
                    this.clickBlock(currentPos, BlockUtil.getClickSide(currentPos), this.rotate.getValue(), unBlock);
                    if (this.spamPlace.getValue()) {
                        if (this.yawStep.getValue() && this.checkFov.getValue()) {
                            Direction side = BlockUtil.getClickSide(currentPos);
                            Vec3d directionVec = new Vec3d((double) currentPos.getX() + 0.5 + (double) side.getVector().getX() * 0.5, (double) currentPos.getY() + 0.5 + (double) side.getVector().getY() * 0.5, (double) currentPos.getZ() + 0.5 + (double) side.getVector().getZ() * 0.5);
                            if (HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
                                CombatUtil.modifyPos = currentPos;
                                CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
                                this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                                CombatUtil.modifyPos = null;
                            }
                        } else {
                            CombatUtil.modifyPos = currentPos;
                            CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
                            this.placeBlock(currentPos, this.rotate.getValue(), anchor);
                            CombatUtil.modifyPos = null;
                        }
                    }
                }
            }
            if (!this.inventorySwap.getValue()) {
                this.doSwap(old);
            }
        }
    }

    private void calc() {
        if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.nullCheck()) {
            return;
        }
        if (this.calcTimer.passed((long) this.updateDelay.getValueFloat())) {
            double damage;
            PredictionSetting._XBpBEveLWEKUGQPHCCIS selfPredict = new PredictionSetting._XBpBEveLWEKUGQPHCCIS(AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player);
            this.calcTimer.reset();
            this.tempPos = null;
            double placeDamage = this.minDamage.getValue();
            double breakDamage = this.breakMin.getValue();
            boolean anchorFound = false;
            List<PlayerEntity> enemies = CombatUtil.getEnemies(this.targetRange.getValue());
            ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS> list = new ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS>();
            for (PlayerEntity player : enemies) {
                list.add(new PredictionSetting._XBpBEveLWEKUGQPHCCIS(player));
            }
            for (PredictionSetting._XBpBEveLWEKUGQPHCCIS pap : list) {
                double d = 0;
                double selfDamage;
                BlockPos pos = EntityUtil.getEntityPos(pap.player, true).up(2);
                if (!BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue()) && (BlockUtil.getBlock(pos) != Blocks.RESPAWN_ANCHOR || BlockUtil.getClickSideStrict(pos) == null) || AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player != null && ((selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue() || this.noSuicide.getValue() && selfDamage > (double) (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getHealth() + AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getAbsorptionAmount())))
                    continue;
                damage = this.getAnchorDamage(pos, pap.player, pap.predict);
                if (!(d > (double) this.headDamage.getValueFloat())) continue;
                this.lastDamage = damage;
                this.displayTarget = pap.player;
                this.tempPos = pos;
                break;
            }
            if (this.tempPos == null) {
                for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat())) {
                    for (PredictionSetting._XBpBEveLWEKUGQPHCCIS pap : list) {
                        double selfDamage;
                        boolean skip;
                        if (this.light.getValue()) {
                            CombatUtil.modifyPos = pos;
                            CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
                            skip = !AutoAnchor_MDcwoWYRcPYheLZJWRZK.canSee(pos.toCenterPos(), pap.predict.getPos());
                            CombatUtil.modifyPos = null;
                            if (skip) continue;
                        }
                        if (BlockUtil.getBlock(pos) != Blocks.RESPAWN_ANCHOR) {
                            double selfDamage2;
                            if (anchorFound || !BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue()))
                                continue;
                            CombatUtil.modifyPos = pos;
                            CombatUtil.modifyBlockState = Blocks.OBSIDIAN.getDefaultState();
                            skip = BlockUtil.getClickSideStrict(pos) == null;
                            CombatUtil.modifyPos = null;
                            if (skip || !((damage = this.getAnchorDamage(pos, pap.player, pap.predict)) >= placeDamage) || AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null && !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.isOff() && !((double) AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastDamage < damage) || AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player != null && ((selfDamage2 = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue() || this.noSuicide.getValue() && selfDamage2 > (double) (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getHealth() + AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getAbsorptionAmount())))
                                continue;
                            this.lastDamage = damage;
                            this.displayTarget = pap.player;
                            placeDamage = damage;
                            this.tempPos = pos;
                            continue;
                        }
                        double damage2 = this.getAnchorDamage(pos, pap.player, pap.predict);
                        if (BlockUtil.getClickSideStrict(pos) == null || !(damage2 >= breakDamage)) continue;
                        if (damage2 >= this.minPrefer.getValue()) {
                            anchorFound = true;
                        }
                        if (!anchorFound && damage2 < placeDamage || AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null && !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.isOff() && !((double) AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lastDamage < damage2) || AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player != null && ((selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue() || this.noSuicide.getValue() && selfDamage > (double) (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getHealth() + AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getAbsorptionAmount())))
                            continue;
                        this.lastDamage = damage2;
                        this.displayTarget = pap.player;
                        breakDamage = damage2;
                        this.tempPos = pos;
                    }
                }
            }
        }
        currentPos = this.tempPos;
    }

    public double getAnchorDamage(BlockPos anchorPos, PlayerEntity target, PlayerEntity predict) {
        if (this.terrainIgnore.getValue()) {
            CombatUtil.terrainIgnore = true;
        }
        double damage = ExplosionUtil.anchorDamage(anchorPos, target, predict);
        CombatUtil.terrainIgnore = false;
        return damage;
    }

    public void placeBlock(BlockPos pos, boolean rotate, int slot) {
        if (BlockUtil.airPlace()) {
            this.clickBlock(pos, Direction.DOWN, rotate, slot);
            return;
        }
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            return;
        }
        this.clickBlock(pos.offset(side), side.getOpposite(), rotate, slot);
    }

    public void clickBlock(BlockPos pos, Direction side, boolean rotate, int slot) {
        if (pos == null) {
            return;
        }
        Vec3d directionVec = new Vec3d((double) pos.getX() + 0.5 + (double) side.getVector().getX() * 0.5, (double) pos.getY() + 0.5 + (double) side.getVector().getY() * 0.5, (double) pos.getZ() + 0.5 + (double) side.getVector().getZ() * 0.5);
        if (rotate && !this.faceVector(directionVec)) {
            return;
        }
        this.doSwap(slot);
        EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        Module_eSdgMXWuzcxgQVaJFmKZ.sendSequencedPacket(id -> new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, id));
        if (this.inventorySwap.getValue()) {
            this.doSwap(slot);
        }
    }

    private void doSwap(int slot) {
        if (this.inventorySwap.getValue()) {
            if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player != null) {
                InventoryUtil.inventorySwap(slot, AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getInventory().selectedSlot);
            }
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public boolean faceVector(Vec3d directionVec) {
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

    public void onAssist() {
        BlockPos placePos;
        this.assistPos = null;
        int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.RESPAWN_ANCHOR) : InventoryUtil.findBlock(Blocks.RESPAWN_ANCHOR);
        int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(Blocks.GLOWSTONE) : InventoryUtil.findBlock(Blocks.GLOWSTONE);
        int old = 0;
        if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player != null) {
            old = AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.getInventory().selectedSlot;
        }
        if (anchor == -1) {
            return;
        }
        if (glowstone == -1) {
            return;
        }
        if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.isSneaking()) {
            return;
        }
        if (this.usingPause.getValue() && AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.player.isUsingItem()) {
            return;
        }
        if (Blink.INSTANCE.isOn() && this.cancelblink.getValue()) {
            return;
        }
        if (!this.assistTimer.passed((long) (this.delay.getValueFloat() * 1000.0f))) {
            return;
        }
        this.assistTimer.reset();
        ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS> list = new ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS>();
        for (PlayerEntity player : CombatUtil.getEnemies(this.assistRange.getValue())) {
            list.add(new PredictionSetting._XBpBEveLWEKUGQPHCCIS(player));
        }
        double bestDamage = this.assistDamage.getValue();
        for (PredictionSetting._XBpBEveLWEKUGQPHCCIS pap : list) {
            double damage;
            BlockPos pos = EntityUtil.getEntityPos(pap.player, true).up(2);
            if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.world != null && AutoAnchor_MDcwoWYRcPYheLZJWRZK.mc.world.getBlockState(pos).getBlock() == Blocks.RESPAWN_ANCHOR) {
                return;
            }
            if (BlockUtil.clientCanPlace(pos, false) && (damage = this.getAnchorDamage(pos, pap.player, pap.predict)) >= bestDamage) {
                bestDamage = damage;
                this.assistPos = pos;
            }
            for (Direction i : Direction.values()) {
                double damage2;
                if (i == Direction.UP || i == Direction.DOWN || !BlockUtil.clientCanPlace(pos.offset(i), false) || !((damage2 = this.getAnchorDamage(pos.offset(i), pap.player, pap.predict)) >= bestDamage))
                    continue;
                bestDamage = damage2;
                this.assistPos = pos.offset(i);
            }
        }
        if (this.assistPos != null && BlockUtil.getPlaceSide(this.assistPos, this.range.getValue()) == null && (placePos = this.getHelper(this.assistPos)) != null) {
            this.doSwap(anchor);
            BlockUtil.placeBlock(placePos, this.rotate.getValue());
            if (this.inventorySwap.getValue()) {
                this.doSwap(anchor);
            } else {
                this.doSwap(old);
            }
        }
    }

    public BlockPos getHelper(BlockPos pos) {
        for (Direction i : Direction.values()) {
            if (this.checkMine.getValue() && HexTech.BREAK.isMining(pos.offset(i)) || !BlockUtil.isStrictDirection(pos.offset(i), i.getOpposite()) || !BlockUtil.canPlace(pos.offset(i)))
                continue;
            return pos.offset(i);
        }
        return null;
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Page {
        General,
        Calc,
        Rotate,
        Assist,
        Render

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public class AutoAnchor_fWvHjchZKtWCdDnpHPYc {

        public AutoAnchor_fWvHjchZKtWCdDnpHPYc() {
        }

        @EventHandler
        public void onRender3D(Render3DEvent event) {
            if (currentPos != null) {
                noPosTimer.reset();
                placeVec3d = currentPos.toCenterPos();
            }
            if (placeVec3d == null) {
                return;
            }
            fade = fadeSpeed.getValue() >= 1.0 ? (noPosTimer.passedMs((long) (startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5) : AnimateUtil.animate(fade, noPosTimer.passedMs((long) (startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5, fadeSpeed.getValue() / 10.0);
            if (fade == 0.0) {
                curVec3d = null;
                return;
            }
            curVec3d = curVec3d == null || sliderSpeed.getValue() >= 1.0 ? placeVec3d : new Vec3d(AnimateUtil.animate(curVec3d.x, placeVec3d.x, sliderSpeed.getValue() / 10.0), AnimateUtil.animate(curVec3d.y, placeVec3d.y, sliderSpeed.getValue() / 10.0), AnimateUtil.animate(curVec3d.z, placeVec3d.z, sliderSpeed.getValue() / 10.0));
            if (render.getValue()) {
                Box cbox = new Box(curVec3d, curVec3d);
                cbox = shrink.getValue() ? cbox.expand(fade) : cbox.expand(0.5);
                MatrixStack matrixStack = event.getMatrixStack();
                if (fill.booleanValue) {
                    Render3DUtil.drawFill(matrixStack, cbox, ColorUtil.injectAlpha(fill.getValue(), (int) ((double) fill.getValue().getAlpha() * fade * 2.0)));
                }
                if (box.booleanValue) {
                    if (!bold.getValue()) {
                        Render3DUtil.drawBox(matrixStack, cbox, ColorUtil.injectAlpha(box.getValue(), (int) ((double) box.getValue().getAlpha() * fade * 2.0)));
                    } else {
                        Render3DUtil.drawLine(cbox, ColorUtil.injectAlpha(box.getValue(), (int) ((double) box.getValue().getAlpha() * fade * 2.0)), lineWidth.getValueInt());
                    }
                }
            }
        }
    }
}
