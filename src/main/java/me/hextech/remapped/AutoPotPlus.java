package me.hextech.remapped;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AutoPotPlus extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoPotPlus INSTANCE;
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 5, 0, 10).setSuffix("s"));
   private final BooleanSetting checkRang = this.add(new BooleanSetting("CheckRang", true).setParent());
   private final SliderSetting rang = this.add(new SliderSetting("Rang", 15, 0, 15, v -> this.checkRang.isOpen()).setSuffix("m"));
   private final BooleanSetting strength = this.add(new BooleanSetting("Strength", true));
   private final BooleanSetting speed = this.add(new BooleanSetting("Speed", true));
   private final BooleanSetting resistance = this.add(new BooleanSetting("Resistance", true));
   private final BooleanSetting checkFly = this.add(new BooleanSetting("CheckFly", false));
   private final BooleanSetting smartCheckGround = this.add(new BooleanSetting("SmartCheckGround", true));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final Timer delayTimer = new Timer();
   PlayerEntity target = null;
   BlockPos throwPos = null;
   private boolean throwing = false;

   public AutoPotPlus() {
      super("AutoPot+", Module_JlagirAibYQgkHtbRnhw.Player);
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
      if (mc.field_1724 != null) {
         this.target = CombatUtil.getClosestEnemy(this.rang.getValue());
         if (!this.checkRang.getValue() || this.target != null) {
            this.throwPos = null;
            this.throwPos = this.findPos();
            if ((!this.checkFly.getValue() || !mc.field_1724.method_6128()) && (!this.smartCheckGround.getValue() || this.throwPos != null)) {
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

               if (this.strength.getValue() && !mc.field_1724.method_6059(StatusEffects.field_5910)) {
                  this.throwing = this.checkThrow(StatusEffects.field_5910);
                  if (this.isThrow() && this.delayTimer.passedMs(this.delay.getValue() * 1000.0)) {
                     this.throwPotion(StatusEffects.field_5910);
                  }
               }
            }
         }
      }
   }

   public void snapAt(Vec3d directionVec) {
      float[] angle = this.getRotation(directionVec);
      EntityUtil.sendYawAndPitch(angle[0], angle[1]);
   }

   public float[] getRotation(Vec3d vec) {
      Vec3d eyesPos = EntityUtil.getEyesPos();
      return this.getRotation(eyesPos, vec);
   }

   public float[] getRotation(Vec3d eyesPos, Vec3d vec) {
      double diffX = vec.field_1352 - eyesPos.field_1352;
      double diffY = vec.field_1351 - eyesPos.field_1351;
      double diffZ = vec.field_1350 - eyesPos.field_1350;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{MathHelper.method_15393(yaw), MathHelper.method_15393(pitch)};
   }

   public void throwPotion(StatusEffect targetEffect) {
      int oldSlot = mc.field_1724.method_31548().field_7545;
      int newSlot;
      if (this.inventory.getValue() && (newSlot = findPotionInventorySlot(targetEffect)) != -1) {
         if (this.throwPos != null) {
            this.snapAt(this.throwPos.method_46558());
         } else {
            EntityUtil.sendYawAndPitch(mc.field_1724.method_36454(), 90.0F);
         }

         InventoryUtil.inventorySwap(newSlot, mc.field_1724.method_31548().field_7545);
         sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
         InventoryUtil.inventorySwap(newSlot, mc.field_1724.method_31548().field_7545);
         EntityUtil.syncInventory();
         this.delayTimer.reset();
      } else if ((newSlot = findPotion(targetEffect)) != -1) {
         if (this.throwPos != null) {
            this.snapAt(this.throwPos.method_46558());
         } else {
            EntityUtil.sendYawAndPitch(mc.field_1724.method_36454(), 90.0F);
         }

         InventoryUtil.switchToSlot(newSlot);
         sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
         InventoryUtil.switchToSlot(oldSlot);
         this.delayTimer.reset();
      }
   }

   public BlockPos findPos() {
      for (float x : new float[]{0.0F, -0.3F, 0.3F}) {
         for (float z : new float[]{0.0F, -0.3F, 0.3F}) {
            BlockPos pos = new BlockPosX(mc.field_1724.method_23317() + (double)x, mc.field_1724.method_23318() - 0.5, mc.field_1724.method_23321() + (double)z);
            if (!BlockUtil.isAir(pos) && mc.field_1687.method_8320(pos).method_26204() != Blocks.field_10343) {
               return pos;
            }
         }
      }

      return null;
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
