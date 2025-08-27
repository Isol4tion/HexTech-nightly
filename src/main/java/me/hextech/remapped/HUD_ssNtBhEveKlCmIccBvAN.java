package me.hextech.remapped;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map$Entry;
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

public class HUD_ssNtBhEveKlCmIccBvAN extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static HUD_ssNtBhEveKlCmIccBvAN INSTANCE;
   private final EnumSetting<HUD_ztERXpljXztBNAdBhxyn> page = this.add(new EnumSetting("Page", HUD_ztERXpljXztBNAdBhxyn.GLOBAL));
   public final BooleanSetting armor = this.add(new BooleanSetting("Armor", true, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.GLOBAL));
   public final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 1000, 0, 2000, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.GLOBAL));
   public final BooleanSetting lowerCase = this.add(new BooleanSetting("LowerCase", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.GLOBAL));
   private final BooleanSetting grayColors = this.add(new BooleanSetting("Gray", true, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.GLOBAL));
   private final BooleanSetting renderingUp = this.add(new BooleanSetting("RenderingUp", true, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.GLOBAL));
   private final BooleanSetting watermark = this.add(
      new BooleanSetting("Watermark", true, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS).setParent()
   );
   public final SliderSetting offset = this.add(
      new SliderSetting("Offset", 8.0, 0.0, 100.0, -1.0, v -> this.watermark.isOpen() && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS)
   );
   public final StringSetting watermarkString = this.add(
      new StringSetting("Text", "ʜᴇӼᴛᴇᴄʜ", v -> this.watermark.isOpen() && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS)
   );
   private final BooleanSetting watermarkShort = this.add(
      new BooleanSetting("Shorten", false, v -> this.watermark.isOpen() && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS)
   );
   private final BooleanSetting watermarkVerColor = this.add(
      new BooleanSetting("VerColor", true, v -> this.watermark.isOpen() && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS)
   );
   private final SliderSetting waterMarkY = this.add(
      new SliderSetting("Height", 2, 2, 12, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.watermark.isOpen())
   );
   private final BooleanSetting idWatermark = this.add(new BooleanSetting("IdWatermark", true, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final BooleanSetting textRadar = this.add(
      new BooleanSetting("TextRadar", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS).setParent()
   );
   private final SliderSetting updatedelay = this.add(
      new SliderSetting("UpdateDelay", 5, 0, 1000, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.textRadar.isOpen())
   );
   private final BooleanSetting health = this.add(
      new BooleanSetting("Health", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.textRadar.isOpen())
   );
   private final BooleanSetting coords = this.add(new BooleanSetting("Position(XYZ)", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final BooleanSetting direction = this.add(new BooleanSetting("Direction", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final BooleanSetting lag = this.add(new BooleanSetting("LagNotifier", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final BooleanSetting greeter = this.add(
      new BooleanSetting("Welcomer", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS).setParent()
   );
   private final EnumSetting<HUD> greeterMode = this.add(
      new EnumSetting("Mode", HUD.PLAYER, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.greeter.isOpen())
   );
   private final BooleanSetting greeterNameColor = this.add(
      new BooleanSetting(
         "NameColor",
         true,
         v -> this.greeter.isOpen() && this.greeterMode.getValue() == HUD.PLAYER && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS
      )
   );
   private final StringSetting greeterText = this.add(
      new StringSetting(
         "WelcomerText",
         "i sniff coke and smoke dope i got 2 habbits",
         v -> this.greeter.isOpen() && this.greeterMode.getValue() == HUD.CUSTOM && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS
      )
   );
   private final BooleanSetting potions = this.add(
      new BooleanSetting("Potions", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS).setParent()
   );
   private final BooleanSetting potionColor = this.add(
      new BooleanSetting("PotionColor", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.potions.isOpen())
   );
   private final BooleanSetting pvphud = this.add(
      new BooleanSetting("PVPHud", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS).setParent()
   );
   public final SliderSetting pvphudoffset = this.add(
      new SliderSetting("PVPHUDOffset", 8.0, 0.0, 100.0, -1.0, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.pvphud.isOpen())
   );
   private final BooleanSetting totemtext = this.add(
      new BooleanSetting("TotemText", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.pvphud.isOpen())
   );
   private final BooleanSetting potiontext = this.add(
      new BooleanSetting("PotionText", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.pvphud.isOpen())
   );
   private final BooleanSetting crtstalText = this.add(
      new BooleanSetting("CrystalText", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.pvphud.isOpen())
   );
   private final BooleanSetting attacktext = this.add(
      new BooleanSetting("ComboBreaksText", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS && this.pvphud.isOpen())
   );
   private final BooleanSetting ping = this.add(new BooleanSetting("Ping", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final BooleanSetting speed = this.add(new BooleanSetting("Speed", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final BooleanSetting tps = this.add(new BooleanSetting("TPS", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final BooleanSetting fps = this.add(new BooleanSetting("FPS", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final BooleanSetting time = this.add(new BooleanSetting("Time", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.ELEMENTS));
   private final EnumSetting colorMode = this.add(
      new EnumSetting("ColorMode", HUD_awERnEnjBmVoXYDZWizd.Pulse, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color)
   );
   private final SliderSetting rainbowSpeed = this.add(
      new SliderSetting(
         "RainbowSpeed",
         200,
         1,
         400,
         v -> (this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.Rainbow || this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.PulseRainbow)
               && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color
      )
   );
   private final SliderSetting saturation = this.add(
      new SliderSetting(
         "Saturation",
         130.0,
         1.0,
         255.0,
         v -> (this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.Rainbow || this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.PulseRainbow)
               && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color
      )
   );
   private final SliderSetting pulseSpeed = this.add(
      new SliderSetting(
         "PulseSpeed",
         100,
         1,
         400,
         v -> (this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.Pulse || this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.PulseRainbow)
               && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color
      )
   );
   private final SliderSetting rainbowDelay = this.add(
      new SliderSetting(
         "Delay", 350, 0, 600, v -> this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.Rainbow && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color
      )
   );
   private final ColorSetting color = this.add(
      new ColorSetting(
         "Color",
         new Color(255, 255, 255, 255),
         v -> this.colorMode.getValue() != HUD_awERnEnjBmVoXYDZWizd.Rainbow && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color
      )
   );
   private final ColorSetting Acolor = this.add(
      new ColorSetting(
         "AttackColor",
         new Color(255, 255, 255, 255),
         v -> this.colorMode.getValue() != HUD_awERnEnjBmVoXYDZWizd.Rainbow && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color
      )
   );
   private final ColorSetting Dcolor = this.add(
      new ColorSetting(
         "DefendColor",
         new Color(255, 255, 255, 255),
         v -> this.colorMode.getValue() != HUD_awERnEnjBmVoXYDZWizd.Rainbow && this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color
      )
   );
   private final BooleanSetting sync = this.add(new BooleanSetting("Sync", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Color));
   private final BooleanSetting debug = this.add(new BooleanSetting("DebugInfo", false, v -> this.page.getValue() == HUD_ztERXpljXztBNAdBhxyn.Dev));
   private final Timer timer = new Timer();
   private Map<String, Integer> players = new HashMap();
   private int counter = 20;
   int progress = 0;
   int pulseProgress = 0;

   public HUD_ssNtBhEveKlCmIccBvAN() {
      super("HUD", "HUD elements drawn on your screen", Module_JlagirAibYQgkHtbRnhw.Client);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (this.timer.passed(this.updatedelay.getValue())) {
         this.players = this.getTextRadarMap();
         this.timer.reset();
      }

      this.progress = this.progress - this.rainbowSpeed.getValueInt();
      this.pulseProgress = this.pulseProgress - this.pulseSpeed.getValueInt();
   }

   @Override
   public void onRender2D(DrawContext drawContext, float tickDelta) {
      if (!nullCheck()) {
         this.counter = 20;
         int width = mc.method_22683().method_4486();
         int height = mc.method_22683().method_4502();
         if (this.armor.getValue()) {
            me.hextech.HexTech.GUI.armorHud.draw(drawContext, tickDelta, null);
         }

         if (this.pvphud.getValue()) {
            this.drawpvphud(drawContext, this.pvphudoffset.getValueInt());
         }

         if (this.textRadar.getValue()) {
            this.drawTextRadar(drawContext, this.watermark.getValue() ? (int)(this.waterMarkY.getValue() + 2.0) : 2);
         }

         if (this.watermark.getValue()) {
            String nameString = this.watermarkString.getValue() + " ";
            String verColor = this.watermarkVerColor.getValue() ? "§f" : "";
            String verString = verColor + (this.watermarkShort.getValue() ? "" : "Nightly");
            drawContext.method_25303(
               mc.field_1772,
               (this.lowerCase.getValue() ? nameString.toLowerCase() : nameString) + verString,
               2,
               this.waterMarkY.getValueInt(),
               this.getColor(this.counter)
            );
            this.counter++;
         }

         if (this.idWatermark.getValue()) {
            String nameString = "ʜᴇӼᴛᴇᴄʜ ";
            String domainString = "8";
            float offset = (float)mc.method_22683().method_4502() / 2.0F - 30.0F;
            drawContext.method_25303(mc.field_1772, nameString + domainString, 2, (int)offset, this.getColor(this.counter));
            this.counter++;
         }

         String grayString = this.grayColors.getValue() ? "§7" : "";
         int i = mc.field_1755 instanceof ChatScreen && this.renderingUp.getValue() ? 13 : (this.renderingUp.getValue() ? -2 : 0);
         if (this.renderingUp.getValue()) {
            if (this.potions.getValue()) {
               for (StatusEffectInstance potionEffect : new ArrayList(mc.field_1724.method_6026())) {
                  String str = this.getColoredPotionString(potionEffect);
                  i += 10;
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? str.toLowerCase() : str,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str.toLowerCase()) : mc.field_1772.method_1727(str)) - 2,
                     height - 2 - i,
                     this.potionColor.getValue() ? potionEffect.method_5579().method_5556() : this.getColor(this.counter)
                  );
                  this.counter++;
               }
            }

            if (this.speed.getValue()) {
               String str = grayString + "速度 §f" + me.hextech.HexTech.SPEED.getSpeedKpH() + " 千米/时";
               i += 10;
               drawContext.method_25303(
                  mc.field_1772,
                  this.lowerCase.getValue() ? str.toLowerCase() : str,
                  width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str.toLowerCase()) : mc.field_1772.method_1727(str)) - 2,
                  height - 2 - i,
                  this.getColor(this.counter)
               );
               this.counter++;
            }

            if (this.time.getValue()) {
               String str = grayString + "时间 §f" + new SimpleDateFormat("h:mm a").format(new Date());
               i += 10;
               drawContext.method_25303(
                  mc.field_1772,
                  this.lowerCase.getValue() ? str.toLowerCase() : str,
                  width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str.toLowerCase()) : mc.field_1772.method_1727(str)) - 2,
                  height - 2 - i,
                  this.getColor(this.counter)
               );
               this.counter++;
            }

            if (this.tps.getValue()) {
               String str = grayString + "服务器稳定性 §f" + me.hextech.HexTech.SERVER.getTPS();
               i += 10;
               drawContext.method_25303(
                  mc.field_1772,
                  this.lowerCase.getValue() ? str.toLowerCase() : str,
                  width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str.toLowerCase()) : mc.field_1772.method_1727(str)) - 2,
                  height - 2 - i,
                  this.getColor(this.counter)
               );
               this.counter++;
            }

            String fpsText = grayString + "帧数 §f" + me.hextech.HexTech.FPS.getFps();
            String str1 = grayString + "延迟 §f" + me.hextech.HexTech.SERVER.getPing();
            if (mc.field_1772.method_1727(str1) > mc.field_1772.method_1727(fpsText)) {
               if (this.ping.getValue()) {
                  i += 10;
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? str1.toLowerCase() : str1,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str1.toLowerCase()) : mc.field_1772.method_1727(str1)) - 2,
                     height - 2 - i,
                     this.getColor(this.counter)
                  );
                  this.counter++;
               }

               if (this.fps.getValue()) {
                  i += 10;
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? fpsText.toLowerCase() : fpsText,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(fpsText.toLowerCase()) : mc.field_1772.method_1727(fpsText)) - 2,
                     height - 2 - i,
                     this.getColor(this.counter)
                  );
               }
            } else {
               if (this.fps.getValue()) {
                  i += 10;
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? fpsText.toLowerCase() : fpsText,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(fpsText.toLowerCase()) : mc.field_1772.method_1727(fpsText)) - 2,
                     height - 2 - i,
                     this.getColor(this.counter)
                  );
                  this.counter++;
               }

               if (this.ping.getValue()) {
                  i += 10;
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? str1.toLowerCase() : str1,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str1.toLowerCase()) : mc.field_1772.method_1727(str1)) - 2,
                     height - 2 - i,
                     this.getColor(this.counter)
                  );
               }
            }
         } else {
            if (this.potions.getValue()) {
               for (StatusEffectInstance potionEffect : new ArrayList(mc.field_1724.method_6026())) {
                  String str = this.getColoredPotionString(potionEffect);
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? str.toLowerCase() : str,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str.toLowerCase()) : mc.field_1772.method_1727(str)) - 2,
                     2 + i++ * 10,
                     this.potionColor.getValue() ? potionEffect.method_5579().method_5556() : this.getColor(this.counter)
                  );
                  this.counter++;
               }
            }

            if (this.speed.getValue()) {
               String str = grayString + "速度 §f" + me.hextech.HexTech.SPEED.getSpeedKpH() + " 千米/时";
               drawContext.method_25303(
                  mc.field_1772,
                  this.lowerCase.getValue() ? str.toLowerCase() : str,
                  width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str.toLowerCase()) : mc.field_1772.method_1727(str)) - 2,
                  2 + i++ * 10,
                  this.getColor(this.counter)
               );
               this.counter++;
            }

            if (this.time.getValue()) {
               String str = grayString + "时间 §f" + new SimpleDateFormat("h:mm a").format(new Date());
               drawContext.method_25303(
                  mc.field_1772,
                  this.lowerCase.getValue() ? str.toLowerCase() : str,
                  width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str.toLowerCase()) : mc.field_1772.method_1727(str)) - 2,
                  2 + i++ * 10,
                  this.getColor(this.counter)
               );
               this.counter++;
            }

            if (this.tps.getValue()) {
               String str = grayString + "服务器稳定性 §f" + me.hextech.HexTech.SERVER.getTPS();
               drawContext.method_25303(
                  mc.field_1772,
                  this.lowerCase.getValue() ? str.toLowerCase() : str,
                  width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str.toLowerCase()) : mc.field_1772.method_1727(str)) - 2,
                  2 + i++ * 10,
                  this.getColor(this.counter)
               );
               this.counter++;
            }

            String fpsText = grayString + "帧数 §f" + me.hextech.HexTech.FPS.getFps();
            String str1 = grayString + "延迟 §f" + me.hextech.HexTech.SERVER.getPing();
            if (mc.field_1772.method_1727(str1) > mc.field_1772.method_1727(fpsText)) {
               if (this.ping.getValue()) {
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? str1.toLowerCase() : str1,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str1.toLowerCase()) : mc.field_1772.method_1727(str1)) - 2,
                     2 + i++ * 10,
                     this.getColor(this.counter)
                  );
                  this.counter++;
               }

               if (this.fps.getValue()) {
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? fpsText.toLowerCase() : fpsText,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(fpsText.toLowerCase()) : mc.field_1772.method_1727(fpsText)) - 2,
                     2 + i++ * 10,
                     this.getColor(this.counter)
                  );
               }
            } else {
               if (this.fps.getValue()) {
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? fpsText.toLowerCase() : fpsText,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(fpsText.toLowerCase()) : mc.field_1772.method_1727(fpsText)) - 2,
                     2 + i++ * 10,
                     this.getColor(this.counter)
                  );
                  this.counter++;
               }

               if (this.ping.getValue()) {
                  drawContext.method_25303(
                     mc.field_1772,
                     this.lowerCase.getValue() ? str1.toLowerCase() : str1,
                     width - (this.lowerCase.getValue() ? mc.field_1772.method_1727(str1.toLowerCase()) : mc.field_1772.method_1727(str1)) - 2,
                     2 + i++ * 10,
                     this.getColor(this.counter)
                  );
               }
            }
         }

         boolean inHell = mc.field_1687.method_27983().equals(World.field_25180);
         int posX = (int)mc.field_1724.method_23317();
         int posY = (int)mc.field_1724.method_23318();
         int posZ = (int)mc.field_1724.method_23321();
         float nether = !inHell ? 0.125F : 8.0F;
         int hposX = (int)(mc.field_1724.method_23317() * (double)nether);
         int hposZ = (int)(mc.field_1724.method_23321() * (double)nether);
         int yawPitch = (int)MathHelper.method_15393(mc.field_1724.method_36454());
         int p = this.coords.getValue() ? 0 : 11;
         i = mc.field_1755 instanceof ChatScreen ? 14 : 0;
         String coordinates = (this.lowerCase.getValue() ? "XYZ: ".toLowerCase() : "XYZ: ")
            + "§f"
            + (
               inHell
                  ? posX + ", " + posY + ", " + posZ + "§7 [§f" + hposX + ", " + hposZ + "§7]§f"
                  : posX + ", " + posY + ", " + posZ + "§7 [§f" + hposX + ", " + hposZ + "§7]"
            );
         String yaw = this.direction.getValue() ? (this.lowerCase.getValue() ? "Yaw: ".toLowerCase() : "Yaw: ") + "§f" + yawPitch : "";
         String coords = this.coords.getValue() ? coordinates : "";
         i += 10;
         this.counter++;
         drawContext.method_25303(mc.field_1772, yaw, 2, height - i - 22 + p, this.getColor(this.counter));
         this.counter++;
         drawContext.method_25303(mc.field_1772, coords, 2, height - i, this.getColor(this.counter));
         this.counter++;
         if (this.greeter.getValue()) {
            this.drawWelcomer(drawContext);
         }

         if (this.lag.getValue()) {
            this.drawLagOMeter(drawContext);
         }
      }
   }

   private void drawWelcomer(DrawContext drawContext) {
      int width = mc.method_22683().method_4486();
      String nameColor = this.greeterNameColor.getValue() ? String.valueOf(Formatting.field_1068) : "";
      String text = this.lowerCase.getValue() ? "Welcome, ".toLowerCase() : "Welcome, ";
      if (this.greeterMode.getValue() == HUD.PLAYER) {
         if (this.greeter.getValue()) {
            text = text + nameColor + mc.method_1548().method_1676();
         }

         drawContext.method_25303(
            mc.field_1772, text + "§0 :')", (int)((float)width / 2.0F - (float)mc.field_1772.method_1727(text) / 2.0F + 2.0F), 2, this.getColor(this.counter)
         );
         this.counter++;
      } else {
         String lel = this.greeterText.getValue();
         if (this.greeter.getValue()) {
            lel = this.greeterText.getValue();
         }

         drawContext.method_25303(
            mc.field_1772, lel, (int)((float)width / 2.0F - (float)mc.field_1772.method_1727(lel) / 2.0F + 2.0F), 2, this.getColor(this.counter)
         );
         this.counter++;
      }
   }

   private void drawpvphud(DrawContext drawContext, int yOffset) {
      double x = (double)mc.method_22683().method_4480() / 4.0;
      double y = (double)mc.method_22683().method_4507() / 4.0 + (double)yOffset;
      int textHeight = 9 + 1;
      String t1 = "Totem " + Formatting.field_1054 + InventoryUtil.getItemCount(Items.field_8288);
      String t2 = "Potion " + Formatting.field_1080 + InventoryUtil.getPotCount(StatusEffects.field_5907);
      String t3 = "Crystal " + Formatting.field_1068 + InventoryUtil.getItemCount(Items.field_8301);
      String A1 = "§4[操控力] | §8压制";
      String D1 = "§3[压制] | §8操控力";
      String t4 = "静态同步";
      List<StatusEffectInstance> effects = new ArrayList(mc.field_1724.method_6026());
      if (this.totemtext.getValue()) {
         drawContext.method_25303(mc.field_1772, t1, (int)(x - (double)(mc.field_1772.method_1727(t1) / 2)), (int)y, this.getColor(this.counter));
         this.counter++;
         y += (double)textHeight;
      }

      if (this.potiontext.getValue()) {
         drawContext.method_25303(mc.field_1772, t2, (int)(x - (double)(mc.field_1772.method_1727(t2) / 2)), (int)y, this.getColor(this.counter));
         this.counter++;
         y += (double)textHeight;
      }

      if (this.crtstalText.getValue()) {
         drawContext.method_25303(mc.field_1772, t3, (int)(x - (double)(mc.field_1772.method_1727(t3) / 2)), (int)y, this.getColor(this.counter));
         this.counter++;
         y += (double)textHeight;
      }

      if (this.attacktext.getValue()) {
         if (ComboBreaks.INSTANCE.isOn()) {
            drawContext.method_25303(mc.field_1772, A1, (int)(x - (double)(mc.field_1772.method_1727(A1) / 2)), (int)y, this.getColorComboA(this.counter));
         }

         if (ComboBreaks.INSTANCE.isOff()) {
            drawContext.method_25303(mc.field_1772, D1, (int)(x - (double)(mc.field_1772.method_1727(D1) / 2)), (int)y, this.getColorComboD(this.counter));
         }

         this.counter++;
         y += (double)textHeight;
      }

      for (StatusEffectInstance potionEffect : effects) {
         if (potionEffect.method_5579() == StatusEffects.field_5907 && potionEffect.method_5578() + 1 > 1) {
            String str = this.getColoredPotionTimeString(potionEffect);
            String t31 = "PotionTime " + Formatting.field_1068 + str;
            if (this.potiontext.getValue()) {
               drawContext.method_25303(mc.field_1772, t31, (int)(x - (double)(mc.field_1772.method_1727(t31) / 2)), (int)y, this.getColor(this.counter));
               this.counter++;
               y += (double)textHeight;
            }
         }
      }
   }

   private void drawLagOMeter(DrawContext drawContext) {
      int width = mc.method_22683().method_4486();
      if (me.hextech.HexTech.SERVER.isServerNotResponding()) {
         String text = "§4"
            + (this.lowerCase.getValue() ? "Server is lagging for ".toLowerCase() : "Server is lagging for ")
            + MathUtil.round((float)me.hextech.HexTech.SERVER.serverRespondingTime() / 1000.0F, 1)
            + "s.";
         drawContext.method_25303(
            mc.field_1772, text, (int)((float)width / 2.0F - (float)mc.field_1772.method_1727(text) / 2.0F + 2.0F), 20, this.getColor(this.counter)
         );
         this.counter++;
      }
   }

   private void drawTextRadar(DrawContext drawContext, int yOffset) {
      if (!this.players.isEmpty()) {
         int y = 9 + 7 + yOffset;

         for (Map$Entry<String, Integer> player : this.players.entrySet()) {
            String text = player.getKey() + " ";
            int textHeight = 9 + 1;
            drawContext.method_25303(mc.field_1772, text, 2, y, this.getColor(this.counter));
            this.counter++;
            y += textHeight;
         }
      }
   }

   private Map<String, Integer> getTextRadarMap() {
      Map<String, Integer> retval = new HashMap();
      DecimalFormat dfDistance = new DecimalFormat("#.#");
      dfDistance.setRoundingMode(RoundingMode.CEILING);
      StringBuilder distanceSB = new StringBuilder();

      for (PlayerEntity player : mc.field_1687.method_18456()) {
         if (!player.method_5767() && !player.method_5477().equals(mc.field_1724.method_5477())) {
            int distanceInt = (int)mc.field_1724.method_5739(player);
            String distance = dfDistance.format((long)distanceInt);
            if (distanceInt >= 25) {
               distanceSB.append(Formatting.field_1060);
            } else if (distanceInt > 10) {
               distanceSB.append(Formatting.field_1054);
            } else {
               distanceSB.append(Formatting.field_1061);
            }

            distanceSB.append(distance);
            retval.put(
               (this.health.getValue() ? "" + this.getHealthColor(player) + round2((double)(player.method_6067() + player.method_6032())) + " " : "")
                  + (me.hextech.HexTech.FRIEND.isFriend(player) ? Formatting.field_1075 : Formatting.field_1070)
                  + player.method_5477().getString()
                  + " "
                  + Formatting.field_1068
                  + "["
                  + Formatting.field_1070
                  + distanceSB
                  + "m"
                  + Formatting.field_1068
                  + "] "
                  + Formatting.field_1060,
               (int)mc.field_1724.method_5739(player)
            );
            distanceSB.setLength(0);
         }
      }

      if (!retval.isEmpty()) {
         retval = MathUtil.sortByValue(retval, false);
      }

      return retval;
   }

   public static float round2(double value) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(1, RoundingMode.HALF_UP);
      return bd.floatValue();
   }

   private Formatting getHealthColor(@NotNull PlayerEntity entity) {
      int health = (int)((float)((int)entity.method_6032()) + entity.method_6067());
      if (health <= 15 && health > 7) {
         return Formatting.field_1054;
      } else {
         return health > 15 ? Formatting.field_1060 : Formatting.field_1061;
      }
   }

   private String getColoredPotionString(StatusEffectInstance effect) {
      StatusEffect potion = effect.method_5579();
      return potion.method_5560().getString()
         + " "
         + (effect.method_5578() + 1)
         + " §f"
         + StatusEffectUtil.method_5577(effect, 1.0F, mc.field_1687.method_54719().method_54748()).getString();
   }

   private String getColoredPotionTimeString(StatusEffectInstance effect) {
      return StatusEffectUtil.method_5577(effect, 1.0F, mc.field_1687.method_54719().method_54748()).getString();
   }

   private int getColor(int counter) {
      if (this.colorMode.getValue() != HUD_awERnEnjBmVoXYDZWizd.Custom) {
         return this.rainbow(counter).getRGB();
      } else {
         return this.sync.getValue() ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.color.getValue().getRGB() : this.color.getValue().getRGB();
      }
   }

   private int getColorComboA(int counter) {
      return this.colorMode.getValue() != HUD_awERnEnjBmVoXYDZWizd.Custom ? this.rainbow(counter).getRGB() : this.Acolor.getValue().getRGB();
   }

   private int getColorComboD(int counter) {
      return this.colorMode.getValue() != HUD_awERnEnjBmVoXYDZWizd.Custom ? this.rainbow(counter).getRGB() : this.Dcolor.getValue().getRGB();
   }

   private Color rainbow(int delay) {
      double rainbowState = Math.ceil(((double)this.progress + (double)delay * this.rainbowDelay.getValue()) / 20.0);
      if (this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.Pulse) {
         return this.sync.getValue()
            ? this.pulseColor(ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.color.getValue(), delay)
            : this.pulseColor(this.color.getValue(), delay);
      } else {
         return this.colorMode.getValue() == HUD_awERnEnjBmVoXYDZWizd.Rainbow
            ? Color.getHSBColor((float)(rainbowState % 360.0 / 360.0), this.saturation.getValueFloat() / 255.0F, 1.0F)
            : this.pulseColor(Color.getHSBColor((float)(rainbowState % 360.0 / 360.0), this.saturation.getValueFloat() / 255.0F, 1.0F), delay);
      }
   }

   private Color pulseColor(Color color, int index) {
      float[] hsb = new float[3];
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
      float brightness = Math.abs(
         (
                  (float)((long)this.pulseProgress % 2000L) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979F) ^ 2127476077)
                     + (float)index / 14.0F * Float.intBitsToFloat(Float.floatToIntBits(0.09192204F) ^ 2109489567)
               )
               % Float.intBitsToFloat(Float.floatToIntBits(0.7858098F) ^ 2135501525)
            - Float.intBitsToFloat(Float.floatToIntBits(6.46708F) ^ 2135880274)
      );
      brightness = Float.intBitsToFloat(Float.floatToIntBits(18.996923F) ^ 2123889075)
         + Float.intBitsToFloat(Float.floatToIntBits(2.7958195F) ^ 2134044341) * brightness;
      hsb[2] = brightness % Float.intBitsToFloat(Float.floatToIntBits(0.8992331F) ^ 2137404452);
      return ColorUtil.injectAlpha(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])), color.getAlpha());
   }
}
