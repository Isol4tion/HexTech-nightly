package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Menu extends Screen {
   private static final Identifier sky;
   private static final Identifier station;
   private static final Identifier pillar;
   private static final Identifier moon;
   private static final Identifier setting;
   private static final Identifier exit;
   private static final Identifier single;
   private static final Identifier multi;
   float zoomSingle = 1.0F;
   float zoomMulti = 1.0F;
   float zoomSetting = 1.0F;
   float zoomExit = 1.0F;

   public Menu() {
      super(Text.method_43471("narrator.screen.title"));
   }

   public void method_25394(DrawContext context, int mouseX, int mouseY, float delta) {
      LogoDrawer.draw(context, this.field_22789 / 4, this.field_22790 / 4, 1.0F);
      context.method_25302(moon, 50, 15, 0, 0, this.field_22789, this.field_22790);
      context.method_25291(sky, 0, 0, 0, 0.0F, 0.0F, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
      context.method_25291(
         station,
         (mouseX - this.field_22789) / 48,
         30 + (mouseY - this.field_22790 / 2) / 80,
         0,
         0.0F,
         0.0F,
         this.field_22789,
         this.field_22790,
         this.field_22789,
         this.field_22790
      );
      MatrixStack matrices = context.method_51448();
      RenderSystem.defaultBlendFunc();
      float maxButtonWidth = (float)this.field_22789 / 2.0F;
      int buttonWidth = this.field_22790 / 12;
      float scaled = 1.2F;
      int startX = this.field_22789 / 4 + 25;
      int buttonY = (this.field_22790 - buttonWidth) / 2;
      int charc = (int)((maxButtonWidth - (float)(4 * buttonWidth)) / 5.0F);
      double zoomAdd = 0.06;
      float alphaFac = (float)(1 - Math.abs(mouseY - this.field_22790 / 2) / this.field_22790) / 255.0F;
      matrices.method_22903();
      if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
         if (this.zoomSingle < scaled) {
            this.zoomSingle = (float)((double)this.zoomSingle + zoomAdd);
         }
      } else if ((double)this.zoomSingle > 1.0) {
         this.zoomSingle = (float)((double)this.zoomSingle - zoomAdd);
      }

      if (this.zoomSingle > 1.0F) {
         matrices.method_46416((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0F);
         matrices.method_22905(this.zoomSingle, this.zoomSingle, 1.0F);
         matrices.method_46416((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0F);
      }

      RenderSystem.setShaderTexture(0, single);
      RenderSystem.texParameter(3553, 10241, 9729);
      RenderSystem.texParameter(3553, 10240, 9729);
      context.method_25291(single, startX, buttonY, 0, 0.0F, 0.0F, buttonWidth, buttonWidth, buttonWidth, buttonWidth);
      matrices.method_22909();
      matrices.method_22903();
      if (this.zoomSingle > 1.0F) {
         matrices.method_46416((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0F);
         matrices.method_22905(this.zoomSingle, this.zoomSingle, 1.0F);
         matrices.method_46416((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0F);
         TextUtil.drawString(
            context,
            "Singleplayer",
            (double)((float)(startX + buttonWidth / 2) - TextUtil.getWidth("Singleplayer")),
            (double)((float)buttonY + (float)buttonWidth + 1.0F),
            new Color(255, 255, 255, 255).getRGB(),
            true
         );
      }

      matrices.method_22909();
      startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc);
      matrices.method_22903();
      if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
         if (this.zoomMulti < scaled) {
            this.zoomMulti = (float)((double)this.zoomMulti + zoomAdd);
         }
      } else if ((double)this.zoomMulti > 1.0) {
         this.zoomMulti = (float)((double)this.zoomMulti - zoomAdd);
      }

      if (this.zoomMulti > 1.0F) {
         matrices.method_46416((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0F);
         matrices.method_22905(Math.min(scaled, this.zoomMulti), Math.min(scaled, this.zoomMulti), 1.0F);
         matrices.method_46416((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0F);
      }

      RenderSystem.setShaderTexture(0, multi);
      RenderSystem.texParameter(3553, 10241, 9729);
      RenderSystem.texParameter(3553, 10240, 9729);
      context.method_25291(multi, startX, buttonY, 0, 0.0F, 0.0F, buttonWidth, buttonWidth, buttonWidth, buttonWidth);
      matrices.method_22909();
      matrices.method_22903();
      if (this.zoomMulti > 1.0F) {
         matrices.method_46416((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0F);
         matrices.method_22905(this.zoomMulti, this.zoomMulti, 1.0F);
         matrices.method_46416((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0F);
         TextUtil.drawString(
            context,
            "Multiplayer",
            (double)((float)(startX + buttonWidth / 2) - TextUtil.getWidth("Multiplayer")),
            (double)((float)buttonY + (float)buttonWidth + 1.0F),
            new Color(255, 255, 255, 255).getRGB(),
            true
         );
      }

      matrices.method_22909();
      startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc);
      matrices.method_22903();
      if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
         if (this.zoomSetting < scaled) {
            this.zoomSetting = (float)((double)this.zoomSetting + zoomAdd);
         }
      } else if ((double)this.zoomSetting > 1.0) {
         this.zoomSetting = (float)((double)this.zoomSetting - zoomAdd);
      }

      if (this.zoomSetting > 1.0F) {
         matrices.method_46416((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0F);
         matrices.method_22905(Math.min(scaled, this.zoomSetting), Math.min(scaled, this.zoomSetting), 1.0F);
         matrices.method_46416((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0F);
      }

      RenderSystem.setShaderTexture(0, setting);
      RenderSystem.texParameter(3553, 10241, 9729);
      RenderSystem.texParameter(3553, 10240, 9729);
      context.method_25291(setting, startX, buttonY, 0, 0.0F, 0.0F, buttonWidth, buttonWidth, buttonWidth, buttonWidth);
      matrices.method_22909();
      matrices.method_22903();
      if (this.zoomSetting > 1.0F) {
         matrices.method_46416((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0F);
         matrices.method_22905(this.zoomSetting, this.zoomSetting, 1.0F);
         matrices.method_46416((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0F);
         TextUtil.drawString(
            context,
            "Singleplayer",
            (double)((float)(startX + buttonWidth / 2) - TextUtil.getWidth("Singleplayer")),
            (double)((float)buttonY + (float)buttonWidth + 1.0F),
            new Color(255, 255, 255, 255).getRGB(),
            true
         );
      }

      matrices.method_22909();
      startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc);
      matrices.method_22903();
      if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
         if (this.zoomExit < scaled) {
            this.zoomExit = (float)((double)this.zoomExit + zoomAdd);
         }
      } else if ((double)this.zoomExit > 1.0) {
         this.zoomExit = (float)((double)this.zoomExit - zoomAdd);
      }

      if (this.zoomExit > 1.0F) {
         matrices.method_46416((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0F);
         matrices.method_22905(Math.min(scaled, this.zoomExit), Math.min(scaled, this.zoomExit), 1.0F);
         matrices.method_46416((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0F);
      }

      RenderSystem.setShaderTexture(0, exit);
      RenderSystem.texParameter(3553, 10241, 9729);
      RenderSystem.texParameter(3553, 10240, 9729);
      context.method_25291(exit, startX, buttonY, 0, 0.0F, 0.0F, buttonWidth, buttonWidth, buttonWidth, buttonWidth);
      matrices.method_22909();
      matrices.method_22903();
      if (this.zoomExit > 1.0F) {
         matrices.method_46416((float)(startX + buttonWidth), (float)(buttonY + buttonWidth), 0.0F);
         matrices.method_22905(this.zoomExit, this.zoomExit, 1.0F);
         matrices.method_46416((float)(-(startX + buttonWidth)), (float)(-(buttonY + buttonWidth)), 0.0F);
         TextUtil.drawString(
            context,
            "Exit",
            (double)((float)(startX + buttonWidth / 2) - TextUtil.getWidth("Exit") - 2.0F),
            (double)((float)buttonY + (float)buttonWidth + 1.0F),
            new Color(255, 255, 255, 255).getRGB(),
            true
         );
      }

      matrices.method_22909();
      RenderSystem.disableBlend();
   }

   public boolean isMouseHoveringRect(float x, float y, int width, int mouseX, int mouseY) {
      return (float)mouseX >= x && (float)mouseY >= y && (float)mouseX <= x + (float)width && (float)mouseY <= y + (float)width;
   }

   public boolean isMouseHoveringRect(float x, float y, int width, double mouseX, double mouseY) {
      return mouseX >= (double)x && mouseY >= (double)y && mouseX <= (double)(x + (float)width) && mouseY <= (double)(y + (float)width);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      float maxButtonWidth = (float)this.field_22789 / 2.0F;
      int buttonWidth = this.field_22790 / 12;
      float scaled = 1.2F;
      int startX = this.field_22789 / 4 + 15;
      int buttonY = (this.field_22790 - buttonWidth) / 2;
      int charc = (int)((maxButtonWidth - (float)(4 * buttonWidth)) / 5.0F);
      if (button == 0) {
         if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
            this.field_22787.method_1507(new SelectWorldScreen(this));
         }

         startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc);
         if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
            if (!Wrapper.mc.field_1690.field_21840) {
               Wrapper.mc.field_1690.field_21840 = true;
               Wrapper.mc.field_1690.method_1640();
            }

            Screen screen = new MultiplayerScreen(this);
            Wrapper.mc.method_1507(screen);
         }

         startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc);
         if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
            Wrapper.mc.method_1507(new OptionsScreen(this, Wrapper.mc.field_1690));
         }

         startX += (int)((double)buttonWidth + 0.2 * (double)buttonWidth + (double)charc);
         if (this.isMouseHoveringRect((float)startX, (float)buttonY, buttonWidth, mouseX, mouseY)) {
            Wrapper.mc.method_1490();
         }
      }

      return super.method_25402(mouseX, mouseY, button);
   }
}
