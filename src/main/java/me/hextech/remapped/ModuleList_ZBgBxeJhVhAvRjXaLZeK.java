package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.hextech.HexTech;
import me.hextech.remapped.AnimateUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.FontRenderers;
import me.hextech.remapped.ModuleList;
import me.hextech.remapped.ModuleList_JTSxbGphPlVCUvdsPgfV;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.opengl.GL11;

public class ModuleList_ZBgBxeJhVhAvRjXaLZeK
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ModuleList_ZBgBxeJhVhAvRjXaLZeK INSTANCE;
    public final EnumSetting<AnimateUtil._AcLZzRdHWZkNeKEYTOwI> animMode = this.add(new EnumSetting<AnimateUtil._AcLZzRdHWZkNeKEYTOwI>("AnimMode", AnimateUtil._AcLZzRdHWZkNeKEYTOwI.Mio));
    public final SliderSetting disableSpeed = this.add(new SliderSetting("DisableSpeed", 0.2, -0.2, 1.0, 0.01));
    public final SliderSetting enableSpeed = this.add(new SliderSetting("EnableSpeed", 0.2, 0.0, 1.0, 0.01));
    public final SliderSetting ySpeed = this.add(new SliderSetting("YSpeed", 0.2, 0.01, 1.0, 0.01));
    private final BooleanSetting font = this.add(new BooleanSetting("Font", false));
    private final SliderSetting height = this.add(new SliderSetting("Height", 0, -2, 10));
    private final SliderSetting textOffset = this.add(new SliderSetting("TextOffset", 0, 0, 10));
    private final SliderSetting xOffset = this.add(new SliderSetting("XOffset", 0, 0, 500));
    private final SliderSetting yOffset = this.add(new SliderSetting("YOffset", 10, 0, 300));
    private final BooleanSetting forgeHax = this.add(new BooleanSetting("ForgeHax", true));
    private final BooleanSetting space = this.add(new BooleanSetting("Space", true));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", false));
    private final BooleanSetting animY = this.add(new BooleanSetting("AnimY", true));
    private final BooleanSetting scissor = this.add(new BooleanSetting("Scissor", false));
    private final BooleanSetting onlyBind = this.add(new BooleanSetting("OnlyBind", true));
    private final EnumSetting<ModuleList> colorMode = this.add(new EnumSetting<ModuleList>("ColorMode", ModuleList.Pulse));
    private final SliderSetting rainbowSpeed = this.add(new SliderSetting("RainbowSpeed", 200, 1, 400, v -> this.colorMode.getValue() == ModuleList.Rainbow));
    private final SliderSetting saturation = this.add(new SliderSetting("Saturation", 130.0, 1.0, 255.0, v -> this.colorMode.getValue() == ModuleList.Rainbow));
    private final SliderSetting pulseSpeed = this.add(new SliderSetting("PulseSpeed", 1.0, 0.0, 5.0, 0.1, v -> this.colorMode.getValue() == ModuleList.Pulse));
    private final SliderSetting pulseCounter = this.add(new SliderSetting("Counter", 10, 1, 50, v -> this.colorMode.getValue() == ModuleList.Pulse));
    private final SliderSetting rainbowDelay = this.add(new SliderSetting("Delay", 350, 0, 600, v -> this.colorMode.getValue() == ModuleList.Rainbow));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255), v -> this.colorMode.getValue() != ModuleList.Rainbow));
    private final ColorSetting endColor = this.add(new ColorSetting("EndColor", new Color(255, 0, 0, 255), v -> this.colorMode.getValue() == ModuleList.Pulse));
    private final BooleanSetting rect = this.add(new BooleanSetting("Rect", true));
    private final BooleanSetting backGround = this.add(new BooleanSetting("BackGround", true).setParent());
    private final BooleanSetting bgSync = this.add(new BooleanSetting("Sync", false, v -> this.backGround.isOpen()));
    private final ColorSetting bgColor = this.add(new ColorSetting("BGColor", new Color(0, 0, 0, 100), v -> this.backGround.isOpen()));
    private final BooleanSetting preY = this.add(new BooleanSetting("PreY", true));
    private final BooleanSetting fold = this.add(new BooleanSetting("Fold", true).setParent());
    private final SliderSetting foldSpeed = this.add(new SliderSetting("FoldSpeed", 0.1, 0.01, 1.0, 0.01, v -> this.fold.isOpen()));
    private final BooleanSetting fade = this.add(new BooleanSetting("Fade", true).setParent());
    private final SliderSetting fadeSpeed = this.add(new SliderSetting("FadeSpeed", 0.1, 0.01, 1.0, 0.01, v -> this.fade.isOpen()));
    private final Timer timer = new Timer();
    boolean update;
    int progress = 0;
    private List<ModuleList_JTSxbGphPlVCUvdsPgfV> modulesList = new ArrayList<ModuleList_JTSxbGphPlVCUvdsPgfV>();
    private boolean aBoolean;

    public ModuleList_ZBgBxeJhVhAvRjXaLZeK() {
        super("ModuleList", Module_JlagirAibYQgkHtbRnhw.Client);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.modulesList.clear();
        for (Module_eSdgMXWuzcxgQVaJFmKZ module : HexTech.MODULE.modules) {
            this.modulesList.add(new ModuleList_JTSxbGphPlVCUvdsPgfV(this, module));
        }
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        int startY;
        if (this.space.getValue() != this.aBoolean) {
            for (ModuleList_JTSxbGphPlVCUvdsPgfV modules : this.modulesList) {
                modules.updateName();
            }
            this.aBoolean = this.space.getValue();
        }
        for (ModuleList_JTSxbGphPlVCUvdsPgfV modules : this.modulesList) {
            modules.update();
        }
        if (this.update) {
            this.modulesList = this.modulesList.stream().sorted(Comparator.comparing(module -> this.getStringWidth(module.name) * -1)).collect(Collectors.toList());
            this.update = false;
        }
        if (this.timer.passed(25L)) {
            this.progress -= this.rainbowSpeed.getValueInt();
            this.timer.reset();
        }
        int lastY = startY = this.down.getValue() ? mc.getWindow().getScaledHeight() - this.yOffset.getValueInt() - this.getFontHeight() : this.yOffset.getValueInt();
        int counter = 20;
        for (ModuleList_JTSxbGphPlVCUvdsPgfV modules : this.modulesList) {
            if (modules.module.isOn() && modules.module.drawnSetting.getValue() && (!this.onlyBind.getValue() || modules.module.getBind().getKey() != -1)) {
                modules.enable();
            } else {
                modules.disable();
            }
            if (modules.isEnabled) {
                modules.fade = this.fade.getValue() ? this.animate(modules.fade, 1.0, this.fadeSpeed.getValue()) : 1.0;
                modules.fold = 1.0;
                modules.x = this.animate(modules.x, this.getStringWidth(this.getSuffix(modules.name)), this.enableSpeed.getValue());
            } else {
                modules.fade = this.fade.getValue() ? this.animate(modules.fade, 0.08, this.fadeSpeed.getValue()) : 1.0;
                modules.fold = this.animate(modules.fold, -0.1, this.foldSpeed.getValue());
                modules.x = this.animate(modules.x, -1.0, this.disableSpeed.getValue());
                if (modules.x <= 0.0 || modules.fade <= 0.084 || this.fold.getValue() && modules.fold <= 0.0) {
                    modules.hide = true;
                    continue;
                }
            }
            if (modules.hide) {
                modules.updateName();
                modules.x = 0.0;
                modules.y = this.animY.getValue() ? (double)startY : (double)lastY;
                modules.nameUpdated = false;
                modules.hide = false;
            }
            if (modules.nameUpdated) {
                modules.nameUpdated = false;
                modules.y = this.animY.getValue() && !modules.isEnabled ? (double)startY : (double)lastY;
            } else {
                modules.y = this.animate(modules.y, this.animY.getValue() && !modules.isEnabled ? (double)startY : (double)lastY, this.ySpeed.getValue());
            }
            ++counter;
            int textX = (int)((double)mc.getWindow().getScaledWidth() - modules.x - this.xOffset.getValue() - (double)(this.rect.getValue() ? 2 : 0));
            if (this.fold.getValue()) {
                drawContext.getMatrices().push();
                drawContext.getMatrices().translate(0.0, modules.y * (1.0 - modules.fold), 0.0);
                drawContext.getMatrices().scale(1.0f, (float)modules.fold, 1.0f);
            }
            if (this.scissor.getValue()) {
                GL11.glEnable((int)3089);
                GL11.glScissor((int)0, (int)0, (int)((mc.getWindow().getWidth() / 2 - this.xOffset.getValueInt() - (this.rect.getValue() ? 2 : 0)) * 2), (int)mc.getWindow().getHeight());
            }
            if (this.backGround.getValue()) {
                Render2DUtil.drawRect(drawContext.getMatrices(), (float)(textX - 1), (float)((int)modules.y), (float)mc.getWindow().getScaledWidth() - (float)this.xOffset.getValueInt() + 1.0f - (float)textX + 1.0f, (float)(this.getFontHeight() + this.height.getValueInt()), this.bgSync.getValue() ? ColorUtil.injectAlpha(this.getColor(counter), (int)((double)this.bgColor.getValue().getAlpha() * modules.fade)) : ColorUtil.injectAlpha(this.bgColor.getValue().getRGB(), (int)((double)this.bgColor.getValue().getAlpha() * modules.fade)));
            }
            if (this.font.getValue()) {
                FontRenderers.Arial.drawString(drawContext.getMatrices(), this.getSuffix(modules.name), textX, (int)(modules.y + 1.0 + (double)this.textOffset.getValueInt()), ColorUtil.injectAlpha(this.getColor(counter), (int)(255.0 * modules.fade)));
            } else {
                drawContext.drawTextWithShadow(ModuleList_ZBgBxeJhVhAvRjXaLZeK.mc.textRenderer, this.getSuffix(modules.name), textX, (int)(modules.y + 1.0 + (double)this.textOffset.getValueInt()), ColorUtil.injectAlpha(this.getColor(counter), (int)(255.0 * modules.fade)));
            }
            if (this.scissor.getValue()) {
                GL11.glDisable((int)3089);
            }
            if (this.fold.getValue()) {
                drawContext.getMatrices().pop();
            }
            if (this.rect.getValue()) {
                Render2DUtil.drawRect(drawContext.getMatrices(), (float)mc.getWindow().getScaledWidth() - (float)this.xOffset.getValueInt() - 1.0f, (float)((int)modules.y), 1.0f, (float)(this.getFontHeight() + this.height.getValueInt()), ColorUtil.injectAlpha(this.getColor(counter), (int)(255.0 * modules.fade)));
            }
            if (!modules.isEnabled && this.preY.getValue()) continue;
            if (this.down.getValue()) {
                lastY -= this.getFontHeight() + this.height.getValueInt();
                continue;
            }
            lastY += this.getFontHeight() + this.height.getValueInt();
        }
    }

    public double animate(double current, double endPoint, double speed) {
        if (speed >= 1.0) {
            return endPoint;
        }
        if (speed == 0.0) {
            return current;
        }
        return AnimateUtil.animate(current, endPoint, speed, this.animMode.getValue());
    }

    private String getSuffix(String s) {
        if (this.forgeHax.getValue()) {
            return s + "\u00a7r<";
        }
        return s;
    }

    private int getColor(int counter) {
        if (this.colorMode.getValue() != ModuleList.Custom) {
            return this.rainbow(counter).getRGB();
        }
        return this.color.getValue().getRGB();
    }

    private Color rainbow(int delay) {
        if (this.colorMode.getValue() == ModuleList.Pulse) {
            return ColorUtil.pulseColor(this.color.getValue(), this.endColor.getValue(), delay, this.pulseCounter.getValueInt(), this.pulseSpeed.getValue());
        }
        if (this.colorMode.getValue() == ModuleList.Rainbow) {
            double rainbowState = Math.ceil(((double)this.progress + (double)delay * this.rainbowDelay.getValue()) / 20.0);
            return Color.getHSBColor((float)(rainbowState % 360.0 / 360.0), this.saturation.getValueFloat() / 255.0f, 1.0f);
        }
        return this.color.getValue();
    }

    private int getStringWidth(String text) {
        if (this.font.getValue()) {
            return (int)FontRenderers.Arial.getWidth(text);
        }
        return ModuleList_ZBgBxeJhVhAvRjXaLZeK.mc.textRenderer.getWidth(text);
    }

    private int getFontHeight() {
        if (this.font.getValue()) {
            return (int)FontRenderers.Arial.getFontHeight();
        }
        Objects.requireNonNull(ModuleList_ZBgBxeJhVhAvRjXaLZeK.mc.textRenderer);
        return 9;
    }
}
