package me.hextech.remapped;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public class moveupV extends Module_eSdgMXWuzcxgQVaJFmKZ {
   final EnumSetting<moveupV_rlgNzzROxlnlLAUMICmS> mode = this.add(new EnumSetting("Mode", moveupV_rlgNzzROxlnlLAUMICmS.Teleport));
   private final BooleanSetting noWeb = this.add(new BooleanSetting("PauseWeb", true));
   private final BooleanSetting onlyburrow = this.add(new BooleanSetting("OnlyBurrow", true));
   private final BooleanSetting pEndChest = this.add(new BooleanSetting("EndChest", true));
   private final BooleanSetting doburrow = this.add(new BooleanSetting("StarBurrow", true));
   private final BooleanSetting movecheck = this.add(new BooleanSetting("MoveCheck", true));
   private final BooleanSetting toggle = this.add(new BooleanSetting("Toggle", false));

   public moveupV() {
      super("MoveClip", Module_JlagirAibYQgkHtbRnhw.Movement);
   }

   @Override
   public void onUpdate() {
      if (!this.movecheck.getValue() || mc.field_1724 == null || !MovementUtil.isMoving()) {
         if (mc.field_1724 == null || !this.noWeb.getValue() || !HoleKickTest.isInWeb(mc.field_1724)) {
            if (mc.field_1724 != null && (!this.onlyburrow.getValue() || Util.isBurrowed(mc.field_1724, !this.pEndChest.getValue()))) {
               switch ((moveupV_rlgNzzROxlnlLAUMICmS)this.mode.getValue()) {
                  case Glitch:
                     double posX = mc.field_1724.method_23317();
                     double posY = (double)Math.round(mc.field_1724.method_23318());
                     double posZ = mc.field_1724.method_23321();
                     boolean onGround = mc.field_1724.method_24828();
                     mc.method_1562().method_52787(new PositionAndOnGround(posX, posY, posZ, onGround));
                     double halfY = 0.005;
                     posY -= halfY;
                     mc.field_1724.method_5814(posX, posY, posZ);
                     mc.method_1562().method_52787(new PositionAndOnGround(posX, posY, posZ, onGround));
                     posY -= halfY * 300.0;
                     mc.field_1724.method_5814(posX, posY, posZ);
                     mc.method_1562().method_52787(new PositionAndOnGround(posX, posY, posZ, onGround));
                     break;
                  case Teleport:
                     mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 3.0, mc.field_1724.method_23321());
                     mc.method_1562()
                        .method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
               }
            }

            if (this.toggle.getValue()) {
               this.disable();
            }

            if (this.doburrow.getValue() && !MovementUtil.isMoving() && !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
               Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
            }
         }
      }
   }
}
