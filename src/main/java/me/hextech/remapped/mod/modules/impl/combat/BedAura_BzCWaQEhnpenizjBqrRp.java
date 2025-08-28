package me.hextech.remapped.mod.modules.impl.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import me.hextech.HexTech;
import me.hextech.remapped.*;
import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.api.utils.combat.CombatUtil;
import me.hextech.remapped.api.utils.entity.EntityUtil;
import me.hextech.remapped.api.utils.render.ColorUtil;
import me.hextech.remapped.api.utils.render.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.api.utils.world.ExplosionUtil;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.mod.modules.settings.impl.ColorSetting;
import me.hextech.remapped.mod.modules.settings.impl.EnumSetting;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BedAura_BzCWaQEhnpenizjBqrRp
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static BedAura_BzCWaQEhnpenizjBqrRp INSTANCE;
    public static BlockPos placePos;
    public final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    public final EnumSetting<BedMod> mode = this.add(new EnumSetting<BedMod>("BedMod", BedMod.NullPoint));
    private final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting noUsing = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Page.General));
    private final EnumSetting<AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_IKgLeKHCELPvcpdGlLhV> calcMode = this.add(new EnumSetting<AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_IKgLeKHCELPvcpdGlLhV>("CalcMode", AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_IKgLeKHCELPvcpdGlLhV.OyVey, v -> this.page.getValue() == Page.General));
    private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.Server, v -> this.page.getValue() == Page.General));
    private final SliderSetting antiSuicide = this.add(new SliderSetting("AntiSuicide", 3.0, 0.0, 10.0, v -> this.page.getValue() == Page.General));
    private final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 12.0, 0.0, 20.0, v -> this.page.getValue() == Page.General));
    private final SliderSetting updateDelay = this.add(new SliderSetting("UpdateDelay", 50, 0, 1000, v -> this.page.getValue() == Page.General));
    private final SliderSetting calcDelay = this.add(new SliderSetting("CalcDelay", 200, 0, 1000, v -> this.page.getValue() == Page.General));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true, v -> this.page.getValue() == Page.Rotate).setParent());
    private final BooleanSetting newRotate = this.add(new BooleanSetting("NewRotate", false, v -> this.rotate.isOpen() && this.page.getValue() == Page.Rotate));
    private final SliderSetting yawStep = this.add(new SliderSetting("YawStep", 0.3f, 0.1f, 1.0, 0.01f, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == Page.Rotate));
    private final BooleanSetting random = this.add(new BooleanSetting("Random", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == Page.Rotate));
    private final BooleanSetting sync = this.add(new BooleanSetting("Sync", false, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == Page.Rotate));
    private final BooleanSetting checkLook = this.add(new BooleanSetting("CheckLook", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == Page.Rotate));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.checkLook.getValue() && this.page.getValue() == Page.Rotate));
    private final BooleanSetting place = this.add(new BooleanSetting("Place", true, v -> this.page.getValue() == Page.Calc));
    private final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 300, 0, 1000, v -> this.page.getValue() == Page.Calc && this.place.getValue()));
    private final BooleanSetting Break = this.add(new BooleanSetting("Breaks", true, v -> this.page.getValue() == Page.Calc));
    private final SliderSetting breakDelay = this.add(new SliderSetting("BreakDelay", 300, 0, 1000, v -> this.page.getValue() == Page.Calc && this.Break.getValue()));
    private final BooleanSetting breakOnlyHasCrystal = this.add(new BooleanSetting("OnlyHasBed", false, v -> this.page.getValue() == Page.Calc && this.Break.getValue()));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0, v -> this.page.getValue() == Page.Calc));
    private final SliderSetting placeMinDamage = this.add(new SliderSetting("MinDamage", 5.0, 0.0, 36.0, v -> this.page.getValue() == Page.Calc));
    private final SliderSetting placeMaxSelf = this.add(new SliderSetting("MaxSelfDamage", 12.0, 0.0, 36.0, v -> this.page.getValue() == Page.Calc));
    private final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, v -> this.page.getValue() == Page.Calc));
    private final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == Page.Render));
    private final BooleanSetting shrink = this.add(new BooleanSetting("Shrink", true, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true, v -> this.page.getValue() == Page.Render && this.render.getValue()).setParent());
    private final SliderSetting outlineAlpha = this.add(new SliderSetting("OutlineAlpha", 150, 0, 255, v -> this.outline.isOpen() && this.page.getValue() == Page.Render && this.render.getValue()));
    private final BooleanSetting box = this.add(new BooleanSetting("Box", true, v -> this.page.getValue() == Page.Render && this.render.getValue()).setParent());
    private final SliderSetting boxAlpha = this.add(new SliderSetting("BoxAlpha", 70, 0, 255, v -> this.box.isOpen() && this.page.getValue() == Page.Render && this.render.getValue()));
    private final BooleanSetting reset = this.add(new BooleanSetting("Reset", true, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255), v -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final SliderSetting animationTime = this.add(new SliderSetting("AnimationTime", 2.0, 0.0, 8.0, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final SliderSetting startFadeTime = this.add(new SliderSetting("StartFadeTime", 0.3, 0.0, 2.0, 0.01, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final SliderSetting fadeTime = this.add(new SliderSetting("FadeTime", 0.3, 0.0, 2.0, 0.01, v -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", 4, 0, 10, v -> this.page.getValue() == Page.Predict));
    private final BooleanSetting terrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true, v -> this.page.getValue() == Page.Predict));
    private final Timer delayTimer = new Timer();
    private final Timer calcTimer = new Timer();
    private final Timer breakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer noPosTimer = new Timer();
    private final FadeUtils_DPfHthPqEJdfXfNYhDbG fadeUtils = new FadeUtils_DPfHthPqEJdfXfNYhDbG(500L);
    private final FadeUtils_DPfHthPqEJdfXfNYhDbG animation = new FadeUtils_DPfHthPqEJdfXfNYhDbG(500L);
    public float lastDamage;
    public Vec3d directionVec = null;
    double lastSize = 0.0;
    private PlayerEntity displayTarget;
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;
    private BlockPos renderPos = null;
    private Box lastBB = null;
    private Box nowBB = null;

    public BedAura_BzCWaQEhnpenizjBqrRp() {
        super("BedAura", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        if (this.displayTarget != null && placePos != null) {
            return this.displayTarget.getName().getString();
        }
        return super.getInfo();
    }

    @Override
    public void onEnable() {
        this.lastYaw = HexTech.ROTATE.lastYaw;
        this.lastPitch = RotateManager.lastPitch;
    }

    @EventHandler
    public void onRotate(RotateEvent event) {
        if (placePos != null && this.newRotate.getValue() && this.directionVec != null) {
            float[] newAngle = this.injectStep(EntityUtil.getLegitRotations(this.directionVec), this.yawStep.getValueFloat());
            this.lastYaw = newAngle[0];
            this.lastPitch = newAngle[1];
            if (this.random.getValue() && new Random().nextBoolean()) {
                this.lastPitch = Math.min(new Random().nextFloat() * 2.0f + this.lastPitch, 90.0f);
            }
            event.setYaw(this.lastYaw);
            event.setPitch(this.lastPitch);
        } else {
            this.lastYaw = HexTech.ROTATE.lastYaw;
            this.lastPitch = RotateManager.lastPitch;
        }
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        this.update();
    }

    @Override
    public void onUpdate() {
        this.update();
    }

    private void update() {
        if (BedAura_BzCWaQEhnpenizjBqrRp.nullCheck()) {
            return;
        }
        this.animUpdate();
        if (!this.delayTimer.passedMs((long)this.updateDelay.getValue())) {
            return;
        }
        if (this.noUsing.getValue() && EntityUtil.isUsing()) {
            placePos = null;
            return;
        }
        if (BedAura_BzCWaQEhnpenizjBqrRp.mc.player.isSneaking()) {
            placePos = null;
            return;
        }
        if (BedAura_BzCWaQEhnpenizjBqrRp.mc.world.getRegistryKey().equals(World.OVERWORLD)) {
            placePos = null;
            return;
        }
        if (this.breakOnlyHasCrystal.getValue() && this.getBed() == -1) {
            placePos = null;
            return;
        }
        this.delayTimer.reset();
        if (this.calcTimer.passedMs(this.calcDelay.getValueInt())) {
            this.calcTimer.reset();
            placePos = null;
            this.lastDamage = 0.0f;
            ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS> list = new ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS>();
            for (PlayerEntity target : CombatUtil.getEnemies(this.targetRange.getRange())) {
                list.add(new PredictionSetting._XBpBEveLWEKUGQPHCCIS(target));
            }
            PredictionSetting._XBpBEveLWEKUGQPHCCIS self = new PredictionSetting._XBpBEveLWEKUGQPHCCIS(BedAura_BzCWaQEhnpenizjBqrRp.mc.player);
            for (BlockPos pos : BlockUtil.getSphere((float)this.range.getValue())) {
                if (!this.canPlaceBed(pos) && !(BlockUtil.getBlock(pos) instanceof BedBlock)) continue;
                for (PredictionSetting._XBpBEveLWEKUGQPHCCIS pap : list) {
                    float damage = this.calculateDamage(pos, pap.player, pap.predict);
                    float selfDamage = this.calculateDamage(pos, self.player, self.predict);
                    if ((double)selfDamage > this.placeMaxSelf.getValue() || this.antiSuicide.getValue() > 0.0 && (double)selfDamage > (double)(BedAura_BzCWaQEhnpenizjBqrRp.mc.player.getHealth() + BedAura_BzCWaQEhnpenizjBqrRp.mc.player.getAbsorptionAmount()) - this.antiSuicide.getValue() || damage < EntityUtil.getHealth(pap.player) && (damage < this.placeMinDamage.getValueFloat() || this.smart.getValue() && damage < selfDamage) || placePos != null && !(damage > this.lastDamage)) continue;
                    this.displayTarget = pap.player;
                    placePos = pos;
                    this.lastDamage = damage;
                }
            }
        }
        if (placePos != null) {
            this.doBed(placePos);
        }
    }

    public void doBed(BlockPos pos) {
        switch (this.mode.getValue().ordinal()) {
            case 0: {
                if (this.canPlaceBed(pos) && !(BlockUtil.getBlock(pos) instanceof BedBlock)) {
                    if (this.getBed() == -1) break;
                    this.doPlace(pos);
                    break;
                }
                this.doBreak(pos);
                break;
            }
            case 1: {
                this.doBreak(pos);
                this.doPlace(pos);
                this.doBreak(pos);
            }
        }
    }

    private void doBreak(BlockPos pos) {
        if (!this.Break.getValue()) {
            return;
        }
        if (BedAura_BzCWaQEhnpenizjBqrRp.mc.world.getBlockState(pos).getBlock() instanceof BedBlock) {
            Direction side = BlockUtil.getClickSide(pos);
            Vec3d directionVec = new Vec3d((double)pos.getX() + 0.5 + (double)side.getVector().getX() * 0.5, (double)pos.getY() + 0.5 + (double)side.getVector().getY() * 0.5, (double)pos.getZ() + 0.5 + (double)side.getVector().getZ() * 0.5);
            if (this.rotate.getValue() && !this.faceVector(directionVec)) {
                return;
            }
            if (!this.breakTimer.passedMs((long)this.breakDelay.getValue())) {
                return;
            }
            this.breakTimer.reset();
            EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
            BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
            BedAura_BzCWaQEhnpenizjBqrRp.mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, BlockUtil.getWorldActionId(BedAura_BzCWaQEhnpenizjBqrRp.mc.world)));
        }
    }

    private void doPlace(BlockPos pos) {
        if (!this.place.getValue()) {
            return;
        }
        int bedSlot = this.getBed();
        if (bedSlot == -1) {
            placePos = null;
            return;
        }
        int oldSlot = BedAura_BzCWaQEhnpenizjBqrRp.mc.player.getInventory().selectedSlot;
        Direction facing = null;
        for (Direction i : Direction.values()) {
            if (i == Direction.UP || i == Direction.DOWN || !BlockUtil.clientCanPlace(pos.offset(i), false) || !BlockUtil.canClick(pos.offset(i).down()) || this.checkMine.getValue() && BlockUtil.isMining(pos.offset(i))) continue;
            facing = i;
            break;
        }
        if (facing != null) {
            Vec3d directionVec = new Vec3d((double)pos.getX() + 0.5 + (double)Direction.UP.getVector().getX() * 0.5, (double)pos.getY() + 0.5 + (double)Direction.UP.getVector().getY() * 0.5, (double)pos.getZ() + 0.5 + (double)Direction.UP.getVector().getZ() * 0.5);
            if (this.rotate.getValue() && !this.faceVector(directionVec)) {
                return;
            }
            if (!this.placeTimer.passedMs((long)this.placeDelay.getValue())) {
                return;
            }
            this.placeTimer.reset();
            this.doSwap(bedSlot);
            if (this.yawDeceive.getValue()) {
                HoleKickTest.pistonFacing(facing.getOpposite());
            }
            BlockUtil.clickBlock(pos.offset(facing).down(), Direction.UP, false);
            if (this.rotate.getValue() && this.sync.getValue()) {
                EntityUtil.faceVector(directionVec);
            }
            if (this.inventorySwap.getValue()) {
                this.doSwap(bedSlot);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(oldSlot);
            }
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        double quad;
        this.update();
        double d = quad = this.noPosTimer.passedMs(this.startFadeTime.getValue() * 1000.0) ? this.fadeUtils.easeOutQuad() : 0.0;
        if (this.nowBB != null && this.render.getValue() && quad < 1.0) {
            Box bb = this.nowBB;
            if (this.shrink.getValue()) {
                bb = this.nowBB.shrink(quad * 0.5, quad * 0.5, quad * 0.5);
                bb = bb.shrink(-quad * 0.5, -quad * 0.5, -quad * 0.5);
            }
            if (this.box.getValue()) {
                Render3DUtil.drawFill(matrixStack, bb, ColorUtil.injectAlpha(this.color.getValue(), (int)(this.boxAlpha.getValue() * Math.abs(quad - 1.0))));
            }
            if (this.outline.getValue()) {
                Render3DUtil.drawBox(matrixStack, bb, ColorUtil.injectAlpha(this.color.getValue(), (int)(this.outlineAlpha.getValue() * Math.abs(quad - 1.0))));
            }
        } else if (this.reset.getValue()) {
            this.nowBB = null;
        }
    }

    private void animUpdate() {
        this.fadeUtils.setLength((long)(this.fadeTime.getValue() * 1000.0));
        if (placePos != null) {
            this.lastBB = new Box(placePos);
            this.noPosTimer.reset();
            if (this.nowBB == null) {
                this.nowBB = this.lastBB;
            }
            if (this.renderPos == null || !this.renderPos.equals(placePos)) {
                this.animation.setLength(this.animationTime.getValue() * 1000.0 <= 0.0 ? 0L : (long)(Math.abs(this.nowBB.minX - this.lastBB.minX) + Math.abs(this.nowBB.minY - this.lastBB.minY) + Math.abs(this.nowBB.minZ - this.lastBB.minZ) <= 5.0 ? (double)((long)((Math.abs(this.nowBB.minX - this.lastBB.minX) + Math.abs(this.nowBB.minY - this.lastBB.minY) + Math.abs(this.nowBB.minZ - this.lastBB.minZ)) * (this.animationTime.getValue() * 1000.0))) : this.animationTime.getValue() * 5000.0));
                this.animation.reset();
                this.renderPos = placePos;
            }
        }
        if (!this.noPosTimer.passedMs((long)(this.startFadeTime.getValue() * 1000.0))) {
            this.fadeUtils.reset();
        }
        double size = this.animation.easeOutQuad();
        if (this.nowBB != null && this.lastBB != null) {
            if (Math.abs(this.nowBB.minX - this.lastBB.minX) + Math.abs(this.nowBB.minY - this.lastBB.minY) + Math.abs(this.nowBB.minZ - this.lastBB.minZ) > 16.0) {
                this.nowBB = this.lastBB;
            }
            if (this.lastSize != size) {
                this.nowBB = new Box(this.nowBB.minX + (this.lastBB.minX - this.nowBB.minX) * size, this.nowBB.minY + (this.lastBB.minY - this.nowBB.minY) * size, this.nowBB.minZ + (this.lastBB.minZ - this.nowBB.minZ) * size, this.nowBB.maxX + (this.lastBB.maxX - this.nowBB.maxX) * size, this.nowBB.maxY + (this.lastBB.maxY - this.nowBB.maxY) * size, this.nowBB.maxZ + (this.lastBB.maxZ - this.nowBB.maxZ) * size);
                this.lastSize = size;
            }
        }
    }

    public int getBed() {
        return this.inventorySwap.getValue() ? InventoryUtil.findClassInventorySlot(BedItem.class) : InventoryUtil.findClass(BedItem.class);
    }

    private void doSwap(int slot) {
        if (this.inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, BedAura_BzCWaQEhnpenizjBqrRp.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public float calculateDamage(BlockPos pos, PlayerEntity player, PlayerEntity predict) {
        CombatUtil.modifyPos = pos;
        CombatUtil.modifyBlockState = Blocks.AIR.getDefaultState();
        float damage = this.calculateDamage(pos.toCenterPos(), player, predict);
        CombatUtil.modifyPos = null;
        return damage;
    }

    public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        if (this.terrainIgnore.getValue()) {
            CombatUtil.terrainIgnore = true;
        }
        float damage = 0.0f;
        switch (this.calcMode.getValue()) {
            case Meteor: {
                damage = (float)MeteorExplosionUtil.crystalDamage(player, pos, predict);
                break;
            }
            case Thunder: {
                damage = ThunderExplosionUtil.calculateDamage(pos, player, predict, 6.0f);
                break;
            }
            case OyVey: {
                damage = OyveyExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), player, predict, 6.0f);
                break;
            }
            case EditionHex: {
                damage = ExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), player, predict, 6.0f);
            }
        }
        CombatUtil.terrainIgnore = false;
        return damage;
    }

    private boolean canPlaceBed(BlockPos pos) {
        if (!(!BlockUtil.canReplace(pos) || this.checkMine.getValue() && BlockUtil.isMining(pos))) {
            for (Direction i : Direction.values()) {
                if (i == Direction.UP || i == Direction.DOWN || !BlockUtil.isStrictDirection(pos.offset(i).down(), Direction.UP) || !this.isTrueFacing(pos.offset(i), i.getOpposite()) || !BlockUtil.clientCanPlace(pos.offset(i), false) || !BlockUtil.canClick(pos.offset(i).down()) || this.checkMine.getValue() && BlockUtil.isMining(pos.offset(i))) continue;
                return true;
            }
        }
        return false;
    }

    private boolean isTrueFacing(BlockPos pos, Direction facing) {
        if (this.yawDeceive.getValue()) {
            return true;
        }
        Vec3d hitVec = pos.toCenterPos().add(new Vec3d(0.0, -0.5, 0.0));
        return Direction.fromRotation(EntityUtil.getLegitRotations(hitVec)[0]) == facing;
    }

    public boolean faceVector(Vec3d directionVec) {
        if (!this.newRotate.getValue()) {
            EntityUtil.faceVector(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        float[] angle = EntityUtil.getLegitRotations(directionVec);
        if (Math.abs(MathHelper.wrapDegrees(angle[0] - this.lastYaw)) < this.fov.getValueFloat() && Math.abs(MathHelper.wrapDegrees(angle[1] - this.lastPitch)) < this.fov.getValueFloat()) {
            if (this.sync.getValue()) {
                EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            }
            return true;
        }
        return !this.checkLook.getValue();
    }

    private float[] injectStep(float[] angle, float steps) {
        if (steps < 0.01f) {
            steps = 0.01f;
        }
        if (steps > 1.0f) {
            steps = 1.0f;
        }
        if (steps < 1.0f && angle != null) {
            float packetPitch;
            float packetYaw = this.lastYaw;
            float diff = MathHelper.wrapDegrees(angle[0] - this.lastYaw);
            if (Math.abs(diff) > 90.0f * steps) {
                angle[0] = packetYaw + diff * (90.0f * steps / Math.abs(diff));
            }
            if (Math.abs(diff = angle[1] - (packetPitch = this.lastPitch)) > 90.0f * steps) {
                angle[1] = packetPitch + diff * (90.0f * steps / Math.abs(diff));
            }
        }
        return new float[]{angle[0], angle[1]};
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Page {
        General,
        Rotate,
        Calc,
        Predict,
        Render

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum BedMod {
        NullPoint,
        Scanner

    }
}
