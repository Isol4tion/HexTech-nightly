package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.hextech.HexTech;
import me.hextech.remapped.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CrystalDamage_eJITUTNYpCPnjaYYZUHH;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.StringSetting;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
        super("FakePlayer", Module_JlagirAibYQgkHtbRnhw.Misc);
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
        FakePlayer.mc.world.addEntity((Entity)fakePlayer);
        fakePlayer.copyPositionAndRotation((Entity)FakePlayer.mc.player);
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
            HexTech.POP.onTotemPop((PlayerEntity)fakePlayer);
            fakePlayer.setStackInHand(Hand.OFF_HAND, new ItemStack((ItemConvertible)Items.TOTEM_OF_UNDYING));
        }
        if (fakePlayer.isDead() && fakePlayer.tryUseTotem(FakePlayer.mc.world.getDamageSources().generic())) {
            fakePlayer.setHealth(10.0f);
            new EntityStatusS2CPacket((Entity)fakePlayer, 35).apply((ClientPlayPacketListener)FakePlayer.mc.player.networkHandler);
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
    public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (this.damage.getValue() && fakePlayer != null && FakePlayer.fakePlayer.hurtTime == 0) {
            Object t;
            if (this.autoTotem.getValue() && fakePlayer.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                fakePlayer.setStackInHand(Hand.OFF_HAND, new ItemStack((ItemConvertible)Items.TOTEM_OF_UNDYING));
            }
            if ((t = event.getPacket()) instanceof ExplosionS2CPacket) {
                ExplosionS2CPacket explosion = (ExplosionS2CPacket)t;
                Vec3d vec3d = new Vec3d(explosion.getX(), explosion.getY(), explosion.getZ());
                if (MathHelper.sqrt((float)((float)vec3d.squaredDistanceTo(fakePlayer.getPos()))) > 10.0f) {
                    return;
                }
                float damage = BlockUtil.getBlock(new BlockPosX(explosion.getX(), explosion.getY(), explosion.getZ())) == Blocks.RESPAWN_ANCHOR ? (float)AutoAnchor_MDcwoWYRcPYheLZJWRZK.INSTANCE.getAnchorDamage(new BlockPosX(explosion.getX(), explosion.getY(), explosion.getZ()), (PlayerEntity)fakePlayer, (PlayerEntity)fakePlayer) : CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateCrystalDamage(new Vec3d(explosion.getX(), explosion.getY(), explosion.getZ()), (PlayerEntity)fakePlayer, (PlayerEntity)fakePlayer);
                fakePlayer.onDamaged(FakePlayer.mc.world.getDamageSources().generic());
                if (fakePlayer.getABSORPTIONAmount() >= damage) {
                    fakePlayer.setAbsorptionAmount(fakePlayer.getABSORPTIONAmount() - damage);
                } else {
                    float damage2 = damage - fakePlayer.getABSORPTIONAmount();
                    fakePlayer.setAbsorptionAmount(0.0f);
                    fakePlayer.setHealth(fakePlayer.getHealth() - damage2);
                }
            }
            if (fakePlayer.isDead() && fakePlayer.tryUseTotem(FakePlayer.mc.world.getDamageSources().generic())) {
                fakePlayer.setHealth(10.0f);
                new EntityStatusS2CPacket((Entity)fakePlayer, 35).apply((ClientPlayPacketListener)FakePlayer.mc.player.networkHandler);
            }
        }
    }
}
