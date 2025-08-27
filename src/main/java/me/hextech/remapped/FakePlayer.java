package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FakePlayer extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
      if (nullCheck()) {
         this.disable();
      } else {
         fakePlayer = new OtherClientPlayerEntity(mc.field_1687, new GameProfile(UUID.fromString("11451466-6666-6666-6666-666666666600"), this.name.getValue()));
         fakePlayer.method_31548().method_7377(mc.field_1724.method_31548());
         mc.field_1687.method_53875(fakePlayer);
         fakePlayer.method_5719(mc.field_1724);
         fakePlayer.field_6283 = mc.field_1724.field_6283;
         fakePlayer.field_6241 = mc.field_1724.field_6241;
         fakePlayer.method_6092(new StatusEffectInstance(StatusEffects.field_5924, 9999, 2));
         fakePlayer.method_6092(new StatusEffectInstance(StatusEffects.field_5898, 9999, 4));
         fakePlayer.method_6092(new StatusEffectInstance(StatusEffects.field_5907, 9999, 1));
      }
   }

   @Override
   public void onUpdate() {
      if (fakePlayer != null && !fakePlayer.method_29504() && fakePlayer.field_17892 == mc.field_1687) {
         fakePlayer.method_6092(new StatusEffectInstance(StatusEffects.field_5924, 9999, 2));
         fakePlayer.method_6092(new StatusEffectInstance(StatusEffects.field_5898, 9999, 4));
         if (this.gApple.getValue() && this.timer.passedMs(4000L)) {
            fakePlayer.method_6092(new StatusEffectInstance(StatusEffects.field_5907, 9999, 1));
            this.timer.reset();
            fakePlayer.method_6073(16.0F);
         }

         if (this.autoTotem.getValue() && fakePlayer.method_6079().method_7909() != Items.field_8288) {
            me.hextech.HexTech.POP.onTotemPop(fakePlayer);
            fakePlayer.method_6122(Hand.field_5810, new ItemStack(Items.field_8288));
         }

         if (fakePlayer.method_29504() && fakePlayer.method_6095(mc.field_1687.method_48963().method_48830())) {
            fakePlayer.method_6033(10.0F);
            new EntityStatusS2CPacket(fakePlayer, (byte)35).method_11471(mc.field_1724.field_3944);
         }
      } else {
         this.disable();
      }
   }

   @Override
   public void onDisable() {
      if (fakePlayer != null) {
         fakePlayer.method_5768();
         fakePlayer.method_31745(RemovalReason.field_26998);
         fakePlayer.method_36209();
         fakePlayer = null;
      }
   }

   @EventHandler
   public void onPacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (this.damage.getValue() && fakePlayer != null && fakePlayer.field_6235 == 0) {
         if (this.autoTotem.getValue() && fakePlayer.method_6079().method_7909() != Items.field_8288) {
            fakePlayer.method_6122(Hand.field_5810, new ItemStack(Items.field_8288));
         }

         if (event.getPacket() instanceof ExplosionS2CPacket explosion) {
            if (MathHelper.method_15355(
                  (float)new Vec3d(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()).method_1025(fakePlayer.method_19538())
               )
               > 10.0F) {
               return;
            }

            float damage;
            if (BlockUtil.getBlock(new BlockPosX(explosion.method_11475(), explosion.method_11477(), explosion.method_11478())) == Blocks.field_23152) {
               damage = (float)AutoAnchor_MDcwoWYRcPYheLZJWRZK.INSTANCE
                  .getAnchorDamage(new BlockPosX(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()), fakePlayer, fakePlayer);
            } else {
               damage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateCrystalDamage(
                  new Vec3d(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()), fakePlayer, fakePlayer
               );
            }

            fakePlayer.method_48922(mc.field_1687.method_48963().method_48830());
            if (fakePlayer.method_6067() >= damage) {
               fakePlayer.method_6073(fakePlayer.method_6067() - damage);
            } else {
               float damage2 = damage - fakePlayer.method_6067();
               fakePlayer.method_6073(0.0F);
               fakePlayer.method_6033(fakePlayer.method_6032() - damage2);
            }
         }

         if (fakePlayer.method_29504() && fakePlayer.method_6095(mc.field_1687.method_48963().method_48830())) {
            fakePlayer.method_6033(10.0F);
            new EntityStatusS2CPacket(fakePlayer, (byte)35).method_11471(mc.field_1724.field_3944);
         }
      }
   }
}
