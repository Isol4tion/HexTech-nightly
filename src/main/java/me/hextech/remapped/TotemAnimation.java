package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class TotemAnimation extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static TotemAnimation instance;
   private final EnumSetting mode = this.add(new EnumSetting("Mode", TotemAnimation_ZVxiGMTWPCIGCYyTrffL.FadeOut));
   private ItemStack floatingItem = null;
   private int floatingItemTimeLeft;

   public TotemAnimation() {
      super("TotemAnimation", Module_JlagirAibYQgkHtbRnhw.Render);
      instance = this;
   }

   public void showFloatingItem(ItemStack floatingItem) {
      this.floatingItem = floatingItem;
      this.floatingItemTimeLeft = this.getTime();
   }

   @Override
   public void onUpdate() {
      if (this.floatingItemTimeLeft > 0) {
         this.floatingItemTimeLeft--;
         if (this.floatingItemTimeLeft == 0) {
            this.floatingItem = null;
         }
      }
   }

   public void renderFloatingItem(int scaledWidth, int scaledHeight, float tickDelta) {
      String var10000 = "\u0000\u0000\u0001\u0000tW43n¢\u0000Ewe@\u001a[*x»J$\u0097åú~vðÍz¿Ê°ôóC1d\u009aD»½ú\u0085úî6bt8ß\u0017¦\u0081[©=\u0014¡VV\u0084éÆbPµþ\u0004ýðß¨pU\bÑãEìHWC\u008aºF\u001f§Bh\u0095\u0016Ò©\u008dÆA\u0088#\u000f\u0013b¼ó4Ó4 ú\u0096\r<ÜqWô\u008a(ß3âØø±õ\u001a#\u0094>§\u0011 (}z§T\u0094Y\bQ\f\u0001þµ\u001f>Òâ\u000b4\u009b7Ò\u009dõå\\Wâq\\2ä U?ß×Æ\u0002Ü\u000f-¬UH¿ È\u0098È`\f÷Å?\u009c6\u008b\u0093É%\u0096eûùU\u0094Ëp.Ð\u0083\\\u0080\u0082¡\u009e6\u0082ôû!Ñí\u0097¯ôW\u0004S\n¸ß\u0016Âùó1x\u0011ë·_ÿS\u0002^!Éà\u0090fR:¤\u0087w\u001eô\u0004\u0006Õ\u0002\u001d½\u0019\u0087O\u0000\u0000\u0001\u0000\u0004I\u001c\u009c¸\u009d%|&\u0006#\u000e¼8\u0011A?\u0004$Ù»\u0001Ì\u0088_NøÈzîÖ\u0015w\u009a\u009dü\u0002×:»Dj~\u0086Ò\u0085\b\u0094\u0013¡'Ö\u0011^1ÑaÉ©/Ý¬¾\u009c1<§x+àËG\fNQ\u0085\f<\u0097Dã\u009dö\u0007ÿ\u0095Ð{öéD?,Ã\u00adÐ¤Ä\u0006 \u001f\u0093ý±n\u009dIWÄ'S0JÝ& _²\u0018\u0001\u008f\nñ®mñ¥ó\u0084ùÃz\u00ad¸9j¤FÞÓ\u0095»èÂød\u0096ÇÆD\\¼'¿\u0004D4ha\u0087lPRíu&¦Ð\u0017âÚÉø8Íß\u009a\u0016ìâMb¬Ií¢öôO\u001aÈFò[ÚxÑÌoÿ\u00948\u009a\u0014\u0087÷\u0088í7þÊ1(i®Ã}¹¤+E°\u0006\u00adv~<\u00130r+q'=I\u000b{\u0091¨te#6ûä§¬¢âÁÄ\u0007\u0016M\u0013¬\u0000\u0000\u00008õ¤\u0091öÀ\u009cÐì%J'\tPE[\u000f\u001déø\u0090X,fVþò\u009dõà\fÔeG\u0092=\u000b\b\u009e3@OÚ\u0085Ç¢\u001b!\u009aÉ\u0017´\u0081¿Loí";
      if (this.floatingItem != null && this.floatingItemTimeLeft > 0 && !this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.Off)) {
         int i = this.getTime() - this.floatingItemTimeLeft;
         float f = ((float)i + tickDelta) / (float)this.getTime();
         float g = f * f;
         float h = f * g;
         float j = 10.25F * h * g - 24.95F * g * g + 25.5F * h - 13.8F * g + 4.0F * f;
         float k = j * (float) Math.PI;
         RenderSystem.enableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         MatrixStack matrixStack = new MatrixStack();
         matrixStack.method_22903();
         float f2 = (float)i + tickDelta;
         float n = 50.0F + 175.0F * MathHelper.method_15374(k);
         if (this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.FadeOut)) {
            float x2 = (float)(Math.sin((double)(f2 * 112.0F / 180.0F)) * 100.0);
            float y2 = (float)(Math.cos((double)(f2 * 112.0F / 180.0F)) * 50.0);
            matrixStack.method_46416((float)(scaledWidth / 2) + x2, (float)(scaledHeight / 2) + y2, -50.0F);
            matrixStack.method_22905(n, -n, n);
         } else if (this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.Size)) {
            matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0F);
            matrixStack.method_22905(n, -n, n);
         } else if (this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.Otkisuli)) {
            matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0F);
            matrixStack.method_22907(RotationAxis.field_40714.rotationDegrees(f2 * 2.0F));
            matrixStack.method_22907(RotationAxis.field_40718.rotationDegrees(f2 * 2.0F));
            matrixStack.method_22905(200.0F - f2 * 1.5F, -200.0F + f2 * 1.5F, 200.0F - f2 * 1.5F);
         } else if (this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.Insert)) {
            matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2), -50.0F);
            matrixStack.method_22907(RotationAxis.field_40714.rotationDegrees(f2 * 3.0F));
            matrixStack.method_22905(200.0F - f2 * 1.5F, -200.0F + f2 * 1.5F, 200.0F - f2 * 1.5F);
         } else if (this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.Fall)) {
            float downFactor = (float)(Math.pow((double)f2, 3.0) * 0.2F);
            matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2) + downFactor, -50.0F);
            matrixStack.method_22907(RotationAxis.field_40718.rotationDegrees(f2 * 5.0F));
            matrixStack.method_22905(200.0F - f2 * 1.5F, -200.0F + f2 * 1.5F, 200.0F - f2 * 1.5F);
         } else if (this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.Rocket)) {
            float downFactor = (float)(Math.pow((double)f2, 3.0) * 0.2F) - 20.0F;
            matrixStack.method_46416((float)(scaledWidth / 2), (float)(scaledHeight / 2) - downFactor, -50.0F);
            matrixStack.method_22907(RotationAxis.field_40716.rotationDegrees(f2 * (float)this.floatingItemTimeLeft * 2.0F));
            matrixStack.method_22905(200.0F - f2 * 1.5F, -200.0F + f2 * 1.5F, 200.0F - f2 * 1.5F);
         } else if (this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.Roll)) {
            float rightFactor = (float)(Math.pow((double)f2, 2.0) * 4.5);
            matrixStack.method_46416((float)(scaledWidth / 2) + rightFactor, (float)(scaledHeight / 2), -50.0F);
            matrixStack.method_22907(RotationAxis.field_40718.rotationDegrees(f2 * 40.0F));
            matrixStack.method_22905(200.0F - f2 * 1.5F, -200.0F + f2 * 1.5F, 200.0F - f2 * 1.5F);
         }

         Immediate immediate = mc.method_22940().method_23000();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F - f);
         mc.method_1480()
            .method_23178(this.floatingItem, ModelTransformationMode.field_4319, 15728880, OverlayTexture.field_21444, matrixStack, immediate, mc.field_1687, 0);
         matrixStack.method_22909();
         immediate.method_22993();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         RenderSystem.disableDepthTest();
      }
   }

   private int getTime() {
      if (this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.FadeOut)) {
         return 10;
      } else {
         return this.mode.getValue().equals(TotemAnimation_ZVxiGMTWPCIGCYyTrffL.Insert) ? 20 : 40;
      }
   }
}
