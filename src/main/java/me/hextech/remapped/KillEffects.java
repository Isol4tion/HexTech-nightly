package me.hextech.remapped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class KillEffects extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static KillEffects INSTANCE;
   public final SliderSetting gethealth = this.add(new SliderSetting("Health", 0, 0, 36));
   public final SliderSetting staytime = this.add(new SliderSetting("Time", 0.0, 0.0, 5000.0, 1.0));
   private final BooleanSetting LightningBolt = this.add(new BooleanSetting("LightningBolt", true));
   private final BooleanSetting remove = this.add(new BooleanSetting("Remove", true));
   private final Map<Entity, Long> renderEntities = new ConcurrentHashMap();
   private final Map<Entity, Long> lightingEntities = new ConcurrentHashMap();

   public KillEffects() {
      super("KillEffects", Module_JlagirAibYQgkHtbRnhw.Misc);
      INSTANCE = this;
   }

   @EventHandler
   public void onRender(Render3DEvent event) {
      if (this.LightningBolt.getValue()) {
         this.renderEntities
            .forEach(
               (entity, time) -> {
                  LightningEntity lightningEntity = new LightningEntity(EntityType.field_6112, mc.field_1687);
                  lightningEntity.method_24203(entity.method_23317(), entity.method_23318(), entity.method_23321());
                  EntitySpawnS2CPacket pac = new EntitySpawnS2CPacket(lightningEntity);
                  pac.method_11178(mc.method_1562());
                  mc.field_1687
                     .method_43128(
                        mc.field_1724,
                        entity.method_23317(),
                        entity.method_23318(),
                        entity.method_23321(),
                        SoundEvents.field_14865,
                        SoundCategory.field_15252,
                        10000.0F,
                        0.16000001F
                     );
                  mc.field_1687
                     .method_43128(
                        mc.field_1724,
                        entity.method_23317(),
                        entity.method_23318(),
                        entity.method_23321(),
                        SoundEvents.field_14956,
                        SoundCategory.field_15252,
                        2.0F,
                        0.1F
                     );
                  this.renderEntities.remove(entity);
                  this.lightingEntities.put(entity, System.currentTimeMillis());
               }
            );
      }
   }

   @EventHandler
   public void onTick(UpdateWalkingEvent event) {
      mc.field_1687.method_18112().forEach(entity -> {
         if (entity instanceof PlayerEntity) {
            if (entity != mc.field_1724 && !this.renderEntities.containsKey(entity) && !this.lightingEntities.containsKey(entity)) {
               if (!(((PlayerEntity)entity).method_6032() > this.gethealth.getValueFloat())) {
                  this.renderEntities.put(entity, System.currentTimeMillis());
               }
            }
         }
      });
      if (!this.lightingEntities.isEmpty()) {
         this.lightingEntities.forEach((entity, time) -> {
            if ((float)(System.currentTimeMillis() - time) > this.staytime.getValueFloat() && this.remove.getValue()) {
               this.lightingEntities.remove(entity);
            }
         });
      }
   }
}
