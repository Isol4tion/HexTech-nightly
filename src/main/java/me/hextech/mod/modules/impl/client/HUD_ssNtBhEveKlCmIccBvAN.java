package me.hextech.mod.modules.impl.client;

import me.hextech.HexTech;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.MathUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.render.ColorUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.setting.ComboBreaks;
import me.hextech.mod.modules.settings.impl.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class HUD_ssNtBhEveKlCmIccBvAN
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HUD_ssNtBhEveKlCmIccBvAN INSTANCE;
    private final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.GLOBAL));
    public final BooleanSetting armor = this.add(new BooleanSetting("Armor", true, v -> this.page.getValue() == Page.GLOBAL));
    public final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 1000, 0, 2000, v -> this.page.getValue() == Page.GLOBAL));
    public final BooleanSetting lowerCase = this.add(new BooleanSetting("LowerCase", false, v -> this.page.getValue() == Page.GLOBAL));
    private final BooleanSetting grayColors = this.add(new BooleanSetting("Gray", true, v -> this.page.getValue() == Page.GLOBAL));
    private final BooleanSetting renderingUp = this.add(new BooleanSetting("RenderingUp", true, v -> this.page.getValue() == Page.GLOBAL));
    private final BooleanSetting watermark = this.add(new BooleanSetting("Watermark", true, v -> this.page.getValue() == Page.ELEMENTS).setParent());
    public final SliderSetting offset = this.add(new SliderSetting("Offset", 8.0, 0.0, 100.0, -1.0, v -> this.watermark.isOpen() && this.page.getValue() == Page.ELEMENTS));
    public final StringSetting watermarkString = this.add(new StringSetting("Text", "\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c", v -> this.watermark.isOpen() && this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting watermarkShort = this.add(new BooleanSetting("Shorten", false, v -> this.watermark.isOpen() && this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting watermarkVerColor = this.add(new BooleanSetting("VerColor", true, v -> this.watermark.isOpen() && this.page.getValue() == Page.ELEMENTS));
    private final SliderSetting waterMarkY = this.add(new SliderSetting("Height", 2, 2, 12, v -> this.page.getValue() == Page.ELEMENTS && this.watermark.isOpen()));
    private final BooleanSetting idWatermark = this.add(new BooleanSetting("IdWatermark", true, v -> this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting textRadar = this.add(new BooleanSetting("TextRadar", false, v -> this.page.getValue() == Page.ELEMENTS).setParent());
    private final SliderSetting updatedelay = this.add(new SliderSetting("UpdateDelay", 5, 0, 1000, v -> this.page.getValue() == Page.ELEMENTS && this.textRadar.isOpen()));
    private final BooleanSetting health = this.add(new BooleanSetting("Health", false, v -> this.page.getValue() == Page.ELEMENTS && this.textRadar.isOpen()));
    private final BooleanSetting coords = this.add(new BooleanSetting("Position(XYZ)", false, v -> this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting direction = this.add(new BooleanSetting("Direction", false, v -> this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting lag = this.add(new BooleanSetting("LagNotifier", false, v -> this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting greeter = this.add(new BooleanSetting("Welcomer", false, v -> this.page.getValue() == Page.ELEMENTS).setParent());
    private final EnumSetting<GreeterMod> greeterMode = this.add(new EnumSetting<GreeterMod>("Mode", GreeterMod.PLAYER, v -> this.page.getValue() == Page.ELEMENTS && this.greeter.isOpen()));
    private final BooleanSetting greeterNameColor = this.add(new BooleanSetting("NameColor", true, v -> this.greeter.isOpen() && this.greeterMode.getValue() == GreeterMod.PLAYER && this.page.getValue() == Page.ELEMENTS));
    private final StringSetting greeterText = this.add(new StringSetting("WelcomerText", "i sniff coke and smoke dope i got 2 habbits", v -> this.greeter.isOpen() && this.greeterMode.getValue() == GreeterMod.CUSTOM && this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting potions = this.add(new BooleanSetting("Potions", false, v -> this.page.getValue() == Page.ELEMENTS).setParent());
    private final BooleanSetting potionColor = this.add(new BooleanSetting("PotionColor", false, v -> this.page.getValue() == Page.ELEMENTS && this.potions.isOpen()));
    private final BooleanSetting pvphud = this.add(new BooleanSetting("PVPHud", false, v -> this.page.getValue() == Page.ELEMENTS).setParent());
    public final SliderSetting pvphudoffset = this.add(new SliderSetting("PVPHUDOffset", 8.0, 0.0, 100.0, -1.0, v -> this.page.getValue() == Page.ELEMENTS && this.pvphud.isOpen()));
    private final BooleanSetting totemtext = this.add(new BooleanSetting("TotemText", false, v -> this.page.getValue() == Page.ELEMENTS && this.pvphud.isOpen()));
    private final BooleanSetting potiontext = this.add(new BooleanSetting("PotionText", false, v -> this.page.getValue() == Page.ELEMENTS && this.pvphud.isOpen()));
    private final BooleanSetting crtstalText = this.add(new BooleanSetting("CrystalText", false, v -> this.page.getValue() == Page.ELEMENTS && this.pvphud.isOpen()));
    private final BooleanSetting attacktext = this.add(new BooleanSetting("ComboBreaksText", false, v -> this.page.getValue() == Page.ELEMENTS && this.pvphud.isOpen()));
    private final BooleanSetting ping = this.add(new BooleanSetting("Ping", false, v -> this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting speed = this.add(new BooleanSetting("Speed", false, v -> this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting tps = this.add(new BooleanSetting("TPS", false, v -> this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting fps = this.add(new BooleanSetting("FPS", false, v -> this.page.getValue() == Page.ELEMENTS));
    private final BooleanSetting time = this.add(new BooleanSetting("Time", false, v -> this.page.getValue() == Page.ELEMENTS));
    private final EnumSetting colorMode = this.add(new EnumSetting<ColorMode>("ColorMode", ColorMode.Pulse, v -> this.page.getValue() == Page.Color));
    private final SliderSetting rainbowSpeed = this.add(new SliderSetting("RainbowSpeed", 200, 1, 400, v -> (this.colorMode.getValue() == ColorMode.Rainbow || this.colorMode.getValue() == ColorMode.PulseRainbow) && this.page.getValue() == Page.Color));
    private final SliderSetting saturation = this.add(new SliderSetting("Saturation", 130.0, 1.0, 255.0, v -> (this.colorMode.getValue() == ColorMode.Rainbow || this.colorMode.getValue() == ColorMode.PulseRainbow) && this.page.getValue() == Page.Color));
    private final SliderSetting pulseSpeed = this.add(new SliderSetting("PulseSpeed", 100, 1, 400, v -> (this.colorMode.getValue() == ColorMode.Pulse || this.colorMode.getValue() == ColorMode.PulseRainbow) && this.page.getValue() == Page.Color));
    private final SliderSetting rainbowDelay = this.add(new SliderSetting("Delay", 350, 0, 600, v -> this.colorMode.getValue() == ColorMode.Rainbow && this.page.getValue() == Page.Color));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255), v -> this.colorMode.getValue() != ColorMode.Rainbow && this.page.getValue() == Page.Color));
    private final ColorSetting Acolor = this.add(new ColorSetting("AttackColor", new Color(255, 255, 255, 255), v -> this.colorMode.getValue() != ColorMode.Rainbow && this.page.getValue() == Page.Color));
    private final ColorSetting Dcolor = this.add(new ColorSetting("DefendColor", new Color(255, 255, 255, 255), v -> this.colorMode.getValue() != ColorMode.Rainbow && this.page.getValue() == Page.Color));
    private final BooleanSetting sync = this.add(new BooleanSetting("Sync", false, v -> this.page.getValue() == Page.Color));
    private final BooleanSetting debug = this.add(new BooleanSetting("DebugInfo", false, v -> this.page.getValue() == Page.Dev));
    private final Timer timer = new Timer();
    int progress = 0;
    int pulseProgress = 0;
    private Map<String, Integer> players = new HashMap<String, Integer>();
    private int counter = 20;

    public HUD_ssNtBhEveKlCmIccBvAN() {
        super("HUD", "HUD elements drawn on your screen", Category.Client);
        INSTANCE = this;
    }

    public static float round2(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @Override
    public void onUpdate() {
        if (this.timer.passed(this.updatedelay.getValue())) {
            this.players = this.getTextRadarMap();
            this.timer.reset();
        }
        this.progress -= this.rainbowSpeed.getValueInt();
        this.pulseProgress -= this.pulseSpeed.getValueInt();
    }

    @Override
    public void onRender2D(final DrawContext drawContext, final float tickDelta) {
        if (nullCheck()) {
            return;
        }
        this.counter = 20;
        final int width = HUD_ssNtBhEveKlCmIccBvAN.mc.getWindow().getScaledWidth();
        final int height = HUD_ssNtBhEveKlCmIccBvAN.mc.getWindow().getScaledHeight();
        if (this.armor.getValue()) {
            HexTech.GUI.armorHud.draw(drawContext, tickDelta, null);
        }
        if (this.pvphud.getValue()) {
            this.drawpvphud(drawContext, this.pvphudoffset.getValueInt());
        }
        if (this.textRadar.getValue()) {
            this.drawTextRadar(drawContext, this.watermark.getValue() ? ((int) (this.waterMarkY.getValue() + 2.0)) : 2);
        }
        if (this.watermark.getValue()) {
            final String nameString = this.watermarkString.getValue();
            final String verColor = this.watermarkVerColor.getValue() ? "§f" : "";
            final String verString = verColor + (this.watermarkShort.getValue() ? "" : "Nightly");
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, (this.lowerCase.getValue() ? nameString.toLowerCase() : nameString) + verString, 2, this.waterMarkY.getValueInt(), this.getColor(this.counter));
            ++this.counter;
        }
        if (this.idWatermark.getValue()) {
            final String nameString = "\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c ";
            final String domainString = "8";
            final float offset = HUD_ssNtBhEveKlCmIccBvAN.mc.getWindow().getScaledHeight() / 2.0f - 30.0f;
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, nameString + domainString, 2, (int) offset, this.getColor(this.counter));
            ++this.counter;
        }
        final String grayString = this.grayColors.getValue() ? "§7" : "";
        int i = (HUD_ssNtBhEveKlCmIccBvAN.mc.currentScreen instanceof ChatScreen && this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            if (this.potions.getValue()) {
                final List<StatusEffectInstance> effects = new ArrayList<StatusEffectInstance>(HUD_ssNtBhEveKlCmIccBvAN.mc.player.getStatusEffects());
                for (final StatusEffectInstance potionEffect : effects) {
                    final String str = this.getColoredPotionString(potionEffect);
                    i += 10;
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str.toLowerCase() : str, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str)) - 2, height - 2 - i, this.potionColor.getValue() ? potionEffect.getEffectType().getColor() : this.getColor(this.counter));
                    ++this.counter;
                }
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "\u901f\u5ea6 §f" + HexTech.SPEED.getSpeedKpH() + " \u5343\u7c73/\u65f6";
                i += 10;
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str2.toLowerCase() : str2, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2)) - 2, height - 2 - i, this.getColor(this.counter));
                ++this.counter;
            }
            if (this.time.getValue()) {
                final String str2 = grayString + "\u65f6\u95f4 §f" + new SimpleDateFormat("h:mm a").format(new Date());
                i += 10;
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str2.toLowerCase() : str2, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2)) - 2, height - 2 - i, this.getColor(this.counter));
                ++this.counter;
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "\u670d\u52a1\u5668\u7a33\u5b9a\u6027 §f" + HexTech.SERVER.getTPS();
                i += 10;
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str2.toLowerCase() : str2, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2)) - 2, height - 2 - i, this.getColor(this.counter));
                ++this.counter;
            }
            final String fpsText = grayString + "\u5e27\u6570 §f" + HexTech.FPS.getFps();
            final String str3 = grayString + "\u5ef6\u8fdf §f" + HexTech.SERVER.getPing();
            if (HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3) > HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText)) {
                if (this.ping.getValue()) {
                    i += 10;
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str3.toLowerCase() : str3, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3)) - 2, height - 2 - i, this.getColor(this.counter));
                    ++this.counter;
                }
                if (this.fps.getValue()) {
                    i += 10;
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? fpsText.toLowerCase() : fpsText, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText)) - 2, height - 2 - i, this.getColor(this.counter));
                }
            } else {
                if (this.fps.getValue()) {
                    i += 10;
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? fpsText.toLowerCase() : fpsText, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText)) - 2, height - 2 - i, this.getColor(this.counter));
                    ++this.counter;
                }
                if (this.ping.getValue()) {
                    i += 10;
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str3.toLowerCase() : str3, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3)) - 2, height - 2 - i, this.getColor(this.counter));
                }
            }
        } else {
            if (this.potions.getValue()) {
                final List<StatusEffectInstance> effects = new ArrayList<StatusEffectInstance>(HUD_ssNtBhEveKlCmIccBvAN.mc.player.getStatusEffects());
                for (final StatusEffectInstance potionEffect : effects) {
                    final String str = this.getColoredPotionString(potionEffect);
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str.toLowerCase() : str, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str)) - 2, 2 + i++ * 10, this.potionColor.getValue() ? potionEffect.getEffectType().getColor() : this.getColor(this.counter));
                    ++this.counter;
                }
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "\u901f\u5ea6 §f" + HexTech.SPEED.getSpeedKpH() + " \u5343\u7c73/\u65f6";
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str2.toLowerCase() : str2, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2)) - 2, 2 + i++ * 10, this.getColor(this.counter));
                ++this.counter;
            }
            if (this.time.getValue()) {
                final String str2 = grayString + "\u65f6\u95f4 §f" + new SimpleDateFormat("h:mm a").format(new Date());
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str2.toLowerCase() : str2, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2)) - 2, 2 + i++ * 10, this.getColor(this.counter));
                ++this.counter;
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "\u670d\u52a1\u5668\u7a33\u5b9a\u6027 §f" + HexTech.SERVER.getTPS();
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str2.toLowerCase() : str2, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str2)) - 2, 2 + i++ * 10, this.getColor(this.counter));
                ++this.counter;
            }
            final String fpsText = grayString + "\u5e27\u6570 §f" + HexTech.FPS.getFps();
            final String str3 = grayString + "\u5ef6\u8fdf §f" + HexTech.SERVER.getPing();
            if (HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3) > HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText)) {
                if (this.ping.getValue()) {
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str3.toLowerCase() : str3, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3)) - 2, 2 + i++ * 10, this.getColor(this.counter));
                    ++this.counter;
                }
                if (this.fps.getValue()) {
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? fpsText.toLowerCase() : fpsText, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText)) - 2, 2 + i++ * 10, this.getColor(this.counter));
                }
            } else {
                if (this.fps.getValue()) {
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? fpsText.toLowerCase() : fpsText, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(fpsText)) - 2, 2 + i++ * 10, this.getColor(this.counter));
                    ++this.counter;
                }
                if (this.ping.getValue()) {
                    drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, this.lowerCase.getValue() ? str3.toLowerCase() : str3, width - (this.lowerCase.getValue() ? HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3.toLowerCase()) : HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(str3)) - 2, 2 + i++ * 10, this.getColor(this.counter));
                }
            }
        }
        final boolean inHell = HUD_ssNtBhEveKlCmIccBvAN.mc.world.getRegistryKey().equals(World.NETHER);
        final int posX = (int) HUD_ssNtBhEveKlCmIccBvAN.mc.player.getX();
        final int posY = (int) HUD_ssNtBhEveKlCmIccBvAN.mc.player.getY();
        final int posZ = (int) HUD_ssNtBhEveKlCmIccBvAN.mc.player.getZ();
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int) (HUD_ssNtBhEveKlCmIccBvAN.mc.player.getX() * nether);
        final int hposZ = (int) (HUD_ssNtBhEveKlCmIccBvAN.mc.player.getZ() * nether);
        final int yawPitch = (int) MathHelper.wrapDegrees(HUD_ssNtBhEveKlCmIccBvAN.mc.player.getYaw());
        final int p = this.coords.getValue() ? 0 : 11;
        i = ((HUD_ssNtBhEveKlCmIccBvAN.mc.currentScreen instanceof ChatScreen) ? 14 : 0);
        String coordinates = (this.lowerCase.getValue() ? "XYZ: ".toLowerCase() : "XYZ: ") + "§f" + (inHell ? (posX + ", " + posY + ", " + posZ + " §7[§f" + hposX + ", " + hposZ + "§7]§f") : (posX + ", " + posY + ", " + posZ + "§7 [§f" + hposX + ", " + hposZ + "§7]"));
        final String yaw = this.direction.getValue() ? ((this.lowerCase.getValue() ? "Yaw: ".toLowerCase() : "Yaw: ") + "§f" + yawPitch) : "";
        final String coords = this.coords.getValue() ? coordinates : "";
        i += 10;
        ++this.counter;
        drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, yaw, 2, height - i - 22 + p, this.getColor(this.counter));
        ++this.counter;
        drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, coords, 2, height - i, this.getColor(this.counter));
        ++this.counter;
        if (this.greeter.getValue()) {
            this.drawWelcomer(drawContext);
        }
        if (this.lag.getValue()) {
            this.drawLagOMeter(drawContext);
        }
    }

    private void drawWelcomer(DrawContext drawContext) {
        Object text;
        int width = mc.getWindow().getScaledWidth();
        String nameColor = this.greeterNameColor.getValue() ? String.valueOf(Formatting.WHITE) : "";
        Object object = text = this.lowerCase.getValue() ? "Welcome, ".toLowerCase() : "Welcome, ";
        if (this.greeterMode.getValue() == GreeterMod.PLAYER) {
            if (this.greeter.getValue()) {
                text = text + nameColor + mc.getSession().getUsername();
            }
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, text + "\u00a70 :')", (int) ((float) width / 2.0f - (float) HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth((String) text) / 2.0f + 2.0f), 2, this.getColor(this.counter));
            ++this.counter;
        } else {
            String lel = this.greeterText.getValue();
            if (this.greeter.getValue()) {
                lel = this.greeterText.getValue();
            }
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, lel, (int) ((float) width / 2.0f - (float) HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(lel) / 2.0f + 2.0f), 2, this.getColor(this.counter));
            ++this.counter;
        }
    }

    private void drawpvphud(DrawContext drawContext, int yOffset) {
        double x = (double) mc.getWindow().getWidth() / 4.0;
        double y = (double) mc.getWindow().getHeight() / 4.0 + (double) yOffset;
        Objects.requireNonNull(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer);
        int textHeight = 9 + 1;
        String t1 = "Totem " + Formatting.YELLOW + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING);
        String t2 = "Potion " + Formatting.GRAY + InventoryUtil.getPotCount(StatusEffects.RESISTANCE);
        String t3 = "Crystal " + Formatting.WHITE + InventoryUtil.getItemCount(Items.END_CRYSTAL);
        String A1 = "\u00a74[\u64cd\u63a7\u529b] | \u00a78\u538b\u5236";
        String D1 = "\u00a73[\u538b\u5236] | \u00a78\u64cd\u63a7\u529b";
        String t4 = "\u9759\u6001\u540c\u6b65";
        ArrayList<StatusEffectInstance> effects = new ArrayList(HUD_ssNtBhEveKlCmIccBvAN.mc.player.getStatusEffects());
        if (this.totemtext.getValue()) {
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, t1, (int) (x - (double) (HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(t1) / 2)), (int) y, this.getColor(this.counter));
            ++this.counter;
            y += textHeight;
        }
        if (this.potiontext.getValue()) {
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, t2, (int) (x - (double) (HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(t2) / 2)), (int) y, this.getColor(this.counter));
            ++this.counter;
            y += textHeight;
        }
        if (this.crtstalText.getValue()) {
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, t3, (int) (x - (double) (HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(t3) / 2)), (int) y, this.getColor(this.counter));
            ++this.counter;
            y += textHeight;
        }
        if (this.attacktext.getValue()) {
            if (ComboBreaks.INSTANCE.isOn()) {
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, A1, (int) (x - (double) (HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(A1) / 2)), (int) y, this.getColorComboA(this.counter));
            }
            if (ComboBreaks.INSTANCE.isOff()) {
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, D1, (int) (x - (double) (HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(D1) / 2)), (int) y, this.getColorComboD(this.counter));
            }
            ++this.counter;
            y += textHeight;
        }
        for (StatusEffectInstance potionEffect : effects) {
            if (potionEffect.getEffectType() != StatusEffects.RESISTANCE || potionEffect.getAmplifier() + 1 <= 1)
                continue;
            String str = this.getColoredPotionTimeString(potionEffect);
            String t31 = "PotionTime " + Formatting.WHITE + str;
            if (!this.potiontext.getValue()) continue;
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, t31, (int) (x - (double) (HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(t31) / 2)), (int) y, this.getColor(this.counter));
            ++this.counter;
            y += textHeight;
        }
    }

    private void drawLagOMeter(DrawContext drawContext) {
        int width = mc.getWindow().getScaledWidth();
        if (HexTech.SERVER.isServerNotResponding()) {
            String text = "\u00a74" + (this.lowerCase.getValue() ? "Server is lagging for ".toLowerCase() : "Server is lagging for ") + MathUtil.round((float) HexTech.SERVER.serverRespondingTime() / 1000.0f, 1) + "s.";
            drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, text, (int) ((float) width / 2.0f - (float) HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer.getWidth(text) / 2.0f + 2.0f), 20, this.getColor(this.counter));
            ++this.counter;
        }
    }

    private void drawTextRadar(DrawContext drawContext, int yOffset) {
        if (!this.players.isEmpty()) {
            Objects.requireNonNull(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer);
            int y = 9 + 7 + yOffset;
            for (Map.Entry<String, Integer> player : this.players.entrySet()) {
                String text = player.getKey() + " ";
                Objects.requireNonNull(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer);
                int textHeight = 9 + 1;
                drawContext.drawTextWithShadow(HUD_ssNtBhEveKlCmIccBvAN.mc.textRenderer, text, 2, y, this.getColor(this.counter));
                ++this.counter;
                y += textHeight;
            }
        }
    }

    private Map<String, Integer> getTextRadarMap() {
        Map<String, Integer> retval = new HashMap<String, Integer>();
        DecimalFormat dfDistance = new DecimalFormat("#.#");
        dfDistance.setRoundingMode(RoundingMode.CEILING);
        StringBuilder distanceSB = new StringBuilder();
        for (PlayerEntity player : HUD_ssNtBhEveKlCmIccBvAN.mc.world.getPlayers()) {
            if (player.isInvisible() || player.getName().equals(HUD_ssNtBhEveKlCmIccBvAN.mc.player.getName())) continue;
            int distanceInt = (int) HUD_ssNtBhEveKlCmIccBvAN.mc.player.distanceTo(player);
            String distance = dfDistance.format(distanceInt);
            if (distanceInt >= 25) {
                distanceSB.append(Formatting.GREEN);
            } else if (distanceInt > 10) {
                distanceSB.append(Formatting.YELLOW);
            } else {
                distanceSB.append(Formatting.RED);
            }
            distanceSB.append(distance);
            retval.put((this.health.getValue() ? String.valueOf(this.getHealthColor(player)) + HUD_ssNtBhEveKlCmIccBvAN.round2(player.getAbsorptionAmount() + player.getHealth()) + " " : "") + (HexTech.FRIEND.isFriend(player) ? Formatting.AQUA : Formatting.RESET) + player.getName().getString() + " " + Formatting.WHITE + "[" + Formatting.RESET + distanceSB + "m" + Formatting.WHITE + "] " + Formatting.GREEN, (int) HUD_ssNtBhEveKlCmIccBvAN.mc.player.distanceTo(player));
            distanceSB.setLength(0);
        }
        if (!retval.isEmpty()) {
            retval = MathUtil.sortByValue(retval, false);
        }
        return retval;
    }

    private Formatting getHealthColor(@NotNull PlayerEntity entity) {
        int health = (int) ((float) ((int) entity.getHealth()) + entity.getAbsorptionAmount());
        if (health <= 15 && health > 7) {
            return Formatting.YELLOW;
        }
        if (health > 15) {
            return Formatting.GREEN;
        }
        return Formatting.RED;
    }

    private String getColoredPotionString(StatusEffectInstance effect) {
        StatusEffect potion = effect.getEffectType();
        return potion.getName().getString() + " " + (effect.getAmplifier() + 1) + " \u00a7f" + StatusEffectUtil.getDurationText(effect, 1.0f, HUD_ssNtBhEveKlCmIccBvAN.mc.world.getTickManager().getTickRate()).getString();
    }

    private String getColoredPotionTimeString(StatusEffectInstance effect) {
        return StatusEffectUtil.getDurationText(effect, 1.0f, HUD_ssNtBhEveKlCmIccBvAN.mc.world.getTickManager().getTickRate()).getString();
    }

    private int getColor(int counter) {
        if (this.colorMode.getValue() != ColorMode.Custom) {
            return this.rainbow(counter).getRGB();
        }
        if (this.sync.getValue()) {
            return ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.color.getValue().getRGB();
        }
        return this.color.getValue().getRGB();
    }

    private int getColorComboA(int counter) {
        if (this.colorMode.getValue() != ColorMode.Custom) {
            return this.rainbow(counter).getRGB();
        }
        return this.Acolor.getValue().getRGB();
    }

    private int getColorComboD(int counter) {
        if (this.colorMode.getValue() != ColorMode.Custom) {
            return this.rainbow(counter).getRGB();
        }
        return this.Dcolor.getValue().getRGB();
    }

    private Color rainbow(int delay) {
        double rainbowState = Math.ceil(((double) this.progress + (double) delay * this.rainbowDelay.getValue()) / 20.0);
        if (this.colorMode.getValue() == ColorMode.Pulse) {
            if (this.sync.getValue()) {
                return this.pulseColor(ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.color.getValue(), delay);
            }
            return this.pulseColor(this.color.getValue(), delay);
        }
        if (this.colorMode.getValue() == ColorMode.Rainbow) {
            return Color.getHSBColor((float) (rainbowState % 360.0 / 360.0), this.saturation.getValueFloat() / 255.0f, 1.0f);
        }
        return this.pulseColor(Color.getHSBColor((float) (rainbowState % 360.0 / 360.0), this.saturation.getValueFloat() / 255.0f, 1.0f), delay);
    }

    private Color pulseColor(Color color, int index) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float) ((long) this.pulseProgress % 2000L) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + (float) index / 14.0f * Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        brightness = Float.intBitsToFloat(Float.floatToIntBits(18.996923f) ^ 0x7E97F9B3) + Float.intBitsToFloat(Float.floatToIntBits(2.7958195f) ^ 0x7F32EEB5) * brightness;
        hsb[2] = brightness % Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return ColorUtil.injectAlpha(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])), color.getAlpha());
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum GreeterMod {
        PLAYER,
        CUSTOM

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum ColorMode {
        Custom,
        Pulse,
        Rainbow,
        PulseRainbow

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Page {
        ELEMENTS,
        GLOBAL,
        Color,
        Dev

    }
}
