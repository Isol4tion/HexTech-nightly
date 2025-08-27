package me.hextech.remapped;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MainHand extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static MainHand INSTANCE;
   public final BooleanSetting mineSlot = this.add(new BooleanSetting("MineSlot", true).setParent());
   public final BooleanSetting pauseEat = this.add(new BooleanSetting("PauseEat", false, v -> this.mineSlot.isOpen()));
   public final BooleanSetting totemPause = this.add(new BooleanSetting("TotemPause", false, v -> this.mineSlot.isOpen()));
   public final BooleanSetting totem = this.add(new BooleanSetting("TotemSlot", true).setParent());
   public final BooleanSetting minePause = this.add(new BooleanSetting("MinePause", false, v -> this.totem.isOpen()));
   private final EnumSetting<MainHand_TQxYHQLcjCDwbwqUjIlv> mode = this.add(
      new EnumSetting("Mode", MainHand_TQxYHQLcjCDwbwqUjIlv.Double, v -> this.mineSlot.isOpen())
   );
   private final SliderSetting damage = this.add(new SliderSetting("Damage", 0.9, 0.0, 1.0, 0.01, v -> this.mineSlot.isOpen()));
   private final SliderSetting time = this.add(new SliderSetting("Time", 1.0, 0.0, 10.0, 1.0, v -> this.mineSlot.isOpen()));
   private final SliderSetting forceHealth = this.add(new SliderSetting("SlotHealth", 3.0, 0.0, 36.0, 0.1, v -> this.totem.isOpen()));
   private final SliderSetting slotPosition = this.add(new SliderSetting("SlotPosition", 1.0, 1.0, 9.0, 1.0, v -> this.totem.isOpen()));
   private final Timer timer = new Timer();
   boolean needSwitch = false;
   int old = -1;
   int slot = -1;
   int slot2 = -1;
   boolean handSlot = false;

   public MainHand() {
      super("MainHand", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   public static int findItemInventorySlot(Item item) {
      for (int i = 35; i >= 0; i--) {
         ItemStack stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_7909() == item) {
            return i < 9 ? i + 36 : i;
         }
      }

      return -1;
   }

   @Override
   public String getInfo() {
      return this.mode.getValue() == MainHand_TQxYHQLcjCDwbwqUjIlv.Double ? "Double" : "All";
   }

   public void updateTotem() {
      if (this.totem.getValue()) {
         this.handSlot = (double)(mc.field_1724.method_6067() + mc.field_1724.method_6032()) - this.getCrystal() <= this.forceHealth.getValue();
         if (!this.minePause.getValue() || !this.needSwitch || mc.field_1724.method_6115()) {
            if (mc.field_1724.method_6047().method_7909() != Items.field_8288) {
               if (this.handSlot) {
                  int item = findItemInventorySlot(Items.field_8288);
                  InventoryUtil.switchToSlot(this.slotPosition.getValueInt() - 1);
                  this.doSwap(item);
                  EntityUtil.syncInventory();
               }
            }
         }
      }
   }

   public void updateMine() {
      if (mc.field_1724.method_29504()) {
         this.needSwitch = false;
      }

      if (this.mineSlot.getValue()) {
         if (!this.handSlot || !this.totemPause.getValue()) {
            if (!this.pauseEat.getValue() || !mc.field_1724.method_6115() || mc.field_1724.method_6058() != Hand.field_5808) {
               if (this.mode.getValue() == MainHand_TQxYHQLcjCDwbwqUjIlv.All) {
                  if (SpeedMine.INSTANCE.isOn()) {
                     this.slot = SpeedMine.INSTANCE.getTool(SpeedMine.breakPos);
                     this.slot2 = SpeedMine.INSTANCE.getTool(SpeedMine.secondPos);
                  }

                  if (this.slot == -1 && this.slot2 == -1) {
                     return;
                  }

                  if (!this.needSwitch) {
                     this.old = mc.field_1724.method_31548().field_7545;
                  }

                  if (this.needSwitch && this.timer.passedMs(this.time.getValue() * 50.0)) {
                     mc.field_1724.method_31548().field_7545 = this.old;
                     this.needSwitch = false;
                  }

                  if (SpeedMine.breakPos != null
                     && (double)MathHelper.method_15355((float)EntityUtil.getEyesPos().method_1025(SpeedMine.breakPos.method_46558()))
                        <= SpeedMine.INSTANCE.range.getValue()
                     && (!BlockUtil.isAir(SpeedMine.breakPos) || SpeedMine.secondPos != null)
                     && (
                        SpeedMine.INSTANCE.done
                           || SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.slot, this.damage.getValue()))
                     )) {
                     this.needSwitch = true;
                     if (mc.field_1724.method_31548().field_7545 != this.slot) {
                        mc.field_1724.method_31548().field_7545 = this.slot;
                     }

                     this.timer.reset();
                  } else if (SpeedMine.breakPos == null
                     && SpeedMine.secondPos != null
                     && SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.slot, this.damage.getValue()))) {
                     this.needSwitch = true;
                     if (mc.field_1724.method_31548().field_7545 != this.slot2) {
                        mc.field_1724.method_31548().field_7545 = this.slot2;
                     }

                     this.timer.reset();
                  }
               } else {
                  this.slot2 = SpeedMine.INSTANCE.getTool(SpeedMine.secondPos);
                  if (this.slot2 == -1) {
                     return;
                  }

                  if (!this.needSwitch) {
                     this.old = mc.field_1724.method_31548().field_7545;
                  }

                  if (this.needSwitch && this.timer.passedMs(this.time.getValue() * 50.0)) {
                     mc.field_1724.method_31548().field_7545 = this.old;
                     this.needSwitch = false;
                  }

                  if (SpeedMine.INSTANCE.isOn()
                     && SpeedMine.secondPos != null
                     && SpeedMine.INSTANCE.secondTimer.passed(SpeedMine.INSTANCE.getBreakTime(SpeedMine.secondPos, this.slot2, this.damage.getValue()))) {
                     this.needSwitch = true;
                     if (mc.field_1724.method_31548().field_7545 != this.slot2) {
                        mc.field_1724.method_31548().field_7545 = this.slot2;
                     }

                     this.timer.reset();
                  }
               }
            }
         }
      }
   }

   public double getCrystal() {
      double maxDMG = 0.0;

      for (Entity entity : mc.field_1687.method_18112()) {
         if (entity instanceof EndCrystalEntity) {
            EndCrystalEntity endCrystal = (EndCrystalEntity)entity;
            if (!(endCrystal.method_5707(mc.field_1724.method_33571()) > 25.0)) {
               double dmg = (double)this.calculateDamage(endCrystal.method_24515().method_46558(), mc.field_1724, mc.field_1724);
               if (dmg > maxDMG) {
                  maxDMG = dmg;
               }
            }
         }
      }

      return maxDMG;
   }

   private void doSwap(int slot) {
      InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
   }

   public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
      return OyveyExplosionUtil.calculateDamage(pos.method_10216(), pos.method_10214(), pos.method_10215(), player, predict, 6.0F);
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         this.updateTotem();
         this.updateMine();
      }
   }
}
