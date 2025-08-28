package me.hextech.remapped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class KillEffects
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static KillEffects INSTANCE;
    public final SliderSetting gethealth = this.add(new SliderSetting("Health", 0, 0, 36));
    public final SliderSetting staytime = this.add(new SliderSetting("Time", 0.0, 0.0, 5000.0, 1.0));
    private final BooleanSetting LightningBolt = this.add(new BooleanSetting("LightningBolt", true));
    private final BooleanSetting remove = this.add(new BooleanSetting("Remove", true));
    private final Map<Entity, Long> renderEntities = new ConcurrentHashMap<Entity, Long>();
    private final Map<Entity, Long> lightingEntities = new ConcurrentHashMap<Entity, Long>();

    public KillEffects() {
        super("KillEffects", Module_JlagirAibYQgkHtbRnhw.Misc);
        INSTANCE = this;
    }

    @EventHandler
    public void onRender(Render3DEvent event) {
        if (this.LightningBolt.getValue()) {
            this.renderEntities.forEach((entity, time) -> {
                LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, KillEffects.mc.world);
                lightningEntity.refreshPositionAfterTeleport(entity.getX(), entity.getY(), entity.getZ());
                EntitySpawnS2CPacket pac = new EntitySpawnS2CPacket(lightningEntity);
                pac.apply(mc.getNetworkHandler());
                KillEffects.mc.world.playSound(KillEffects.mc.player, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0f, 0.16000001f);
                KillEffects.mc.world.playSound(KillEffects.mc.player, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0f, 0.1f);
                this.renderEntities.remove(entity);
                this.lightingEntities.put(entity, System.currentTimeMillis());
            });
        }
    }

    @EventHandler
    public void onTick(UpdateWalkingEvent event) {
        KillEffects.mc.world.getEntities().forEach(entity -> {
            if (!(entity instanceof PlayerEntity)) {
                return;
            }
            if (entity == KillEffects.mc.player || this.renderEntities.containsKey(entity) || this.lightingEntities.containsKey(entity)) {
                return;
            }
            if (((PlayerEntity)entity).getHealth() > this.gethealth.getValueFloat()) {
                return;
            }
            this.renderEntities.put(entity, System.currentTimeMillis());
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
