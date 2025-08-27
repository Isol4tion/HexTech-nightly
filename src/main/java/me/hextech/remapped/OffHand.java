package me.hextech.remapped;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class OffHand extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final BooleanSetting mainHand = this.add(new BooleanSetting("MainHand", false));
   private final BooleanSetting crystal = this.add(new BooleanSetting("OffHandCrystal", false));
   private final SliderSetting health = this.add(new SliderSetting("Health", 16.0, 0.0, 36.0, 0.1));
   private final Timer timer = new Timer();
   int totems = 0;

   public OffHand() {
      super("OffHand", Module_JlagirAibYQgkHtbRnhw.Combat);
   }

   @Override
   public String getInfo() {
      return String.valueOf(this.totems);
   }

   @EventHandler
   public void onUpdateWalking(UpdateWalkingEvent event) {
      this.update();
   }

   @Override
   public void onUpdate() {
      this.update();
   }

   private void update() {
      if (!nullCheck()) {
         this.totems = InventoryUtil.getItemCount(Items.field_8288);
         if (mc.field_1755 == null
            || mc.field_1755 instanceof ChatScreen
            || mc.field_1755 instanceof InventoryScreen
            || mc.field_1755 instanceof ClickGuiScreen
            || mc.field_1755 instanceof GameMenuScreen) {
            if (this.timer.passedMs(200L)) {
               if ((double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()) > this.health.getValue()) {
                  if (!this.mainHand.getValue() && this.crystal.getValue() && mc.field_1724.method_6079().method_7909() != Items.field_8301) {
                     int itemSlot = InventoryUtil.findItemInventorySlot(Items.field_8301);
                     if (itemSlot != -1) {
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, mc.field_1724);
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 45, 0, SlotActionType.field_7790, mc.field_1724);
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, mc.field_1724);
                        EntityUtil.syncInventory();
                        this.timer.reset();
                     }
                  }
               } else if (mc.field_1724.method_6047().method_7909() != Items.field_8288 && mc.field_1724.method_6079().method_7909() != Items.field_8288) {
                  int itemSlot = InventoryUtil.findItemInventorySlot(Items.field_8288);
                  if (itemSlot != -1) {
                     if (this.mainHand.getValue()) {
                        InventoryUtil.switchToSlot(0);
                        if (mc.field_1724.method_31548().method_5438(0).method_7909() != Items.field_8288) {
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, mc.field_1724);
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 36, 0, SlotActionType.field_7790, mc.field_1724);
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, mc.field_1724);
                           EntityUtil.syncInventory();
                        }
                     } else {
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, mc.field_1724);
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 45, 0, SlotActionType.field_7790, mc.field_1724);
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 0, SlotActionType.field_7790, mc.field_1724);
                        EntityUtil.syncInventory();
                     }

                     this.timer.reset();
                  }
               }
            }
         }
      }
   }
}
