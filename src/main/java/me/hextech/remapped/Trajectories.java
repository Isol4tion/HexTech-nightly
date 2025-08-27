package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class Trajectories extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final ColorSetting color = this.add(new ColorSetting("Color", new Color(575714484)));
   private final ColorSetting lcolor = this.add(new ColorSetting("LandedColor", new Color(575714484)));

   public Trajectories() {
      super("Trajectories", Module_JlagirAibYQgkHtbRnhw.Render);
   }

   private boolean isThrowable(Item item) {
      return item instanceof EnderPearlItem
         || item instanceof TridentItem
         || item instanceof ExperienceBottleItem
         || item instanceof SnowballItem
         || item instanceof EggItem
         || item instanceof SplashPotionItem
         || item instanceof LingeringPotionItem;
   }

   private float getDistance(Item item) {
      return item instanceof BowItem ? 1.0F : 0.4F;
   }

   private float getThrowVelocity(Item item) {
      if (item instanceof SplashPotionItem || item instanceof LingeringPotionItem) {
         return 0.5F;
      } else if (item instanceof ExperienceBottleItem) {
         return 0.59F;
      } else {
         return item instanceof TridentItem ? 2.0F : 1.5F;
      }
   }

   private int getThrowPitch(Item item) {
      return !(item instanceof SplashPotionItem) && !(item instanceof LingeringPotionItem) && !(item instanceof ExperienceBottleItem) ? 0 : 20;
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (!nullCheck()) {
         ItemStack mainHand = mc.field_1724.method_6047();
         ItemStack offHand = mc.field_1724.method_6079();
         Hand hand;
         if (!(mainHand.method_7909() instanceof BowItem) && !(mainHand.method_7909() instanceof CrossbowItem) && !this.isThrowable(mainHand.method_7909())) {
            if (!(offHand.method_7909() instanceof BowItem) && !(offHand.method_7909() instanceof CrossbowItem) && !this.isThrowable(offHand.method_7909())) {
               return;
            }

            hand = Hand.field_5810;
         } else {
            hand = Hand.field_5808;
         }

         boolean prev_bob = (Boolean)mc.field_1690.method_42448().method_41753();
         mc.field_1690.method_42448().method_41748(false);
         if (mainHand.method_7909() instanceof CrossbowItem && EnchantmentHelper.method_8225(Enchantments.field_9108, mainHand) != 0) {
            this.calcTrajectory(matrixStack, hand == Hand.field_5810 ? offHand.method_7909() : mainHand.method_7909(), mc.field_1724.method_36454() - 10.0F);
            this.calcTrajectory(matrixStack, hand == Hand.field_5810 ? offHand.method_7909() : mainHand.method_7909(), mc.field_1724.method_36454());
            this.calcTrajectory(matrixStack, hand == Hand.field_5810 ? offHand.method_7909() : mainHand.method_7909(), mc.field_1724.method_36454() + 10.0F);
         } else {
            this.calcTrajectory(matrixStack, hand == Hand.field_5810 ? offHand.method_7909() : mainHand.method_7909(), mc.field_1724.method_36454());
         }

         mc.field_1690.method_42448().method_41748(prev_bob);
      }
   }

   private void calcTrajectory(MatrixStack matrixStack, Item item, float yaw) {
      double x = MathUtil.interpolate(mc.field_1724.field_6014, mc.field_1724.method_23317(), mc.method_1488());
      double y = MathUtil.interpolate(mc.field_1724.field_6036, mc.field_1724.method_23318(), mc.method_1488());
      double z = MathUtil.interpolate(mc.field_1724.field_5969, mc.field_1724.method_23321(), mc.method_1488());
      y = y + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()) - 0.1000000014901161;
      if (item == mc.field_1724.method_6047().method_7909()) {
         x -= (double)(MathHelper.method_15362(yaw / 180.0F * (float) Math.PI) * 0.16F);
         z -= (double)(MathHelper.method_15374(yaw / 180.0F * (float) Math.PI) * 0.16F);
      } else {
         x += (double)(MathHelper.method_15362(yaw / 180.0F * (float) Math.PI) * 0.16F);
         z += (double)(MathHelper.method_15374(yaw / 180.0F * (float) Math.PI) * 0.16F);
      }

      float maxDist = this.getDistance(item);
      double motionX = (double)(
         -MathHelper.method_15374(yaw / 180.0F * (float) Math.PI) * MathHelper.method_15362(mc.field_1724.method_36455() / 180.0F * (float) Math.PI) * maxDist
      );
      double motionY = (double)(-MathHelper.method_15374((mc.field_1724.method_36455() - (float)this.getThrowPitch(item)) / 180.0F * 3.141593F) * maxDist);
      double motionZ = (double)(
         MathHelper.method_15362(yaw / 180.0F * (float) Math.PI) * MathHelper.method_15362(mc.field_1724.method_36455() / 180.0F * (float) Math.PI) * maxDist
      );
      float power = (float)mc.field_1724.method_6048() / 20.0F;
      power = (power * power + power * 2.0F) / 3.0F;
      if (power > 1.0F) {
         power = 1.0F;
      }

      float distance = MathHelper.method_15355((float)(motionX * motionX + motionY * motionY + motionZ * motionZ));
      motionX /= (double)distance;
      motionY /= (double)distance;
      motionZ /= (double)distance;
      float pow = (item instanceof BowItem ? power * 2.0F : (item instanceof CrossbowItem ? 2.2F : 1.0F)) * this.getThrowVelocity(item);
      motionX *= (double)pow;
      motionY *= (double)pow;
      motionZ *= (double)pow;
      if (!mc.field_1724.method_24828()) {
         motionY += mc.field_1724.method_18798().method_10214();
      }

      for (int i = 0; i < 300; i++) {
         Vec3d lastPos = new Vec3d(x, y, z);
         x += motionX;
         y += motionY;
         z += motionZ;
         if (mc.field_1687.method_8320(new BlockPos((int)x, (int)y, (int)z)).method_26204() == Blocks.field_10382) {
            motionX *= 0.8;
            motionY *= 0.8;
            motionZ *= 0.8;
         } else {
            motionX *= 0.99;
            motionY *= 0.99;
            motionZ *= 0.99;
         }

         if (item instanceof BowItem) {
            motionY -= 0.05F;
         } else if (mc.field_1724.method_6047().method_7909() instanceof CrossbowItem) {
            motionY -= 0.05F;
         } else {
            motionY -= 0.03F;
         }

         Vec3d pos = new Vec3d(x, y, z);

         for (Entity ent : mc.field_1687.method_18112()) {
            if (!(ent instanceof ArrowEntity)
               && !ent.equals(mc.field_1724)
               && ent.method_5829().method_994(new Box(x - 0.4, y - 0.4, z - 0.4, x + 0.4, y + 0.4, z + 0.4))) {
               Render3DUtil.draw3DBox(matrixStack, ent.method_5829(), this.lcolor.getValue());
               break;
            }
         }

         BlockHitResult bhr = mc.field_1687.method_17742(new RaycastContext(lastPos, pos, ShapeType.field_17559, FluidHandling.field_1348, mc.field_1724));
         if (bhr != null && bhr.method_17783() == Type.field_1332) {
            Render3DUtil.draw3DBox(matrixStack, new Box(bhr.method_17777()), this.lcolor.getValue());
            break;
         }

         if (y <= -65.0) {
            break;
         }

         if (motionX != 0.0 || motionY != 0.0 || motionZ != 0.0) {
            Render3DUtil.drawLine(
               (double)((float)lastPos.field_1352),
               (double)((float)lastPos.field_1351),
               (double)((float)lastPos.field_1350),
               (double)((float)x),
               (double)((float)y),
               (double)((float)z),
               this.color.getValue(),
               2.0F
            );
         }
      }
   }
}
