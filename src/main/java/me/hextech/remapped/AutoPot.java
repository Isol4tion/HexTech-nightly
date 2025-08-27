package me.hextech.remapped;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;

public class AutoPot extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoPot INSTANCE;
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 5, 0, 10).setSuffix("s"));
   private final BooleanSetting speed = this.add(new BooleanSetting("Speed", true));
   private final BooleanSetting resistance = this.add(new BooleanSetting("Resistance", true));
   private final BooleanSetting slowFalling = this.add(new BooleanSetting("SlowFalling", true));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final Timer delayTimer = new Timer();
   private boolean throwing = false;

   public AutoPot() {
      super("AutoPot", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static int findPotionInventorySlot(StatusEffect targetEffect) {
      for (int i = 0; i < 45; i++) {
         ItemStack itemStack = mc.field_1724.method_31548().method_5438(i);
         if (Item.method_7880(itemStack.method_7909()) == Item.method_7880(Items.field_8436)) {
            for (StatusEffectInstance effect : PotionUtil.method_8067(itemStack)) {
               if (effect.method_5579() == targetEffect) {
                  return i < 9 ? i + 36 : i;
               }
            }
         }
      }

      return -1;
   }

   public static int findPotion(StatusEffect targetEffect) {
      for (int i = 0; i < 9; i++) {
         ItemStack itemStack = InventoryUtil.getStackInSlot(i);
         if (Item.method_7880(itemStack.method_7909()) == Item.method_7880(Items.field_8436)) {
            for (StatusEffectInstance effect : PotionUtil.method_8067(itemStack)) {
               if (effect.method_5579() == targetEffect) {
                  return i;
               }
            }
         }
      }

      return -1;
   }

   @Override
   public void onDisable() {
      this.throwing = false;
   }

   @Override
   public void onUpdate() {
      if (!this.onlyGround.getValue()
         || mc.field_1724.method_24828() && !mc.field_1687.method_22347(new BlockPosX(mc.field_1724.method_19538().method_1031(0.0, -1.0, 0.0)))) {
         if (this.speed.getValue() && !mc.field_1724.method_6059(StatusEffects.field_5904)) {
            this.throwing = this.checkThrow(StatusEffects.field_5904);
            if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
               this.throwPotion(StatusEffects.field_5904);
               return;
            }
         }

         if (this.resistance.getValue()
            && (!mc.field_1724.method_6059(StatusEffects.field_5907) || mc.field_1724.method_6112(StatusEffects.field_5907).method_5578() < 2)) {
            this.throwing = this.checkThrow(StatusEffects.field_5907);
            if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
               this.throwPotion(StatusEffects.field_5907);
            }
         }
      }
   }

   public void throwPotion(StatusEffect targetEffect) {
      int oldSlot = mc.field_1724.method_31548().field_7545;
      int newSlot;
      if (this.inventory.getValue() && (newSlot = findPotionInventorySlot(targetEffect)) != -1) {
         EntityUtil.sendYawAndPitch(mc.field_1724.method_36454(), 90.0F);
         InventoryUtil.inventorySwap(newSlot, mc.field_1724.method_31548().field_7545);
         sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
         InventoryUtil.inventorySwap(newSlot, mc.field_1724.method_31548().field_7545);
         EntityUtil.syncInventory();
         this.delayTimer.reset();
      } else if ((newSlot = findPotion(targetEffect)) != -1) {
         EntityUtil.sendYawAndPitch(mc.field_1724.method_36454(), 90.0F);
         InventoryUtil.switchToSlot(newSlot);
         sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
         InventoryUtil.switchToSlot(oldSlot);
         this.delayTimer.reset();
      }
   }

   public boolean isThrow() {
      return this.throwing;
   }

   public boolean checkThrow(StatusEffect targetEffect) {
      if (this.isOff()) {
         return false;
      } else if (mc.field_1755 != null
         && !(mc.field_1755 instanceof ChatScreen)
         && !(mc.field_1755 instanceof InventoryScreen)
         && !(mc.field_1755 instanceof ClickGuiScreen)
         && !(mc.field_1755 instanceof GameMenuScreen)) {
         return false;
      } else {
         return this.usingPause.getValue() && mc.field_1724.method_6115()
            ? false
            : findPotion(targetEffect) != -1 || this.inventory.getValue() && findPotionInventorySlot(targetEffect) != -1;
      }
   }
}
