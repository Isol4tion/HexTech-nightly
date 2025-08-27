package me.hextech.remapped;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import me.hextech.asm.accessors.IEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.util.math.Vec3d;

public class FakeLag_pNelqtbEdFyayuoaPLch extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
      if (!nullCheck()) {
         if (!this.ping.getValue() || !(event.getPacket() instanceof CommonPingS2CPacket) && !(event.getPacket() instanceof KeepAliveS2CPacket)) {
            if (event.getPacket() instanceof EntityS2CPacket entityS2CPacket && entityS2CPacket.method_11645(mc.field_1687) instanceof PlayerEntity player) {
               if (player == mc.field_1724) {
                  return;
               }

               if (map.containsKey(player)
                  && this.entity.getValue()
                  && ((Vec3d)map.get(player)).method_1022(mc.field_1724.method_19538())
                     < new Vec3d((double)entityS2CPacket.method_36150(), (double)entityS2CPacket.method_36151(), (double)entityS2CPacket.method_36152())
                        .method_1022(mc.field_1724.method_19538())) {
                  this.packet.add(new FakeLag(this, entityS2CPacket));
                  event.cancel();
               }

               map.put(player, player.method_19538());
            }
         } else {
            this.packet.add(new FakeLag(this, event.getPacket()));
            event.cancel();
         }
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
            Render3DUtil.draw3DBox(matrixStack, ((IEntity)mc.field_1724).getDimensions().method_30757(vec3d).method_1009(0.0, 0.1, 0.0), color, false, true);
         }
      }
   }

   @Override
   public void onDisable() {
      if (nullCheck()) {
         this.packet.clear();
      } else {
         for (FakeLag p : this.packet) {
            p.apply();
         }
      }
   }

   private void update() {
      if (nullCheck()) {
         this.packet.clear();
      } else {
         this.packet.removeIf(FakeLag::send);
      }
   }
}
