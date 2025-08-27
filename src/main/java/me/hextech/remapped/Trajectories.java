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
        ItemStack mainHand = Trajectories.mc.player.method_6047();
        ItemStack offHand = Trajectories.mc.player.method_6079();
        if (mainHand.getItem() instanceof BowItem || mainHand.getItem() instanceof CrossbowItem || this.isThrowable(mainHand.getItem())) {
            hand = Hand.MAIN_HAND;
        } else if (offHand.getItem() instanceof BowItem || offHand.getItem() instanceof CrossbowItem || this.isThrowable(offHand.getItem())) {
            hand = Hand.OFF_HAND;
        } else {
            return;
        }
        boolean prev_bob = (Boolean)Trajectories.mc.options.getBobView().getValue();
        Trajectories.mc.options.getBobView().setValue((Object)false);
        if (mainHand.getItem() instanceof CrossbowItem && EnchantmentHelper.method_8225((Enchantment)Enchantments.field_9108, (ItemStack)mainHand) != 0) {
            this.calcTrajectory(matrixStack, hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), Trajectories.mc.player.method_36454() - 10.0f);
            this.calcTrajectory(matrixStack, hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), Trajectories.mc.player.method_36454());
            this.calcTrajectory(matrixStack, hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), Trajectories.mc.player.method_36454() + 10.0f);
        } else {
            this.calcTrajectory(matrixStack, hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), Trajectories.mc.player.method_36454());
        }
        Trajectories.mc.options.getBobView().setValue((Object)prev_bob);
    }

    private void calcTrajectory(MatrixStack matrixStack, Item item, float yaw) {
        double x = MathUtil.interpolate(Trajectories.mc.player.field_6014, Trajectories.mc.player.method_23317(), mc.method_1488());
        double y = MathUtil.interpolate(Trajectories.mc.player.field_6036, Trajectories.mc.player.method_23318(), mc.method_1488());
        double z = MathUtil.interpolate(Trajectories.mc.player.field_5969, Trajectories.mc.player.method_23321(), mc.method_1488());
        y = y + (double)Trajectories.mc.player.method_18381(Trajectories.mc.player.method_18376()) - 0.1000000014901161;
        if (item == Trajectories.mc.player.method_6047().getItem()) {
            x -= (double)(MathHelper.cos((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
            z -= (double)(MathHelper.sin((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
        } else {
            x += (double)(MathHelper.cos((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
            z += (double)(MathHelper.sin((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
        }
        float maxDist = this.getDistance(item);
        double motionX = -MathHelper.sin((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.cos((float)(Trajectories.mc.player.method_36455() / 180.0f * (float)Math.PI)) * maxDist;
        double motionY = -MathHelper.sin((float)((Trajectories.mc.player.method_36455() - (float)this.getThrowPitch(item)) / 180.0f * 3.141593f)) * maxDist;
        double motionZ = MathHelper.cos((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.cos((float)(Trajectories.mc.player.method_36455() / 180.0f * (float)Math.PI)) * maxDist;
        float power = (float)Trajectories.mc.player.method_6048() / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) {
            power = 1.0f;
        }
        float distance = MathHelper.sqrt((float)((float)(motionX * motionX + motionY * motionY + motionZ * motionZ)));
        motionX /= (double)distance;
        motionY /= (double)distance;
        motionZ /= (double)distance;
        float pow = (item instanceof BowItem ? power * 2.0f : (item instanceof CrossbowItem ? 2.2f : 1.0f)) * this.getThrowVelocity(item);
        motionX *= (double)pow;
        motionY *= (double)pow;
        motionZ *= (double)pow;
        if (!Trajectories.mc.player.method_24828()) {
            motionY += Trajectories.mc.player.method_18798().method_10214();
        }
        for (int i = 0; i < 300; ++i) {
            BlockHitResult bhr;
            Vec3d lastPos = new Vec3d(x, y, z);
            if (Trajectories.mc.world.method_8320(new BlockPos((int)(x += motionX), (int)(y += motionY), (int)(z += motionZ))).method_26204() == Blocks.WATER) {
                motionX *= 0.8;
                motionY *= 0.8;
                motionZ *= 0.8;
            } else {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }
            motionY = item instanceof BowItem ? (motionY -= (double)0.05f) : (Trajectories.mc.player.method_6047().getItem() instanceof CrossbowItem ? (motionY -= (double)0.05f) : (motionY -= (double)0.03f));
            Vec3d pos = new Vec3d(x, y, z);
            for (Entity ent : Trajectories.mc.world.getEntities()) {
                if (ent instanceof ArrowEntity || ent.equals((Object)Trajectories.mc.player) || !ent.method_5829().intersects(new Box(x - 0.4, y - 0.4, z - 0.4, x + 0.4, y + 0.4, z + 0.4))) continue;
                Render3DUtil.draw3DBox(matrixStack, ent.method_5829(), this.lcolor.getValue());
                break;
            }
            if ((bhr = Trajectories.mc.world.method_17742(new RaycastContext(lastPos, pos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)Trajectories.mc.player))) != null && bhr.method_17783() == HitResult.Type.BLOCK) {
                Render3DUtil.draw3DBox(matrixStack, new Box(bhr.getBlockPos()), this.lcolor.getValue());
                break;
            }
            if (y <= -65.0) break;
            if (motionX == 0.0 && motionY == 0.0 && motionZ == 0.0) continue;
            Render3DUtil.drawLine((float)lastPos.x, (float)lastPos.y, (float)lastPos.z, (float)x, (float)y, (float)z, this.color.getValue(), 2.0f);
        }
    }
}
