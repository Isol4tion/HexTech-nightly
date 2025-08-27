package me.hextech.remapped;

import java.util.Random;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public class BowBomb extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final Timer delayTimer = new Timer();
   private final BooleanSetting rotation = this.add(new BooleanSetting("Rotation", false));
   private final SliderSetting spoofs = this.add(new SliderSetting("Spoofs", 50.0, 0.0, 200.0, 1.0));
   private final EnumSetting<BowBomb_rjdFZKIVfyUHfSoAeHLl> exploit = this.add(new EnumSetting("Exploit", BowBomb_rjdFZKIVfyUHfSoAeHLl.Strong));
   private final BooleanSetting minimize = this.add(new BooleanSetting("Minimize", false));
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 5.0, 0.0, 10.0).setSuffix("s"));
   private final SliderSetting activeTime = this.add(new SliderSetting("ActiveTime", 0.4F, 0.0, 3.0).setSuffix("s"));
   private final Random random = new Random();
   private final Timer activeTimer = new Timer();

   public BowBomb() {
      super("BowBomb", "exploit", Module_JlagirAibYQgkHtbRnhw.Combat);
   }

   @Override
   public void onUpdate() {
      if (!mc.field_1724.method_6115() || mc.field_1724.method_6030().method_7909() != Items.field_8102) {
         this.activeTimer.reset();
      }
   }

   @EventHandler
   protected void onPacketSend(PacketEvent event) {
      if (!nullCheck()
         && this.delayTimer.passedMs((long)(this.delay.getValue() * 1000.0))
         && this.activeTimer.passedMs((long)(this.activeTime.getValue() * 1000.0))) {
         if (event.getPacket() instanceof PlayerActionC2SPacket packet && packet.method_12363() == Action.field_12974) {
            mc.field_1724.field_3944.method_52787(new ClientCommandC2SPacket(mc.field_1724, Mode.field_12981));
            if (this.exploit.getValue() == BowBomb_rjdFZKIVfyUHfSoAeHLl.Fast) {
               for (int i = 0; i < this.getRuns(); i++) {
                  this.spoof(
                     mc.field_1724.method_23317(),
                     this.minimize.getValue() ? mc.field_1724.method_23318() : mc.field_1724.method_23318() - 1.0E-10,
                     mc.field_1724.method_23321(),
                     true
                  );
                  this.spoof(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-10, mc.field_1724.method_23321(), false);
               }
            }

            if (this.exploit.getValue() == BowBomb_rjdFZKIVfyUHfSoAeHLl.Strong) {
               for (int i = 0; i < this.getRuns(); i++) {
                  this.spoof(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-10, mc.field_1724.method_23321(), false);
                  this.spoof(
                     mc.field_1724.method_23317(),
                     this.minimize.getValue() ? mc.field_1724.method_23318() : mc.field_1724.method_23318() - 1.0E-10,
                     mc.field_1724.method_23321(),
                     true
                  );
               }
            }

            if (this.exploit.getValue() == BowBomb_rjdFZKIVfyUHfSoAeHLl.Phobos) {
               for (int i = 0; i < this.getRuns(); i++) {
                  this.spoof(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.3E-13, mc.field_1724.method_23321(), true);
                  this.spoof(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.7E-13, mc.field_1724.method_23321(), false);
               }
            }

            if (this.exploit.getValue() == BowBomb_rjdFZKIVfyUHfSoAeHLl.Strict) {
               double[] strict_direction = new double[]{
                  100.0 * -Math.sin(Math.toRadians((double)mc.field_1724.method_36454())),
                  100.0 * Math.cos(Math.toRadians((double)mc.field_1724.method_36454()))
               };

               for (int i = 0; i < this.getRuns(); i++) {
                  if (this.random.nextBoolean()) {
                     this.spoof(
                        mc.field_1724.method_23317() - strict_direction[0],
                        mc.field_1724.method_23318(),
                        mc.field_1724.method_23321() - strict_direction[1],
                        false
                     );
                  } else {
                     this.spoof(
                        mc.field_1724.method_23317() + strict_direction[0],
                        mc.field_1724.method_23318(),
                        mc.field_1724.method_23321() + strict_direction[1],
                        true
                     );
                  }
               }
            }

            this.delayTimer.reset();
         }
      }
   }

   private void spoof(double x, double y, double z, boolean ground) {
      if (this.rotation.getValue()) {
         mc.field_1724.field_3944.method_52787(new Full(x, y, z, mc.field_1724.method_36454(), mc.field_1724.method_36455(), ground));
      } else {
         mc.field_1724.field_3944.method_52787(new PositionAndOnGround(x, y, z, ground));
      }
   }

   private int getRuns() {
      return this.spoofs.getValueInt();
   }
}
