package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;

import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.mod.modules.settings.impl.ColorSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemHUD
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ItemHUD INSTANCE;
    private final SliderSetting xOffset = this.add(new SliderSetting("X", 0, 0, 2000));
    private final SliderSetting yOffset = this.add(new SliderSetting("Y", 10, 0, 2000));
    private final SliderSetting xPlus = this.add(new SliderSetting("Full x Plus", 25, 0, 300));
    private final SliderSetting TextxPlus = this.add(new SliderSetting("Text x Plus", 0, 0, 300));
    private final SliderSetting yPlus = this.add(new SliderSetting("Y Plus", 10, 0, 300));
    private final SliderSetting scale = this.add(new SliderSetting("Scale", 1.0, 0.0, 10.0, 0.1));
    private final ColorSetting topC = this.add(new ColorSetting("Top Color", new Color(0, 0, 0, 100)));
    private final ColorSetting colorBack = this.add(new ColorSetting("BackGround", new Color(0, 0, 0, 200)));
    private final ColorSetting textColor = this.add(new ColorSetting("Text", new Color(255, 255, 255, 255)));
    private final SliderSetting blur = this.add(new SliderSetting("Blur", 20, 0, 50));
    private final SliderSetting ra = this.add(new SliderSetting("radius", 3, 0, 10));
    public BooleanSetting customFont = this.add(new BooleanSetting("CustomFont", false));

    public ItemHUD() {
        super("ItemHUD", Module_JlagirAibYQgkHtbRnhw.Client);
        INSTANCE = this;
    }

    public static void drawItem(DrawContext drawContext, ItemStack itemStack, int x, int y, float scale) {
        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(0.0f, 0.0f, 401.0f);
        int scaledX = (int)((float)x / scale);
        int scaledY = (int)((float)y / scale);
        drawContext.drawItem(itemStack, scaledX, scaledY);
        matrices.pop();
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, this.getItemCount(Items.ENCHANTED_GOLDEN_APPLE)));
        list.add(new ItemStack(Items.TOTEM_OF_UNDYING, this.getItemCount(Items.TOTEM_OF_UNDYING)));
        list.add(new ItemStack(Items.END_CRYSTAL, this.getItemCount(Items.END_CRYSTAL)));
        list.add(new ItemStack(Items.RESPAWN_ANCHOR, this.getItemCount(Items.RESPAWN_ANCHOR)));
        list.add(new ItemStack(Items.GLOWSTONE, this.getItemCount(Items.GLOWSTONE)));
        list.add(new ItemStack(Items.PISTON, this.getItemCount(Items.PISTON)));
        list.add(new ItemStack(Items.REDSTONE_BLOCK, this.getItemCount(Items.REDSTONE_BLOCK)));
        list.add(new ItemStack(Items.COBWEB, this.getItemCount(Items.COBWEB)));
        list.add(new ItemStack(Items.EXPERIENCE_BOTTLE, this.getItemCount(Items.EXPERIENCE_BOTTLE)));
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).getCount() < 1) {
                list.get(i).setCount(Integer.MAX_VALUE);
            }
            ItemHUD.drawItem(drawContext, list.get(i), this.xOffset.getValueInt() + this.xPlus.getValueInt() * i, this.yOffset.getValueInt(), this.scale.getValueFloat());
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(this.scale.getValueFloat(), this.scale.getValueFloat(), this.scale.getValueFloat());
            if (list.get(i).getCount() != Integer.MAX_VALUE) {
                TextUtil.drawString(drawContext, String.valueOf(list.get(i).getCount()), ((float)(this.xOffset.getValueInt() + this.xPlus.getValueInt() * i) + this.TextxPlus.getValueFloat()) / this.scale.getValueFloat(), ((double)this.yOffset.getValueInt() + 12.5 + this.yPlus.getValue()) / (double)this.scale.getValueFloat(), this.textColor.getValue().getRGB());
            } else {
                TextUtil.drawString(drawContext, "0", ((float)(this.xOffset.getValueInt() + this.xPlus.getValueInt() * i) + this.TextxPlus.getValueFloat()) / this.scale.getValueFloat(), ((double)this.yOffset.getValueInt() + 12.5 + this.yPlus.getValue()) / (double)this.scale.getValueFloat(), this.textColor.getValue().getRGB());
            }
            drawContext.getMatrices().pop();
        }
    }

    public int getItemCount(Item item) {
        if (ItemHUD.mc.player == null) {
            return 0;
        }
        int n = 0;
        int n2 = 44;
        for (int i = 0; i <= n2; ++i) {
            ItemStack itemStack = ItemHUD.mc.player.getInventory().getStack(i);
            if (itemStack.getItem() != item) continue;
            n += itemStack.getCount();
        }
        return n;
    }
}
