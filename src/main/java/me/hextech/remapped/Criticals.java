package me.hextech.remapped;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public class Criticals extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Criticals INSTANCE;
   public final EnumSetting<Criticals_llXqHCnomcmaIkSSIBHS> mode = this.add(new EnumSetting("Mode", Criticals_llXqHCnomcmaIkSSIBHS.Packet));

   public Criticals() {
      super("Criticals", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static Entity getEntity(PlayerInteractEntityC2SPacket packet) {
      PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
      packet.method_11052(packetBuf);
      return mc.field_1687 == null ? null : mc.field_1687.method_8469(packetBuf.method_10816());
   }

   public static Criticals_QenzavIULhSqCVPmsILH getInteractType(PlayerInteractEntityC2SPacket packet) {
      PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
      packet.method_11052(packetBuf);
      packetBuf.method_10816();
      return (Criticals_QenzavIULhSqCVPmsILH)packetBuf.method_10818(Criticals_QenzavIULhSqCVPmsILH.class);
   }

   @Override
   public String getInfo() {
      return ((Criticals_llXqHCnomcmaIkSSIBHS)this.mode.getValue()).name();
   }

   @EventHandler
   public void onPacketSend(PacketEvent event) {
      Entity entity;
      if (!Aura.INSTANCE.sweeping
         && !TPAura_LycLkxHLQeGfgqfryvmV.attacking
         && event.getPacket() instanceof PlayerInteractEntityC2SPacket packet
         && getInteractType(packet) == Criticals_QenzavIULhSqCVPmsILH.ATTACK
         && !((entity = getEntity(packet)) instanceof EndCrystalEntity)) {
         mc.field_1724.method_7277(entity);
         this.doCrit();
      }
   }

   public void doCrit() {
      if (Aura.INSTANCE.isOn()
         && (mc.field_1724.method_24828() || mc.field_1724.method_31549().field_7479)
         && !mc.field_1724.method_5771()
         && !mc.field_1724.method_5869()) {
         if (this.mode.getValue() == Criticals_llXqHCnomcmaIkSSIBHS.Strict
            && mc.field_1687.method_8320(mc.field_1724.method_24515()).method_26204() != Blocks.field_10343) {
            mc.field_1724
               .field_3944
               .method_52787(
                  new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.062600301692775, mc.field_1724.method_23321(), false)
               );
            mc.field_1724
               .field_3944
               .method_52787(
                  new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.07260029960661, mc.field_1724.method_23321(), false)
               );
            mc.field_1724
               .field_3944
               .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
            mc.field_1724
               .field_3944
               .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
         } else if (this.mode.getValue() == Criticals_llXqHCnomcmaIkSSIBHS.NCP) {
            mc.field_1724
               .field_3944
               .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.0625, mc.field_1724.method_23321(), false));
            mc.field_1724
               .field_3944
               .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
         } else if (this.mode.getValue() == Criticals_llXqHCnomcmaIkSSIBHS.Packet) {
            mc.field_1724
               .field_3944
               .method_52787(
                  new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.058293536E-5, mc.field_1724.method_23321(), false)
               );
            mc.field_1724
               .field_3944
               .method_52787(
                  new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 9.16580235E-6, mc.field_1724.method_23321(), false)
               );
            mc.field_1724
               .field_3944
               .method_52787(
                  new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0371854E-7, mc.field_1724.method_23321(), false)
               );
         } else if (this.mode.getValue() == Criticals_llXqHCnomcmaIkSSIBHS.LowPacket) {
            mc.field_1724
               .field_3944
               .method_52787(
                  new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7, mc.field_1724.method_23321(), false)
               );
            mc.field_1724
               .field_3944
               .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
         }
      }
   }
}
