package me.hextech.mod.modules.impl.misc;

import com.mojang.authlib.GameProfile;
import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.utils.combat.CrystalDamage_eJITUTNYpCPnjaYYZUHH;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.combat.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.StringSetting;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class FakePlayer
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static FakePlayer INSTANCE;
    public static OtherClientPlayerEntity fakePlayer;
    private final StringSetting name = this.add(new StringSetting("Name", "7XED1337"));
    private final BooleanSetting damage = this.add(new BooleanSetting("Damage", true));
    private final BooleanSetting autoTotem = this.add(new BooleanSetting("OffHand", true));
    private final BooleanSetting gApple = this.add(new BooleanSetting("GApple", true));
    private final Timer timer = new Timer();
    int pops = 0;

    public FakePlayer() {
        super("FakePlayer", Category.Misc);
        this.setDescription("Spawn fakeplayer.");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.name.getValue();
    }

    @Override
    public void onEnable() {
        this.pops = 0;
        if (FakePlayer.nullCheck()) {
            this.disable();
            return;
        }
        fakePlayer = new OtherClientPlayerEntity(FakePlayer.mc.world, new GameProfile(UUID.fromString("11451466-6666-6666-6666-666666666600"), this.name.getValue()));
        fakePlayer.getInventory().clone(FakePlayer.mc.player.getInventory());
        FakePlayer.mc.world.addEntity(fakePlayer);
        fakePlayer.copyPositionAndRotation(FakePlayer.mc.player);
        FakePlayer.fakePlayer.bodyYaw = FakePlayer.mc.player.bodyYaw;
        FakePlayer.fakePlayer.headYaw = FakePlayer.mc.player.headYaw;
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 9999, 2));
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 9999, 4));
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 9999, 1));
    }

    @Override
    public void onUpdate() {
        if (fakePlayer == null || fakePlayer.isDead() || FakePlayer.fakePlayer.clientWorld != FakePlayer.mc.world) {
            this.disable();
            return;
        }
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 9999, 2));
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 9999, 4));
        if (this.gApple.getValue() && this.timer.passedMs(4000L)) {
            fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 9999, 1));
            this.timer.reset();
            fakePlayer.setAbsorptionAmount(16.0f);
        }
        if (this.autoTotem.getValue() && fakePlayer.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            HexTech.POP.onTotemPop(fakePlayer);
            fakePlayer.setStackInHand(Hand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
        }
        if (fakePlayer.isDead() && fakePlayer.tryUseTotem(FakePlayer.mc.world.getDamageSources().generic())) {
            fakePlayer.setHealth(10.0f);
            new EntityStatusS2CPacket(fakePlayer, (byte) 35).apply(FakePlayer.mc.player.networkHandler);
        }
    }

    @Override
    public void onDisable() {
        if (fakePlayer == null) {
            return;
        }
        fakePlayer.kill();
        fakePlayer.setRemoved(Entity.RemovalReason.KILLED);
        fakePlayer.onRemoved();
        fakePlayer = null;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        if (this.damage.getValue() && fakePlayer != null && FakePlayer.fakePlayer.hurtTime == 0) {
            Object t;
            if (this.autoTotem.getValue() && fakePlayer.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                fakePlayer.setStackInHand(Hand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
            }
            if ((t = event.getPacket()) instanceof ExplosionS2CPacket) {
                ExplosionS2CPacket explosion = (ExplosionS2CPacket)t;
                Vec3d vec3d = new Vec3d(explosion.getX(), explosion.getY(), explosion.getZ());
                if (MathHelper.sqrt((float)vec3d.squaredDistanceTo(fakePlayer.getPos())) > 10.0f) {
                    return;
                }
                float damage = BlockUtil.getBlock(new BlockPosX(explosion.getX(), explosion.getY(), explosion.getZ())) == Blocks.RESPAWN_ANCHOR ? (float)AutoAnchor_MDcwoWYRcPYheLZJWRZK.INSTANCE.getAnchorDamage(new BlockPosX(explosion.getX(), explosion.getY(), explosion.getZ()), fakePlayer, fakePlayer) : CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateCrystalDamage(new Vec3d(explosion.getX(), explosion.getY(), explosion.getZ()), fakePlayer, fakePlayer);
                fakePlayer.onDamaged(FakePlayer.mc.world.getDamageSources().generic());
                if (fakePlayer.getAbsorptionAmount() >= damage) {
                    fakePlayer.setAbsorptionAmount(fakePlayer.getAbsorptionAmount() - damage);
                } else {
                    float damage2 = damage - fakePlayer.getAbsorptionAmount();
                    fakePlayer.setAbsorptionAmount(0.0f);
                    fakePlayer.setHealth(fakePlayer.getHealth() - damage2);
                }
            }
            if (fakePlayer.isDead() && fakePlayer.tryUseTotem(FakePlayer.mc.world.getDamageSources().generic())) {
                fakePlayer.setHealth(10.0f);
                new EntityStatusS2CPacket(fakePlayer, (byte) 35).apply(FakePlayer.mc.player.networkHandler);
            }
        }
    }
}
