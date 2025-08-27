package me.hextech.remapped;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public class NoSlow_PaVUKKxFbWGbplzMaucl extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static NoSlow_PaVUKKxFbWGbplzMaucl INSTANCE;
   private final EnumSetting<NoSlow_dcwUNFGCrJDcinmdQVVo> mode = this.add(new EnumSetting("Mode", NoSlow_dcwUNFGCrJDcinmdQVVo.Vanilla));
   private final BooleanSetting soulSand = this.add(new BooleanSetting("SoulSand", true));
   private final BooleanSetting active = this.add(new BooleanSetting("Gui", true));
   private final EnumSetting<NoSlow> clickBypass = this.add(new EnumSetting("Bypass", NoSlow.None));
   private final BooleanSetting sneak = this.add(new BooleanSetting("Sneak", false));
   private final Queue<ClickSlotC2SPacket> storedClicks = new LinkedList();
   private final AtomicBoolean pause = new AtomicBoolean();

   public NoSlow_PaVUKKxFbWGbplzMaucl() {
      super("NoSlow", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   private static float getMovementMultiplier(boolean positive, boolean negative) {
      if (positive == negative) {
         return 0.0F;
      } else {
         return positive ? 1.0F : -1.0F;
      }
   }

   @Override
   public String getInfo() {
      return ((NoSlow_dcwUNFGCrJDcinmdQVVo)this.mode.getValue()).name();
   }

   @EventHandler
   public void onUpdate(UpdateWalkingEvent event) {
      if (!event.isPost()) {
         if (mc.field_1724.method_6115() && !mc.field_1724.method_3144() && !mc.field_1724.method_6128()) {
            switch ((NoSlow_dcwUNFGCrJDcinmdQVVo)this.mode.getValue()) {
               case NCP:
                  mc.method_1562().method_52787(new UpdateSelectedSlotC2SPacket(mc.field_1724.method_31548().field_7545));
                  break;
               case Grim:
                  if (mc.field_1724.method_6058() == Hand.field_5810) {
                     mc.method_1562().method_52787(new UpdateSelectedSlotC2SPacket(mc.field_1724.method_31548().field_7545 % 8 + 1));
                     mc.method_1562().method_52787(new UpdateSelectedSlotC2SPacket(mc.field_1724.method_31548().field_7545 % 7 + 2));
                     mc.method_1562().method_52787(new UpdateSelectedSlotC2SPacket(mc.field_1724.method_31548().field_7545));
                  } else {
                     sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5810, id));
                  }
            }
         }

         if (this.active.getValue() && !(mc.field_1755 instanceof ChatScreen)) {
            for (KeyBinding k : new KeyBinding[]{mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849}) {
               k.method_23481(InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(k.method_1428()).method_1444()));
            }

            InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1894.method_1428()).method_1444());
            mc.field_1690
               .field_1867
               .method_23481(
                  Sprint.INSTANCE.isOn()
                     || InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1867.method_1428()).method_1444())
               );
            if (this.sneak.getValue()) {
               mc.field_1690
                  .field_1832
                  .method_23481(
                     InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1832.method_1428()).method_1444())
                  );
            }
         }
      }
   }

   @EventHandler
   public void keyboard(KeyboardInputEvent event) {
      if (this.active.getValue() && !(mc.field_1755 instanceof ChatScreen)) {
         for (KeyBinding k : new KeyBinding[]{mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849}) {
            k.method_23481(InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(k.method_1428()).method_1444()));
         }

         InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1894.method_1428()).method_1444());
         mc.field_1690
            .field_1867
            .method_23481(
               Sprint.INSTANCE.isOn()
                  || InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1867.method_1428()).method_1444())
            );
         if (this.sneak.getValue()) {
            mc.field_1690
               .field_1832
               .method_23481(
                  InputUtil.method_15987(mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1832.method_1428()).method_1444())
               );
         }

         mc.field_1724.field_3913.field_3910 = mc.field_1690.field_1894.method_1434();
         mc.field_1724.field_3913.field_3909 = mc.field_1690.field_1881.method_1434();
         mc.field_1724.field_3913.field_3908 = mc.field_1690.field_1913.method_1434();
         mc.field_1724.field_3913.field_3906 = mc.field_1690.field_1849.method_1434();
         mc.field_1724.field_3913.field_3905 = getMovementMultiplier(mc.field_1724.field_3913.field_3910, mc.field_1724.field_3913.field_3909);
         mc.field_1724.field_3913.field_3907 = getMovementMultiplier(mc.field_1724.field_3913.field_3908, mc.field_1724.field_3913.field_3906);
         mc.field_1724.field_3913.field_3904 = mc.field_1690.field_1903.method_1434();
         mc.field_1724.field_3913.field_3903 = mc.field_1690.field_1832.method_1434();
      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent e) {
      if (!nullCheck() && MovementUtil.isMoving() && mc.field_1690.field_1903.method_1434() && !this.pause.get()) {
         if (e.getPacket() instanceof ClickSlotC2SPacket click) {
            switch ((NoSlow)this.clickBypass.getValue()) {
               case StrictNCP:
                  if (mc.field_1724.method_24828()
                     && !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0, 0.0656, 0.0)).iterator().hasNext()) {
                     if (mc.field_1724.method_5624()) {
                        mc.method_1562()
                           .method_52787(
                              new ClientCommandC2SPacket(mc.field_1724, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.field_12985)
                           );
                     }

                     mc.method_1562()
                        .method_52787(
                           new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.0656, mc.field_1724.method_23321(), false)
                        );
                  }
                  break;
               case GrimSwap:
                  if (click.method_12195() != SlotActionType.field_7790 && click.method_12195() != SlotActionType.field_7793) {
                     int syncId = mc.field_1724.field_7512.field_7763;
                     int closePacketCount = 2 + (int)(Math.random() * 2.0);

                     for (int i = 0; i < closePacketCount; i++) {
                        mc.method_1562().method_52787(new CloseHandledScreenC2SPacket(syncId));
                     }

                     int randomSlot = (int)(Math.random() * 9.0);
                     mc.method_1562().method_52787(new UpdateSelectedSlotC2SPacket(randomSlot));
                     mc.method_1562().method_52787(new UpdateSelectedSlotC2SPacket(mc.field_1724.method_31548().field_7545));
                     sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
                     sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5810, id));
                     if (mc.field_1724.method_24828()) {
                        double offset = 2.71875E-7 + Math.random() * 1.0E-7;
                        mc.method_1562()
                           .method_52787(
                              new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + offset, mc.field_1724.method_23321(), false)
                           );
                     }
                  }
                  break;
               case MatrixNcp:
                  mc.method_1562()
                     .method_52787(new ClientCommandC2SPacket(mc.field_1724, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.field_12985));
                  mc.field_1690.field_1894.method_23481(false);
                  mc.field_1724.field_3913.field_3905 = 0.0F;
                  mc.field_1724.field_3913.field_3910 = false;
                  break;
               case Delay:
                  this.storedClicks.add(click);
                  e.cancel();
                  break;
               case StrictNCP2:
                  if (mc.field_1724.method_24828()
                     && !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0, 2.71875E-7, 0.0)).iterator().hasNext()) {
                     if (mc.field_1724.method_5624()) {
                        mc.method_1562()
                           .method_52787(
                              new ClientCommandC2SPacket(mc.field_1724, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.field_12985)
                           );
                     }

                     mc.method_1562()
                        .method_52787(
                           new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7, mc.field_1724.method_23321(), false)
                        );
                  }
            }
         }

         if (e.getPacket() instanceof CloseHandledScreenC2SPacket && this.clickBypass.is(NoSlow.Delay)) {
            this.pause.set(true);

            while (!this.storedClicks.isEmpty()) {
               mc.method_1562().method_52787((Packet)this.storedClicks.poll());
            }

            this.pause.set(false);
         }
      }
   }

   @EventHandler
   public void onPacketSendPost(PacketEvent_nYAfaBsVQmKvWkMiCKYv e) {
      if (e.getPacket() instanceof ClickSlotC2SPacket && mc.field_1724.method_5624() && this.clickBypass.is(NoSlow.StrictNCP)) {
         mc.method_1562()
            .method_52787(new ClientCommandC2SPacket(mc.field_1724, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.field_12981));
      }
   }

   public boolean noSlow() {
      return this.isOn() && this.mode.getValue() != NoSlow_dcwUNFGCrJDcinmdQVVo.None;
   }

   public boolean soulSand() {
      return this.isOn() && this.soulSand.getValue();
   }
}
