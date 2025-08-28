package me.hextech.remapped;

import java.awt.Color;
import java.util.HashMap;

import me.hextech.remapped.api.utils.world.BlockUtil;
import me.hextech.remapped.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class PlaceRender
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final HashMap<BlockPos, PlaceRender_BKSSHzfecITsyTxUXmfE> renderMap = new HashMap();
    public static PlaceRender INSTANCE;
    public final EnumSetting<AutoCrystal_QcRVYRsOqpKivetoXSJa.AutoCrystal_ohSMJidwaoXtIVckTOpo> colortype = this.add(new EnumSetting<AutoCrystal_QcRVYRsOqpKivetoXSJa.AutoCrystal_ohSMJidwaoXtIVckTOpo>("ColorType", AutoCrystal_QcRVYRsOqpKivetoXSJa.AutoCrystal_ohSMJidwaoXtIVckTOpo.Custom));
    public final SliderSetting fadeTime = this.add(new SliderSetting("FadeTime", 500, 0, 3000));
    public final SliderSetting timeout = this.add(new SliderSetting("TimeOut", 500, 0, 3000));
    public final BooleanSetting sync = this.add(new BooleanSetting("Sync", true));
    private final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255)).injectBoolean(true));
    private final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final ColorSetting tryPlaceBox = this.add(new ColorSetting("TryPlaceBox", new Color(178, 178, 178, 255)).injectBoolean(true));
    private final ColorSetting tryPlaceFill = this.add(new ColorSetting("TryPlaceFill", new Color(255, 119, 119, 157)).injectBoolean(true));
    private final EnumSetting<FadeUtils> quad = this.add(new EnumSetting<FadeUtils>("Quad", FadeUtils.In));
    private final EnumSetting<_MgMtxnzmBTDvbcFtDFtE> mode = this.add(new EnumSetting<_MgMtxnzmBTDvbcFtDFtE>("Mode", _MgMtxnzmBTDvbcFtDFtE.All));

    public PlaceRender() {
        super("PlaceRender", Module_JlagirAibYQgkHtbRnhw.Render);
        this.enable();
        INSTANCE = this;
    }

    public PlaceRender_BKSSHzfecITsyTxUXmfE create(BlockPos pos) {
        return new PlaceRender_BKSSHzfecITsyTxUXmfE( pos);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        BlockUtil.placedPos.forEach(pos -> renderMap.put(pos, new PlaceRender_BKSSHzfecITsyTxUXmfE(pos)));
        BlockUtil.placedPos.clear();
        if (renderMap.isEmpty()) {
            return;
        }
        boolean shouldClear = true;
        for (PlaceRender_BKSSHzfecITsyTxUXmfE placePosition : renderMap.values()) {
            double quads;
            if (placePosition.isAir) {
                if (!PlaceRender.mc.world.isAir(placePosition.pos)) {
                    placePosition.isAir = false;
                } else {
                    if (placePosition.timer.passed(this.timeout.getValue())) continue;
                    placePosition.fade.reset();
                    Box aBox = new Box(placePosition.pos);
                    if (this.tryPlaceFill.booleanValue) {
                        Render3DUtil.drawFill(matrixStack, aBox, this.tryPlaceFill.getValue());
                    }
                    if (this.tryPlaceBox.booleanValue) {
                        Render3DUtil.drawBox(matrixStack, aBox, this.tryPlaceBox.getValue());
                    }
                    shouldClear = false;
                    continue;
                }
            }
            if ((quads = placePosition.fade.getQuad(this.quad.getValue())) == 1.0) continue;
            shouldClear = false;
            double alpha = this.mode.getValue() == _MgMtxnzmBTDvbcFtDFtE.Fade || this.mode.getValue() == _MgMtxnzmBTDvbcFtDFtE.All ? 1.0 - quads : 1.0;
            double size = this.mode.getValue() == _MgMtxnzmBTDvbcFtDFtE.Shrink || this.mode.getValue() == _MgMtxnzmBTDvbcFtDFtE.All ? quads : 0.0;
            Box aBox = new Box(placePosition.pos).expand(-size * 0.5, -size * 0.5, -size * 0.5);
            if (this.colortype.getValue().equals(AutoCrystal_QcRVYRsOqpKivetoXSJa.AutoCrystal_ohSMJidwaoXtIVckTOpo.Custom)) {
                if (this.fill.booleanValue) {
                    Render3DUtil.drawFill(matrixStack, aBox, ColorUtil.injectAlpha(this.fill.getValue(), (int)((double)this.fill.getValue().getAlpha() * alpha)));
                }
                if (this.box.booleanValue) {
                    Render3DUtil.drawBox(matrixStack, aBox, ColorUtil.injectAlpha(this.box.getValue(), (int)((double)this.box.getValue().getAlpha() * alpha)));
                }
            }
            if (!this.colortype.getValue().equals(AutoCrystal_QcRVYRsOqpKivetoXSJa.AutoCrystal_ohSMJidwaoXtIVckTOpo.Sync)) continue;
            if (ColorsSetting.INSTANCE.box.booleanValue) {
                Render3DUtil.drawFill(matrixStack, aBox, ColorUtil.injectAlpha(ColorsSetting.INSTANCE.box.getValue(), (int)((double)ColorsSetting.INSTANCE.box.getValue().getAlpha() * alpha)));
            }
            if (!ColorsSetting.INSTANCE.online.booleanValue) continue;
            Render3DUtil.drawBox(matrixStack, aBox, ColorUtil.injectAlpha(ColorsSetting.INSTANCE.online.getValue(), (int)((double)ColorsSetting.INSTANCE.online.getValue().getAlpha() * alpha)));
        }
        if (shouldClear) {
            renderMap.clear();
        }
    }

    public enum _MgMtxnzmBTDvbcFtDFtE {
        Fade,
        Shrink,
        All

    }

    public class PlaceRender_BKSSHzfecITsyTxUXmfE{
        public final FadeUtils_DPfHthPqEJdfXfNYhDbG fade;
        public final BlockPos pos;
        public final Timer timer;
        public boolean isAir;

        public PlaceRender_BKSSHzfecITsyTxUXmfE(BlockPos placePos) {
            this.fade = new FadeUtils_DPfHthPqEJdfXfNYhDbG((long) fadeTime.getValue());
            this.pos = placePos;
            this.timer = new Timer();
            this.isAir = true;
        }
    }
}
