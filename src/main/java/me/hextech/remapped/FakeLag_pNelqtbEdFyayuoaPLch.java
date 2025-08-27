package me.hextech.remapped;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import me.hextech.asm.accessors.IEntity;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.FakeLag;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FakeLag_pNelqtbEdFyayuoaPLch
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final HashMap<PlayerEntity, Vec3d> map = new HashMap();
    private final SliderSetting spoof = this.add(new SliderSetting("Spoof", 500.0, 0.0, 5000.0, 1.0));
    private final BooleanSetting ping = this.add(new BooleanSetting("Ping", true));
    private final BooleanSetting entity = this.add(new BooleanSetting("Entity", true));
    private final CopyOnWriteArrayList<FakeLag> packet = new CopyOnWriteArrayList();

    public FakeLag_pNelqtbEdFyayuoaPLch() {
        super("FakeLag", Module_JlagirAibYQgkHtbRnhw.Player);
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        EntityS2CPacket entityS2CPacket;
        Entity entity;
        if (FakeLag_pNelqtbEdFyayuoaPLch.nullCheck()) {
            return;
        }
        if (this.ping.getValue() && (event.getPacket() instanceof CommonPingS2CPacket || event.getPacket() instanceof KeepAliveS2CPacket)) {
            this.packet.add(new FakeLag(this, (Packet)event.getPacket()));
            event.cancel();
            return;
        }
        Object t = event.getPacket();
        if (t instanceof EntityS2CPacket && (entity = (entityS2CPacket = (EntityS2CPacket)t).getEntity((World)FakeLag_pNelqtbEdFyayuoaPLch.mc.world)) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            if (player == FakeLag_pNelqtbEdFyayuoaPLch.mc.player) {
                return;
            }
            if (map.containsKey(player) && this.entity.getValue()) {
                Vec3d vec3d = new Vec3d((double)entityS2CPacket.getDeltaX(), (double)entityS2CPacket.getDeltaY(), (double)entityS2CPacket.getDeltaZ());
                if (map.get(player).distanceTo(FakeLag_pNelqtbEdFyayuoaPLch.mc.player.method_19538()) < vec3d.distanceTo(FakeLag_pNelqtbEdFyayuoaPLch.mc.player.method_19538())) {
                    this.packet.add(new FakeLag(this, (Packet)entityS2CPacket));
                    event.cancel();
                }
            }
            map.put(player, player.method_19538());
        }
    }

    @Override
    public void onUpdate() {
        this.update();
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        this.update();
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        this.update();
        if (this.entity.getValue()) {
            for (Vec3d vec3d : map.values()) {
                Color color = new Color(255, 255, 255, 100);
                Render3DUtil.draw3DBox(matrixStack, ((IEntity)FakeLag_pNelqtbEdFyayuoaPLch.mc.player).getDimensions().getBoxAt(vec3d).expand(0.0, 0.1, 0.0), color, false, true);
            }
        }
    }

    @Override
    public void onDisable() {
        if (FakeLag_pNelqtbEdFyayuoaPLch.nullCheck()) {
            this.packet.clear();
            return;
        }
        for (FakeLag p : this.packet) {
            p.apply();
        }
    }

    private void update() {
        if (FakeLag_pNelqtbEdFyayuoaPLch.nullCheck()) {
            this.packet.clear();
            return;
        }
        this.packet.removeIf(FakeLag::send);
    }
}
