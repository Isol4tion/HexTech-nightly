package me.hextech.remapped;

import me.hextech.asm.accessors.IEntityVelocityUpdateS2CPacket;
import me.hextech.asm.accessors.IExplosionS2CPacket;
import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.api.utils.entity.EntityUtil;
import me.hextech.remapped.mod.modules.impl.setting.BypassSetting_RInKGmTQYgWFRhsUOiJP;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

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
        if (nullCheck()) {
            return;
        }
        if (Velocity.mc.player != null && (Velocity.mc.player.isTouchingWater() || Velocity.mc.player.isSubmergedInWater() || Velocity.mc.player.isInLava()) && this.pauseInLiquid.getValue()) {
            return;
        }
        if (this.hitboxpush.getValue()) {
            final EntityStatusS2CPacket packet4 = e.getPacket();
            if (packet4 instanceof EntityStatusS2CPacket) {
                final EntityStatusS2CPacket packet = packet4;
                if (packet.getStatus() == 31) {
                    final Entity getEntity = packet.getEntity(Velocity.mc.world);
                    if (getEntity instanceof final FishingBobberEntity fishHook) {
                        if (fishHook.getHookedEntity() == Velocity.mc.player) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
        if (BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grimvelocity.getValue() && !EntityUtil.isInsideBlock()) {
            return;
        }
        final float h = this.horizontal.getValueFloat() / 100.0f;
        final float v = this.vertical.getValueFloat() / 100.0f;
        if (e.getPacket() instanceof ExplosionS2CPacket) {
            final IExplosionS2CPacket packet2 = e.getPacket();
            packet2.setX(packet2.getX() * h);
            packet2.setY(packet2.getY() * v);
            packet2.setZ(packet2.getZ() * h);
            if (this.noExplosions.getValue()) {
                e.cancel();
            }
            return;
        }
        final EntityVelocityUpdateS2CPacket packet5 = e.getPacket();
        if (packet5 instanceof EntityVelocityUpdateS2CPacket) {
            final EntityVelocityUpdateS2CPacket packet3 = packet5;
            if (packet3.getId() == Velocity.mc.player.getId()) {
                if (this.horizontal.getValue() == 0.0 && this.vertical.getValue() == 0.0) {
                    e.cancel();
                }
                else {
                    ((IEntityVelocityUpdateS2CPacket)packet3).setX((int)(packet3.getVelocityX() * h));
                    ((IEntityVelocityUpdateS2CPacket)packet3).setY((int)(packet3.getVelocityY() * v));
                    ((IEntityVelocityUpdateS2CPacket)packet3).setZ((int)(packet3.getVelocityZ() * h));
                }
            }
        }
    }
}
