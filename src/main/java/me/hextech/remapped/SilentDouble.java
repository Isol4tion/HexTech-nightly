package me.hextech.remapped;

import me.hextech.asm.mixins.IClientPlayerInteractionManager;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;

public class SilentDouble extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static int slotMain;
   public static int swithc2;
   public static SilentDouble INSTANCE;
   public final EnumSetting<SilentDouble_JPSoqiNZyGTsyFDufNku> mode = this.add(new EnumSetting("SwitchMode", SilentDouble_JPSoqiNZyGTsyFDufNku.SilentDouble));
   public final SliderSetting dmg = this.add(new SliderSetting("Switch", 0.89, 0.0, 1.5, 0.01));
   public final SliderSetting lastdmg = this.add(new SliderSetting("LastSwitch", 0.9, 0.0, 1.5, 0.01));
   public final SliderSetting enddmg = this.add(new SliderSetting("EndSwitch", 1.2, 0.0, 2.0, 0.1));
   public BooleanSetting syncinv = this.add(new BooleanSetting("InvSync", true));
   public BooleanSetting pause = this.add(new BooleanSetting("NoUsingMine", true));
   public BooleanSetting groundcheck = this.add(new BooleanSetting("GroundCheck", true));

   public SilentDouble() {
      super("SilentDouble", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      this.update();
   }

   public void update() {
      if (((SilentDouble_JPSoqiNZyGTsyFDufNku)this.mode.getValue()).equals(SilentDouble_JPSoqiNZyGTsyFDufNku.SilentDouble)) {
         if (this.pause.getValue() && mc.field_1724.method_6115()) {
            return;
         }

         if (this.groundcheck.getValue() && !mc.field_1724.method_24828()) {
            return;
         }

         if (SpeedMine.secondPos != null
            && !SpeedMine.INSTANCE
               .secondTimer
               .passed(
                  SpeedMine.INSTANCE
                     .getBreakTime(
                        SpeedMine.secondPos,
                        this.getTool(SpeedMine.secondPos) == -1 ? mc.field_1724.method_31548().field_7545 : this.getTool(SpeedMine.secondPos),
                        (double)this.dmg.getValueFloat()
                     )
               )) {
            slotMain = mc.field_1724.method_31548().field_7545;
         }

         if (SpeedMine.secondPos != null
            && SpeedMine.INSTANCE
               .secondTimer
               .passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.getTool(SpeedMine.secondPos), (double)this.lastdmg.getValueFloat()))) {
            if (mc.field_1724.method_6047().method_7909() == Items.field_8367) {
               if (!mc.field_1690.field_1904.method_1434()) {
                  mc.field_1724.field_3944.method_52787(new UpdateSelectedSlotC2SPacket(this.getTool(SpeedMine.secondPos)));
                  swithc2 = 1;
               } else if (swithc2 == 1) {
                  mc.field_1724.field_3944.method_52787(new UpdateSelectedSlotC2SPacket(slotMain));
                  if (this.syncinv.getValue()) {
                     EntityUtil.syncInventory();
                  }
               }
            } else {
               mc.field_1724.field_3944.method_52787(new UpdateSelectedSlotC2SPacket(this.getTool(SpeedMine.secondPos)));
               swithc2 = 1;
            }
         }

         if (SpeedMine.secondPos != null
            && SpeedMine.INSTANCE
               .secondTimer
               .passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.getTool(SpeedMine.secondPos), (double)this.enddmg.getValueFloat()))
            && swithc2 == 1) {
            mc.field_1724.field_3944.method_52787(new UpdateSelectedSlotC2SPacket(slotMain));
            if (this.syncinv.getValue()) {
               EntityUtil.syncInventory();
            }
         }
      }

      if (((SilentDouble_JPSoqiNZyGTsyFDufNku)this.mode.getValue()).equals(SilentDouble_JPSoqiNZyGTsyFDufNku.Switch)) {
         this.sendPacket(new UpdateSelectedSlotC2SPacket(mc.field_1724.method_31548().field_7545));
      } else if (this.mode.getValue() == SilentDouble_JPSoqiNZyGTsyFDufNku.Switch && SpeedMine.INSTANCE.lastSlot != -1) {
         mc.field_1724.method_31548().field_7545 = SpeedMine.INSTANCE.lastSlot;
         ((IClientPlayerInteractionManager)mc.field_1761).syncSelected();
         SpeedMine.INSTANCE.lastSlot = -1;
      }

      boolean canUpdate = false;
   }

   public int getTool(BlockPos pos) {
      return SpeedMine.INSTANCE.getTool(pos) == -1 ? mc.field_1724.method_31548().field_7545 : SpeedMine.INSTANCE.getTool(pos);
   }
}
