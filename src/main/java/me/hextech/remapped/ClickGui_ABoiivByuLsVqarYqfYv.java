package me.hextech.remapped;

import java.awt.Color;

public class ClickGui_ABoiivByuLsVqarYqfYv extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static final FadeUtils_DPfHthPqEJdfXfNYhDbG fade = new FadeUtils_DPfHthPqEJdfXfNYhDbG(500L);
   public static ClickGui_ABoiivByuLsVqarYqfYv INSTANCE;
   private final EnumSetting<ClickGui_PrFcfRftxumUysizqxkG> page = this.add(new EnumSetting("Page", ClickGui_PrFcfRftxumUysizqxkG.General));
   public final EnumSetting<ClickGui_lDjmIzzmqoIBLakgOLww> menu = this.add(
      new EnumSetting("MainMenu", ClickGui_lDjmIzzmqoIBLakgOLww.HexTech, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final EnumSetting<AnimateUtil_AcLZzRdHWZkNeKEYTOwI> animMode = this.add(
      new EnumSetting("AnimMode", AnimateUtil_AcLZzRdHWZkNeKEYTOwI.Normal, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final EnumSetting<ClickGui_qvrpuTqAQSFqXVuTIwHB> uiType = this.add(
      new EnumSetting("UIType", ClickGui_qvrpuTqAQSFqXVuTIwHB.Old, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final BooleanSetting maxFill = this.add(new BooleanSetting("MaxFill", false, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General));
   public final SliderSetting height = this.add(
      new SliderSetting("Height", 12.0, 10.0, 20.0, 1.0, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final ColorSetting bindC = this.add(
      new ColorSetting("BindText", new Color(255, 255, 255), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General).injectBoolean(true)
   );
   public final ColorSetting gearColor = this.add(
      new ColorSetting("Gear", new Color(150, 150, 150), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General).injectBoolean(true)
   );
   public final EnumSetting<ClickGui> mode = this.add(
      new EnumSetting("EnableAnim", ClickGui.Reset, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final BooleanSetting scissor = this.add(new BooleanSetting("Scissor", false, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General));
   public final BooleanSetting snow = this.add(new BooleanSetting("Snow", false, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General));
   public final BooleanSetting blackground = this.add(
      new BooleanSetting("BackGround", true, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final ColorSetting customBackground = this.add(
      new ColorSetting("CustomBackground", new Color(0, 0, 0, 36), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General).injectBoolean(false)
   );
   public final ColorSetting endColor = this.add(
      new ColorSetting(
         "EndColor", new Color(255, 0, 0, 80), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General && this.customBackground.booleanValue
      )
   );
   public final SliderSetting animationSpeed = this.add(
      new SliderSetting("AnimationSpeed", 0.9, 0.01, 1.0, 0.01, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final SliderSetting sliderSpeed = this.add(
      new SliderSetting("SliderSpeed", 0.2, 0.01, 1.0, 0.01, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final SliderSetting booleanSpeed = this.add(
      new SliderSetting("BooleanSpeed", 0.4, 0.01, 1.0, 0.01, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final BooleanSetting customFont = this.add(new BooleanSetting("CustomFont", true, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General));
   public final SliderSetting fontsize = this.add(
      new SliderSetting("FontSize", 7.0, 1.0, 20.0, 1.0, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.General)
   );
   public final ColorSetting color = this.add(
      new ColorSetting("Main", new Color(215, 215, 215, 161), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting mainEnd = this.add(
      new ColorSetting("MainEnd", -2113929216, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color).injectBoolean(false)
   );
   public final ColorSetting mainHover = this.add(
      new ColorSetting("MainHover", new Color(186, 188, 252), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting categoryEnd = this.add(
      new ColorSetting("CategoryEnd", -2113929216, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color).injectBoolean(false)
   );
   public final ColorSetting disableText = this.add(
      new ColorSetting("DisableText", new Color(255, 255, 255), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting enableText = this.add(
      new ColorSetting("EnableText", new Color(255, 255, 255), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting enableTextS = this.add(new ColorSetting("EnableText2", -2424832, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color));
   public final ColorSetting mbgColor = this.add(
      new ColorSetting("Module", new Color(63, 63, 63, 42), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting moduleEnd = this.add(
      new ColorSetting("ModuleEnd", -2113929216, v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color).injectBoolean(false)
   );
   public final ColorSetting moduleEnable = this.add(
      new ColorSetting("ModuleEnable", new Color(91, 91, 91), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting mhColor = this.add(
      new ColorSetting("ModuleHover", new Color(152, 152, 152, 123), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting sbgColor = this.add(
      new ColorSetting("Setting", new Color(24, 24, 24, 0), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting shColor = this.add(
      new ColorSetting("SettingHover", new Color(152, 152, 152, 123), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   public final ColorSetting bgColor = this.add(
      new ColorSetting("Background", new Color(24, 24, 24, 42), v -> this.page.getValue() == ClickGui_PrFcfRftxumUysizqxkG.Color)
   );
   int lastHeight;

   public ClickGui_ABoiivByuLsVqarYqfYv() {
      super("ClickGui", Module_JlagirAibYQgkHtbRnhw.Client);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (!(mc.field_1755 instanceof ClickGuiScreen)) {
         this.disable();
      }
   }

   @Override
   public void onEnable() {
      if (this.lastHeight != this.height.getValueInt()) {
         for (ClickGuiTab tab : me.hextech.HexTech.GUI.tabs) {
            for (Component component : tab.getChildren()) {
               if (component instanceof ModuleComponent moduleComponent) {
                  for (Component settingComponent : moduleComponent.getSettingsList()) {
                     settingComponent.setHeight(this.height.getValueInt());
                     settingComponent.defaultHeight = this.height.getValueInt();
                  }
               }

               component.setHeight(this.height.getValueInt());
               component.defaultHeight = this.height.getValueInt();
            }
         }

         this.lastHeight = this.height.getValueInt();
      }

      if (this.mode.getValue() == ClickGui.Reset) {
         for (ClickGuiTab tab : me.hextech.HexTech.GUI.tabs) {
            for (Component component : tab.getChildren()) {
               component.currentOffset = 0.0;
               if (component instanceof ModuleComponent) {
                  ModuleComponent moduleComponent = (ModuleComponent)component;
                  moduleComponent.isPopped = false;

                  for (Component settingComponent : moduleComponent.getSettingsList()) {
                     settingComponent.currentOffset = 0.0;
                     if (settingComponent instanceof SliderComponent sliderComponent) {
                        sliderComponent.renderSliderPosition = 0.0;
                     } else if (settingComponent instanceof BooleanComponent booleanComponent) {
                        booleanComponent.currentWidth = 0.0;
                     } else if (settingComponent instanceof ColorComponents colorComponents) {
                        colorComponents.currentWidth = 0.0;
                     }
                  }
               }
            }

            tab.currentHeight = 0.0;
         }
      }

      Notify_EXlgYplaRzfgofOPOkyB.notifyList.clear();
      sendNotify("版本:§bʜᴇӼᴛᴇᴄʜ§8-§4[NIGHTLY]");
      fade.reset();
      if (nullCheck()) {
         this.disable();
      } else {
         mc.method_1507(GuiManager.clickGui);
      }
   }

   @Override
   public void onDisable() {
      if (mc.field_1755 instanceof ClickGuiScreen) {
         mc.method_1507(null);
      }
   }
}
