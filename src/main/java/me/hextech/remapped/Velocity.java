package me.hextech.remapped;

import me.hextech.asm.accessors.IEntityVelocityUpdateS2CPacket;
import me.hextech.asm.accessors.IExplosionS2CPacket;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

public class Velocity extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
      if (!nullCheck()) {
         if (mc.field_1724 == null
            || !mc.field_1724.method_5799() && !mc.field_1724.method_5869() && !mc.field_1724.method_5771()
            || !this.pauseInLiquid.getValue()) {
            if (this.hitboxpush.getValue()
               && e.getPacket() instanceof EntityStatusS2CPacket packet
               && packet.method_11470() == 31
               && packet.method_11469(mc.field_1687) instanceof FishingBobberEntity fishHook
               && fishHook.method_26957() == mc.field_1724) {
               e.setCancelled(true);
            }

            if (!BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.grimvelocity.getValue() || EntityUtil.isInsideBlock()) {
               float h = this.horizontal.getValueFloat() / 100.0F;
               float v = this.vertical.getValueFloat() / 100.0F;
               if (e.getPacket() instanceof ExplosionS2CPacket) {
                  IExplosionS2CPacket packet = e.getPacket();
                  packet.setX(packet.getX() * h);
                  packet.setY(packet.getY() * v);
                  packet.setZ(packet.getZ() * h);
                  if (this.noExplosions.getValue()) {
                     e.cancel();
                  }
               } else {
                  if (e.getPacket() instanceof EntityVelocityUpdateS2CPacket packet && packet.method_11818() == mc.field_1724.method_5628()) {
                     if (this.horizontal.getValue() == 0.0 && this.vertical.getValue() == 0.0) {
                        e.cancel();
                     } else {
                        ((IEntityVelocityUpdateS2CPacket)packet).setX((int)((float)packet.method_11815() * h));
                        ((IEntityVelocityUpdateS2CPacket)packet).setY((int)((float)packet.method_11816() * v));
                        ((IEntityVelocityUpdateS2CPacket)packet).setZ((int)((float)packet.method_11819() * h));
                     }
                  }
               }
            }
         }
      }
   }
}
