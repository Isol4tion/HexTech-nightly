package me.hextech.remapped;

import java.awt.Color;
import java.util.HashMap;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class PlaceRender extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static final HashMap<BlockPos, PlaceRender_BKSSHzfecITsyTxUXmfE> renderMap = new HashMap();
   public static PlaceRender INSTANCE;
   public final EnumSetting<AutoCrystal_ohSMJidwaoXtIVckTOpo> colortype = this.add(new EnumSetting("ColorType", AutoCrystal_ohSMJidwaoXtIVckTOpo.Custom));
   public final SliderSetting fadeTime = this.add(new SliderSetting("FadeTime", 500, 0, 3000));
   public final SliderSetting timeout = this.add(new SliderSetting("TimeOut", 500, 0, 3000));
   public final BooleanSetting sync = this.add(new BooleanSetting("Sync", true));
   private final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255)).injectBoolean(true));
   private final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100)).injectBoolean(true));
   private final ColorSetting tryPlaceBox = this.add(new ColorSetting("TryPlaceBox", new Color(178, 178, 178, 255)).injectBoolean(true));
   private final ColorSetting tryPlaceFill = this.add(new ColorSetting("TryPlaceFill", new Color(255, 119, 119, 157)).injectBoolean(true));
   private final EnumSetting<FadeUtils> quad = this.add(new EnumSetting("Quad", FadeUtils.In));
   private final EnumSetting<PlaceRender_MgMtxnzmBTDvbcFtDFtE> mode = this.add(new EnumSetting("Mode", PlaceRender_MgMtxnzmBTDvbcFtDFtE.All));

   public PlaceRender() {
      super("PlaceRender", Module_JlagirAibYQgkHtbRnhw.Render);
      this.enable();
      INSTANCE = this;
   }

   public PlaceRender_BKSSHzfecITsyTxUXmfE create(BlockPos pos) {
      return new PlaceRender_BKSSHzfecITsyTxUXmfE(this, pos);
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      BlockUtil.placedPos.forEach(pos -> renderMap.put(pos, new PlaceRender_BKSSHzfecITsyTxUXmfE(this, pos)));
      BlockUtil.placedPos.clear();
      if (!renderMap.isEmpty()) {
         boolean shouldClear = true;

         for (PlaceRender_BKSSHzfecITsyTxUXmfE placePosition : renderMap.values()) {
            if (placePosition.isAir) {
               if (mc.field_1687.method_22347(placePosition.pos)) {
                  if (!placePosition.timer.passed(this.timeout.getValue())) {
                     placePosition.fade.reset();
                     Box aBox = new Box(placePosition.pos);
                     if (this.tryPlaceFill.booleanValue) {
                        Render3DUtil.drawFill(matrixStack, aBox, this.tryPlaceFill.getValue());
                     }

                     if (this.tryPlaceBox.booleanValue) {
                        Render3DUtil.drawBox(matrixStack, aBox, this.tryPlaceBox.getValue());
                     }

                     shouldClear = false;
                  }
                  continue;
               }

               placePosition.isAir = false;
            }

            double quads = placePosition.fade.getQuad((FadeUtils)this.quad.getValue());
            if (quads != 1.0) {
               shouldClear = false;
               double alpha = this.mode.getValue() != PlaceRender_MgMtxnzmBTDvbcFtDFtE.Fade && this.mode.getValue() != PlaceRender_MgMtxnzmBTDvbcFtDFtE.All
                  ? 1.0
                  : 1.0 - quads;
               double size = this.mode.getValue() != PlaceRender_MgMtxnzmBTDvbcFtDFtE.Shrink && this.mode.getValue() != PlaceRender_MgMtxnzmBTDvbcFtDFtE.All
                  ? 0.0
                  : quads;
               Box aBoxx = new Box(placePosition.pos).method_1009(-size * 0.5, -size * 0.5, -size * 0.5);
               if (((AutoCrystal_ohSMJidwaoXtIVckTOpo)this.colortype.getValue()).equals(AutoCrystal_ohSMJidwaoXtIVckTOpo.Custom)) {
                  if (this.fill.booleanValue) {
                     Render3DUtil.drawFill(
                        matrixStack, aBoxx, ColorUtil.injectAlpha(this.fill.getValue(), (int)((double)this.fill.getValue().getAlpha() * alpha))
                     );
                  }

                  if (this.box.booleanValue) {
                     Render3DUtil.drawBox(matrixStack, aBoxx, ColorUtil.injectAlpha(this.box.getValue(), (int)((double)this.box.getValue().getAlpha() * alpha)));
                  }
               }

               if (((AutoCrystal_ohSMJidwaoXtIVckTOpo)this.colortype.getValue()).equals(AutoCrystal_ohSMJidwaoXtIVckTOpo.Sync)) {
                  if (ColorsSetting.INSTANCE.box.booleanValue) {
                     Render3DUtil.drawFill(
                        matrixStack,
                        aBoxx,
                        ColorUtil.injectAlpha(ColorsSetting.INSTANCE.box.getValue(), (int)((double)ColorsSetting.INSTANCE.box.getValue().getAlpha() * alpha))
                     );
                  }

                  if (ColorsSetting.INSTANCE.online.booleanValue) {
                     Render3DUtil.drawBox(
                        matrixStack,
                        aBoxx,
                        ColorUtil.injectAlpha(
                           ColorsSetting.INSTANCE.online.getValue(), (int)((double)ColorsSetting.INSTANCE.online.getValue().getAlpha() * alpha)
                        )
                     );
                  }
               }
            }
         }

         if (shouldClear) {
            renderMap.clear();
         }
      }
   }
}
