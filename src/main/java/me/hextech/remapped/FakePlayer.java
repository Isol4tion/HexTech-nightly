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
        fakePlayer.method_5719((Entity)FakePlayer.mc.player);
        FakePlayer.fakePlayer.field_6283 = FakePlayer.mc.player.field_6283;
        FakePlayer.fakePlayer.field_6241 = FakePlayer.mc.player.field_6241;
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.field_5924, 9999, 2));
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.field_5898, 9999, 4));
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.field_5907, 9999, 1));
    }

    @Override
    public void onUpdate() {
        if (fakePlayer == null || fakePlayer.method_29504() || FakePlayer.fakePlayer.field_17892 != FakePlayer.mc.world) {
            this.disable();
            return;
        }
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.field_5924, 9999, 2));
        fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.field_5898, 9999, 4));
        if (this.gApple.getValue() && this.timer.passedMs(4000L)) {
            fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.field_5907, 9999, 1));
            this.timer.reset();
            fakePlayer.method_6073(16.0f);
        }
        if (this.autoTotem.getValue() && fakePlayer.method_6079().getItem() != Items.TOTEM_OF_UNDYING) {
            HexTech.POP.onTotemPop((PlayerEntity)fakePlayer);
            fakePlayer.method_6122(Hand.OFF_HAND, new ItemStack((ItemConvertible)Items.TOTEM_OF_UNDYING));
        }
        if (fakePlayer.method_29504() && fakePlayer.method_6095(FakePlayer.mc.world.method_48963().generic())) {
            fakePlayer.method_6033(10.0f);
            new EntityStatusS2CPacket((Entity)fakePlayer, 35).apply((ClientPlayPacketListener)FakePlayer.mc.player.networkHandler);
        }
    }

    @Override
    public void onDisable() {
        if (fakePlayer == null) {
            return;
        }
        fakePlayer.method_5768();
        fakePlayer.method_31745(Entity.RemovalReason.KILLED);
        fakePlayer.method_36209();
        fakePlayer = null;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (this.damage.getValue() && fakePlayer != null && FakePlayer.fakePlayer.field_6235 == 0) {
            Object t;
            if (this.autoTotem.getValue() && fakePlayer.method_6079().getItem() != Items.TOTEM_OF_UNDYING) {
                fakePlayer.method_6122(Hand.OFF_HAND, new ItemStack((ItemConvertible)Items.TOTEM_OF_UNDYING));
            }
            if ((t = event.getPacket()) instanceof ExplosionS2CPacket) {
                ExplosionS2CPacket explosion = (ExplosionS2CPacket)t;
                Vec3d vec3d = new Vec3d(explosion.method_11475(), explosion.method_11477(), explosion.method_11478());
                if (MathHelper.sqrt((float)((float)vec3d.squaredDistanceTo(fakePlayer.method_19538()))) > 10.0f) {
                    return;
                }
                float damage = BlockUtil.getBlock(new BlockPosX(explosion.method_11475(), explosion.method_11477(), explosion.method_11478())) == Blocks.RESPAWN_ANCHOR ? (float)AutoAnchor_MDcwoWYRcPYheLZJWRZK.INSTANCE.getAnchorDamage(new BlockPosX(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()), (PlayerEntity)fakePlayer, (PlayerEntity)fakePlayer) : CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateCrystalDamage(new Vec3d(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()), (PlayerEntity)fakePlayer, (PlayerEntity)fakePlayer);
                fakePlayer.method_48922(FakePlayer.mc.world.method_48963().generic());
                if (fakePlayer.method_6067() >= damage) {
                    fakePlayer.method_6073(fakePlayer.method_6067() - damage);
                } else {
                    float damage2 = damage - fakePlayer.method_6067();
                    fakePlayer.method_6073(0.0f);
                    fakePlayer.method_6033(fakePlayer.method_6032() - damage2);
                }
            }
            if (fakePlayer.method_29504() && fakePlayer.method_6095(FakePlayer.mc.world.method_48963().generic())) {
                fakePlayer.method_6033(10.0f);
                new EntityStatusS2CPacket((Entity)fakePlayer, 35).apply((ClientPlayPacketListener)FakePlayer.mc.player.networkHandler);
            }
        }
    }
}
