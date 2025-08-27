package me.hextech.remapped;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class PredictionSetting extends Module_eSdgMXWuzcxgQVaJFmKZ implements Wrapper {
   public static PredictionSetting INSTANCE;
   public final EnumSetting<PredictionSetting_JgmYdSqRlhzWcxRMZVQF> page = this.add(new EnumSetting("Page", PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction));
   public final EnumSetting<PredictionSetting_mJSQReswTiaqOSqkjOmh> mode = this.add(
      new EnumSetting("Mode", PredictionSetting_mJSQReswTiaqOSqkjOmh.HexTech, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction)
   );
   public final SliderSetting placeExtrap = this.add(
      new SliderSetting(
            "PlaceExtrap",
            3,
            0,
            20,
            v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction
                  && this.mode.getValue() == PredictionSetting_mJSQReswTiaqOSqkjOmh.Aurora
         )
         .setSuffix("tick")
   );
   public final SliderSetting breakExtrap = this.add(
      new SliderSetting(
            "BreakExtrap",
            2,
            0,
            20,
            v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction
                  && this.mode.getValue() == PredictionSetting_mJSQReswTiaqOSqkjOmh.Aurora
         )
         .setSuffix("tick")
   );
   public final SliderSetting extrapTicks = this.add(
      new SliderSetting(
            "ExtrapTicks",
            3,
            1,
            20,
            v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction
                  && this.mode.getValue() == PredictionSetting_mJSQReswTiaqOSqkjOmh.Aurora
         )
         .setSuffix("hist")
   );
   public final SliderSetting selfExtrap = this.add(
      new SliderSetting(
            "SelfExtrap",
            2,
            0,
            20,
            v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction
                  && this.mode.getValue() == PredictionSetting_mJSQReswTiaqOSqkjOmh.Aurora
         )
         .setSuffix("tick")
   );
   public final SliderSetting smoothTicks = this.add(
      new SliderSetting(
            "SmoothTicks",
            3,
            0,
            20,
            v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction
                  && this.mode.getValue() == PredictionSetting_mJSQReswTiaqOSqkjOmh.Aurora
         )
         .setSuffix("tick")
   );
   public final BooleanSetting step = this.add(
      new BooleanSetting(
         "Step",
         false,
         v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction
               && this.mode.getValue() == PredictionSetting_mJSQReswTiaqOSqkjOmh.Aurora
      )
   );
   public final SliderSetting breakextrap = this.add(
      new SliderSetting(
            "BreakExtrap",
            0,
            0,
            10,
            v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction
                  && this.mode.getValue() == PredictionSetting_mJSQReswTiaqOSqkjOmh.HexTech
         )
         .setSuffix("tick")
   );
   public final BooleanSetting prediction = this.add(
      new BooleanSetting("Prediction", true, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Prediction)
   );
   public final BooleanSetting idPredict = this.add(
      new BooleanSetting("ID-Predict§4[OnlyAC]", false, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.IDPrediction)
   );
   public final SliderSetting idStartOffset = this.add(
      new SliderSetting(
         "ID-StartOffset", 1, 0, 10, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()
      )
   );
   public final SliderSetting idPacketOffset = this.add(
      new SliderSetting(
         "ID-PacketOffset", 1, 1, 10, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()
      )
   );
   public final SliderSetting idPackets = this.add(
      new SliderSetting("ID-Packets", 3, 1, 10, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue())
   );
   public final SliderSetting idStartDelay = this.add(
      new SliderSetting(
            "ID-StartDelay", 50, 0, 1000, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()
         )
         .setSuffix("ms")
   );
   public final SliderSetting idPacketDelay = this.add(
      new SliderSetting(
            "ID-PacketDelay", 50, 0, 1000, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.IDPrediction && this.idPredict.getValue()
         )
         .setSuffix("ms")
   );
   public final BooleanSetting drawnSelf = this.add(
      new BooleanSetting("DrawnSelf", true, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Render)
   );
   public final BooleanSetting drawnTarget = this.add(
      new BooleanSetting("DrawnTarget", true, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Render)
   );
   public final EnumSetting<PredictionSetting_JkDAbATfmVbQbOuEIDLo> renderMode = this.add(
      new EnumSetting("RenderMode", PredictionSetting_JkDAbATfmVbQbOuEIDLo.Line, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Render)
   );
   public final SliderSetting subSteps = this.add(
      new SliderSetting(
         "subSteps",
         15,
         5,
         50,
         v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Render
               && this.renderMode.getValue() == PredictionSetting_JkDAbATfmVbQbOuEIDLo.Line
      )
   );
   public final ColorSetting color = this.add(
      new ColorSetting("Color", new Color(255, 255, 255, 120), v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Render)
   );
   public final SliderSetting lineWidth = this.add(
      new SliderSetting("LineWidth", 1.5, 0.1, 5.0, 0.1, v -> this.page.getValue() == PredictionSetting_JgmYdSqRlhzWcxRMZVQF.Render)
   );
   private final Map<PlayerEntity, List<Vec3d>> pathCache = new HashMap();

   public PredictionSetting() {
      super("PredictionSetting", "PredictionSetting", Module_JlagirAibYQgkHtbRnhw.Setting);
      INSTANCE = this;
   }

   @Override
   public void onRender3D(MatrixStack matrices, float pt) {
      if (mc.field_1687 != null && mc.field_1724 != null) {
         this.pathCache.clear();
         if (this.drawnSelf.getValue()) {
            this.pathCache.put(mc.field_1724, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.simulate(mc.field_1724, this.selfExtrap.getValueInt()));
         }

         if (this.drawnTarget.getValue()) {
            for (PlayerEntity p : mc.field_1687.method_18456()) {
               if (p != mc.field_1724 && !(p.method_5858(mc.field_1724) > 4096.0)) {
                  int ticks = Math.max(this.placeExtrap.getValueInt(), this.breakExtrap.getValueInt());
                  this.pathCache.put(p, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.simulate(p, ticks));
               }
            }
         }

         this.pathCache.forEach((real, path) -> {
            switch ((PredictionSetting_JkDAbATfmVbQbOuEIDLo)this.renderMode.getValue()) {
               case Box:
                  this.drawBox(matrices, pt, real, path);
                  break;
               case Jello:
                  this.drawJello(matrices, pt, real, path);
                  break;
               case Line:
                  this.drawLinePath(matrices, path);
            }
         });
      }
   }

   private void drawBox(MatrixStack matrices, float pt, PlayerEntity real, List<Vec3d> path) {
      if (!path.isEmpty()) {
         Vec3d last = (Vec3d)path.get(path.size() - 1);
         Box box = real.method_5829().method_997(last.method_1020(real.method_19538()));
         Render3DUtil.draw3DBox(matrices, box, this.color.getValue(), true, true);
      }
   }

   private void drawJello(MatrixStack matrices, float pt, PlayerEntity real, List<Vec3d> path) {
      if (!path.isEmpty()) {
         Vec3d last = (Vec3d)path.get(path.size() - 1);
         PlayerEntity fake = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createPredict(
            real, Math.max(this.placeExtrap.getValueInt(), this.breakExtrap.getValueInt()), this.smoothTicks.getValueInt()
         );
         fake.method_33574(last);
         JelloUtil.drawJello(matrices, fake, this.color.getValue());
      }
   }

   private void drawLinePath(MatrixStack matrices, List<Vec3d> path) {
      if (path != null && path.size() >= 2) {
         Vec3d prev = (Vec3d)path.get(0);

         for (int i = 1; i < path.size(); i++) {
            Vec3d cur = (Vec3d)path.get(i);

            for (int j = 1; j <= this.subSteps.getValueInt(); j++) {
               double t = (double)j / (double)this.subSteps.getValueInt();
               Vec3d interp = new Vec3d(
                  prev.field_1352 + (cur.field_1352 - prev.field_1352) * t,
                  prev.field_1351 + (cur.field_1351 - prev.field_1351) * t,
                  prev.field_1350 + (cur.field_1350 - prev.field_1350) * t
               );
               Vec3d before = new Vec3d(
                  prev.field_1352 + (cur.field_1352 - prev.field_1352) * (t - 1.0 / (double)this.subSteps.getValueInt()),
                  prev.field_1351 + (cur.field_1351 - prev.field_1351) * (t - 1.0 / (double)this.subSteps.getValueInt()),
                  prev.field_1350 + (cur.field_1350 - prev.field_1350) * (t - 1.0 / (double)this.subSteps.getValueInt())
               );
               Render3DUtil.drawLine(
                  before.field_1352,
                  before.field_1351,
                  before.field_1350,
                  interp.field_1352,
                  interp.field_1351,
                  interp.field_1350,
                  this.color.getValue(),
                  this.lineWidth.getValueFloat()
               );
            }

            prev = cur;
         }
      }
   }
}
