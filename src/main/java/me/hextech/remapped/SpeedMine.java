package me.hextech.remapped;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.hextech.HexTech;
import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.api.events.impl.ClickBlockEvent;
import me.hextech.remapped.api.utils.combat.CanPlaceCrystal;
import me.hextech.remapped.api.utils.combat.CombatUtil;
import me.hextech.remapped.api.utils.entity.EntityUtil;
import me.hextech.remapped.api.utils.render.ColorUtil;
import me.hextech.remapped.api.utils.render.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.mod.gui.clickgui.ClickGuiScreen;
import me.hextech.remapped.mod.modules.impl.combat.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.mod.modules.impl.setting.BypassSetting_RInKGmTQYgWFRhsUOiJP;
import me.hextech.remapped.mod.modules.impl.setting.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.mod.modules.settings.impl.BindSetting;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.mod.modules.settings.impl.ColorSetting;
import me.hextech.remapped.mod.modules.settings.impl.EnumSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class SpeedMine
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final List<Block> godBlocks;
    public static SpeedMine INSTANCE;
    public static BlockPos breakPos;
    public static BlockPos secondPos;
    public static double progress;
    public static double secondProgress;
    public static boolean sendGroundPacket;
    static DecimalFormat df;
    final Timer secondTimer = new Timer();
    private final EnumSetting<_uySyZpuFdxDcRzYECOEM> page = this.add(new EnumSetting<_uySyZpuFdxDcRzYECOEM>("Page", _uySyZpuFdxDcRzYECOEM.General));
    public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, 3.0, 10.0, 0.1, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    public final BooleanSetting hotBar = this.add(new BooleanSetting("HotbarSwap", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    public final BooleanSetting invSwapBypass = this.add(new BooleanSetting("InvSwapBypass", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    public final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("SwingMode", SwingSide.All, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    public final BooleanSetting preferWeb = this.add(new BooleanSetting("PreferWeb", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    public final BooleanSetting preferHead = this.add(new BooleanSetting("PreferHead", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    public final BooleanSetting farCancel = this.add(new BooleanSetting("FarCancel", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    public final BooleanSetting crystal = this.add(new BooleanSetting("Crystal", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place)).setParent());
    private final BooleanSetting onlyHeadBomber = this.add(new BooleanSetting("OnlyCev", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place) && this.crystal.isOpen()));
    private final BooleanSetting waitPlace = this.add(new BooleanSetting("WaitPlace", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place) && this.crystal.isOpen()));
    private final BooleanSetting spamPlace = this.add(new BooleanSetting("SpamPlace", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place) && this.crystal.isOpen()));
    private final BooleanSetting afterBreak = this.add(new BooleanSetting("AfterBreak", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place) && this.crystal.isOpen()));
    private final BooleanSetting checkDamage = this.add(new BooleanSetting("DetectProgress", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place) && this.crystal.isOpen()));
    private final SliderSetting crystalDamage = this.add(new SliderSetting("Progress", 0.9f, 0.0, 1.0, 0.01, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place) && this.crystal.isOpen() && this.checkDamage.getValue()));
    public final BindSetting obsidian = this.add(new BindSetting("Obsidian", -1, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place)));
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100), v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    public final ColorSetting endColor = this.add(new ColorSetting("EndColor", new Color(25, 255, 50, 100), v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)).injectBoolean(false));
    public final ColorSetting endboxColor = this.add(new ColorSetting("EndBoxColor", new Color(25, 255, 50, 100), v -> this.endColor.booleanValue));
    public final ColorSetting doubleColor = this.add(new ColorSetting("DoubleColor", new Color(88, 94, 255, 100), v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    public final SliderSetting textscale = this.add(new SliderSetting("Scale", 1.0, 0.0, 3.0, 0.01, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    public final SliderSetting doubletext = this.add(new SliderSetting("SilentDouble", -1.0, -3.0, 3.0, 0.01, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 50.0, 0.0, 500.0, 1.0, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final SliderSetting damage = this.add(new SliderSetting("Damage", 0.7f, 0.0, 2.0, 0.01, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final SliderSetting maxBreak = this.add(new SliderSetting("MaxBreak", 3.0, 0.0, 20.0, 1.0, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final BooleanSetting instant = this.add(new BooleanSetting("Instant", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final BooleanSetting wait = this.add(new BooleanSetting("Wait", true, v -> !this.instant.getValue() && this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final BooleanSetting mineAir = this.add(new BooleanSetting("MineAir", true, v -> this.wait.getValue() && !this.instant.getValue() && this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final BooleanSetting doubleBreak = this.add(new BooleanSetting("DoubleBreak", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General))).setParent();
    private final BooleanSetting stopPacket = this.add(new BooleanSetting("StopPacket", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General) && this.doubleBreak.isOpen()));
    private final BooleanSetting setAir = this.add(new BooleanSetting("SetAir", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final BooleanSetting swing = this.add(new BooleanSetting("Swing", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final BooleanSetting endSwing = this.add(new BooleanSetting("EndSwing", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.General)));
    private final BooleanSetting switchReset = this.add(new BooleanSetting("SwitchReset", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    private final BooleanSetting checkGround = this.add(new BooleanSetting("CheckGround", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    private final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check) && this.checkGround.getValue()));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    private final BooleanSetting bypassGround = this.add(new BooleanSetting("BypassGround", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    private final SliderSetting bypassTime = this.add(new SliderSetting("BypassTime", 400, 0, 2000, v -> this.bypassGround.getValue() && this.page.is(_uySyZpuFdxDcRzYECOEM.Check)));
    private final BooleanSetting rotate = this.add(new BooleanSetting("StartRotate", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Rotation)));
    private final BooleanSetting endRotate = this.add(new BooleanSetting("EndRotate", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Rotation)));
    private final SliderSetting syncTime = this.add(new SliderSetting("Sync", 300, 0, 1000, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Rotation)));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Rotation)));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Rotation)));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Rotation)));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 30, 0, 50, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Rotation)));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Rotation)));
    private final BindSetting enderChest = this.add(new BindSetting("EnderChest", -1, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place)));
    private final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 100, 0, 1000, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Place)));
    private final BooleanSetting autoColor = this.add(new BooleanSetting("AutoColor", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final BooleanSetting bold = this.add(new BooleanSetting("Bold", false, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final SliderSetting lineWidth = this.add(new SliderSetting("LineWidth", 4, 1, 5, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final BooleanSetting text = this.add(new BooleanSetting("Text", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final BooleanSetting slientdouble = this.add(new BooleanSetting("DoubleText", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final BooleanSetting box = this.add(new BooleanSetting("Box", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final EnumSetting<FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad> quad = this.add(new EnumSetting<FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad>("Quad", FadeUtils_DPfHthPqEJdfXfNYhDbG.Quad.In, v -> this.page.is(_uySyZpuFdxDcRzYECOEM.Render)));
    private final Timer mineTimer = new Timer();
    private final FadeUtils_DPfHthPqEJdfXfNYhDbG animationTime = new FadeUtils_DPfHthPqEJdfXfNYhDbG(1000L);
    private final FadeUtils_DPfHthPqEJdfXfNYhDbG secondAnim = new FadeUtils_DPfHthPqEJdfXfNYhDbG(1000L);
    private final Timer delayTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer sync = new Timer();
    public Vec3d directionVec = null;
    int lastSlot = -1;
    boolean done = false;
    boolean skip = false;
    private boolean startPacket = false;
    private int breakNumber = 0;

    public SpeedMine() {
        super("SpeedMine", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    public static BlockPos getBreakPos() {
        if (INSTANCE.isOn()) {
            return breakPos;
        }
        return null;
    }

    public static boolean canPlaceCrystal(BlockPos pos, boolean ignoreItem) {
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        return !(BlockUtil.getBlock(obsPos) != Blocks.BEDROCK && BlockUtil.getBlock(obsPos) != Blocks.OBSIDIAN || BlockUtil.getClickSideStrict(obsPos) == null || !SpeedMine.noEntity(boost, ignoreItem) || !SpeedMine.noEntity(boost.up(), ignoreItem) || CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() && !SpeedMine.mc.world.isAir(boost.up()));
    }

    public static boolean noEntity(BlockPos pos, boolean ignoreItem) {
        for (Entity entity : BlockUtil.getEntities(new Box(pos))) {
            if (entity instanceof ItemEntity && ignoreItem || entity instanceof ArmorStandEntity && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.obsMode.getValue()) continue;
            return false;
        }
        return true;
    }

    @Override
    public String getInfo() {
        if (progress >= 1.0) {
            return "Done";
        }
        return df.format(progress * 100.0) + "%";
    }

    private int findCrystal() {
        if (!this.hotBar.getValue()) {
            return InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL);
        }
        return InventoryUtil.findItem(Items.END_CRYSTAL);
    }

    private int findBlock(Block block) {
        if (!this.hotBar.getValue()) {
            return InventoryUtil.findBlockInventorySlot(block);
        }
        return InventoryUtil.findBlock(block);
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.rotate.getValue() && this.yawStep.getValue() && this.directionVec != null && !this.sync.passed(this.syncTime.getValue())) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    private void doSwap(int slot, int inv) {
        if (this.hotBar.getValue()) {
            InventoryUtil.switchToSlot(slot);
        } else {
            InventoryUtil.inventorySwap(inv, SpeedMine.mc.player.getInventory().selectedSlot);
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        this.onUpdate();
        if (SpeedMine.mc.player != null && !SpeedMine.mc.player.isCreative()) {
            double breakTime;
            int slot;
            if (secondPos != null) {
                slot = this.getTool(secondPos);
                if (slot == -1) {
                    slot = SpeedMine.mc.player.getInventory().selectedSlot;
                }
                breakTime = this.getBreakTime(secondPos, slot);
                secondProgress = (double)this.secondTimer.getPassedTimeMs() / breakTime;
                if (this.isAir(secondPos)) {
                    secondPos = null;
                    return;
                }
                double iProgress = secondProgress > 1.0 ? 1.0 : secondProgress;
                double ease = (1.0 - this.secondAnim.getQuad(this.quad.getValue())) * 0.5;
                if (!this.bold.getValue()) {
                    Render3DUtil.draw3DBox(matrixStack, new Box(secondPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease), ColorUtil.injectAlpha(this.doubleColor.getValue(), (int)((double)this.doubleColor.getValue().getAlpha() * iProgress)), this.outline.getValue(), this.box.getValue());
                } else {
                    Render3DUtil.drawLine(new Box(secondPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease), ColorUtil.injectAlpha(this.doubleColor.getValue(), (int)((double)this.doubleColor.getValue().getAlpha() * iProgress)), this.lineWidth.getValueInt());
                    Render3DUtil.drawFill(matrixStack, new Box(secondPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease), ColorUtil.injectAlpha(this.doubleColor.getValue(), (int)((double)this.doubleColor.getValue().getAlpha() * iProgress)));
                }
            } else {
                secondProgress = 0.0;
            }
            if (breakPos != null) {
                double iProgress;
                slot = this.getTool(breakPos);
                if (slot == -1) {
                    slot = SpeedMine.mc.player.getInventory().selectedSlot;
                }
                breakTime = this.getBreakTime(breakPos, slot);
                progress = (double)this.mineTimer.getPassedTimeMs() / breakTime;
                this.animationTime.setLength((long)this.getBreakTime(breakPos, slot));
                double ease = (1.0 - this.animationTime.getQuad(this.quad.getValue())) * 0.5;
                Color color = this.color.getValue();
                double d = iProgress = progress > 1.0 ? 1.0 : progress;
                if (!this.bold.getValue()) {
                    Render3DUtil.draw3DBox(matrixStack, new Box(breakPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease), ColorUtil.injectAlpha(this.autoColor.getValue() ? new Color((int)(255.0 * iProgress), (int)(255.0 * iProgress), 0) : (!this.endColor.booleanValue ? color : (iProgress >= 1.0 ? this.endColor.getValue() : color)), (int)(!this.endColor.booleanValue ? (double)color.getAlpha() * iProgress : (iProgress >= 1.0 ? (double)this.endColor.getValue().getAlpha() * iProgress : (double)color.getAlpha() * iProgress))), this.outline.getValue(), this.box.getValue());
                } else {
                    Render3DUtil.drawLine(new Box(breakPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease), ColorUtil.injectAlpha(this.autoColor.getValue() ? new Color((int)(255.0 * iProgress), (int)(255.0 * iProgress), 0) : (!this.endColor.booleanValue ? color : (iProgress >= 1.0 ? this.endboxColor.getValue() : color)), (int)(!this.endColor.booleanValue ? (double)color.getAlpha() * iProgress : (iProgress >= 1.0 ? (double)this.endColor.getValue().getAlpha() * iProgress : (double)color.getAlpha() * iProgress))), this.lineWidth.getValueInt());
                    Render3DUtil.drawFill(matrixStack, new Box(breakPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease), ColorUtil.injectAlpha(this.autoColor.getValue() ? new Color((int)(255.0 * iProgress), (int)(255.0 * iProgress), 0) : (!this.endColor.booleanValue ? color : (iProgress >= 1.0 ? this.endColor.getValue() : color)), (int)(!this.endColor.booleanValue ? (double)color.getAlpha() * iProgress : (iProgress >= 1.0 ? (double)this.endColor.getValue().getAlpha() * iProgress : (double)color.getAlpha() * iProgress))));
                }
                if (this.text.getValue()) {
                    if (MainHand.INSTANCE.isOn() && MainHand.INSTANCE.mineSlot.getValue() && this.isAir(breakPos)) {
                        Render3DUtil.drawText3DSlient("SwitchDouble", breakPos.toCenterPos(), -1);
                    }
                    if (this.isAir(breakPos)) {
                        Render3DUtil.drawText3DMine("Waiting", breakPos.toCenterPos(), -1);
                    } else if ((double)((int)this.mineTimer.getPassedTimeMs()) < breakTime) {
                        Render3DUtil.drawText3DMine(df.format(progress * 100.0) + "%", breakPos.toCenterPos(), -1);
                    } else {
                        Render3DUtil.drawText3DMine("100.0%", breakPos.toCenterPos(), -1);
                    }
                }
            } else {
                progress = 0.0;
            }
        }
    }

    @Override
    public void onLogin() {
        this.startPacket = false;
        breakPos = null;
        secondPos = null;
    }

    @Override
    public void onDisable() {
        this.startPacket = false;
        breakPos = null;
    }

    @Override
    public void onUpdate() {
        if (this.usingPause.getValue() && SpeedMine.mc.player.isUsingItem()) {
            return;
        }
        if (this.skip) {
            this.skip = false;
            return;
        }
        if (SpeedMine.mc.player.isDead()) {
            secondPos = null;
        }
        if (secondPos != null) {
            double time = this.getBreakTime(secondPos, SpeedMine.mc.player.getInventory().selectedSlot, 1.1);
            if (this.secondTimer.passed(time)) {
                secondPos = null;
            } else if (this.stopPacket.getValue()) {
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, secondPos, BlockUtil.getClickSide(secondPos), id));
            }
        }
        if (secondPos != null && this.isAir(secondPos)) {
            secondPos = null;
        }
        if (SpeedMine.mc.player.isCreative()) {
            this.startPacket = false;
            this.breakNumber = 0;
            breakPos = null;
            return;
        }
        if (breakPos == null) {
            this.breakNumber = 0;
            this.startPacket = false;
            return;
        }
        if (this.isAir(breakPos)) {
            this.breakNumber = 0;
        }
        if ((double)this.breakNumber > this.maxBreak.getValue() - 1.0 && this.maxBreak.getValue() > 0.0 || !this.wait.getValue() && this.isAir(breakPos) && !this.instant.getValue()) {
            if (breakPos.equals(secondPos)) {
                secondPos = null;
            }
            this.startPacket = false;
            this.breakNumber = 0;
            breakPos = null;
            return;
        }
        if (godBlocks.contains(SpeedMine.mc.world.getBlockState(breakPos).getBlock())) {
            breakPos = null;
            this.startPacket = false;
            return;
        }
        if ((double)MathHelper.sqrt((float) EntityUtil.getEyesPos().squaredDistanceTo(breakPos.toCenterPos())) > this.range.getValue()) {
            if (this.farCancel.getValue()) {
                this.startPacket = false;
                this.breakNumber = 0;
                breakPos = null;
            }
            return;
        }
        if (breakPos.equals(AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos)) {
            return;
        }
        if (!(this.hotBar.getValue() || SpeedMine.mc.currentScreen == null || SpeedMine.mc.currentScreen instanceof ChatScreen || SpeedMine.mc.currentScreen instanceof InventoryScreen || SpeedMine.mc.currentScreen instanceof ClickGuiScreen)) {
            return;
        }
        int slot = this.getTool(breakPos);
        if (slot == -1) {
            slot = SpeedMine.mc.player.getInventory().selectedSlot;
        }
        if (this.isAir(breakPos)) {
            if (this.shouldCrystal()) {
                Direction[] directionArray = Direction.values();
                int n = directionArray.length;
                for (int i = 0; i < n; ++i) {
                    Direction facing = directionArray[i];
                    CombatUtil.attackCrystal(breakPos.offset(facing), this.rotate.getValue(), true);
                }
            }
            if (this.placeTimer.passedMs(this.placeDelay.getValue()) && BlockUtil.canPlace(breakPos) && SpeedMine.mc.currentScreen == null) {
                int obsidian;
                if (this.enderChest.isPressed()) {
                    int eChest = this.findBlock(Blocks.ENDER_CHEST);
                    if (eChest != -1) {
                        int oldSlot = SpeedMine.mc.player.getInventory().selectedSlot;
                        this.doSwap(eChest, eChest);
                        BlockUtil.placeBlock(breakPos, this.rotate.getValue(), true);
                        this.doSwap(oldSlot, eChest);
                        this.placeTimer.reset();
                    }
                } else if (this.obsidian.isPressed() && (obsidian = this.findBlock(Blocks.OBSIDIAN)) != -1) {
                    int hasCrystal = 0;
                    if (this.shouldCrystal()) {
                        for (Entity entity : BlockUtil.getEntities(new Box(breakPos.up()))) {
                            if (!(entity instanceof EndCrystalEntity)) continue;
                            hasCrystal = 1;
                            break;
                        }
                    }
                    if (hasCrystal == 0 || this.spamPlace.getValue()) {
                        int oldSlot = SpeedMine.mc.player.getInventory().selectedSlot;
                        this.doSwap(obsidian, obsidian);
                        BlockUtil.placeBlock(breakPos, this.rotate.getValue(), true);
                        this.doSwap(oldSlot, obsidian);
                        this.placeTimer.reset();
                    }
                }
            }
            this.breakNumber = 0;
        } else if (SpeedMine.canPlaceCrystal(breakPos.up(), true) && this.shouldCrystal() && (this.placeTimer.passedMs(this.placeDelay.getValue()) ? (this.checkDamage.getValue() ? (double)this.mineTimer.getPassedTimeMs() / this.getBreakTime(breakPos, slot) >= this.crystalDamage.getValue() && !this.placeCrystal() : !this.placeCrystal()) : this.startPacket)) {
            return;
        }
        if (this.waitPlace.getValue()) {
            for (Direction i : Direction.values()) {
                if (!breakPos.offset(i).equals(AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos)) continue;
                if (!CanPlaceCrystal.canPlaceCrystal(AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos, false, false)) break;
                return;
            }
        }
        if (!this.delayTimer.passedMs((long)this.delay.getValue())) {
            return;
        }
        if (this.startPacket) {
            if (this.isAir(breakPos)) {
                return;
            }
            if (this.onlyGround.getValue() && !SpeedMine.mc.player.isOnGround()) {
                return;
            }
            this.done = this.mineTimer.passedMs((long)this.getBreakTime(breakPos, slot));
            if (this.done) {
                boolean shouldSwitch;
                if (this.endRotate.getValue()) {
                    Vec3i vec3i = BlockUtil.getClickSide(breakPos).getVector();
                    if (!this.faceVector(breakPos.toCenterPos().add(new Vec3d((double)vec3i.getX() * 0.5, (double)vec3i.getY() * 0.5, (double)vec3i.getZ() * 0.5)))) {
                        return;
                    }
                }
                int old = SpeedMine.mc.player.getInventory().selectedSlot;
                if (this.hotBar.getValue()) {
                    shouldSwitch = slot != old;
                } else {
                    if (slot < 9) {
                        slot += 36;
                    }
                    boolean bl = shouldSwitch = old + 36 != slot;
                }
                if (shouldSwitch) {
                    if (this.hotBar.getValue()) {
                        InventoryUtil.switchToSlot(slot);
                    } else if (this.invSwapBypass.getValue()) {
                        InventoryUtil.inventorySwap(slot, old);
                    } else {
                        SpeedMine.mc.interactionManager.clickSlot(SpeedMine.mc.player.currentScreenHandler.syncId, slot, old, SlotActionType.SWAP, SpeedMine.mc.player);
                    }
                }
                if (this.endSwing.getValue()) {
                    EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
                }
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, BlockUtil.getClickSide(breakPos), id));
                if (shouldSwitch) {
                    if (this.hotBar.getValue()) {
                        InventoryUtil.switchToSlot(old);
                    } else {
                        if (this.invSwapBypass.getValue()) {
                            InventoryUtil.inventorySwap(slot, old);
                        } else {
                            SpeedMine.mc.interactionManager.clickSlot(SpeedMine.mc.player.currentScreenHandler.syncId, slot, old, SlotActionType.SWAP, SpeedMine.mc.player);
                        }
                        EntityUtil.syncInventory();
                    }
                }
                ++this.breakNumber;
                this.delayTimer.reset();
                if (this.afterBreak.getValue() && this.shouldCrystal()) {
                    for (Direction facing : Direction.values()) {
                        CombatUtil.attackCrystal(breakPos.offset(facing), this.rotate.getValue(), true);
                    }
                }
                if (this.setAir.getValue()) {
                    SpeedMine.mc.world.setBlockState(breakPos, Blocks.AIR.getDefaultState());
                }
                this.skip = true;
            }
        } else {
            if (!this.mineAir.getValue() && this.isAir(breakPos)) {
                return;
            }
            Direction side = BlockUtil.getClickSide(breakPos);
            if (this.rotate.getValue()) {
                Vec3i vec3i = side.getVector();
                if (!this.faceVector(breakPos.toCenterPos().add(new Vec3d((double)vec3i.getX() * 0.5, (double)vec3i.getY() * 0.5, (double)vec3i.getZ() * 0.5)))) {
                    return;
                }
            }
            this.mineTimer.reset();
            this.done = false;
            this.animationTime.reset();
            if (this.swing.getValue()) {
                EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
            }
            if (this.doubleBreak.getValue()) {
                if (secondPos == null || this.isAir(secondPos)) {
                    double breakTime = this.getBreakTime(breakPos, slot, 1.0);
                    this.secondAnim.reset();
                    this.secondAnim.setLength((long)breakTime);
                    this.secondTimer.reset();
                    secondPos = breakPos;
                }
                if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grim.getValue()) {
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakPos, side, id));
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, side, id));
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, side, id));
                } else {
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, side, id));
                }
            }
            SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
            if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grim.getValue()) {
                if (!this.doubleBreak.getValue()) {
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, side, id));
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakPos, side, id));
                } else {
                    SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakPos, side, id));
                }
            }
        }
    }

    private boolean placeCrystal() {
        int crystal = this.findCrystal();
        if (crystal != -1) {
            int oldSlot = SpeedMine.mc.player.getInventory().selectedSlot;
            this.doSwap(crystal, crystal);
            BlockUtil.placeCrystal(breakPos.up(), this.rotate.getValue());
            this.doSwap(oldSlot, crystal);
            this.placeTimer.reset();
            return !this.waitPlace.getValue();
        }
        return true;
    }

    @EventHandler
    public void onAttackBlock(ClickBlockEvent event) {
        if (SpeedMine.nullCheck()) {
            return;
        }
        if (SpeedMine.mc.player.isCreative()) {
            return;
        }
        event.cancel();
        BlockPos pos = event.getPos();
        if (pos.equals(breakPos)) {
            return;
        }
        if (godBlocks.contains(SpeedMine.mc.world.getBlockState(pos).getBlock())) {
            return;
        }
        if (breakPos != null && this.preferWeb.getValue() && BlockUtil.getBlock(breakPos) == Blocks.COBWEB) {
            return;
        }
        if (breakPos != null && this.preferHead.getValue() && EntityUtil.getPlayerPos(true).up().equals(breakPos)) {
            return;
        }
        if (BlockUtil.getClickSideStrict(pos) == null) {
            return;
        }
        if ((double)MathHelper.sqrt((float)EntityUtil.getEyesPos().squaredDistanceTo(pos.toCenterPos())) > this.range.getValue()) {
            return;
        }
        breakPos = pos;
        this.breakNumber = 0;
        this.startPacket = false;
        this.mineTimer.reset();
        this.done = false;
        this.animationTime.reset();
        this.skip = true;
        Direction side = BlockUtil.getClickSide(breakPos);
        if (this.rotate.getValue()) {
            Vec3i vec3i = side.getVector();
            if (!this.faceVector(breakPos.toCenterPos().add(new Vec3d((double)vec3i.getX() * 0.5, (double)vec3i.getY() * 0.5, (double)vec3i.getZ() * 0.5)))) {
                return;
            }
        }
        this.mineTimer.reset();
        this.done = false;
        this.animationTime.reset();
        if (this.swing.getValue()) {
            EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
        }
        if (this.doubleBreak.getValue()) {
            if (secondPos == null || this.isAir(secondPos)) {
                int slot = this.getTool(breakPos);
                if (slot == -1) {
                    slot = SpeedMine.mc.player.getInventory().selectedSlot;
                }
                double breakTime = this.getBreakTime(breakPos, slot, 1.0);
                this.secondAnim.reset();
                this.secondAnim.setLength((long)breakTime);
                this.secondTimer.reset();
                secondPos = breakPos;
            }
            if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grim.getValue()) {
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakPos, side, id));
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, side, id));
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, side, id));
            } else {
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, side, id));
            }
        }
        SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
        if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grim.getValue()) {
            if (!this.doubleBreak.getValue()) {
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, side, id));
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, side, id));
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakPos, side, id));
            } else {
                SpeedMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakPos, side, id));
            }
        }
    }

    public void mine(BlockPos pos) {
        if (SpeedMine.nullCheck()) {
            return;
        }
        if (SpeedMine.mc.player.isCreative()) {
            SpeedMine.mc.interactionManager.attackBlock(pos, BlockUtil.getClickSide(pos));
            return;
        }
        if (this.isOff()) {
            SpeedMine.mc.interactionManager.attackBlock(pos, BlockUtil.getClickSide(pos));
            return;
        }
        if (pos.equals(breakPos)) {
            return;
        }
        if (godBlocks.contains(SpeedMine.mc.world.getBlockState(pos).getBlock())) {
            return;
        }
        if (breakPos != null && this.preferWeb.getValue() && BlockUtil.getBlock(breakPos) == Blocks.COBWEB) {
            return;
        }
        if (breakPos != null && this.preferHead.getValue() && EntityUtil.getPlayerPos(true).up().equals(breakPos)) {
            return;
        }
        if (BlockUtil.getClickSideStrict(pos) == null) {
            return;
        }
        if ((double)MathHelper.sqrt((float)EntityUtil.getEyesPos().squaredDistanceTo(pos.toCenterPos())) > this.range.getValue()) {
            return;
        }
        breakPos = pos;
        this.breakNumber = 0;
        this.startPacket = false;
        this.mineTimer.reset();
        this.done = false;
        this.animationTime.reset();
        this.skip = true;
    }

    private boolean shouldCrystal() {
        return this.crystal.getValue() && (!this.onlyHeadBomber.getValue() || this.obsidian.isPressed());
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!this.yawStep.getValue()) {
            RotateManager.TrueVec3d(directionVec);
            return true;
        }
        this.sync.reset();
        this.directionVec = directionVec;
        if (HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
        }
        return !this.checkFov.getValue();
    }

    int getTool(BlockPos pos) {
        if (this.hotBar.getValue()) {
            int index = -1;
            float CurrentFastest = 1.0f;
            for (int i = 0; i < 9; ++i) {
                float destroySpeed;
                float digSpeed;
                ItemStack stack = SpeedMine.mc.player.getInventory().getStack(i);
                if (stack == ItemStack.EMPTY || !((digSpeed = (float)EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack)) + (destroySpeed = stack.getMiningSpeedMultiplier(SpeedMine.mc.world.getBlockState(pos))) > CurrentFastest)) continue;
                CurrentFastest = digSpeed + destroySpeed;
                index = i;
            }
            return index;
        }
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        float CurrentFastest = 1.0f;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            float destroySpeed;
            float digSpeed;
            if (entry.getValue().getItem() instanceof AirBlockItem || !((digSpeed = (float)EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, entry.getValue())) + (destroySpeed = entry.getValue().getMiningSpeedMultiplier(SpeedMine.mc.world.getBlockState(pos))) > CurrentFastest)) continue;
            CurrentFastest = digSpeed + destroySpeed;
            slot.set(entry.getKey());
        }
        return slot.get();
    }

    @EventHandler(priority=-200)
    public void onPacketSend(PacketEvent event) {
        if (SpeedMine.nullCheck() || SpeedMine.mc.player.isCreative()) {
            return;
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            if (this.bypassGround.getValue() && !SpeedMine.mc.player.isFallFlying() && breakPos != null && (!this.isAir(breakPos) || secondPos != null) && this.bypassTime.getValue() > 0.0 && MathHelper.sqrt((float)breakPos.toCenterPos().squaredDistanceTo(EntityUtil.getEyesPos())) <= this.range.getValueFloat() + 2.0f) {
                int slot = this.getTool(breakPos);
                if (slot == -1) {
                    slot = SpeedMine.mc.player.getInventory().selectedSlot;
                }
                double breakTime = this.getBreakTime(breakPos, slot) - this.bypassTime.getValue();
                if (secondPos == null) {
                    if (breakTime <= 0.0 || this.mineTimer.passedMs((long)breakTime)) {
                        sendGroundPacket = true;
                        ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(true);
                    }
                } else {
                    double breakTime2 = this.getBreakTime(secondPos, slot) - this.bypassTime.getValue();
                    if (breakTime <= 0.0 || this.mineTimer.passedMs((long)breakTime) || breakTime2 <= 0.0) {
                        sendGroundPacket = true;
                        ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(true);
                    }
                }
            } else {
                sendGroundPacket = false;
            }
            return;
        }
        Object t = event.getPacket();
        if (t instanceof UpdateSelectedSlotC2SPacket packet) {
            if (packet.getSelectedSlot() != this.lastSlot) {
                this.lastSlot = packet.getSelectedSlot();
                if (this.switchReset.getValue()) {
                    this.startPacket = false;
                    this.mineTimer.reset();
                    this.done = false;
                    this.animationTime.reset();
                }
            }
            return;
        }
        if (!(event.getPacket() instanceof PlayerActionC2SPacket)) {
            return;
        }
        if (((PlayerActionC2SPacket)event.getPacket()).getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
            if (breakPos == null || !((PlayerActionC2SPacket)event.getPacket()).getPos().equals(breakPos)) {
                return;
            }
            this.startPacket = true;
        } else if (((PlayerActionC2SPacket)event.getPacket()).getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
            if (breakPos == null || !((PlayerActionC2SPacket)event.getPacket()).getPos().equals(breakPos)) {
                return;
            }
            if (!this.instant.getValue()) {
                this.startPacket = false;
            }
        }
    }

    public final double getBreakTime(BlockPos pos, int slot) {
        return this.getBreakTime(pos, slot, this.damage.getValue());
    }

    public final double getBreakTime(BlockPos pos, int slot, double damage) {
        return (double)(1.0f / this.getBlockStrength(pos, SpeedMine.mc.player.getInventory().getStack(slot)) / 20.0f * 1000.0f) * damage;
    }

    public float getBlockStrength(BlockPos position, ItemStack itemStack) {
        BlockState state = SpeedMine.mc.world.getBlockState(position);
        float hardness = state.getHardness(SpeedMine.mc.world, position);
        if (hardness < 0.0f) {
            return 0.0f;
        }
        return this.getDigSpeed(state, itemStack) / hardness / 30.0f;
    }

    public float getDigSpeed(BlockState state, ItemStack itemStack) {
        boolean inWeb;
        int efficiencyModifier;
        float digSpeed = this.getDestroySpeed(state, itemStack);
        if (digSpeed > 1.0f && (efficiencyModifier = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack)) > 0 && !itemStack.isEmpty()) {
            digSpeed += (float)(StrictMath.pow(efficiencyModifier, 2.0) + 1.0);
        }
        if (SpeedMine.mc.player.hasStatusEffect(StatusEffects.HASTE)) {
            digSpeed *= 1.0f + (float)(SpeedMine.mc.player.getStatusEffect(StatusEffects.HASTE).getAmplifier() + 1) * 0.2f;
        }
        if (SpeedMine.mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            digSpeed *= (switch (SpeedMine.mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1E-4f;
            });
        }
        if (SpeedMine.mc.player.isSubmergedInWater() && !EnchantmentHelper.hasAquaAffinity(SpeedMine.mc.player)) {
            digSpeed /= 5.0f;
        }
        boolean bl = inWeb = HexTech.PLAYER.isInWeb(SpeedMine.mc.player) && SpeedMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.COBWEB;
        if ((!SpeedMine.mc.player.isOnGround() || inWeb) && SpeedMine.INSTANCE.checkGround.getValue() && (!this.smart.getValue() || SpeedMine.mc.player.isFallFlying() || inWeb)) {
            digSpeed /= 5.0f;
        }
        return digSpeed < 0.0f ? 0.0f : digSpeed;
    }

    public float getDestroySpeed(BlockState state, ItemStack itemStack) {
        float destroySpeed = 1.0f;
        if (itemStack != null && !itemStack.isEmpty()) {
            destroySpeed *= itemStack.getMiningSpeedMultiplier(state);
        }
        return destroySpeed;
    }

    private boolean isAir(BlockPos breakPos) {
        return SpeedMine.mc.world.isAir(breakPos) || BlockUtil.getBlock(breakPos) == Blocks.FIRE && BlockUtil.hasCrystal(breakPos);
    }

    static {
        df = new DecimalFormat("0.0");
        godBlocks = Arrays.asList(Blocks.COMMAND_BLOCK, Blocks.LAVA_CAULDRON, Blocks.LAVA, Blocks.WATER_CAULDRON, Blocks.WATER, Blocks.BEDROCK, Blocks.BARRIER, Blocks.END_PORTAL, Blocks.NETHER_PORTAL, Blocks.END_PORTAL_FRAME);
    }

    public enum _uySyZpuFdxDcRzYECOEM {
        General,
        Check,
        Place,
        Rotation,
        Render

    }
}
