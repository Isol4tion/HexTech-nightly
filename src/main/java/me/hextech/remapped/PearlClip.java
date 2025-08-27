package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.AutoPearl;
import me.hextech.remapped.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PearlClip
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static PearlClip INSTANCE;
    public static float lastYaw;
    public static float lastPitch;
    public final BooleanSetting autoYaw = this.add(new BooleanSetting("AutoYaw", true));
    public final BooleanSetting bypass = this.add(new BooleanSetting("Bypass", true));
    private final BooleanSetting rotation = this.add(new BooleanSetting("Rotation", false));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("checkFov", false));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 10, 0, 50));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 100, 0, 100));
    private final BooleanSetting sync = this.add(new BooleanSetting("Sync", true));
    public Vec3d directionVec = null;
    Vec3d targetPos;

    public PearlClip() {
        super("PearlClip", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.directionVec != null) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    private boolean faceVector(Vec3d directionVec) {
        this.directionVec = directionVec;
        float[] angle = EntityUtil.getLegitRotations(directionVec);
        if (Math.abs(MathHelper.wrapDegrees((float)(angle[0] - lastYaw))) < this.fov.getValueFloat() && Math.abs(MathHelper.wrapDegrees((float)(angle[1] - lastPitch))) < this.fov.getValueFloat()) {
            if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.rotatepacket.getValue()) {
                EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            }
            return true;
        }
        return !this.checkFov.getValue();
    }

    private void updatePos() {
        this.targetPos = new Vec3d(PearlClip.mc.player.method_23317() + MathHelper.clamp((double)(this.roundToClosest(PearlClip.mc.player.method_23317(), Math.floor(PearlClip.mc.player.method_23317()) + 0.241, Math.floor(PearlClip.mc.player.method_23317()) + 0.759) - PearlClip.mc.player.method_23317()), (double)-0.2, (double)0.2), PearlClip.mc.player.method_23318() - 0.5, PearlClip.mc.player.method_23321() + MathHelper.clamp((double)(this.roundToClosest(PearlClip.mc.player.method_23321(), Math.floor(PearlClip.mc.player.method_23321()) + 0.241, Math.floor(PearlClip.mc.player.method_23321()) + 0.759) - PearlClip.mc.player.method_23321()), (double)-0.2, (double)0.2));
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        this.updatePos();
        if (!this.faceVector(this.targetPos)) {
            return;
        }
        if (this.sync.getValue()) {
            AutoPearl.INSTANCE.throwPearl(this.autoYaw.getValue() ? HexTech.ROTATE.getRotation(this.targetPos)[0] : PearlClip.mc.player.method_36454(), this.bypass.getValue() ? 89.0f : 80.0f);
        } else {
            this.throwPearl();
        }
        this.disable();
    }

    @Override
    public void onUpdate() {
        this.updatePos();
        if (!this.faceVector(this.targetPos)) {
            return;
        }
        this.throwPearl();
        this.disable();
    }

    public void throwPearl() {
        int pearl;
        AutoPearl.throwing = true;
        if (PearlClip.mc.player.method_6047().getItem() == Items.ENDER_PEARL) {
            PearlClip.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
        } else if (AutoPearl.INSTANCE.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlot(Items.ENDER_PEARL)) != -1) {
            InventoryUtil.inventorySwap(pearl, PearlClip.mc.player.method_31548().selectedSlot);
            PearlClip.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
            InventoryUtil.inventorySwap(pearl, PearlClip.mc.player.method_31548().selectedSlot);
            EntityUtil.syncInventory();
        } else {
            pearl = InventoryUtil.findItem(Items.ENDER_PEARL);
            if (pearl != -1) {
                int old = PearlClip.mc.player.method_31548().selectedSlot;
                InventoryUtil.switchToSlot(pearl);
                PearlClip.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
                InventoryUtil.switchToSlot(old);
            }
        }
        AutoPearl.throwing = false;
    }

    @Override
    public void onEnable() {
        this.directionVec = null;
        if (PearlClip.nullCheck()) {
            this.disable();
            return;
        }
        this.updatePos();
        if (this.rotation.getValue()) {
            return;
        }
        if (this.sync.getValue()) {
            AutoPearl.INSTANCE.throwPearl(this.autoYaw.getValue() ? HexTech.ROTATE.getRotation(this.targetPos)[0] : PearlClip.mc.player.method_36454(), this.bypass.getValue() ? 89.0f : 80.0f);
        } else {
            this.throwPearl();
        }
        this.disable();
    }

    private double roundToClosest(double num, double low, double high) {
        double d2 = high - num;
        double d1 = num - low;
        if (d2 > d1) {
            return low;
        }
        return high;
    }
}
