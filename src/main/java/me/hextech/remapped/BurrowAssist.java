package me.hextech.remapped;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BurrowAssist extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static BurrowAssist INSTANCE;
   public static Timer delay = new Timer();
   public final HashMap<PlayerEntity, Double> playerSpeeds = new HashMap();
   private final SliderSetting Delay = this.add(new SliderSetting("Delay", 100, 0, 1000));
   private final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", 4, 0, 10));
   private final BooleanSetting terrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true));
   private final SliderSetting cRange = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0));
   private final SliderSetting breakMinSelf = this.add(new SliderSetting("BreakSelf", 12.0, 0.0, 36.0));
   public BooleanSetting pause = this.add(new BooleanSetting("PauseEat", true));
   public SliderSetting speed = this.add(new SliderSetting("MaxSpeed", 8, 0, 20));
   public BooleanSetting ccheck = this.add(new BooleanSetting("CheckCrystal", true).setParent());
   public BooleanSetting mcheck = this.add(new BooleanSetting("CheckMine", true).setParent());
   public BooleanSetting checkPos = this.add(new BooleanSetting("CheckPos", true, v -> this.mcheck.isOpen()));
   public BooleanSetting mself = this.add(new BooleanSetting("Self", true));

   public BurrowAssist() {
      super("BurrowAssist", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
   }

   private static boolean canbur() {
      BlockPosX pos1 = new BlockPosX(mc.field_1724.method_23317() + 0.3, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() + 0.3);
      BlockPosX pos2 = new BlockPosX(mc.field_1724.method_23317() - 0.3, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() + 0.3);
      BlockPosX pos3 = new BlockPosX(mc.field_1724.method_23317() + 0.3, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() - 0.3);
      BlockPosX pos4 = new BlockPosX(mc.field_1724.method_23317() - 0.3, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() - 0.3);
      BlockPos playerPos = EntityUtil.getPlayerPos(true);
      return Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.canPlace(pos1)
         || Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.canPlace(pos2)
         || Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.canPlace(pos3)
         || Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.canPlace(pos4);
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         if (delay.passed((long)this.Delay.getValue())) {
            if (!this.pause.getValue() || !mc.field_1724.method_6115()) {
               if (!mc.field_1690.field_1903.method_1434()) {
                  if (canbur()) {
                     if (mc.field_1724.method_24828()
                        && this.getPlayerSpeed(mc.field_1724) < (double)this.speed.getValueInt()
                        && (
                           this.ccheck.getValue() && this.mcheck.getValue()
                              ? this.findcrystal() || this.checkmine(this.mself.getValue())
                              : (!this.ccheck.getValue() || this.findcrystal()) && (!this.mcheck.getValue() || this.checkmine(this.mself.getValue()))
                        )) {
                        if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                           return;
                        }

                        Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
                        delay.reset();
                     }
                  }
               }
            }
         }
      }
   }

   public boolean findcrystal() {
      PredictionSetting_XBpBEveLWEKUGQPHCCIS self = new PredictionSetting_XBpBEveLWEKUGQPHCCIS(mc.field_1724);

      for (Entity crystal : mc.field_1687.method_18112()) {
         if (crystal instanceof EndCrystalEntity
            && !(EntityUtil.getEyesPos().method_1022(crystal.method_19538()) > this.cRange.getValue())
            && !((double)this.calculateDamage(crystal.method_19538(), self.player, self.predict) < this.breakMinSelf.getValue())) {
            return true;
         }
      }

      return false;
   }

   public double getPlayerSpeed(PlayerEntity player) {
      return this.playerSpeeds.get(player) == null ? 0.0 : this.turnIntoKpH((Double)this.playerSpeeds.get(player));
   }

   public double turnIntoKpH(double input) {
      return (double)MathHelper.method_15355((float)input) * 71.2729367892;
   }

   public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
      if (this.terrainIgnore.getValue()) {
         CombatUtil.terrainIgnore = true;
      }

      float damage = 0.0F;
      damage = (float)MeteorExplosionUtil.crystalDamage(player, pos, predict);
      CombatUtil.terrainIgnore = false;
      return damage;
   }

   public boolean checkmine(boolean self) {
      ArrayList<BlockPos> pos = new ArrayList();
      pos.add(EntityUtil.getPlayerPos(true));
      pos.add(new BlockPosX(mc.field_1724.method_23317() + 0.4, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() + 0.4));
      pos.add(new BlockPosX(mc.field_1724.method_23317() - 0.4, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() + 0.4));
      pos.add(new BlockPosX(mc.field_1724.method_23317() + 0.4, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() - 0.4));
      pos.add(new BlockPosX(mc.field_1724.method_23317() - 0.4, mc.field_1724.method_23318() + 0.5, mc.field_1724.method_23321() - 0.4));

      for (MineManager breakData : new HashMap(me.hextech.HexTech.BREAK.breakMap).values()) {
         if (breakData != null && breakData.getEntity() != null) {
            for (BlockPos pos1 : pos) {
               if (pos1.equals(breakData.pos) && breakData.getEntity() != mc.field_1724) {
                  return true;
               }
            }
         }
      }

      if (!self) {
         return false;
      } else {
         for (BlockPos pos1x : pos) {
            if (pos1x.equals(SpeedMine.breakPos)) {
               return true;
            }
         }

         return false;
      }
   }
}
