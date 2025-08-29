package me.hextech.mod.modules.impl.render;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.events.impl.ParticleEvent_KqEBTPXRWHlYeOSnrLnf;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.client.particle.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;

public class NoRender
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static NoRender INSTANCE;
    public final BooleanSetting weather = this.add(new BooleanSetting("Weather", true));
    public final BooleanSetting invisible = this.add(new BooleanSetting("Invisible", false));
    public final BooleanSetting potions = this.add(new BooleanSetting("Potions", true));
    public final BooleanSetting xp = this.add(new BooleanSetting("XP", true));
    public final BooleanSetting arrows = this.add(new BooleanSetting("Arrows", false));
    public final BooleanSetting eggs = this.add(new BooleanSetting("Eggs", false));
    public final BooleanSetting item = this.add(new BooleanSetting("Item", false));
    public final BooleanSetting armor = this.add(new BooleanSetting("Armor", false));
    public final BooleanSetting hurtCam = this.add(new BooleanSetting("HurtCam", true));
    public final BooleanSetting fireOverlay = this.add(new BooleanSetting("FireOverlay", true));
    public final BooleanSetting waterOverlay = this.add(new BooleanSetting("WaterOverlay", true));
    public final BooleanSetting blockOverlay = this.add(new BooleanSetting("BlockOverlay", true));
    public final BooleanSetting portal = this.add(new BooleanSetting("Portal", true));
    public final BooleanSetting totem = this.add(new BooleanSetting("Totem", true));
    public final BooleanSetting nausea = this.add(new BooleanSetting("Nausea", true));
    public final BooleanSetting blindness = this.add(new BooleanSetting("Blindness", true));
    public final BooleanSetting fog = this.add(new BooleanSetting("Fog", false));
    public final BooleanSetting darkness = this.add(new BooleanSetting("Darkness", true));
    public final BooleanSetting fireEntity = this.add(new BooleanSetting("EntityFire", true));
    public final BooleanSetting antiTitle = this.add(new BooleanSetting("Title", false));
    public final BooleanSetting antiPlayerCollision = this.add(new BooleanSetting("PlayerCollision", true));
    public final BooleanSetting effect = this.add(new BooleanSetting("Effect", true));
    public final BooleanSetting elderGuardian = this.add(new BooleanSetting("Guardian", false));
    public final BooleanSetting explosions = this.add(new BooleanSetting("Explosions", true));
    public final BooleanSetting campFire = this.add(new BooleanSetting("CampFire", false));
    public final BooleanSetting fireworks = this.add(new BooleanSetting("Fireworks", false));

    public NoRender() {
        super("NoRender", Category.Render);
        this.setDescription("Disables all overlays and potion effects.");
        INSTANCE = this;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        if (event.getPacket() instanceof TitleS2CPacket && this.antiTitle.getValue()) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        for (Entity ent : NoRender.mc.world.getEntities()) {
            if (ent instanceof PotionEntity && this.potions.getValue()) {
                NoRender.mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
            if (ent instanceof ExperienceBottleEntity && this.xp.getValue()) {
                NoRender.mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
            if (ent instanceof ArrowEntity && this.arrows.getValue()) {
                NoRender.mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
            if (ent instanceof EggEntity && this.eggs.getValue()) {
                NoRender.mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
            if (!(ent instanceof ItemEntity) || !this.item.getValue()) continue;
            NoRender.mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
        }
    }

    @EventHandler
    public void onParticle(ParticleEvent_KqEBTPXRWHlYeOSnrLnf.AddParticle event) {
        if (this.elderGuardian.getValue() && event.particle instanceof ElderGuardianAppearanceParticle) {
            event.setCancelled(true);
        } else if (this.explosions.getValue() && event.particle instanceof ExplosionLargeParticle) {
            event.setCancelled(true);
        } else if (this.campFire.getValue() && event.particle instanceof CampfireSmokeParticle) {
            event.setCancelled(true);
        } else if (this.fireworks.getValue() && (event.particle instanceof FireworksSparkParticle.FireworkParticle || event.particle instanceof FireworksSparkParticle.Flash)) {
            event.setCancelled(true);
        } else if (this.effect.getValue() && event.particle instanceof SpellParticle) {
            event.cancel();
        }
    }
}
