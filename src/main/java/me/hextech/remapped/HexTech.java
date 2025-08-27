package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HexTech extends Screen implements Wrapper {
   private final Identifier logo = new Identifier("nullpoint", "icon.png");
   private final Identifier bg = new Identifier("nullpoint", "menu/bg2.png");
   private final Identifier single = new Identifier("nullpoint", "menu/single.png");
   private final Identifier multi = new Identifier("nullpoint", "menu/multi.png");
   private final Identifier setting = new Identifier("nullpoint", "menu/setting.png");
   private final Identifier clickGui = new Identifier("nullpoint", "menu/clickgui.png");
   private final Identifier alt = new Identifier("nullpoint", "menu/alt.png");
   private final Identifier exit = new Identifier("nullpoint", "menu/exit.png");
   private final float buttonHeight = 30.0F;
   private final float buttonWidth = 200.0F;
   private final float[] zoom = new float[]{1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F};
   private static final float MAX_ZOOM;
   private static final double ZOOM_SPEED;

   public HexTech() {
      super(Text.method_30163("HexTech"));
   }

   public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
      if (mc.method_22683() != null) {
         float offsetX = ((float)mouseX - (float)this.field_22789 / 2.0F) / 48.0F;
         float offsetY = ((float)mouseY - (float)this.field_22790 / 2.0F) / 80.0F;
         drawContext.method_25291(this.bg, (int)offsetX, (int)offsetY, 0, 0.0F, 0.0F, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
         float buttonSpacing = 8.0F;
         float baseX = (float)(this.field_22789 / 2) - 200.0F / 2.0F;
         float baseY = (float)(this.field_22790 / 2) - 2.0F * 30.0F;

         for (int i = 0; i < 6; i++) {
            float y = baseY + (float)i * (30.0F + buttonSpacing);
            boolean hover = this.isMouseHoveringRect((double)baseX, (double)y, 200.0, 30.0, (double)mouseX, (double)mouseY);
            if (hover) {
               this.zoom[i] = Math.min(1.12F, this.zoom[i] + 0.04F);
            } else {
               this.zoom[i] = Math.max(1.0F, this.zoom[i] - 0.04F);
            }

            MatrixStack matrices = drawContext.method_51448();
            matrices.method_22903();
            float cx = baseX + 100.0F;
            float cy = y + 15.0F;
            matrices.method_46416(cx, cy, 0.0F);
            matrices.method_22905(this.zoom[i], this.zoom[i], 1.0F);
            matrices.method_46416(-cx, -cy, 0.0F);
            Render2DUtil.drawRound(matrices, baseX, y, 200.0F, 30.0F, 8.0F, hover ? new Color(40, 40, 40, 150) : new Color(40, 40, 40, 80), true, 1000);
            int iconSize = 25;

            Identifier iconTex = switch (i) {
               case 0 -> this.single;
               case 1 -> this.multi;
               case 2 -> this.setting;
               case 3 -> this.clickGui;
               case 4 -> this.alt;
               case 5 -> this.exit;
               default -> this.single;
            };
            drawContext.method_25290(
               iconTex, (int)(baseX + (float)iconSize + 8.0F), (int)(y + (30.0F - (float)iconSize) / 2.0F), 0.0F, 0.0F, iconSize, iconSize, iconSize, iconSize
            );

            String label = switch (i) {
               case 0 -> "Singleplayer";
               case 1 -> "Multiplayer";
               case 2 -> "Setting";
               case 3 -> "ClickGui";
               case 4 -> "AltManager";
               case 5 -> "Exit";
               default -> "";
            };
            float fh = FontRenderers.Arial.getFontHeight();
            FontRenderers.Arial
               .drawCenteredString(matrices, label, (double)this.field_22789 / 2.0, (double)y + (double)(30.0F - fh) / 2.0, Color.WHITE.getRGB());
            matrices.method_22909();
         }

         int logoSize = this.field_22790 / 8;
         drawContext.method_25291(
            this.logo, this.field_22789 / 2 - logoSize / 2, (int)(baseY - (float)logoSize - 30.0F), 0, 0.0F, 0.0F, logoSize, logoSize, logoSize, logoSize
         );
         FontRenderers.ui
            .drawString(drawContext.method_51448(), "ʜᴇӼᴛᴇᴄʜ-8", 5.0F, (float)this.field_22790 - FontRenderers.ui.getFontHeight(), Color.WHITE.getRGB());
      }
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      float buttonSpacing = 8.0F;
      float x = (float)(this.field_22789 / 2) - 200.0F / 2.0F;
      float y = (float)(this.field_22790 / 2) - 2.0F * 30.0F;
      if (button == 0) {
         if (this.isMouseHoveringRect((double)x, (double)y, 200.0, 30.0, mouseX, mouseY)) {
            mc.method_1507(new SelectWorldScreen(this));
         }

         float y2 = y + 30.0F + buttonSpacing;
         if (this.isMouseHoveringRect((double)x, (double)y2, 200.0, 30.0, mouseX, mouseY)) {
            if (!mc.field_1690.field_21840) {
               mc.field_1690.field_21840 = true;
               mc.field_1690.method_1640();
            }

            mc.method_1507(new MultiplayerScreen(this));
         }

         float y3 = y2 + 30.0F + buttonSpacing;
         if (this.isMouseHoveringRect((double)x, (double)y3, 200.0, 30.0, mouseX, mouseY)) {
            mc.method_1507(new OptionsScreen(this, mc.field_1690));
         }

         float y4 = y3 + 30.0F + buttonSpacing;
         if (this.isMouseHoveringRect((double)x, (double)y4, 200.0, 30.0, mouseX, mouseY)) {
            mc.method_1507(new ClickGuiScreen());
         }

         float y5 = y4 + 30.0F + buttonSpacing;
         if (this.isMouseHoveringRect((double)x, (double)y5, 200.0, 30.0, mouseX, mouseY)) {
            mc.method_1507(new AltScreen(new HexTech()));
         }

         float y6 = y5 + 30.0F + buttonSpacing;
         if (this.isMouseHoveringRect((double)x, (double)y6, 200.0, 30.0, mouseX, mouseY)) {
            mc.method_1490();
         }
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean isMouseHoveringRect(double x, double y, double w, double h, double mouseX, double mouseY) {
      return mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + h;
   }
}
