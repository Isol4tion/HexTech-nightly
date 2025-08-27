package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
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
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class Trajectories
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(575714484)));
    private final ColorSetting lcolor = this.add(new ColorSetting("LandedColor", new Color(575714484)));

    public Trajectories() {
        super("Trajectories", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    private boolean isThrowable(Item item) {
        return item instanceof EnderPearlItem || item instanceof TridentItem || item instanceof ExperienceBottleItem || item instanceof SnowballItem || item instanceof EggItem || item instanceof SplashPotionItem || item instanceof LingeringPotionItem;
    }

    private float getDistance(Item item) {
        return item instanceof BowItem ? 1.0f : 0.4f;
    }

    private float getThrowVelocity(Item item) {
        if (item instanceof SplashPotionItem || item instanceof LingeringPotionItem) {
            return 0.5f;
        }
        if (item instanceof ExperienceBottleItem) {
            return 0.59f;
        }
        if (item instanceof TridentItem) {
            return 2.0f;
        }
        return 1.5f;
    }

    private int getThrowPitch(Item item) {
        if (item instanceof SplashPotionItem || item instanceof LingeringPotionItem || item instanceof ExperienceBottleItem) {
            return 20;
        }
        return 0;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        Hand hand;
        if (Trajectories.nullCheck()) {
            return;
        }
        ItemStack mainHand = Trajectories.mc.field_1724.method_6047();
        ItemStack offHand = Trajectories.mc.field_1724.method_6079();
        if (mainHand.method_7909() instanceof BowItem || mainHand.method_7909() instanceof CrossbowItem || this.isThrowable(mainHand.method_7909())) {
            hand = Hand.field_5808;
        } else if (offHand.method_7909() instanceof BowItem || offHand.method_7909() instanceof CrossbowItem || this.isThrowable(offHand.method_7909())) {
            hand = Hand.field_5810;
        } else {
            return;
        }
        boolean prev_bob = (Boolean)Trajectories.mc.field_1690.method_42448().method_41753();
        Trajectories.mc.field_1690.method_42448().method_41748((Object)false);
        if (mainHand.method_7909() instanceof CrossbowItem && EnchantmentHelper.method_8225((Enchantment)Enchantments.field_9108, (ItemStack)mainHand) != 0) {
            this.calcTrajectory(matrixStack, hand == Hand.field_5810 ? offHand.method_7909() : mainHand.method_7909(), Trajectories.mc.field_1724.method_36454() - 10.0f);
            this.calcTrajectory(matrixStack, hand == Hand.field_5810 ? offHand.method_7909() : mainHand.method_7909(), Trajectories.mc.field_1724.method_36454());
            this.calcTrajectory(matrixStack, hand == Hand.field_5810 ? offHand.method_7909() : mainHand.method_7909(), Trajectories.mc.field_1724.method_36454() + 10.0f);
        } else {
            this.calcTrajectory(matrixStack, hand == Hand.field_5810 ? offHand.method_7909() : mainHand.method_7909(), Trajectories.mc.field_1724.method_36454());
        }
        Trajectories.mc.field_1690.method_42448().method_41748((Object)prev_bob);
    }

    private void calcTrajectory(MatrixStack matrixStack, Item item, float yaw) {
        double x = MathUtil.interpolate(Trajectories.mc.field_1724.field_6014, Trajectories.mc.field_1724.method_23317(), mc.method_1488());
        double y = MathUtil.interpolate(Trajectories.mc.field_1724.field_6036, Trajectories.mc.field_1724.method_23318(), mc.method_1488());
        double z = MathUtil.interpolate(Trajectories.mc.field_1724.field_5969, Trajectories.mc.field_1724.method_23321(), mc.method_1488());
        y = y + (double)Trajectories.mc.field_1724.method_18381(Trajectories.mc.field_1724.method_18376()) - 0.1000000014901161;
        if (item == Trajectories.mc.field_1724.method_6047().method_7909()) {
            x -= (double)(MathHelper.method_15362((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
            z -= (double)(MathHelper.method_15374((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
        } else {
            x += (double)(MathHelper.method_15362((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
            z += (double)(MathHelper.method_15374((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
        }
        float maxDist = this.getDistance(item);
        double motionX = -MathHelper.method_15374((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.method_15362((float)(Trajectories.mc.field_1724.method_36455() / 180.0f * (float)Math.PI)) * maxDist;
        double motionY = -MathHelper.method_15374((float)((Trajectories.mc.field_1724.method_36455() - (float)this.getThrowPitch(item)) / 180.0f * 3.141593f)) * maxDist;
        double motionZ = MathHelper.method_15362((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.method_15362((float)(Trajectories.mc.field_1724.method_36455() / 180.0f * (float)Math.PI)) * maxDist;
        float power = (float)Trajectories.mc.field_1724.method_6048() / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) {
            power = 1.0f;
        }
        float distance = MathHelper.method_15355((float)((float)(motionX * motionX + motionY * motionY + motionZ * motionZ)));
        motionX /= (double)distance;
        motionY /= (double)distance;
        motionZ /= (double)distance;
        float pow = (item instanceof BowItem ? power * 2.0f : (item instanceof CrossbowItem ? 2.2f : 1.0f)) * this.getThrowVelocity(item);
        motionX *= (double)pow;
        motionY *= (double)pow;
        motionZ *= (double)pow;
        if (!Trajectories.mc.field_1724.method_24828()) {
            motionY += Trajectories.mc.field_1724.method_18798().method_10214();
        }
        for (int i = 0; i < 300; ++i) {
            BlockHitResult bhr;
            Vec3d lastPos = new Vec3d(x, y, z);
            if (Trajectories.mc.field_1687.method_8320(new BlockPos((int)(x += motionX), (int)(y += motionY), (int)(z += motionZ))).method_26204() == Blocks.field_10382) {
                motionX *= 0.8;
                motionY *= 0.8;
                motionZ *= 0.8;
            } else {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }
            motionY = item instanceof BowItem ? (motionY -= (double)0.05f) : (Trajectories.mc.field_1724.method_6047().method_7909() instanceof CrossbowItem ? (motionY -= (double)0.05f) : (motionY -= (double)0.03f));
            Vec3d pos = new Vec3d(x, y, z);
            for (Entity ent : Trajectories.mc.field_1687.method_18112()) {
                if (ent instanceof ArrowEntity || ent.equals((Object)Trajectories.mc.field_1724) || !ent.method_5829().method_994(new Box(x - 0.4, y - 0.4, z - 0.4, x + 0.4, y + 0.4, z + 0.4))) continue;
                Render3DUtil.draw3DBox(matrixStack, ent.method_5829(), this.lcolor.getValue());
                break;
            }
            if ((bhr = Trajectories.mc.field_1687.method_17742(new RaycastContext(lastPos, pos, RaycastContext.ShapeType.field_17559, RaycastContext.FluidHandling.field_1348, (Entity)Trajectories.mc.field_1724))) != null && bhr.method_17783() == HitResult.Type.field_1332) {
                Render3DUtil.draw3DBox(matrixStack, new Box(bhr.method_17777()), this.lcolor.getValue());
                break;
            }
            if (y <= -65.0) break;
            if (motionX == 0.0 && motionY == 0.0 && motionZ == 0.0) continue;
            Render3DUtil.drawLine((float)lastPos.field_1352, (float)lastPos.field_1351, (float)lastPos.field_1350, (float)x, (float)y, (float)z, this.color.getValue(), 2.0f);
        }
    }
}
