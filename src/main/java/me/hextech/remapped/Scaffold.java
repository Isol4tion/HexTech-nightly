package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.remapped.AnimateUtil;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.HoleSnap;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Scaffold
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static Vec3d lastVec3d;
    public final SliderSetting rotateTime = this.add(new SliderSetting("KeepRotate", 1000.0, 0.0, 3000.0, 10.0));
    private final BooleanSetting tower = this.add(new BooleanSetting("Tower", true));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", false));
    private final BooleanSetting render = this.add(new BooleanSetting("Render", true).setParent());
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100), v -> this.render.isOpen()));
    public final SliderSetting sliderSpeed = this.add(new SliderSetting("SliderSpeed", 0.2, 0.01, 1.0, 0.01, v -> this.render.isOpen()));
    private final BooleanSetting esp = this.add(new BooleanSetting("ESP", true, v -> this.render.isOpen()));
    private final BooleanSetting box = this.add(new BooleanSetting("Box", true, v -> this.render.isOpen()));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true, v -> this.render.isOpen()));
    private final Timer timer = new Timer();
    private final Timer towerTimer = new Timer();
    private float[] angle = null;
    private BlockPos pos;

    public Scaffold() {
        super("Scaffold", Module_JlagirAibYQgkHtbRnhw.Player);
    }

    @EventHandler(priority=100)
    public void onRotation(RotateEvent event) {
        if (this.rotate.getValue() && !this.timer.passedMs(this.rotateTime.getValueInt()) && this.angle != null) {
            event.setYaw(this.angle[0]);
            event.setPitch(this.angle[1]);
        }
    }

    @Override
    public void onEnable() {
        lastVec3d = null;
        this.pos = null;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (this.render.getValue()) {
            if (this.esp.getValue()) {
                GL11.glEnable((int)3042);
                double temp = 0.01;
                for (double i = 0.0; i < 0.8; i += temp) {
                    HoleSnap.doCircle(matrixStack, ColorUtil.injectAlpha(this.color.getValue(), (int)Math.min((double)(this.color.getValue().getAlpha() * 2) / (0.8 / temp), 255.0)), i, new Vec3d(MathUtil.interpolate(Scaffold.mc.player.field_6038, Scaffold.mc.player.method_23317(), partialTicks), MathUtil.interpolate(Scaffold.mc.player.field_5971, Scaffold.mc.player.method_23318(), partialTicks), MathUtil.interpolate(Scaffold.mc.player.field_5989, Scaffold.mc.player.method_23321(), partialTicks)), 5);
                }
                RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glDisable((int)3042);
            }
            if (this.pos != null) {
                Vec3d cur = this.pos.toCenterPos();
                lastVec3d = lastVec3d == null ? cur : new Vec3d(AnimateUtil.animate(lastVec3d.method_10216(), cur.x, this.sliderSpeed.getValue()), AnimateUtil.animate(lastVec3d.method_10214(), cur.y, this.sliderSpeed.getValue()), AnimateUtil.animate(lastVec3d.method_10215(), cur.z, this.sliderSpeed.getValue()));
                Render3DUtil.draw3DBox(matrixStack, new Box(lastVec3d.add(0.5, 0.5, 0.5), lastVec3d.add(-0.5, -0.5, -0.5)), ColorUtil.injectAlpha(this.color.getValue(), this.color.getValue().getAlpha()), this.outline.getValue(), this.box.getValue());
            }
        }
    }

    @Override
    public void onUpdate() {
        int block = InventoryUtil.findBlock();
        if (block == -1) {
            return;
        }
        BlockPos placePos = EntityUtil.getPlayerPos().down();
        if (BlockUtil.clientCanPlace(placePos, false)) {
            int old = Scaffold.mc.player.method_31548().selectedSlot;
            if (BlockUtil.getPlaceSide(placePos) == null) {
                double distance = 1000.0;
                BlockPos bestPos = null;
                for (Direction i : Direction.values()) {
                    if (i == Direction.UP || !BlockUtil.canPlace(placePos.offset(i)) || bestPos != null && !(Scaffold.mc.player.method_5707(placePos.offset(i).toCenterPos()) < distance)) continue;
                    bestPos = placePos.offset(i);
                    distance = Scaffold.mc.player.method_5707(placePos.offset(i).toCenterPos());
                }
                if (bestPos != null) {
                    placePos = bestPos;
                } else {
                    return;
                }
            }
            if (this.rotate.getValue()) {
                Direction side = BlockUtil.getPlaceSide(placePos);
                this.angle = EntityUtil.getLegitRotations(placePos.offset(side).toCenterPos().add((double)side.getOpposite().method_10163().getX() * 0.5, (double)side.getOpposite().method_10163().getY() * 0.5, (double)side.getOpposite().method_10163().getZ() * 0.5));
                this.timer.reset();
            }
            InventoryUtil.switchToSlot(block);
            BlockUtil.placeBlock(placePos, this.rotate.getValue(), false);
            InventoryUtil.switchToSlot(old);
            this.pos = placePos;
            if (this.tower.getValue() && Scaffold.mc.options.jumpKey.isPressed() && !MovementUtil.isMoving()) {
                MovementUtil.setMotionY(0.42);
                MovementUtil.setMotionX(0.0);
                MovementUtil.setMotionZ(0.0);
                if (this.towerTimer.passedMs(1500L)) {
                    MovementUtil.setMotionY(-0.28);
                    this.towerTimer.reset();
                }
            } else {
                this.towerTimer.reset();
            }
        }
    }
}
