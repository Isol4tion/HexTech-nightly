package me.hextech.remapped;

import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

public class AntiWeakness_SVNjAQUSXMmCfEPBellQ extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
   private final EnumSetting<AntiWeakness> swapMode = this.add(new EnumSetting("SwapMode", AntiWeakness.Inventory));
   private final BooleanSetting onlyCrystal = this.add(new BooleanSetting("OnlyCrystal", true));
   private final Timer delayTimer = new Timer();
   boolean ignore = false;
   private PlayerInteractEntityC2SPacket lastPacket = null;

   public AntiWeakness_SVNjAQUSXMmCfEPBellQ() {
      super("AntiWeakness", "anti weak", Module_JlagirAibYQgkHtbRnhw.Combat);
   }

   @Override
   public String getInfo() {
      return ((AntiWeakness)this.swapMode.getValue()).name();
   }

   @EventHandler(
      priority = -200
   )
   public void onPacketSend(PacketEvent event) {
      if (!nullCheck()) {
         if (!event.isCancelled()) {
            if (!this.ignore) {
               if (mc.field_1724.method_6112(StatusEffects.field_5911) != null) {
                  if (!(mc.field_1724.method_6047().method_7909() instanceof SwordItem)) {
                     if (this.delayTimer.passedMs(this.delay.getValue())) {
                        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet
                           && Criticals.getInteractType(packet) == Criticals_QenzavIULhSqCVPmsILH.ATTACK) {
                           if (this.onlyCrystal.getValue() && !(Criticals.getEntity(packet) instanceof EndCrystalEntity)) {
                              return;
                           }

                           this.lastPacket = event.getPacket();
                           this.delayTimer.reset();
                           this.ignore = true;
                           this.doAnti();
                           this.ignore = false;
                           event.cancel();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void doAnti() {
      if (this.lastPacket != null) {
         int strong;
         if (this.swapMode.getValue() != AntiWeakness.Inventory) {
            strong = InventoryUtil.findClass(SwordItem.class);
         } else {
            strong = InventoryUtil.findClassInventorySlot(SwordItem.class);
         }

         if (strong != -1) {
            int old = mc.field_1724.method_31548().field_7545;
            if (this.swapMode.getValue() != AntiWeakness.Inventory) {
               InventoryUtil.switchToSlot(strong);
            } else {
               mc.field_1761
                  .method_2906(mc.field_1724.field_7512.field_7763, strong, mc.field_1724.method_31548().field_7545, SlotActionType.field_7791, mc.field_1724);
            }

            mc.field_1724.field_3944.method_52787(this.lastPacket);
            if (this.swapMode.getValue() != AntiWeakness.Inventory) {
               if (this.swapMode.getValue() != AntiWeakness.Normal) {
                  InventoryUtil.switchToSlot(old);
               }
            } else {
               mc.field_1761
                  .method_2906(mc.field_1724.field_7512.field_7763, strong, mc.field_1724.method_31548().field_7545, SlotActionType.field_7791, mc.field_1724);
               EntityUtil.syncInventory();
            }
         }
      }
   }
}
