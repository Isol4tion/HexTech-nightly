package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.mod.modules.settings.impl.ColorSetting;
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
        ItemStack mainHand = Trajectories.mc.player.getMainHandStack();
        ItemStack offHand = Trajectories.mc.player.getOffHandStack();
        if (mainHand.getItem() instanceof BowItem || mainHand.getItem() instanceof CrossbowItem || this.isThrowable(mainHand.getItem())) {
            hand = Hand.MAIN_HAND;
        } else if (offHand.getItem() instanceof BowItem || offHand.getItem() instanceof CrossbowItem || this.isThrowable(offHand.getItem())) {
            hand = Hand.OFF_HAND;
        } else {
            return;
        }
        boolean prev_bob = Trajectories.mc.options.getBobView().getValue();
        Trajectories.mc.options.getBobView().setValue(false);
        if (mainHand.getItem() instanceof CrossbowItem && EnchantmentHelper.getLevel(Enchantments.MULTISHOT, mainHand) != 0) {
            this.calcTrajectory(matrixStack, hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), Trajectories.mc.player.getYaw() - 10.0f);
            this.calcTrajectory(matrixStack, hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), Trajectories.mc.player.getYaw());
            this.calcTrajectory(matrixStack, hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), Trajectories.mc.player.getYaw() + 10.0f);
        } else {
            this.calcTrajectory(matrixStack, hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), Trajectories.mc.player.getYaw());
        }
        Trajectories.mc.options.getBobView().setValue(prev_bob);
    }

    private void calcTrajectory(MatrixStack matrixStack, Item item, float yaw) {
        double x = MathUtil.interpolate(Trajectories.mc.player.prevX, Trajectories.mc.player.getX(), mc.getTickDelta());
        double y = MathUtil.interpolate(Trajectories.mc.player.prevY, Trajectories.mc.player.getY(), mc.getTickDelta());
        double z = MathUtil.interpolate(Trajectories.mc.player.prevZ, Trajectories.mc.player.getZ(), mc.getTickDelta());
        y = y + (double)Trajectories.mc.player.getEyeHeight(Trajectories.mc.player.getPose()) - 0.1000000014901161;
        if (item == Trajectories.mc.player.getMainHandStack().getItem()) {
            x -= MathHelper.cos(yaw / 180.0f * (float)Math.PI) * 0.16f;
            z -= MathHelper.sin(yaw / 180.0f * (float)Math.PI) * 0.16f;
        } else {
            x += MathHelper.cos(yaw / 180.0f * (float)Math.PI) * 0.16f;
            z += MathHelper.sin(yaw / 180.0f * (float)Math.PI) * 0.16f;
        }
        float maxDist = this.getDistance(item);
        double motionX = -MathHelper.sin(yaw / 180.0f * (float)Math.PI) * MathHelper.cos(Trajectories.mc.player.getPitch() / 180.0f * (float)Math.PI) * maxDist;
        double motionY = -MathHelper.sin((Trajectories.mc.player.getPitch() - (float)this.getThrowPitch(item)) / 180.0f * 3.141593f) * maxDist;
        double motionZ = MathHelper.cos(yaw / 180.0f * (float)Math.PI) * MathHelper.cos(Trajectories.mc.player.getPitch() / 180.0f * (float)Math.PI) * maxDist;
        float power = (float)Trajectories.mc.player.getItemUseTime() / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) {
            power = 1.0f;
        }
        float distance = MathHelper.sqrt((float)(motionX * motionX + motionY * motionY + motionZ * motionZ));
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        float pow = (item instanceof BowItem ? power * 2.0f : (item instanceof CrossbowItem ? 2.2f : 1.0f)) * this.getThrowVelocity(item);
        motionX *= pow;
        motionY *= pow;
        motionZ *= pow;
        if (!Trajectories.mc.player.isOnGround()) {
            motionY += Trajectories.mc.player.getVelocity().getY();
        }
        for (int i = 0; i < 300; ++i) {
            BlockHitResult bhr;
            Vec3d lastPos = new Vec3d(x, y, z);
            if (Trajectories.mc.world.getBlockState(new BlockPos((int)(x += motionX), (int)(y += motionY), (int)(z += motionZ))).getBlock() == Blocks.WATER) {
                motionX *= 0.8;
                motionY *= 0.8;
                motionZ *= 0.8;
            } else {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }
            motionY = item instanceof BowItem ? (motionY -= 0.05f) : (Trajectories.mc.player.getMainHandStack().getItem() instanceof CrossbowItem ? (motionY -= 0.05f) : (motionY -= 0.03f));
            Vec3d pos = new Vec3d(x, y, z);
            for (Entity ent : Trajectories.mc.world.getEntities()) {
                if (ent instanceof ArrowEntity || ent.equals(Trajectories.mc.player) || !ent.getBoundingBox().intersects(new Box(x - 0.4, y - 0.4, z - 0.4, x + 0.4, y + 0.4, z + 0.4))) continue;
                Render3DUtil.draw3DBox(matrixStack, ent.getBoundingBox(), this.lcolor.getValue());
                break;
            }
            if ((bhr = Trajectories.mc.world.raycast(new RaycastContext(lastPos, pos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, Trajectories.mc.player))) != null && bhr.getType() == HitResult.Type.BLOCK) {
                Render3DUtil.draw3DBox(matrixStack, new Box(bhr.getBlockPos()), this.lcolor.getValue());
                break;
            }
            if (y <= -65.0) break;
            if (motionX == 0.0 && motionY == 0.0 && motionZ == 0.0) continue;
            Render3DUtil.drawLine((float)lastPos.x, (float)lastPos.y, (float)lastPos.z, (float)x, (float)y, (float)z, this.color.getValue(), 2.0f);
        }
    }
}
