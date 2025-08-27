package me.hextech.remapped;

import me.hextech.remapped.AnimateUtil;
import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.AutoCrystal_ohSMJidwaoXtIVckTOpo;
import me.hextech.remapped.Cleaner_iFwqnooxsJEmHoVteFeQ;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.ColorsSetting;
import me.hextech.remapped.ComboBreaks;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.ListenerText;
import me.hextech.remapped.Render3DEvent;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.WebAuraTick_gaIdrzDzsbegzNTtPQoV;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/*
 * Exception performing whole class analysis ignored.
 */
public static class AutoCrystal {
    final /* synthetic */ AutoCrystal_QcRVYRsOqpKivetoXSJa this$0;

    public AutoCrystal(AutoCrystal_QcRVYRsOqpKivetoXSJa this$0) {
        this.this$0 = this$0;
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null) {
            this.this$0.noPosTimer.reset();
            AutoCrystal_QcRVYRsOqpKivetoXSJa.placeVec3d = AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos.method_10074().toCenterPos();
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.placeVec3d == null) {
            return;
        }
        this.this$0.fade = this.this$0.fadeSpeed.getValue() >= 1.0 ? (this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5) : AnimateUtil.animate(this.this$0.fade, this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5, this.this$0.fadeSpeed.getValue() / 10.0);
        if (this.this$0.fade == 0.0) {
            AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d = null;
            return;
        }
        AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d = AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d == null || this.this$0.sliderSpeed.getValue() >= 1.0 ? AutoCrystal_QcRVYRsOqpKivetoXSJa.placeVec3d : new Vec3d(AnimateUtil.animate(AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.field_1352, AutoCrystal_QcRVYRsOqpKivetoXSJa.placeVec3d.field_1352, this.this$0.sliderSpeed.getValue() / 10.0), AnimateUtil.animate(AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.field_1351, AutoCrystal_QcRVYRsOqpKivetoXSJa.placeVec3d.field_1351, this.this$0.sliderSpeed.getValue() / 10.0), AnimateUtil.animate(AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.field_1350, AutoCrystal_QcRVYRsOqpKivetoXSJa.placeVec3d.field_1350, this.this$0.sliderSpeed.getValue() / 10.0));
        if (this.this$0.render.getValue()) {
            Box cbox = new Box(AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d, AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d);
            cbox = this.this$0.shrink.getValue() ? cbox.method_1014(this.this$0.fade) : cbox.method_1014((double)this.this$0.expand.getValueFloat());
            MatrixStack matrixStack = event.getMatrixStack();
            if (this.this$0.colortype.getValue().equals((Object)AutoCrystal_ohSMJidwaoXtIVckTOpo.Custom)) {
                if (this.this$0.box.booleanValue) {
                    Render3DUtil.drawFill(matrixStack, cbox, ColorUtil.injectAlpha(this.this$0.box.getValue(), (int)((double)this.this$0.box.getValue().getAlpha() * this.this$0.fade * 2.0)));
                }
                if (this.this$0.online.booleanValue) {
                    if (!this.this$0.bold.getValue()) {
                        Render3DUtil.drawBox(matrixStack, cbox, ColorUtil.injectAlpha(this.this$0.online.getValue(), (int)((double)this.this$0.online.getValue().getAlpha() * this.this$0.fade * 2.0)));
                    } else {
                        Render3DUtil.drawLine(cbox, ColorUtil.injectAlpha(this.this$0.online.getValue(), (int)((double)this.this$0.online.getValue().getAlpha() * this.this$0.fade * 2.0)), this.this$0.lineWidth.getValueInt());
                    }
                }
            }
            if (this.this$0.colortype.getValue().equals((Object)AutoCrystal_ohSMJidwaoXtIVckTOpo.ComboBreaks) && ComboBreaks.INSTANCE.isOn()) {
                if (ComboBreaks.INSTANCE.Acolor.booleanValue) {
                    Render3DUtil.drawFill(matrixStack, cbox, ColorUtil.injectAlpha(ComboBreaks.INSTANCE.Acolor.getValue(), (int)((double)ComboBreaks.INSTANCE.Acolor.getValue().getAlpha() * this.this$0.fade * 2.0)));
                }
                if (ComboBreaks.INSTANCE.Aonline.booleanValue) {
                    if (!this.this$0.bold.getValue()) {
                        Render3DUtil.drawBox(matrixStack, cbox, ColorUtil.injectAlpha(ComboBreaks.INSTANCE.Aonline.getValue(), (int)((double)ComboBreaks.INSTANCE.Aonline.getValue().getAlpha() * this.this$0.fade * 2.0)));
                    } else {
                        Render3DUtil.drawLine(cbox, ColorUtil.injectAlpha(ComboBreaks.INSTANCE.Aonline.getValue(), (int)((double)ComboBreaks.INSTANCE.Aonline.getValue().getAlpha() * this.this$0.fade * 2.0)), this.this$0.lineWidth.getValueInt());
                    }
                }
            }
            if (this.this$0.colortype.getValue().equals((Object)AutoCrystal_ohSMJidwaoXtIVckTOpo.ComboBreaks) && ComboBreaks.INSTANCE.isOff()) {
                if (ComboBreaks.INSTANCE.Dcolor.booleanValue) {
                    Render3DUtil.drawFill(matrixStack, cbox, ColorUtil.injectAlpha(ComboBreaks.INSTANCE.Dcolor.getValue(), (int)((double)ComboBreaks.INSTANCE.Dcolor.getValue().getAlpha() * this.this$0.fade * 2.0)));
                }
                if (ComboBreaks.INSTANCE.Donline.booleanValue) {
                    if (!this.this$0.bold.getValue()) {
                        Render3DUtil.drawBox(matrixStack, cbox, ColorUtil.injectAlpha(ComboBreaks.INSTANCE.Donline.getValue(), (int)((double)ComboBreaks.INSTANCE.Donline.getValue().getAlpha() * this.this$0.fade * 2.0)));
                    } else {
                        Render3DUtil.drawLine(cbox, ColorUtil.injectAlpha(ComboBreaks.INSTANCE.Donline.getValue(), (int)((double)ComboBreaks.INSTANCE.Donline.getValue().getAlpha() * this.this$0.fade * 2.0)), this.this$0.lineWidth.getValueInt());
                    }
                }
            }
            if (this.this$0.colortype.getValue().equals((Object)AutoCrystal_ohSMJidwaoXtIVckTOpo.Sync)) {
                if (ColorsSetting.INSTANCE.box.booleanValue) {
                    Render3DUtil.drawFill(matrixStack, cbox, ColorUtil.injectAlpha(ColorsSetting.INSTANCE.box.getValue(), (int)((double)ColorsSetting.INSTANCE.box.getValue().getAlpha() * this.this$0.fade * 2.0)));
                }
                if (ColorsSetting.INSTANCE.online.booleanValue) {
                    if (!this.this$0.bold.getValue()) {
                        Render3DUtil.drawBox(matrixStack, cbox, ColorUtil.injectAlpha(ColorsSetting.INSTANCE.online.getValue(), (int)((double)ColorsSetting.INSTANCE.online.getValue().getAlpha() * this.this$0.fade * 2.0)));
                    } else {
                        Render3DUtil.drawLine(cbox, ColorUtil.injectAlpha(ColorsSetting.INSTANCE.online.getValue(), (int)((double)ColorsSetting.INSTANCE.online.getValue().getAlpha() * this.this$0.fade * 2.0)), this.this$0.lineWidth.getValueInt());
                    }
                }
            }
        }
        if (this.this$0.text.booleanValue && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0))) {
            ListenerText.drawText3D1("" + this.this$0.lastDamage, AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d, this.this$0.text.getValue());
        }
        if (this.this$0.showCB_A.booleanValue && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0)) && ComboBreaks.INSTANCE.isOn()) {
            ListenerText.drawText3D4("[\u8fdb\u653b\u6a21\u5f0f]", AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.method_1031(0.0, -1.0, 0.0), this.this$0.showCB_A.getValue());
        }
        if (this.this$0.showCB_D.booleanValue && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0)) && ComboBreaks.INSTANCE.isOff()) {
            ListenerText.drawText3D4("[\u538b\u5236\u4e2d..]", AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.method_1031(0.0, -1.0, 0.0), this.this$0.showCB_D.getValue());
        }
        if (this.this$0.misscatext.booleanValue && this.this$0.lastDamage > 0.0f && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0))) {
            ListenerText.drawText3D2(String.format("%.1f Sync", Float.valueOf(this.this$0.tempDamage)), AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.method_1031(0.0, -1.0, 0.0), this.this$0.misscatext.getValue());
        }
        if (this.this$0.spamtext.booleanValue && WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.isOn() && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0))) {
            ListenerText.drawText3D3(String.format("[Web > %.0f > %.0f]", Float.valueOf(WebAuraTick_gaIdrzDzsbegzNTtPQoV.lastYaw), Float.valueOf(WebAuraTick_gaIdrzDzsbegzNTtPQoV.lastPitch)), AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.method_1031(0.0, -1.15, 0.0), this.this$0.spamtext.getValue());
        }
        if (this.this$0.nullpostext.booleanValue && AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0))) {
            ListenerText.drawText3D2(String.format("WaitSync", AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null), AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.method_1031(0.0, -1.0, 0.0), this.this$0.nullpostext.getValue());
        }
        if (this.this$0.showIf.booleanValue && ComboBreaks.INSTANCE.isOn() && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0))) {
            ListenerText.drawText3DIF1(String.format("[\u5168\u529f\u7387]", AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null), AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.method_1031(0.0, -1.0, 0.0), this.this$0.showIf.getValue());
        }
        if (this.this$0.showif2.booleanValue && this.this$0.forceWeb.getValue() && ComboBreaks.INSTANCE.isOff() && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0))) {
            ListenerText.drawText3DIF1(String.format("[\u65e0\u6781\u53d8\u901f]", AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null), AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.method_1031(0.0, -1.0, 0.0), this.this$0.showif2.getValue());
        }
        if (this.this$0.showCleaner.booleanValue && Cleaner_iFwqnooxsJEmHoVteFeQ.INSTANCE.isOn() && !this.this$0.noPosTimer.passedMs((long)(this.this$0.startFadeTime.getValue() * 1000.0))) {
            ListenerText.drawText3DCleaner(String.format("[\u6e05\u7406\u8718\u86db\u7f51\u4e2d..]", AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null), AutoCrystal_QcRVYRsOqpKivetoXSJa.curVec3d.method_1031(0.0, -1.15, 0.0), this.this$0.showCleaner.getValue());
        }
        if (this.this$0.syncdebug.getValue() && AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos != null) {
            CommandManager.sendChatMessage("[!]Waiting CrystalPos, Try do Sync");
        }
    }
}
