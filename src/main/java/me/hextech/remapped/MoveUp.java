package me.hextech.remapped;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public class MoveUp extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static MoveUp INSTANCE;
   public final SliderSetting rubberbandPackets = this.add(new SliderSetting("Packet", 1.0, 0.0, 9.0, 1.0));
   public final SliderSetting setPosition = this.add(new SliderSetting("SetPosition", 1.0, -5.0, 5.0, 0.1));
   public final SliderSetting rubberbandOffset = this.add(new SliderSetting("Offset", 1.0, 0.0, 9.0, 1.0));
   final EnumSetting<MoveUp_FPLgygTQIdxRiYXqovqv> mode = this.add(new EnumSetting("Mode", MoveUp_FPLgygTQIdxRiYXqovqv.Jump));
   private final BooleanSetting noWeb = this.add(new BooleanSetting("PauseWeb", true));
   private final BooleanSetting onlyburrow = this.add(new BooleanSetting("OnlyBurrow", true));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("GroundCheck", true));
   private final BooleanSetting pEndChest = this.add(new BooleanSetting("EndChest", true));
   private final BooleanSetting doburrow = this.add(new BooleanSetting("StartBurrow", true));
   private final BooleanSetting toggle = this.add(new BooleanSetting("Toggle", false));

   public MoveUp() {
      super("MoveUp", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (mc.field_1724 == null || !this.noWeb.getValue() || !HoleKickTest.isInWeb(mc.field_1724)) {
         if (mc.field_1724 == null || !this.onlyGround.getValue() || mc.field_1724.method_24828()) {
            if (mc.field_1724 != null && (!this.onlyburrow.getValue() || Util.isBurrowed(mc.field_1724, !this.pEndChest.getValue()))) {
               switch ((MoveUp_FPLgygTQIdxRiYXqovqv)this.mode.getValue()) {
                  case Packet:
                     if (mc.field_1724 != null && (!this.onlyburrow.getValue() || Util.isBurrowed(mc.field_1724, !this.pEndChest.getValue()))) {
                        double y = 0.0;
                        double velocity = 0.42;

                        while (y < 1.1) {
                           y += velocity;
                           velocity = (velocity - 0.08) * 0.98;
                           this.sendPacket(
                              new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318() + y, mc.field_1724.method_23321(), false)
                           );
                        }

                        for (int i = 0; i < this.rubberbandPackets.getValueInt(); i++) {
                           this.sendPacket(
                              new PositionAndOnGround(
                                 mc.field_1724.method_23317(),
                                 mc.field_1724.method_23318() + y + (double)this.rubberbandOffset.getValueInt(),
                                 mc.field_1724.method_23321(),
                                 false
                              )
                           );
                        }
                     }
                     break;
                  case Jump:
                     mc.method_1562()
                        .method_52787(
                           new PositionAndOnGround(
                              mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.4199999868869781, mc.field_1724.method_23321(), false
                           )
                        );
                     mc.method_1562()
                        .method_52787(
                           new PositionAndOnGround(
                              mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.7531999805212017, mc.field_1724.method_23321(), false
                           )
                        );
                     mc.field_1724
                        .method_5814(
                           mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)this.setPosition.getValueFloat(), mc.field_1724.method_23321()
                        );
                     mc.method_1562()
                        .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
               }
            }

            if (this.doburrow.getValue() && !MovementUtil.isMoving() && !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
               Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
            }

            if (this.toggle.getValue()) {
               this.disable();
            }
         }
      }
   }
}
