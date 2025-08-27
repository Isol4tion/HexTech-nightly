package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.TextUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemHUD
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final Stack<Float> alphaMultipliers;
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
        MatrixStack matrices = drawContext.method_51448();
        matrices.method_22903();
        matrices.method_22905(scale, scale, 1.0f);
        matrices.method_46416(0.0f, 0.0f, 401.0f);
        int scaledX = (int)((float)x / scale);
        int scaledY = (int)((float)y / scale);
        drawContext.method_51427(itemStack, scaledX, scaledY);
        matrices.method_22909();
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack((ItemConvertible)Items.field_8367, this.getItemCount(Items.field_8367)));
        list.add(new ItemStack((ItemConvertible)Items.field_8288, this.getItemCount(Items.field_8288)));
        list.add(new ItemStack((ItemConvertible)Items.field_8301, this.getItemCount(Items.field_8301)));
        list.add(new ItemStack((ItemConvertible)Items.field_23141, this.getItemCount(Items.field_23141)));
        list.add(new ItemStack((ItemConvertible)Items.field_8801, this.getItemCount(Items.field_8801)));
        list.add(new ItemStack((ItemConvertible)Items.field_8249, this.getItemCount(Items.field_8249)));
        list.add(new ItemStack((ItemConvertible)Items.field_8793, this.getItemCount(Items.field_8793)));
        list.add(new ItemStack((ItemConvertible)Items.field_8786, this.getItemCount(Items.field_8786)));
        list.add(new ItemStack((ItemConvertible)Items.field_8287, this.getItemCount(Items.field_8287)));
        for (int i = 0; i < list.size(); ++i) {
            if (((ItemStack)list.get(i)).method_7947() < 1) {
                ((ItemStack)list.get(i)).method_7939(Integer.MAX_VALUE);
            }
            ItemHUD.drawItem(drawContext, (ItemStack)list.get(i), this.xOffset.getValueInt() + this.xPlus.getValueInt() * i, this.yOffset.getValueInt(), this.scale.getValueFloat());
            drawContext.method_51448().method_22903();
            drawContext.method_51448().method_22905(this.scale.getValueFloat(), this.scale.getValueFloat(), this.scale.getValueFloat());
            if (((ItemStack)list.get(i)).method_7947() != Integer.MAX_VALUE) {
                TextUtil.drawString(drawContext, String.valueOf(((ItemStack)list.get(i)).method_7947()), (double)(((float)(this.xOffset.getValueInt() + this.xPlus.getValueInt() * i) + this.TextxPlus.getValueFloat()) / this.scale.getValueFloat()), ((double)this.yOffset.getValueInt() + 12.5 + this.yPlus.getValue()) / (double)this.scale.getValueFloat(), this.textColor.getValue().getRGB());
            } else {
                TextUtil.drawString(drawContext, "0", (double)(((float)(this.xOffset.getValueInt() + this.xPlus.getValueInt() * i) + this.TextxPlus.getValueFloat()) / this.scale.getValueFloat()), ((double)this.yOffset.getValueInt() + 12.5 + this.yPlus.getValue()) / (double)this.scale.getValueFloat(), this.textColor.getValue().getRGB());
            }
            drawContext.method_51448().method_22909();
        }
    }

    public int getItemCount(Item item) {
        if (ItemHUD.mc.player == null) {
            return 0;
        }
        int n = 0;
        int n2 = 44;
        for (int i = 0; i <= n2; ++i) {
            ItemStack itemStack = ItemHUD.mc.player.method_31548().method_5438(i);
            if (itemStack.method_7909() != item) continue;
            n += itemStack.method_7947();
        }
        return n;
    }
}
