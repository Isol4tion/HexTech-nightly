package me.hextech.remapped;

import me.hextech.asm.accessors.IEntityVelocityUpdateS2CPacket;
import me.hextech.asm.accessors.IExplosionS2CPacket;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.BypassSetting_RInKGmTQYgWFRhsUOiJP;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.SliderSetting;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.world.World;

public class Velocity
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Velocity INSTANCE;
    public final BooleanSetting noExplosions = this.add(new BooleanSetting("Explosions", false));
    public final BooleanSetting waterPush = this.add(new BooleanSetting("Water", true));
    public final BooleanSetting pauseInLiquid = this.add(new BooleanSetting("PauseInLiquid", false));
    public final BooleanSetting entityPush = this.add(new BooleanSetting("EntityPush", true));
    public final BooleanSetting blockPush = this.add(new BooleanSetting("BlockPush", true));
    public final BooleanSetting hitboxpush = this.add(new BooleanSetting("HitBoxPush", true));
    private final SliderSetting horizontal = this.add(new SliderSetting("Horizontal", 0.0, 0.0, 100.0, 1.0));
    private final SliderSetting vertical = this.add(new SliderSetting("Vertical", 0.0, 0.0, 100.0, 1.0));

    public Velocity() {
        super("Velocity", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.horizontal.getValueInt() + "%, " + this.vertical.getValueInt() + "%";
    }

    @EventHandler
    public void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu e) {
        IExplosionS2CPacket packet;
        FishingBobberEntity fishHook;
        EntityStatusS2CPacket packet2;
        Object object;
        if (Velocity.nullCheck()) {
            return;
        }
        if (Velocity.mc.player != null && (Velocity.mc.player.method_5799() || Velocity.mc.player.method_5869() || Velocity.mc.player.method_5771()) && this.pauseInLiquid.getValue()) {
            return;
        }
        if (this.hitboxpush.getValue() && (object = e.getPacket()) instanceof EntityStatusS2CPacket && (packet2 = (EntityStatusS2CPacket)object).getStatus() == 31 && (object = packet2.getEntity((World)Velocity.mc.world)) instanceof FishingBobberEntity && (fishHook = (FishingBobberEntity)object).getHookedEntity() == Velocity.mc.player) {
            e.setCancelled(true);
        }
        if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grimvelocity.getValue() && !EntityUtil.isInsideBlock()) {
            return;
        }
        float h = this.horizontal.getValueFloat() / 100.0f;
        float v = this.vertical.getValueFloat() / 100.0f;
        if (e.getPacket() instanceof ExplosionS2CPacket) {
            packet = (IExplosionS2CPacket)e.getPacket();
            packet.setX(packet.getX() * h);
            packet.setY(packet.getY() * v);
            packet.setZ(packet.getZ() * h);
            if (this.noExplosions.getValue()) {
                e.cancel();
            }
            return;
        }
        Object t = e.getPacket();
        if (t instanceof EntityVelocityUpdateS2CPacket && (packet = (EntityVelocityUpdateS2CPacket)t).getEntityId() == Velocity.mc.player.getId()) {
            if (this.horizontal.getValue() == 0.0 && this.vertical.getValue() == 0.0) {
                e.cancel();
            } else {
                ((IEntityVelocityUpdateS2CPacket)((Object)packet)).setX((int)((float)packet.method_11815() * h));
                ((IEntityVelocityUpdateS2CPacket)((Object)packet)).setY((int)((float)packet.method_11816() * v));
                ((IEntityVelocityUpdateS2CPacket)((Object)packet)).setZ((int)((float)packet.method_11819() * h));
            }
        }
    }
}
